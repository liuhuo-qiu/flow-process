/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.common;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.Result.java,  v  0.1  2020/7/16  10:02  49796  Exp  $$
 */
public class Result<T>{

    /**
     * 系统响应码
     */
    private String code;

    /**
     * 业务异常的时候的异常码
     */
    private String errorCode;

    /**
     * 系统异常时候的异常信息
     */
    private String message;

    /**
     * 返回的数据对象
     */
    private T data;

    /**
     * Getter  method  for  property      errorCode.
     *
     * @return property  value  of  errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Setter method for property   errorCode .
     *
     * @param errorCode value to be assigned to property errorCode
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
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

    /**
     * Getter  method  for  property      message.
     *
     * @return property  value  of  message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter method for property   message .
     *
     * @param message value to be assigned to property message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter  method  for  property      data.
     *
     * @return property  value  of  data
     */
    public T getData() {
        return data;
    }

    /**
     * Setter method for property   data .
     *
     * @param data value to be assigned to property data
     */
    public void setData(T data) {
        this.data = data;
    }
}
