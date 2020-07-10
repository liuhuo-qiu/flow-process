/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;

/**
 * 节点参数
 * @author 49796
 * @version :  com.wj.updater.entity.ProcessParam.java,  v  0.1  2020/6/29  10:17  49796  Exp  $$
 */
@TableName("process_param")
@Table("process_param")
public class ProcessParam extends BaseEntity{

    /**
     * id
     */
    @IsKey
    @Column(type = MySqlTypeConstant.VARCHAR,length = 32,comment = "主键")
    private String id;

    /**
     * 流程ID
     */
    @Column(type = MySqlTypeConstant.VARCHAR,length = 32,comment = "流程ID" ,isNull = false)
    private String processId;


    /**
     * 节点Id
     */
    @Column(type = MySqlTypeConstant.VARCHAR,length = 32,comment = "节点Id" ,isNull = false)
    private String nodeId;

    /**
     * 参数类型  node  process
     */
    @Column(type = MySqlTypeConstant.VARCHAR,length = 16,comment = "参数类型" ,isNull = false)
    private String type;

    /**
     * 参数名称
     */
    @Column(type = MySqlTypeConstant.VARCHAR,length = 64,comment = "参数名称" ,isNull = false)
    private String fieldName;

    /**
     * 参数类型  string  int  date
     */
    @Column(type = MySqlTypeConstant.VARCHAR,length = 64,comment = "参数类型 string  int  date" ,isNull = false)
    private String fieldType;

    /**
     * 是否必须
     */
    @Column(comment ="是否必须",isNull = false)
    private Boolean required;

    /**
     * 参数默认值
     */
    @Column(type = MySqlTypeConstant.VARCHAR,length = 64,comment = "参数默认值")
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
