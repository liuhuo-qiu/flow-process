/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qlj.flow.contact.NodeType;
import com.qlj.flow.contact.ProcessStatusEnum;
import com.qlj.flow.entity.FlowProcess;
import com.qlj.flow.entity.ProcessContext;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessNodeRecord;
import com.qlj.flow.entity.ProcessParam;
import com.qlj.flow.entity.ProcessRecord;
import com.qlj.flow.exception.ServiceException;
import com.qlj.flow.mapper.FlowProcessMapper;
import com.qlj.flow.mapper.ProcessContextMapper;
import com.qlj.flow.mapper.ProcessNodeMapper;
import com.qlj.flow.mapper.ProcessNodeRecordMapper;
import com.qlj.flow.mapper.ProcessRecordMapper;
import com.qlj.flow.schedule.NodeSchedule;
import com.qlj.flow.service.FlowProcessService;
import com.qlj.flow.service.ProcessNodeService;
import com.qlj.flow.service.ProcessParamService;
import com.qlj.flow.util.ProcessUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.service.impl.FlowProcessServiceImpl.java,  v  0.1  2020/6/29  14:29  49796  Exp  $$
 */
@Service
public class FlowProcessServiceImpl extends ServiceImpl<FlowProcessMapper,FlowProcess> implements FlowProcessService {
    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(FlowProcessServiceImpl.class);

    /**
     * 流程参数mapper
     */
    @Resource
    private ProcessParamService paramService;


    /**
     * 节点服务类
     */
    private ProcessNodeService nodeService;

    /**
     * 流程实例mapper
     */
    @Resource
    private ProcessRecordMapper processRecordMapper;

    /**
     * 流程mapper
     */
    @Resource
    private FlowProcessMapper flowProcessMapper;

    /**
     * 流程节点mapper
     */
    @Resource
    private ProcessNodeMapper processNodeMapper;

    /**
     * 流程节点实例mapper
     */
    @Resource
    private ProcessNodeRecordMapper nodeRecordMapper;

    /**
     * 节点执行任务队列
     */
    @Resource
    private NodeSchedule nodeSchedule;

    /**
     * 上下文服务
     */
    @Resource
    private ProcessContextMapper contextMapper;

    /**
     * 创建流程
     * @param process
     * @return
     */
    @Override
    public int createProcess(FlowProcess process) {
        int insert = flowProcessMapper.insert(process);
        if(insert<0){
            throw new ServiceException("流程创建失败");
        }
        //构造开始节点
        ProcessNode startNode=new ProcessNode();
        startNode.setProcessId(process.getId());
        startNode.setName("开始");
        startNode.setType(NodeType.START.getCode());
        startNode.setRetryTime(5);
        createProcessNode(startNode);
        return insert;
    }

    /**
     * 创建流程节点
     * @param processNode
     * @return
     */
    @Override
    public int createProcessNode(ProcessNode processNode) {
        int insert = processNodeMapper.insert(processNode);
        if(insert<0){
            throw new ServiceException("流程节点");
        }
        return insert;
    }

    /**
     * 更新执行实例
     * @param processRecord
     * @return
     */
    @Override
    public int updateProcessRecord(ProcessRecord processRecord) {
        int i = processRecordMapper.updateById(processRecord);
        if(i<0){
            throw new ServiceException("更新流程实例失败");
        }
        return i;
    }

    /**
     * 更改执行实例的状态
     * @param processRecordId
     * @param status
     * @return
     */
    @Override
    public int updateProcessRecordStatus(String processRecordId, ProcessStatusEnum status) {
        ProcessRecord update=new ProcessRecord();
        update.setId(processRecordId);
        update.setStatus(status.getCode());
        return updateProcessRecord(update);
    }

    /**
     * 更改流程的状态
     * @param processId
     * @param status
     * @return
     */
    @Override
    public int updateProcessStatus(String processId, String status) {
        FlowProcess process=new FlowProcess();
        process.setId(processId);
        process.setStatus(status);
        int i = flowProcessMapper.updateById(process);
        if(i<=0){
            throw new ServiceException("更新失败");
        }
        return i;
    }

