/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.wj.updater.contact;

/**
 * 流程状态枚举
 * @author 49796
 * @version :  com.wj.updater.contact.ProcessStatusEnum.java,  v  0.1  2020/6/29  15:13  49796  Exp  $$
 */
public enum ProcessStatusEnum {


    /**
     * 节点开始执行
     */
    START("START","开始执行"),

    /**
     * 执行中
     */
    RUNNING("RUNNING","执行中!"),

    /**
     * 执行完毕，等待下个节点执行
     */
    COMPLETE("COMPLETE","执行完毕"),

    /**
     * 执行成功
     */
    SUCCESS("SUCCESS","执行成功"),

    /**
     * 执行异常
     */
    ERROR("ERROR","执行异常"),

    /**
     * 彻底执行失败
     */
    FAILED("FAILED","彻底执行失败"),


    ;

    /**
     * code
     */
    private String code;

    /**
     * 说明
     */
    private String description;


    /**
     *
     * @param code
     * @param description
     */
    ProcessStatusEnum(String code,String description){
        this.code=code;
        this.description=description;
    }

    /**
     * 获取code
     * @return
     */
    public String getCode(){
        return this.code;
    }

}
