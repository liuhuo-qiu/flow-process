/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qlj.flow.contact.ProcessStatusEnum;
import com.qlj.flow.entity.FlowProcess;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessNodeRecord;
import com.qlj.flow.entity.ProcessParam;
import com.qlj.flow.entity.ProcessRecord;
import com.qlj.flow.exception.ServiceException;
import com.qlj.flow.mapper.FlowProcessMapper;
import com.qlj.flow.mapper.ProcessNodeMapper;
import com.qlj.flow.mapper.ProcessNodeRecordMapper;
import com.qlj.flow.mapper.ProcessParamMapper;
import com.qlj.flow.mapper.ProcessRecordMapper;
import com.qlj.flow.schedule.NodeSchedule;
import com.qlj.flow.service.FlowProcessService;
import com.qlj.flow.util.ProcessUtil;
import com.qlj.flow.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.service.impl.FlowProcessServiceImpl.java,  v  0.1  2020/6/29  14:29  49796  Exp  $$
 */
@Service
public class FlowProcessServiceImpl implements FlowProcessService {
    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(FlowProcessServiceImpl.class);

    /**
     * 流程参数mapper
     */
    private ProcessParamMapper processParamMapper;

    /**
     * 流程实例mapper
     */
    private ProcessRecordMapper processRecordMapper;

    /**
     * 流程mapper
     */
    private FlowProcessMapper flowProcessMapper;

    /**
     * 流程节点mapper
     */
    private ProcessNodeMapper processNodeMapper;

    /**
     * 流程节点实例mapper
     */
    private ProcessNodeRecordMapper nodeRecordMapper;

    /**
     * 节点执行任务队列
     */
    private NodeSchedule nodeSchedule;


    /**
     *
     * @param processRecordId  流程实例ID
     * @param nodeId  节点ID
     */
    @Override
    public void executeNode(String processRecordId, String nodeId) {

    }

    /**
     *
     * @param processId  流程ID
     * @param params  流程参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startProcess(String processId, JSONObject params) {
        if(StringUtils.isBlank(processId)){
            throw new ServiceException("流程ID为空!");
        }
        if(null==params){
            throw new ServiceException("params不能为null!");
        }
        ProcessRecord processRecord=new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setParam(params.toJSONString());
        processRecord.setId(UUIDUtil.randomUuid());
        try{
            FlowProcess flowProcess = flowProcessMapper.selectById(processId);
            if(null==flowProcess){
                throw new ServiceException("流程"+processId+"不存在!");
            }
            List<ProcessParam> processParams = processParamMapper.queryProcessParamList(processId);
            //流程执行参数检查
            ProcessUtil.checkParam(processParams,params);

            //查询流程开始节点
            ProcessNode processNode = processNodeMapper.queryStartNode(processId);
            executeNodeRecord(processNode,params);

            //流程执行中
            processRecord.setStatus(ProcessStatusEnum.RUNNING.getCode());
        }catch (Exception e){
            logger.warn("流程实例创建异常",e);
            processRecord.setErrorMessage(e.getMessage());
            processRecord.setStatus(ProcessStatusEnum.FAILED.getCode());
        }
        processRecordMapper.insert(processRecord);
    }

    /**
     * 错误节点重试
     * @param processRecordId 流程实例id
     * @param errorNodeRecordId 错误节点实例id
     * @param nodeId  节点id
     * @param params 节点参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retryErrorNode(String processRecordId,String errorNodeRecordId, String nodeId, JSONObject params) {
        //更新节点为执行失败
        ProcessNodeRecord nodeRecord=new ProcessNodeRecord();
        nodeRecord.setId(errorNodeRecordId);
        nodeRecord.setStatus(ProcessStatusEnum.FAILED.getCode());
        nodeRecordMapper.updateById(nodeRecord);

        //检查已经重试次数，如果错误次数超过重试次数，则不再继续执行当前节点
        QueryWrapper<ProcessNodeRecord> countQuery=new QueryWrapper<>();
        countQuery.eq("process_recordId",processRecordId);
        countQuery.eq("node_id",nodeId);
        Integer errorCount = nodeRecordMapper.selectCount(countQuery);
        ProcessNode processNode = processNodeMapper.selectById(nodeId);
        if(errorCount>processNode.getRetryTime()){
            return;
        }

        executeNodeRecord(nodeId,params,true);
    }

    /**
     *  执行节点
     * @param params 节点参数
     * @param nodeId 节点Id
     */
    @Override
    public void executeNodeRecord(String nodeId, JSONObject params) {
        executeNodeRecord(nodeId,params,false);
    }


    /**
     *  执行节点
     * @param params 节点参数
     * @param nodeId 节点Id
     * @param isRetry 是否是重试请求
     */
    @Override
    public void executeNodeRecord(String nodeId, JSONObject params,boolean isRetry) {
        ProcessNode node = processNodeMapper.selectById(nodeId);
        if(null==node){
            throw new ServiceException(String.format("节点不存在nodeId:%s",nodeId));
        }
        executeNodeRecord(node,params,isRetry);
    }


    /**
     *  执行节点
     * @param params 节点参数
     * @param node 节点对象
     */
    @Override
    public void executeNodeRecord(ProcessNode node, JSONObject params) {
        executeNodeRecord(node,params,false);
    }

    /**
     *  执行节点
     * @param params 节点参数
     * @param node 节点对象
     * @param isRetry 是否是重试
     */
    @Override
    public void executeNodeRecord(ProcessNode node, JSONObject params,Boolean isRetry) {

        //构造开始节点参数
        List<ProcessParam> nodeParams = processParamMapper.queryProcessNodeParamList(node.getProcessId(), node.getId());

        JSONObject nodeParam=new JSONObject();
        nodeParams.forEach(p->nodeParam.put(p.getFieldName(),params.getString(p.getFieldName())));

        ProcessNodeRecord nodeRecord=new ProcessNodeRecord();
        nodeRecord.setId(UUIDUtil.randomUuid());
        nodeRecord.setRetry(isRetry);
        nodeRecord.setNodeId(node.getId());
        nodeRecord.setParams(nodeParam.toJSONString());
        nodeRecord.setStatus(ProcessStatusEnum.START.getCode());
        //节点实例入库
        nodeRecordMapper.insert(nodeRecord);
        //加入待执行队列
        nodeSchedule.addData(nodeRecord);
    }

}
