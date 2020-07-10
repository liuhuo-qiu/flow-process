/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.mapper.ProcessNodeMapper;
import com.qlj.flow.service.ProcessNodeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.service.impl.ProcessNodeServiceImpl.java,  v  0.1  2020/7/6  11:27  49796  Exp  $$
 */
@Service
public class ProcessNodeServiceImpl extends ServiceImpl<ProcessNodeMapper, ProcessNode> implements ProcessNodeService {

    /**
     * 根据上一个节点id，查询所有子节点
     * @param lastNodeId
     * @return
     */
    @Override
    public List<ProcessNode> getByLastNode(String lastNodeId) {
        QueryWrapper<ProcessNode> query=new QueryWrapper<>();
        query.eq("last_node",lastNodeId);
        return list(query);
    }
}
