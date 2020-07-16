/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qlj.flow.contact.ProcessStatusEnum;
import com.qlj.flow.entity.FlowProcess;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessNodeRecord;
import com.qlj.flow.entity.ProcessRecord;

/**
 * 流程服务类
 * @author 49796
 * @version :  com.wj.updater.service.FlowProcessService.java,  v  0.1  2020/6/29  11:01  49796  Exp  $$
 */
public interface FlowProcessService extends IService<FlowProcess> {


    /**
     * 创建流程
     * @param process
     * @return
     */
    public int createProcess(FlowProcess process);


    /**
     * 创建流程节点
     * @param processNode
     * @return
     */
    public int createProcessNode(ProcessNode processNode);


    /**
     * 更改执行实例
     * @param processRecord
     * @return
     */
    public int updateProcessRecord(ProcessRecord processRecord);

    /**
     * 更改执行实例的状态
     * @param processRecordId
     * @param status
     * @return
     */
    public int updateProcessRecordStatus(String processRecordId, ProcessStatusEnum status);


    /**
     * 更改流程的状态
     * @param processId
     * @param status
     * @return
     */
    public int updateProcessStatus(String processId, String status);

    /**
     * 根据流程实例ID查询当前实例是否还有启动未执行完毕的节点
     * @param processRecordId
     * @return
     */
    public int queryStartedProcessByRecordId(String processRecordId);

    /**
     * 执行流程节点
     * @param processRecordId  流程实例ID
     * @param nodeId  节点ID
     */
    public void executeNode(String processRecordId,String nodeId);

    /**
     *  执行节点
     * @param params 节点参数
     * @param nodeId 节点ID
     */
    public void executeNodeRecord(String processRecordId,String nodeId, JSONObject params);

    /**
     *  执行节点
     * @param params 节点参数
     * @param nodeId 节点ID
     * @param isRetry 是否是重试
     */
    public void executeNodeRecord(String processRecordId,String nodeId, JSONObject params,boolean isRetry);

    /**
     *  执行节点
     * @param params 节点参数
     * @param node 节点对象
     */
    public void executeNodeRecord(String processRecordId,ProcessNode node, JSONObject params);

    /**
     *  执行节点
     * @param params 节点参数
     * @param node 节点对象
     * @param isRetry 是否是重试
     */
    public void executeNodeRecord(String processRecordId,ProcessNode node, JSONObject params,Boolean isRetry);

    /**
     * 开始一个流程
     * @param processId  流程ID
     * @param params  流程参数
     */
    public ProcessRecord startProcess(String processId, JSONObject params);

    /**
     * 错误节点重试
     * @param processRecordId 流程实例id
     * @param errorNodeRecordId 错误节点实例id
     * @param nodeId  节点id
     * @param params 节点参数
     */
    public void retryErrorNode(String processRecordId,String errorNodeRecordId,String nodeId,JSONObject params);

    /**
     * 获取流程实例的最终返回值
     * @param processId
     * @param processRecordId
     * @return
     */
    public JSONObject getProcessResult(String processId,String processRecordId);
}
