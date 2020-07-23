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
 * 流程（定义的流程对象）
 * @author 49796
 * @version :  com.wj.updater.entity.FlowProcess.java,  v  0.1  2020/6/29  10:14  49796  Exp  $$
 */
@TableName("flow_process")
@Table("flow_process")
public class FlowProcess extends BaseEntity{
    /**
     *启用状态
     */
    public static final String STATUS_ENABLE="1";
    /**
     * 禁用状态
     */
    public static final String STATUS_DISABLED="2";

    /**
     * 流程Id
     */
    @IsKey
    @Column(type = MySqlTypeConstant.VARCHAR,length = 32,comment = "主键")
    private String id;

    /**
     * 流程名称
     */
    @Column(type = MySqlTypeConstant.VARCHAR,length = 64,comment = "流程名称" ,isNull = false)
    private String name;

    /**
     * 流程code
     */
    @Column(type = MySqlTypeConstant.VARCHAR,length = 64,comment = "流程名称" ,isNull = false)
    private String code;

    /**
     * 流程状态  1启用 2禁用
     */
    @Column(type = MySqlTypeConstant.VARCHAR,length = 4,comment = "流程状态  1启用 2禁用" ,isNull = false)
    private String status;


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
     * Getter  method  for  property      code.
     *
     * @return property  value  of  code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter method for property   code .
     *
     * @param code value to be assigned to property code
     */
    public void setCode(String code) {
        this.code = code;
    }
}