    /**
     * 根据流程实例ID查询当前实例是否还有启动未执行完毕的节点
     * @param processRecordId
     * @return
     */
    @Override
    public int queryStartedProcessByRecordId(String processRecordId) {
        QueryWrapper<ProcessRecord> recordQuery=new QueryWrapper<>();
        recordQuery.eq("id",processRecordId);
        recordQuery.in("status", Arrays.asList(
                ProcessStatusEnum.RUNNING.getCode(),
                ProcessStatusEnum.COMPLETE.getCode(),
                ProcessStatusEnum.START.getCode()
        ));
        return processRecordMapper.selectCount(recordQuery);
    }

    /**
     *
     * @param processRecordId  流程实例ID
     * @param nodeId  节点ID
     */
    @Override
    public void executeNode(String processRecordId, String nodeId) {

        ProcessNode node = nodeService.getById(nodeId);
        QueryWrapper<ProcessNodeRecord> nodeQuery=new QueryWrapper<>();

        Integer count = nodeRecordMapper.selectCount(nodeQuery);
        Integer retryTime = node.getRetryTime();
        if(null==retryTime){
            //默认最大执行次数5次
            retryTime=5;
        }
        if(null!=count&&count>retryTime){
            logger.warn(String.format("流程实例 %s 节点 %s 已经执行 %s次，超出节点最大执行次数限制%s！",processRecordId,nodeId,count,retryTime));
            //如果没有子节点，查询当前实例下是否还有执行中节点
            int runningNodeCount= queryStartedProcessByRecordId(processRecordId);
            if(runningNodeCount>1){
                return;
            }
            ProcessRecord processRecord = new ProcessRecord();
            processRecord.setId(processRecordId);
            processRecord.setStatus(ProcessStatusEnum.SUCCESS.getCode());
            //获取流程实例的最终result
            JSONObject processResult = getProcessResult(node.getProcessId(),processRecordId);
            processRecord.setResult(processResult.toJSONString());
            //更新流程最终状态为执行成功
            updateProcessRecord(processRecord);
            return;
        }

        ProcessRecord processRecord = processRecordMapper.selectById(processRecordId);

        //查询出节点参数配置
        List<ProcessParam> nodeParams = paramService.queryProcessNodeParamList(processRecord.getProcessId(), nodeId);

        //查询出上下文中对应的节点参数值
        QueryWrapper<ProcessContext> contextQuery=new QueryWrapper<>();
        contextQuery.eq("process_record_id",processRecordId);
        contextQuery.in("field",nodeParams.stream().map(ProcessParam::getFieldName).collect(Collectors.toList()));
        List<ProcessContext> processContexts = contextMapper.selectList(contextQuery);
        JSONObject nodeParam=new JSONObject();

        //构造节点参数
        nodeParams.forEach(np->nodeParam.put(np.getFieldName(),np.getValue()));
        processContexts.forEach(ctx->nodeParam.put(ctx.getField(),ctx.getValue()));

        executeNodeRecord(processRecordId,nodeId,nodeParam);
    }

