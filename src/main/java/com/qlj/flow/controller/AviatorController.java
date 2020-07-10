/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.controller;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.qlj.flow.util.AviatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 执行引擎测试
 * @author 49796
 * @version :  com.qlj.flow.controller.AviatorController.java,  v  0.1  2020/7/9  10:36  49796  Exp  $$
 */
@RestController
public class AviatorController {

    /**
     * 执行引擎
     * @param express
     * @param env
     * @return
     */
    @RequestMapping("/doAviator")
    public Object doAviator(String express, JSONObject env){
        AviatorEvaluatorInstance instance = AviatorUtil.getInstance();
        Expression expression = instance.compile(express);
        return expression.execute(env);
    }
}
