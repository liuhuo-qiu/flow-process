/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 流程上下文
 * @author 49796
 * @version :  com.wj.updater.entity.ProcessContext.java,  v  0.1  2020/6/29  14:09  49796  Exp  $$
 */
@TableName("process_context")
public class ProcessContext {
    /**
     * id
     */
    private String id;
    /**
     * 流程ID
     */
    private String processRecordId;
    /**
     * 上下文key
     */
    private String field;

    /**
     * 类型
     */
    private String fieldType;

    /**
     * 值
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
     * Getter  method  for  property      field.
     *
     * @return property  value  of  field
     */
    public String getField() {
        return field;
    }

    /**
     * Setter method for property   field .
     *
     * @param field value to be assigned to property field
     */
    public void setField(String field) {
        this.field = field;
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
