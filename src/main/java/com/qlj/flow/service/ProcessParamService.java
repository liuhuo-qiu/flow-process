/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qlj.flow.entity.ProcessParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.service.ProcessParamService.java,  v  0.1  2020/7/6  11:19  49796  Exp  $$
 */
public interface ProcessParamService extends IService<ProcessParam> {

    /**
     * 根据流程id，查询流程入参定义
     * @param processId
     * @return
     */
    List<ProcessParam> queryProcessParamList(String processId);


    /**
     * 根据流程ID和节点ID，查询节点参数
     * @param nodeId
     * @param nodeId
     * @return
     */
    List<ProcessParam> queryProcessNodeParamList(String nodeId);

    /**
     * 根据流程ID和节点ID，查询节点参数
     * @param processId
     * @param nodeId
     * @return
     */
    List<ProcessParam> queryProcessNodeParamList(String processId,String nodeId);
}
