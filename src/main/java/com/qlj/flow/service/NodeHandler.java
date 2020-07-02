/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service;

import com.alibaba.fastjson.JSONObject;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessParam;

import java.util.List;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.service.NodeHandler.java,  v  0.1  2020/6/29  10:56  49796  Exp  $$
 */
public interface NodeHandler {

    /**
     * 判断node是否满足handler执行条件
     * @param node
     * @return
     */
    public boolean support(ProcessNode node);

    /**
     * 节点操作
     * @param config 节点配置
     * @param params  节点入参
     */
    public void doHandler(JSONObject config, List<ProcessParam> params);

    /**
     * 检查节点是否执行完毕
     * @param node
     * @return
     */
    public boolean checkStatus(ProcessNode node);
}
