/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;

import java.util.Date;

/**
 * 流程（定义的流程对象）
 * @author 49796
 * @version :  com.wj.updater.entity.FlowProcess.java,  v  0.1  2020/6/29  10:14  49796  Exp  $$
 */
@TableName("flow_process")
@Table("flow_process")
public class FlowProcess extends BaseEntity{

    /**
     * 流程Id
     */
    @IsKey
    @Column(length = 32,comment = "主键")
    private String id;

    /**
     * 流程名称
     */
    @Column(length = 64,comment = "流程名称")
    private String name;




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



}
