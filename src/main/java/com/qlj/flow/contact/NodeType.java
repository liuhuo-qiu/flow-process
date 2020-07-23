/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.contact;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.contact.NodeType.java,  v  0.1  2020/6/29  10:47  49796  Exp  $$
 */
public enum NodeType {
    /**
     * 空节点
     */
    NONE("NONE"),
    /**
     * 远程http请求数据节点
     */
    HTTP_API("HTTP_API"),

    /**
     * 规则节点
     */
    RULE("RULE"),

    /**
     * 表达式引擎节点
     */
    AVIATOR("AVIATOR"),

    /**
     * 开始节点
     */
    START("START"),

    /**
     * 结束节点
     */
    END("END"),

    /**
     * 条件判断节点
     */
    CONDITION("CONDITION"),

    /**
     * 审批节点
     */
    APPROVE("APPROVE"),


    /**
     * 流程节点
     */
    PROCESS("PROCESS"),

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
