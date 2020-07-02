/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service.node;

import com.alibaba.fastjson.JSONObject;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessParam;
import com.qlj.flow.service.NodeHandler;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.service.node.ApiNodeHandler.java,  v  0.1  2020/6/29  10:56  49796  Exp  $$
 */
@Service
public class ApiNodeHandler implements NodeHandler {


    /**
     * 判断node是否满足apiNodeHandler执行条件
     * @param node
     * @return
     */
    @Override
    public boolean support(ProcessNode node) {
        return true;
    }

    /**
     *
     * @param config 节点配置
     * @param params  节点入参
     */
    @Override
    public void doHandler(JSONObject config, List<ProcessParam> params) {

    }


    @Override
    public boolean checkStatus(ProcessNode node) {
        return false;
    }
}