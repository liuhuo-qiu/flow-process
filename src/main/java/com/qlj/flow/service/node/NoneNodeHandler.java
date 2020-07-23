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
 * @version :  com.qlj.flow.service.node.StartNodeHandler.java,  v  0.1  2020/7/10  16:47  49796  Exp  $$
 */
@Service
public class NoneNodeHandler implements NodeHandler {

    /**
     *
     * @param node
     * @return
     */
    @Override
    public boolean support(ProcessNode node) {
        return StringUtils.equalsIgnoreCase(node.getType(), NodeType.NONE.getCode());
    }

    /**
     *
     * @param nodeRecord 节点实例
     * @return
     */
    @Override
    public Object doHandler(ProcessNodeRecord nodeRecord) {
        return new JSONObject();
    }

}
