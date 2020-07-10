/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qlj.flow.entity.ProcessParam;
import com.qlj.flow.mapper.ProcessParamMapper;
import com.qlj.flow.service.ProcessParamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.service.impl.ProcessParamService.java,  v  0.1  2020/7/6  11:26  49796  Exp  $$
 */
@Service
public class ProcessParamServiceImpl extends ServiceImpl<ProcessParamMapper,ProcessParam> implements ProcessParamService {

    /**
     * 根据流程id，查询流程入参定义
     * @param processId
     * @return
     */
    @Override
    public List<ProcessParam> queryProcessParamList(String processId) {
        return getBaseMapper().queryProcessParamList(processId);
    }

    /**
     * 根据流程ID和节点ID，查询节点参数
     * @param processId
     * @param nodeId
     * @return
     */
    @Override
    public List<ProcessParam> queryProcessNodeParamList(String processId, String nodeId) {
        return getBaseMapper().queryProcessNodeParamList(processId,nodeId);
    }

    /**
     * 根据流程ID和节点ID，查询节点参数
     * @param nodeId
     * @param nodeId
     * @return
     */
    @Override
    public List<ProcessParam> queryProcessNodeParamList(String nodeId) {
        return getBaseMapper().queryProcessNodeParamListByNodeId(nodeId);
    }
}
