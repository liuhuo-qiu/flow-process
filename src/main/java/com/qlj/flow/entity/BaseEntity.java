/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;

import java.util.Date;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.entity.BaseEntity.java,  v  0.1  2020/7/2  15:20  49796  Exp  $$
 */
public class BaseEntity {

    /**
     * 创建人
     */
    @Column(length = 64,comment = "创建人")
    private String creator;

    /**
     * 创建时间
     */
    @Column(type= MySqlTypeConstant.DATETIME,comment = "创建时间")
    private Date createTime;

    /**
     * 最后更改人
     */
    @Column(length = 64,comment = "最后更新人")
    private String modifier;

    /**
     * 最后更改时间
     */
    @Column(type= MySqlTypeConstant.DATETIME,comment = "最后更新时间")
    private Date modifyTime;

    /**
     * Getter  method  for  property      creator.
     *
     * @return property  value  of  creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Setter method for property   creator .
     *
     * @param creator value to be assigned to property creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Getter  method  for  property      createTime.
     *
     * @return property  value  of  createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Setter method for property   createTime .
     *
     * @param createTime value to be assigned to property createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Getter  method  for  property      modifier.
     *
     * @return property  value  of  modifier
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * Setter method for property   modifier .
     *
     * @param modifier value to be assigned to property modifier
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * Getter  method  for  property      modifyTime.
     *
     * @return property  value  of  modifyTime
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * Setter method for property   modifyTime .
     *
     * @param modifyTime value to be assigned to property modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
