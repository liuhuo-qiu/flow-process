/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service.node;

import com.alibaba.fastjson.JSONObject;
import com.qlj.flow.contact.NodeType;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessNodeRecord;
import com.qlj.flow.service.NodeHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
        return StringUtils.equalsIgnoreCase(node.getType(), NodeType.HTTP_API.getCode());
    }

    /**
     *
     * @param nodeRecord 节点实例
     */
    @Override
    public Object doHandler(ProcessNodeRecord nodeRecord) {
        JSONObject result=new JSONObject();

        return result;
    }
}
