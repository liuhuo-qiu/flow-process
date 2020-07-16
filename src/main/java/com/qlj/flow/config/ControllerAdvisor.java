/**
 * www.tuanbangbang.com
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.config;

import com.alibaba.fastjson.JSONObject;
import com.qlj.flow.common.Result;
import com.qlj.flow.exception.ServiceException;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 *  controller统一返回对象
 *  @authorr qiulujin
 *  crate ControllerAdvisor.java on  2019-08-15 14:33
 */
@ControllerAdvice
public class ControllerAdvisor implements ResponseBodyAdvice<Object> {

    /**
     * 日志对象
     */
    private Logger logger= LoggerFactory.getLogger(ControllerAdvisor.class);



    /**
     * 全局异常处理
     * @param throwable
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value= Throwable.class)
    public Result errorHandler(Throwable throwable){
        Result result=new Result();
        result.setCode("500");
        if(throwable instanceof ServiceException){
            ServiceException se=(ServiceException)throwable;
            if(StringUtils.equalsIgnoreCase("NEED_LOGIN",se.getErrorCode())){
                result.setCode("403");
            }
            result.setErrorCode(se.getErrorCode());
            result.setMessage(se.getMessage());
            if(!StringUtils.equalsIgnoreCase("NOT_LOGIN",se.getErrorCode())){
                logger.error(se.getMessage(),throwable);
            }
        }else if(throwable instanceof DuplicateKeyException){
            result.setErrorCode("DUPLICATE");
            result.setMessage("重复操作");
            logger.error(throwable.getMessage(),throwable);
        }else if(throwable instanceof ClientAbortException ||
                throwable instanceof RedisSystemException){
            result.setErrorCode("500");
        }else{
            result.setErrorCode(throwable.getClass().getName());
            result.setMessage(throwable.getMessage());
            logger.error("捕捉到全局异常",throwable);
        }
        return result;
    }

    /**
     * 是否能进行处理
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * 统一返回result对象
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if(null==o){
            Result<Object> result=new Result<>();
            result.setCode("200");
            return result;
        }
        //已经是result对象的不用再封装一层
        if(o instanceof Result){
            return o;
        }
        Result<Object> result=new Result<>();
        result.setCode("200");
        result.setData(o);
        return result;
    }


}
