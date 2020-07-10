/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.controller;

import com.alibaba.fastjson.JSONObject;
import com.qlj.flow.entity.FlowProcess;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessRecord;
import com.qlj.flow.service.FlowProcessService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 流程配置controller
 * @author 49796
 * @version :  com.qlj.flow.controller.FlowProcessController.java,  v  0.1  2020/7/10  09:39  49796  Exp  $$
 */
@RestController
@RequestMapping("/flowProcess")
public class FlowProcessController {


    /**
     *
     */
    @Resource
    private FlowProcessService processService;

    /**
     * 创建流程
     * @return 创建的流程信息
     */
    @PostMapping("/createProcess")
    public FlowProcess createProcess(String name){
        FlowProcess flowProcess = new FlowProcess();
        flowProcess.setStatus(FlowProcess.STATUS_ENABLE);
        flowProcess.setName(name);
        processService.createProcess(flowProcess);
        return flowProcess;
    }

    /**
     * 创建流程中的节点
     * @param processId  流程id
     * @param lastNode  上一个节点id
     * @param name  当前节点名称
     * @param config  节点配置
     * @return 创建的节点信息
     */
    @PostMapping("/createProcessNode")
    public ProcessNode createProcessNode(String processId,String lastNode,String name,String config){
        ProcessNode node=new ProcessNode();
        node.setProcessId(processId);
        node.setLastNode(lastNode);
        node.setName(name);
        node.setConfig(config);
        processService.createProcessNode(node);
        return node;
    }

    /**
     * 创建流程
     * @return 创建的流程信息
     */
    @PostMapping("/startProcess")
    public ProcessRecord startProcess(String processId, String param){
        return processService.startProcess(processId,JSONObject.parseObject(param));
    }

    /**
     * 创建流程
     * @return 创建的流程信息
     */
    @PostMapping("/disabledProcess")
    public boolean disabledProcess(String id){
        processService.updateProcessStatus(id,FlowProcess.STATUS_DISABLED);
        return true;
    }

    /**
     * 创建流程
     * @return 创建的流程信息
     */
    @PostMapping("/enabledProcess")
    public boolean enabledProcess(String id){
        processService.updateProcessStatus(id,FlowProcess.STATUS_ENABLE);
        return true;
    }


}
