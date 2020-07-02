/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service;

import com.alibaba.fastjson.JSONObject;
import com.qlj.flow.entity.ProcessNode;

/**
 * 流程服务类
 * @author 49796
 * @version :  com.wj.updater.service.FlowProcessService.java,  v  0.1  2020/6/29  11:01  49796  Exp  $$
 */
public interface FlowProcessService {

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
    public void executeNodeRecord(String nodeId, JSONObject params);

    /**
     *  执行节点
     * @param params 节点参数
     * @param nodeId 节点ID
     * @param isRetry 是否是重试
     */
    public void executeNodeRecord(String nodeId, JSONObject params,boolean isRetry);

    /**
     *  执行节点
     * @param params 节点参数
     * @param node 节点对象
     */
    public void executeNodeRecord(ProcessNode node, JSONObject params);

    /**
     *  执行节点
     * @param params 节点参数
     * @param node 节点对象
     * @param isRetry 是否是重试
     */
    public void executeNodeRecord(ProcessNode node, JSONObject params,Boolean isRetry);

    /**
     * 开始一个流程
     * @param processId  流程ID
     * @param params  流程参数
     */
    public void startProcess(String processId, JSONObject params);

    /**
     * 错误节点重试
     * @param processRecordId 流程实例id
     * @param errorNodeRecordId 错误节点实例id
     * @param nodeId  节点id
     * @param params 节点参数
     */
    public void retryErrorNode(String processRecordId,String errorNodeRecordId,String nodeId,JSONObject params);
}
