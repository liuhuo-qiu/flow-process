/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.wj.updater.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 流程（定义的流程对象）
 * @author 49796
 * @version :  com.wj.updater.entity.FlowProcess.java,  v  0.1  2020/6/29  10:14  49796  Exp  $$
 */
@TableName("flow_process")
public class FlowProcess {

    /**
     * 流程Id
     */
    private String id;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date modifyTime;


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


}
