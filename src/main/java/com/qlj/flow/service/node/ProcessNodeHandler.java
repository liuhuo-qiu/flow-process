/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.service.node;

import com.alibaba.fastjson.JSONObject;
import com.qlj.flow.contact.NodeType;
import com.qlj.flow.contact.ProcessStatusEnum;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessNodeRecord;
import com.qlj.flow.entity.ProcessRecord;
import com.qlj.flow.exception.ServiceException;
import com.qlj.flow.service.FlowProcessService;
import com.qlj.flow.service.NodeHandler;
import com.qlj.flow.service.ProcessNodeRecordService;
import com.qlj.flow.service.ProcessNodeService;
import com.qlj.flow.service.ProcessRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.service.node.ProcessNodeHandler.java,  v  0.1  2020/7/23  15:54  49796  Exp  $$
 */
@Service
public class ProcessNodeHandler  implements NodeHandler {

    /**
     *  流程服务类
     */
    @Resource
    private FlowProcessService flowProcessService;

    /**
     * 节点服务类
     */
    @Resource
    private ProcessNodeService nodeService;

    /**
     * 流程实例服务类
     */
    @Resource
    private ProcessRecordService processRecordService;

    /**
     * 节点实例服务类
     */
    @Resource
    private ProcessNodeRecordService nodeRecordService;

    /**
     *
     * @param node
     * @return
     */
    @Override
    public boolean support(ProcessNode node) {
        return StringUtils.equalsIgnoreCase(node.getType(), NodeType.PROCESS.getCode());
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
        String processId = config.getString("processId");
        JSONObject params = JSONObject.parseObject(nodeRecord.getParams());
        ProcessRecord processRecord = flowProcessService.startProcess(processId, params);

        JSONObject result=new JSONObject();
        result.put("processRecordId",processRecord.getId());
        nodeRecord.setResult(result.toJSONString());
        nodeRecordService.updateById(nodeRecord);
        //异步操作，返回null
        return null;
    }

    /**
     *
     * @param nodeRecord
     * @return
     */
    @Override
    public Object checkStatus(ProcessNodeRecord nodeRecord) {
        JSONObject result=JSONObject.parseObject(nodeRecord.getResult());
        String processRecordId = result.getString("processRecordId");
        if(null==processRecordId){
            throw new ServiceException(String.format("流程节点%s检查时无对应的流程实例",nodeRecord.getId()));
        }
        ProcessRecord processRecord = processRecordService.getById(processRecordId);
        if(StringUtils.equalsIgnoreCase(processRecord.getStatus(), ProcessStatusEnum.RUNNING.getCode())){
            return null;
        }
        if(StringUtils.equalsIgnoreCase(processRecord.getStatus(), ProcessStatusEnum.FAILED.getCode())){
            nodeRecord.setStatus(ProcessStatusEnum.FAILED.getCode());
            throw new ServiceException(processRecord.getErrorMessage());
        }
        return processRecord.getResult();
    }
}
