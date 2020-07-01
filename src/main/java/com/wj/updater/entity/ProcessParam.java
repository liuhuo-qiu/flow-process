/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.wj.updater.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 节点参数
 * @author 49796
 * @version :  com.wj.updater.entity.ProcessParam.java,  v  0.1  2020/6/29  10:17  49796  Exp  $$
 */
@TableName("process_param")
public class ProcessParam {

    /**
     * id
     */
    private String id;

    /**
     * 流程ID
     */
    private String processId;


    /**
     * 节点Id
     */
    private String nodeId;

    /**
     * 参数类型  node  process
     */
    private String type;

    /**
     * 参数名称
     */
    private String fieldName;

    /**
     * 参数类型  string  int  date
     */
    private String fieldType;

    /**
     * 是否必须
     */
    private Boolean required;

    /**
     * 参数值
     */
    private String value;

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
     * Getter  method  for  property      fieldName.
     *
     * @return property  value  of  fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Setter method for property   fieldName .
     *
     * @param fieldName value to be assigned to property fieldName
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Getter  method  for  property      fieldType.
     *
     * @return property  value  of  fieldType
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     * Setter method for property   fieldType .
     *
     * @param fieldType value to be assigned to property fieldType
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * Getter  method  for  property      required.
     *
     * @return property  value  of  required
     */
    public Boolean getRequired() {
        return required;
    }

    /**
     * Setter method for property   required .
     *
     * @param required value to be assigned to property required
     */
    public void setRequired(Boolean required) {
        this.required = required;
    }

    /**
     * Getter  method  for  property      value.
     *
     * @return property  value  of  value
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter method for property   value .
     *
     * @param value value to be assigned to property value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
