/**
 * www.tuanbangbang.com
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *  @authorr qiulujin
 *  crate CorsInterceptor.java on  2019-09-07 13:54
 */
@Component
public class CorsInterceptor implements HandlerInterceptor {

    /**
     *  日志对象
     */
    private static final Logger logger= LoggerFactory.getLogger(CorsInterceptor.class);




    /**
     * 设置跨域信息
     * @param response
     */
    public Boolean setCorsConfig(HttpServletRequest request, HttpServletResponse response){
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "*");
        return true;
    }

    /**
     * 跨域拦截器
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if(!setCorsConfig(request,response)){
            return false;
        }

        // 如果是OPTIONS则结束请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }

        return true;
    }
}
