/**
 * www.tuanbangbang.com
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.wj.updater.exception;

/**
 *
 *  @authorr qiulujin
 *  crate ServiceException.java on  2019-08-14 18:14
 */
public class ServiceException extends RuntimeException {

    /**
     * 自定义异常码
     */
    private String errorCode;

    /**
     *
     */
    public ServiceException(){
        super();
    }

    /**
     *
     * @param message
     */
    public ServiceException(String message){
        super(message);
    }

    /**
     *
     * @param message
     * @param errorCode
     */
    public ServiceException(String message, String errorCode){
        super(message);
        this.errorCode=errorCode;
    }


    /**
     *
     * @param message
     * @param errorCode
     * @param e
     */
    public ServiceException(String message, String errorCode, Throwable e){
        super(message,e);
        this.errorCode=errorCode;
    }

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
}
