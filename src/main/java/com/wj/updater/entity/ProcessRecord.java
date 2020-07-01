/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.wj.updater.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 流程实例
 * @author 49796
 * @version :  com.wj.updater.entity.ProcessRecord.java,  v  0.1  2020/6/29  11:10  49796  Exp  $$
 */
@TableName("process_record")
public class ProcessRecord {

    /**
     * 流程实例id
     */
    private String id;
    /**
     * 流程ID
     */
    private String processId;

    /**
     * 启动流程实例时传入的参数
     */
    private String param;

    /**
     * 流程状态
     */
    private String status;


    /**
     * 实例最终输出
     */
    private String result;

    /**
     * 流程异常信息
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
     * Getter  method  for  property      processId.
     *
     * @return property  value  of  processId
     */
    public String getProcessId() {
        return processId;
    }

    /**
     * Setter method for property   processId .
     *
     * @param processId value to be assigned to property processId
     */
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    /**
     * Getter  method  for  property      param.
     *
     * @return property  value  of  param
     */
    public String getParam() {
        return param;
    }

    /**
     * Setter method for property   param .
     *
     * @param param value to be assigned to property param
     */
    public void setParam(String param) {
        this.param = param;
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
}
