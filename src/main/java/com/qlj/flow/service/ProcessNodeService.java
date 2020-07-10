/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qlj.flow.entity.ProcessNode;

import java.util.List;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.service.ProcessNodeService.java,  v  0.1  2020/7/6  11:19  49796  Exp  $$
 */
public interface ProcessNodeService extends IService<ProcessNode> {

    /**
     * 根据上一个节点id，查询所有子节点
     * @param lastNodeId
     * @return
     */
    List<ProcessNode> getByLastNode(String lastNodeId);
}