    /**
     *
     * @param processId  流程ID
     * @param params  流程参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessRecord startProcess(String processId, JSONObject params) {
        if(StringUtils.isBlank(processId)){
            throw new ServiceException("流程ID为空!");
        }
        if(null==params){
            throw new ServiceException("params不能为null!");
        }
        ProcessRecord processRecord=new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setParam(params.toJSONString());
        //流程执行中
        processRecord.setStatus(ProcessStatusEnum.RUNNING.getCode());
        processRecordMapper.insert(processRecord);
        try{
            FlowProcess flowProcess = flowProcessMapper.selectById(processId);
            if(null==flowProcess){
                throw new ServiceException("流程"+processId+"不存在!");
            }
            List<ProcessParam> processParams = paramService.queryProcessParamList(processId);
            //流程执行参数检查
            ProcessUtil.checkParam(processParams,params);
            //将参数加入流程实例上下文
            processParams.forEach(param->{
                ProcessContext processContext = new ProcessContext();
                processContext.setProcessRecordId(processRecord.getId());
                processContext.setField(param.getFieldName());
                processContext.setValue(param.getValue());
                contextMapper.insert(processContext);
            });

            //查询流程开始节点
            ProcessNode processNode = processNodeMapper.queryStartNode(processId);

            executeNodeRecord(processRecord.getId(),processNode,params);
        }catch (Exception e){
            logger.warn("流程实例创建异常",e);
            processRecord.setErrorMessage(e.getMessage());
            processRecord.setStatus(ProcessStatusEnum.FAILED.getCode());
            processRecordMapper.updateById(processRecord);
        }
        return processRecord;
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

        executeNodeRecord(processRecordId,nodeId,params,true);
    }

    /**
     *  执行节点
     * @param params 节点参数
     * @param nodeId 节点Id
     */
    @Override
    public void executeNodeRecord(String processRecordId,String nodeId, JSONObject params) {
        executeNodeRecord(processRecordId,nodeId,params,false);
    }


    /**
     *  执行节点
     * @param params 节点参数
     * @param nodeId 节点Id
     * @param isRetry 是否是重试请求
     */
    @Override
    public void executeNodeRecord(String processRecordId,String nodeId, JSONObject params,boolean isRetry) {
        ProcessNode node = processNodeMapper.selectById(nodeId);
        if(null==node){
            throw new ServiceException(String.format("节点不存在nodeId:%s",nodeId));
        }
        executeNodeRecord(processRecordId,node,params,isRetry);
    }


    /**
     *  执行节点
     * @param params 节点参数
     * @param node 节点对象
     */
    @Override
    public void executeNodeRecord(String processRecordId,ProcessNode node, JSONObject params) {
        executeNodeRecord(processRecordId,node,params,false);
    }

    /**
     *  执行节点
     * @param params 节点参数
     * @param node 节点对象
     * @param isRetry 是否是重试
     */
    @Override
    public void executeNodeRecord(String processRecordId,ProcessNode node, JSONObject params,Boolean isRetry) {
        ProcessNodeRecord nodeRecord=new ProcessNodeRecord();
        nodeRecord.setRetry(isRetry);
        nodeRecord.setNodeId(node.getId());
        nodeRecord.setParams(params.toJSONString());
        nodeRecord.setStatus(ProcessStatusEnum.START.getCode());
        nodeRecord.setProcessRecordId(processRecordId);

        //节点实例入库
        nodeRecordMapper.insert(nodeRecord);
        //加入待执行队列
        nodeSchedule.addData(nodeRecord);
    }


    /**
     * 获取流程实例的最终返回值
     * @param processId
     * @param processRecordId
     * @return
     */
    @Override
    public JSONObject getProcessResult(String processId,String processRecordId) {
        //提取所有type为end的节点中的result合并为整个流程实例的result
        JSONObject processResult=new JSONObject();
        QueryWrapper<ProcessNode> nodeQuery=new QueryWrapper<>();
        nodeQuery.eq("process_id",processId);
        nodeQuery.eq("type", NodeType.END.getCode());
        List<ProcessNode> endNodeList = nodeService.list(nodeQuery);

        QueryWrapper<ProcessNodeRecord> nodeRecordQuery=new QueryWrapper<>();
        nodeRecordQuery.eq("process_record_id",processRecordId);
        List<String> endNodeIdList = endNodeList.stream().map(item -> item.getId()).collect(Collectors.toList());
        nodeRecordQuery.in("node_id",endNodeIdList);
        List<ProcessNodeRecord> list = nodeRecordMapper.selectList(nodeRecordQuery);
        list.forEach(item -> {
            String result = item.getResult();
            if(StringUtils.isBlank(result)){
                return;
            }
            try{
                JSONObject resultO = JSONObject.parseObject(result);
                processResult.putAll(resultO);
            }catch (Exception e){
                logger.warn(String.format("节点%s 实例%s 结果转换为json异常：%s",item.getNodeId(),item.getId(),e.getMessage()));
            }
        });
        return processResult;
    }
}
