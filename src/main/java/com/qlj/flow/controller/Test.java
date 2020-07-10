/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.controller;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.qlj.flow.util.AviatorUtil;
import com.qlj.flow.util.TextUtil;

import java.util.Map;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.controller.Test.java,  v  0.1  2020/7/9  14:04  49796  Exp  $$
 */
public class Test {

    /**
     *  main 函数
     * @param args
     */
    public static void main(String[] args) {
        try{
            String read = TextUtil.read("C:\\Users\\49796\\Desktop\\覃方华税务.txt");
            JSONObject jsonObject = JSONObject.parseObject(read);
            Map<String,Object> params=new JSONObject();
            jsonObject.keySet().forEach(key->{
                params.put(key,jsonObject.get(key));
            });

            String expressStr = TextUtil.read("C:\\Users\\49796\\Desktop\\test.js");

            AviatorEvaluatorInstance instance = AviatorUtil.getInstance();
            Expression expression = instance.compile(expressStr);

            Object execute = expression.execute(params);


            System.out.println(JSONObject.toJSONString(execute));

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
