/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.util;

import com.alibaba.fastjson.JSONObject;
import com.qlj.flow.entity.ProcessParam;
import com.qlj.flow.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.util.ProcessUtil.java,  v  0.1  2020/6/29  14:29  49796  Exp  $$
 */
public class ProcessUtil{
    /**
     * 参数检查
     * @return
     */
    public static boolean checkParam(List<ProcessParam> processParams, JSONObject params){
        processParams.forEach(param->{
            String value = params.getString(param.getFieldName());
            if(param.getRequired()&&StringUtils.isEmpty(value)){
                throw new ServiceException(param.getFieldName()+"不能为空");
            }
            param.setValue(value);
        });
        return true;
    }



}
