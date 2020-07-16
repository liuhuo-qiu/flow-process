/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service;

import com.alibaba.fastjson.JSONObject;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessNodeRecord;
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
     *      所有异常情况统一抛出ServiceException，外层会捕获异常记录到节点执行记录中
     * @param nodeRecord 节点实例
     * @return object  节点实例的执行结果
     *          如果为异步节点则此处为发起异步处理，返回null
     *          异步处理发起失败直接抛出ServiceException
     */
    public Object doHandler(ProcessNodeRecord nodeRecord);

    /**
     * 检查节点是否执行完毕(仅有异步操作才有此草错)
     *      所有异常情况统一抛出ServiceException，外层会捕获异常记录到节点执行记录中
     *    执行完毕（未执行完毕返回null，执行失败返回抛出异常，执行成功返回节点的result）
     * @param nodeRecord
     * @return  节点实例的执行结果
     */
    default Object checkStatus(ProcessNodeRecord nodeRecord){return null;}
}
