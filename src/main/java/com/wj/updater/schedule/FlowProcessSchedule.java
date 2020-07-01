/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.wj.updater.schedule;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wj.updater.contact.ProcessStatusEnum;
import com.wj.updater.entity.ProcessContext;
import com.wj.updater.entity.ProcessNode;
import com.wj.updater.entity.ProcessNodeRecord;
import com.wj.updater.entity.ProcessParam;
import com.wj.updater.entity.ProcessRecord;
import com.wj.updater.mapper.ProcessContextMapper;
import com.wj.updater.mapper.ProcessNodeMapper;
import com.wj.updater.mapper.ProcessNodeRecordMapper;
import com.wj.updater.mapper.ProcessParamMapper;
import com.wj.updater.mapper.ProcessRecordMapper;
import com.wj.updater.service.FlowProcessService;
import com.wj.updater.util.ProcessUtil;
import com.wj.updater.util.UUIDUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  流程定时任务扫描
 * @author 49796
 * @version :  com.wj.updater.schedule.FlowProcessSchedule.java,  v  0.1  2020/6/29  11:35  49796  Exp  $$
 */
public class FlowProcessSchedule{

    /**
     * 流程实例mapper
     */
    private ProcessRecordMapper processRecordMapper;

    /**
     * 节点实例mapper
     */
    private ProcessNodeRecordMapper nodeRecordMapper;

    /**
     * 节点mapper
     */
    private ProcessNodeMapper nodeMapper;

    /**
     * 节点参数mapper
     */
    private ProcessParamMapper paramMapper;

    /**
     * 流程实例上下文
     */
    private ProcessContextMapper contextMapper;

    /**
     * 流程服务类
     */
    private FlowProcessService flowProcessService;



    /**
     *  TODO 改为队列循环执行，上一次执行完毕之后睡眠指定时间之后执行下一次，定时任务可能导致重复执行
     * 定时扫描数据库中流程状态为running的流程，并且获取待执行节点，加入等待执行队列中，等待执行
     */
    @Scheduled(cron = "0 */1 0 0 * ?")
    public void scannerProcess(){
        QueryWrapper<ProcessRecord> processQuery=new QueryWrapper<>();
        processQuery.eq("status", ProcessStatusEnum.RUNNING.getCode());
        //查询出所有正在执行中的流程  TODO 分页查询，一次拉取列表查询时间长了之后数据量太大
        List<ProcessRecord> processRecords = processRecordMapper.selectList(processQuery);
        if(CollectionUtils.isEmpty(processRecords)){
            return;
        }
        processRecords.forEach(this::runCompleteNode);
    }

    /**
     * 扫描执行错误的节点
     */
    @Scheduled(cron = "0 */5 0 0 * ?")
    public void scannerErrorNode(){
        QueryWrapper<ProcessNodeRecord> nodeQuery=new QueryWrapper<>();
        nodeQuery.eq("status", ProcessStatusEnum.ERROR.getCode());
        List<ProcessNodeRecord> processNodeRecords = nodeRecordMapper.selectList(nodeQuery);
        if(CollectionUtils.isEmpty(processNodeRecords)){
            return;
        }
        //将执行错误的节点重新加入执行队列
        processNodeRecords.forEach(nodeRecord -> {
            JSONObject nodeParam = JSONObject.parseObject(nodeRecord.getParams());
            flowProcessService.retryErrorNode(nodeRecord.getProcessRecordId(),
                    nodeRecord.getId(),nodeRecord.getNodeId(),
                    nodeParam);
        });
    }

    /**
     * 根据流程实例，找出执行完毕，等待执行下一个节点的节点执行
     * @param process
     */
    public void runCompleteNode(ProcessRecord process){
        QueryWrapper<ProcessNodeRecord> nodeQuery=new QueryWrapper<>();
        nodeQuery.eq("status", ProcessStatusEnum.COMPLETE.getCode());
        nodeQuery.eq("process_record_id",process.getId());
        List<ProcessNodeRecord> processNodeRecords = nodeRecordMapper.selectList(nodeQuery);
        if(CollectionUtils.isEmpty(processNodeRecords)){
            return;
        }
        processNodeRecords.forEach(this::runChildNode);
    }

    /**
     * 根据节点，执行下一层节点
     * @param nodeRecord
     */
    public void runChildNode(ProcessNodeRecord nodeRecord){
        QueryWrapper<ProcessNode> nodeQuery=new QueryWrapper<>();
        nodeQuery.eq("last_node",nodeRecord.getNodeId());
        List<ProcessNode> processNodes = nodeMapper.selectList(nodeQuery);

        processNodes.forEach(node->{
            //查询出节点参数配置
            List<ProcessParam> nodeParams = paramMapper.queryProcessNodeParamList(node.getProcessId(), node.getId());

            //查询出上下文中对应的节点参数值
            QueryWrapper<ProcessContext> contextQuery=new QueryWrapper<>();
            contextQuery.eq("process_record_id",nodeRecord.getProcessRecordId());
            contextQuery.in("field",nodeParams.stream().map(ProcessParam::getFieldName).collect(Collectors.toList()));
            List<ProcessContext> processContexts = contextMapper.selectList(contextQuery);
            JSONObject nodeParam=new JSONObject();

            //构造节点参数
            nodeParams.forEach(np->nodeParam.put(np.getFieldName(),np.getValue()));
            processContexts.forEach(ctx->nodeParam.put(ctx.getField(),ctx.getValue()));

            //执行节点
            flowProcessService.executeNodeRecord(node,nodeParam);
        });

    }

}
