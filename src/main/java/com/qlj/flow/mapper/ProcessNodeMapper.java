/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qlj.flow.entity.ProcessNode;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.mapper.ProcessNodeMapper.java,  v  0.1  2020/6/29  14:30  49796  Exp  $$
 */
public interface ProcessNodeMapper extends BaseMapper<ProcessNode> {

    String COMMON_COLUMN=" `id`,`type`,`name`,`config`,last_node as lastNode ";

    /**
     * 根据流程id查询流程开始节点
     * @param processId
     * @return
     */
    @Select("select "+COMMON_COLUMN+" from process_node where process_id=#{processId} and `type`='START'")
    ProcessNode queryStartNode(String processId);


}
