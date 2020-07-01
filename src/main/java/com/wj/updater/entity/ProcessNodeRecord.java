/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.wj.updater.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 节点实例
 * @author 49796
 * @version :  com.wj.updater.entity.ProcessNodeRecord.java,  v  0.1  2020/6/29  11:12  49796  Exp  $$
 */
@TableName("process_node_record")
public class ProcessNodeRecord {

    /**
     * 流程实例ID
     */
    private String id;


    /**
     * 流程实例
     */
    private String processRecordId;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 是否重试实例
     */
    private Boolean retry;

    /**
     * 节点入参
     */
    private String params;

    /**
     * 节点出参
     */
    private String result;

    /**
     * 当前节点执行状态  running   success error  failed
     */
    private String status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * Getter  method  for  property      id.
     *
     * @return property  value  of  id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for property   id .
     *
     * @param id value to be assigned to property id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter  method  for  property      processRecordId.
     *
     * @return property  value  of  processRecordId
     */
    public String getProcessRecordId() {
        return processRecordId;
    }

    /**
     * Setter method for property   processRecordId .
     *
     * @param processRecordId value to be assigned to property processRecordId
     */
    public void setProcessRecordId(String processRecordId) {
        this.processRecordId = processRecordId;
    }

    /**
     * Getter  method  for  property      nodeId.
     *
     * @return property  value  of  nodeId
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * Setter method for property   nodeId .
     *
     * @param nodeId value to be assigned to property nodeId
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * Getter  method  for  property      retry.
     *
     * @return property  value  of  retry
     */
    public Boolean getRetry() {
        return retry;
    }

    /**
     * Setter method for property   retry .
     *
     * @param retry value to be assigned to property retry
     */
    public void setRetry(Boolean retry) {
        this.retry = retry;
    }

    /**
     * Getter  method  for  property      params.
     *
     * @return property  value  of  params
     */
    public String getParams() {
        return params;
    }

    /**
     * Setter method for property   params .
     *
     * @param params value to be assigned to property params
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * Getter  method  for  property      result.
     *
     * @return property  value  of  result
     */
    public String getResult() {
        return result;
    }

    /**
     * Setter method for property   result .
     *
     * @param result value to be assigned to property result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Getter  method  for  property      status.
     *
     * @return property  value  of  status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setter method for property   status .
     *
     * @param status value to be assigned to property status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Getter  method  for  property      errorMessage.
     *
     * @return property  value  of  errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Setter method for property   errorMessage .
     *
     * @param errorMessage value to be assigned to property errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
