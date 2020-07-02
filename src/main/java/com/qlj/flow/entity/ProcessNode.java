/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 节点，(定义流程的节点)
 * @author 49796
 * @version :  com.wj.updater.entity.ProcessNode.java,  v  0.1  2020/6/29  10:14  49796  Exp  $$
 */
@TableName("process_node")
public class ProcessNode {

    /**
     * 节点Id
     */
    private String id;

    /**
     * 流程ID
     */
    private String processId;

    /**
     * 节点类型
     */
    private String type;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点配置一个JSON
     */
    private String config;

    /**
     * 上一个节点id
     */
    private String lastNode;

    /**
     * 错误重试次数上限
     */
    private Integer retryTime;

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
     * Getter  method  for  property      type.
     *
     * @return property  value  of  type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for property   type .
     *
     * @param type value to be assigned to property type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter  method  for  property      name.
     *
     * @return property  value  of  name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property   name .
     *
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter  method  for  property      config.
     *
     * @return property  value  of  config
     */
    public String getConfig() {
        return config;
    }

    /**
     * Setter method for property   config .
     *
     * @param config value to be assigned to property config
     */
    public void setConfig(String config) {
        this.config = config;
    }

    /**
     * Getter  method  for  property      lastNode.
     *
     * @return property  value  of  lastNode
     */
    public String getLastNode() {
        return lastNode;
    }

    /**
     * Setter method for property   lastNode .
     *
     * @param lastNode value to be assigned to property lastNode
     */
    public void setLastNode(String lastNode) {
        this.lastNode = lastNode;
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
     * Getter  method  for  property      retryTime.
     *
     * @return property  value  of  retryTime
     */
    public Integer getRetryTime() {
        return retryTime;
    }

    /**
     * Setter method for property   retryTime .
     *
     * @param retryTime value to be assigned to property retryTime
     */
    public void setRetryTime(Integer retryTime) {
        this.retryTime = retryTime;
    }
}
