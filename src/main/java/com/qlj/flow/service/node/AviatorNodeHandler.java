/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service.node;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.qlj.flow.contact.NodeType;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessNodeRecord;
import com.qlj.flow.exception.ServiceException;
import com.qlj.flow.service.NodeHandler;
import com.qlj.flow.service.ProcessNodeService;
import com.qlj.flow.util.AviatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.service.impl.AviatorNodeHandler.java,  v  0.1  2020/7/10  16:49  49796  Exp  $$
 */
@Service
public class AviatorNodeHandler implements NodeHandler{

    /**
     * 节点服务类
     */
    @Resource
    private ProcessNodeService nodeService;
    /**
     *
     * @param node
     * @return
     */
    @Override
    public boolean support(ProcessNode node) {
        return StringUtils.equalsIgnoreCase(node.getType(), NodeType.AVIATOR.getCode());
    }

    /**
     *
     * @param nodeRecord 节点实例
     * @return
     */
    @Override
    public Object doHandler(ProcessNodeRecord nodeRecord) {
        ProcessNode node = nodeService.getById(nodeRecord.getNodeId());
        String configStr = node.getConfig();
        if(StringUtils.isBlank(configStr)){
            throw new ServiceException(String.format("节点配置错误，当前节点%s未配置config",node.getId()));
        }
        JSONObject config = JSONObject.parseObject(configStr);

        AviatorEvaluatorInstance instance = AviatorUtil.getInstance();

        Expression express = instance.compile(node.getName(), config.getString("express"), true);
        JSONObject params = JSONObject.parseObject(nodeRecord.getParams());

        return express.execute(params);
    }

}
