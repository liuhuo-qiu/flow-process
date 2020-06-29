/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.wj.updater.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author qiulujin
 * @version :  com.wj.intaller.updater.controller.UpdateController.java,  v  0.1  2020/5/18  10:00  qiulujin  Exp  $$
 */
@RestController
@RequestMapping("/update")
public class UpdateController {
    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(UpdateController.class);

    /**
     * 检查是否有系统更新
     * @param appName  客户端名称
     * @param v  客户端当前版本
     * @return  {
     *     url:""   //新版本下载地址，如果没有新版本则返回空
     * }
     */
    @RequestMapping("/check/{appName}")
    public JSONObject checkUpdate(String v, @PathVariable("appName") String appName){
        JSONObject result=new JSONObject();
        result.put("url","");
        result.put("appName",appName);

        result.put("v",v);
        logger.warn("appName:"+appName+",  version :"+v);
        return result;
    }
}
