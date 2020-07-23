/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qlj.flow.contact.NodeType;
import com.qlj.flow.entity.FlowProcess;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessNodeRecord;
import com.qlj.flow.entity.ProcessRecord;
import com.qlj.flow.service.FlowProcessService;
import com.qlj.flow.service.ProcessNodeRecordService;
import com.qlj.flow.service.ProcessNodeService;
import com.qlj.flow.service.ProcessRecordService;
import com.qlj.flow.util.TextUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 流程配置controller
 * @author 49796
 * @version :  com.qlj.flow.controller.FlowProcessController.java,  v  0.1  2020/7/10  09:39  49796  Exp  $$
 */
@RestController
@RequestMapping("/flowProcess")
public class FlowProcessController {
    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(FlowProcessController.class);


    /**
     * 流程服务类
     */
    @Resource
    private FlowProcessService processService;

    /**
     * 流程节点服务类
     */
    @Resource
    private ProcessNodeService nodeService;

    /**
     * 流程实例对象服务
     */
    @Resource
    private ProcessRecordService processRecordService;

    /**
     * 节点实例服务类
     */
    @Resource
    private ProcessNodeRecordService nodeRecordService;


    @PostMapping("/initNode")
    public boolean initNode(String processId,String lastNodeId,String filePath){
        try{
            String read = TextUtil.read(filePath);
            JSONArray array = JSONArray.parseArray(read);
            String configTemplate="{\"express\":\"\\n\\nreturn {\\n\\t\\\"${field}\\\":\\\"\\\",\\n}\"}";
            for(int i=0;i<array.size();i++){
                JSONObject jsonObject = array.getJSONObject(i);
                String name=jsonObject.getString("desc");
                String field = jsonObject.getString("field");
                ProcessNode node=new ProcessNode();
                node.setType(NodeType.CONDITION.getCode());
                node.setName(name);
                node.setLastNode(lastNodeId);
                node.setProcessId(processId);
                node.setConfig(StringUtils.replace(configTemplate,"${field}",field));
                processService.createProcessNode(node);
            }
            return true;
        }catch (Exception e){
            logger.error("初始化异常",e);
            return false;
        }
    }

    /**
     * 创建流程
     * @return 创建的流程信息
     */
    @PostMapping("/createProcess")
    public FlowProcess createProcess(String name,String code){
        FlowProcess flowProcess = new FlowProcess();
        flowProcess.setStatus(FlowProcess.STATUS_ENABLE);
        flowProcess.setName(name);
        flowProcess.setCode(code);
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
    public ProcessNode createProcessNode(String processId,String lastNode,String name,String code,String config){

        //TODO 流程状态为发布中 禁止新增节点
        ProcessNode node=new ProcessNode();
        node.setProcessId(processId);
        node.setLastNode(lastNode);
        node.setName(name);
        node.setConfig(config);
        node.setCode(code);
        node.setType(NodeType.NONE.getCode());
        processService.createProcessNode(node);
        return node;
    }


    /**
     * 更新节点配置
     * @param node
     * @return
     */
    @PostMapping("/updateProcessNode")
    public Boolean updateProcessNode(ProcessNode node){
        ProcessNode byId = nodeService.getById(node.getId());
        //TODO 流程状态为发布中 禁止修改节点
        boolean result = nodeService.updateById(node);
        return result;
    }

    /**
     * 启动流程实例
     * @return 启动流程实例
     */
    @PostMapping("/startProcess")
    public ProcessRecord startProcess(String processId, String param){
        return processService.startProcess(processId,JSONObject.parseObject(param));
    }

    /**
     * 禁用流程
     * @return 禁用流程
     */
    @PostMapping("/disabledProcess")
    public boolean disabledProcess(String id){
        processService.updateProcessStatus(id,FlowProcess.STATUS_DISABLED);
        return true;
    }

    /**
     * 启用流程
     * @return 启用流程
     */
    @PostMapping("/enabledProcess")
    public boolean enabledProcess(String id){
        processService.updateProcessStatus(id,FlowProcess.STATUS_ENABLE);
        return true;
    }

    /**
     * 根据节点查询流程
     * @param id
     * @return
     */
    @PostMapping("/queryFlowProcessById")
    public FlowProcess queryFlowProcessById(String id){
        return processService.getById(id);
    }

    /**
     *  根据流程id，查询所有节点
     * @param processId
     * @return
     */
    @PostMapping("/queryNodeListByProcessId")
    public List<ProcessNode> queryNodeListByProcessId(String processId){
        QueryWrapper<ProcessNode> query=new QueryWrapper<>();
        query.eq("process_id",processId);
        return nodeService.list(query);
    }

    /**
     *  根据流程实例id，查询流程实例对象
     * @param id
     * @return
     */
    @PostMapping("/queryProcessRecordById")
    public ProcessRecord queryProcessRecordById(String id){
        return processRecordService.getById(id);
    }

    /**
     *  根据流程实例id，查询所有节点实例
     * @param processRecordId
     * @return
     */
    @PostMapping("/queryNodeRecordByProcessRecordId")
    public List<ProcessNodeRecord> queryNodeRecordByProcessRecordId(String processRecordId){
        QueryWrapper<ProcessNodeRecord> query=new QueryWrapper<>();
        query.eq("process_record_id",processRecordId);
        return nodeRecordService.list(query);
    }

    /**
     *  查询流程列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("/page")
    public Page<FlowProcess> queryNodeRecordByProcessRecordId(@RequestParam(defaultValue = "1") Integer pageNo,
                                                              @RequestParam(defaultValue = "20") Integer pageSize,
                                                              String status,
                                                              String name){
        QueryWrapper<FlowProcess> query= new QueryWrapper<>();
        query.eq("status",status);
        if(StringUtils.isNotBlank(name)){
            query.like("name",name);
        }
        Page<FlowProcess> page = processService.page(new Page<>(pageNo,pageSize), query);
        return page;
    }
}
