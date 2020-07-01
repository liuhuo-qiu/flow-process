/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.wj.updater.contact;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.contact.NodeType.java,  v  0.1  2020/6/29  10:47  49796  Exp  $$
 */
public enum NodeType {
    /**
     * 远程http请求数据节点
     */
    HTTP_API("http_api"),

    /**
     * 规则节点
     */
    RULE("rule"),

    /**
     * 开始节点
     */
    START("start"),

    /**
     * 结束节点
     */
    END("end"),

    /**
     * 条件判断节点
     */
    CONDITION("condition"),

    /**
     * 审批节点
     */
    APPROVE("approve"),

    ;

    /**
     * 节点类型
     */
    private String code;

    /**
     *
     * @param code
     */
    NodeType(String code){
        this.code=code;
    }

    /**
     * 获取code
     * @return
     */
    public String getCode(){
        return code;
    }
}
