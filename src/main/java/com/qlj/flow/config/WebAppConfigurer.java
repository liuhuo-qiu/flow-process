/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.qlj.flow.exception.ServiceException;
import com.qlj.flow.interceptor.CorsInterceptor;
import com.qlj.flow.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

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
     * 注入RequestMappingHandlerAdapter
     */
    @Resource
    private RequestMappingHandlerAdapter handlerAdapter;

    /**
     * 将Jackson2HttpMessageConverter 处理顺序提前
     *  可避免返回string这些造成ClassCastException
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 1.定义一个converters转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        // 3.在converter中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 4. 添加mediaTypes
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);

        converters.add(0, fastConverter);
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor).addPathPatterns("/**");
    }

    /**
     * 自定义参数转换
     */
    @PostConstruct
    public void initEditableValidation(){
        ConfigurableWebBindingInitializer webBindingInitializer =(ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();

        if(webBindingInitializer!=null){
            GenericConversionService conversionService =(GenericConversionService) webBindingInitializer.getConversionService();
            if(null==conversionService){
                return;
            }
            conversionService.addConverter(new Converter<String, Date>(){
                @Override
                public Date convert(String s) {
                    if(StringUtils.equalsIgnoreCase(s,"null")){
                        return null;
                    }
                    if(StringUtils.equalsIgnoreCase(s,"undefined")){
                        return null;
                    }
                    if(StringUtils.indexOf(s,":")<0){
                        try{
                            long time = NumberUtils.toLong(s);
                            return new Date(time);
                        }catch (Exception e){
                            throw new ServiceException(String.format("日期格式转换错误:%s, %s",s,e.getMessage()));
                        }
                    }
                    for(String format: DateUtil.ALL_FORMAT){
                        try{
                            Date parse = DateUtil.parse(s, format);
                            if(null!=parse){
                                return parse;
                            }
                        }catch (Exception e){

                        }
                    }
                    throw new ServiceException("日期格式转换错误:"+s);
                }
            });
        }

    }

    /**
     * 初始化一个线程池
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(20);
        // 设置最大线程数
        executor.setMaxPoolSize(200);
        // 设置队列容量
        executor.setQueueCapacity(20);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("tbb-thread-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}
