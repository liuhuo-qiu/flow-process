/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.config;

import com.qlj.flow.interceptor.CorsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.config.WebAppConfigurer.java,  v  0.1  2020/7/16  10:00  49796  Exp  $$
 */
@Configuration
@EnableAsync
public class WebAppConfigurer extends WebMvcConfigurationSupport {
    /**
     *  cors拦截器
     */
    @Resource
    private CorsInterceptor corsInterceptor;

    /**
     * 将Jackson2HttpMessageConverter 处理顺序提前
     *  可避免返回string这些造成ClassCastException
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new MappingJackson2HttpMessageConverter());
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor).addPathPatterns("/**");
    }

}
