/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qlj.flow.entity.ProcessParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.mapper.ProcessParamMapper.java,  v  0.1  2020/6/29  14:31  49796  Exp  $$
 */
public interface ProcessParamMapper  extends BaseMapper<ProcessParam> {

    String COMMON_COLUMN=" id,process_id as processId,node_id as nodeId,`type`,field_name as fieldName," +
            "field_type as fieldType,`required`,`value` ";

    /**
     * 根据流程id，查询流程入参定义
     * @param processId
     * @return
     */
    @Select("select "+COMMON_COLUMN+" from process_param where process_id=#{processId} and `type`='PROCESS'")
    List<ProcessParam> queryProcessParamList(String processId);

    /**
     * 根据流程ID和节点ID，查询节点参数
     * @param processId
     * @param nodeId
     * @return
     */
    @Select("select "+COMMON_COLUMN+" from process_param where process_id=#{processId} and `type`='NODE' and node_id=#{nodeId}")
    List<ProcessParam> queryProcessNodeParamList(@Param("processId") String processId,@Param("nodeId") String nodeId);

    /**
     * 根据节点id查询节点参数
     * @param nodeId
     * @return
     */
    @Select("select "+COMMON_COLUMN+" from process_param where `type`='NODE' and node_id=#{nodeId}")
    List<ProcessParam> queryProcessNodeParamListByNodeId(@Param("nodeId") String nodeId);
}
