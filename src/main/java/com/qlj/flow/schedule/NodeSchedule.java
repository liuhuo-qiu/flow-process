/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.schedule;

import com.alibaba.fastjson.JSONObject;
import com.qlj.flow.contact.ProcessStatusEnum;
import com.qlj.flow.entity.ProcessNode;
import com.qlj.flow.entity.ProcessNodeRecord;
import com.qlj.flow.entity.ProcessParam;
import com.qlj.flow.exception.ServiceException;
import com.qlj.flow.service.FlowProcessService;
import com.qlj.flow.service.NodeHandler;
import com.qlj.flow.service.ProcessNodeRecordService;
import com.qlj.flow.service.ProcessNodeService;
import com.qlj.flow.service.ProcessParamService;
import com.qlj.flow.util.ProcessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 节点定时任务,从节点队列取出一个待执行节点执行
 * @author 49796
 * @version :  com.wj.updater.schedule.NodeSchedule.java,  v  0.1  2020/6/29  11:37  49796  Exp  $$
 */
@Component
public class NodeSchedule extends AbstractSchedule<ProcessNodeRecord> {

    /**
     * 日志对象
     */
    private static final Logger logger= LoggerFactory.getLogger(NodeSchedule.class);

    /**
     * 节点
     */
    @Resource
    private List<NodeHandler> nodeHandlers;

    /**
     * 节点mapper
     */
    @Resource
    private ProcessNodeService nodeService;

    /**
     * 节点参数mapper
     */
    @Resource
    private ProcessParamService paramService;


    /**
     * 流程服务类
     */
    @Resource
    private FlowProcessService flowProcessService;

    /**
     * 节点实例服务
     */
    @Resource
    private ProcessNodeRecordService nodeRecordService;

    /**
     *  TODO
     * 执行节点
     * 每个节点执行完毕之后都判断一次
     *  当前节点是否还有子节点
     *  如果有子节点，则将子节点加入执行队列
     *  如果没有子节点，则判断当前流程是否还有其他节点还在执行
     *      如果没有其他节点还在执行，则表示当前流程已经执行结束，更改流程状态
     * @param nodeRecord
     */
    @Override
    public void execute(ProcessNodeRecord nodeRecord) {
        String handlerClass="";
        try{
            nodeRecord.setStatus(ProcessStatusEnum.RUNNING.getCode());

            //参数检查
            JSONObject params = JSONObject.parseObject(nodeRecord.getParams());
            List<ProcessParam> nodeParams = paramService.queryProcessNodeParamList(nodeRecord.getNodeId());
            ProcessUtil.checkParam(nodeParams,params);

            //查找handler
            ProcessNode node = nodeService.getById(nodeRecord.getNodeId());
            NodeHandler handler=null;
            for(NodeHandler hdr:nodeHandlers){
                if(hdr.support(node)){
                    handler=hdr;
                    break;
                }
            }
            if(null==handler){
                throw new ServiceException(String.format("未找到节点类型 %s 对应的handler",node.getType()));
            }
            //执行节点
            handlerClass=handler.getClass().getSimpleName();
            Object result= handler.doHandler(nodeRecord);
            if(result==null){
                nodeRecord.setStatus(ProcessStatusEnum.COMPLETE.getCode());
                //返回null表示未执行完成，等待节点处理中
                nodeRecordService.updateById(nodeRecord);
            }else{
                nodeRecord.setResult(result.toString());
                recordExecuteFinish(nodeRecord);
            }
        }catch (Exception e){
            logger.warn(String.format("节点 %s 执行异常 %s",handlerClass,JSONObject.toJSONString(nodeRecord)),e);
            //异常时候收集异常信息
            nodeRecord.setErrorMessage(e.getMessage());
            nodeRecord.setStatus(ProcessStatusEnum.ERROR.getCode());
            nodeRecordService.updateById(nodeRecord);
        }
    }




    /**
     * 节点执行完毕之后的操作
     * @param nodeRecord
     */
    private void recordExecuteFinish(ProcessNodeRecord nodeRecord){
        //如果节点执行完毕，则查询子节点加入执行队列
        nodeRecord.setStatus(ProcessStatusEnum.SUCCESS.getCode());
        try{
            //查询出当前节点的所有子节点，加入执行队列
            List<ProcessNode> children = nodeService.getByLastNode(nodeRecord.getNodeId());
            if(!CollectionUtils.isEmpty(children)){
                children.forEach(child->{
                    flowProcessService.executeNode(nodeRecord.getProcessRecordId(),child.getId());
                });
            }else{
                //如果没有子节点，查询当前实例下是否还有执行中节点
                int count= flowProcessService.queryStartedProcessByRecordId(nodeRecord.getProcessRecordId());
                if(count>1){
                    return;
                }
                flowProcessService.updateProcessRecordStatus(nodeRecord.getProcessRecordId(),ProcessStatusEnum.SUCCESS);
            }
        }catch (Exception e){
            logger.warn(String.format("节点实例Id %s 在查询子节点并添加到执行队列时异常",nodeRecord.getId()),e);
        }
        nodeRecordService.updateById(nodeRecord);
    }

    /**
     *  TODO
     * 定时任务， 扫描running状态的，并且最近更新时间为5分钟以前的任务
     * 调用handler的checkStatus方法检查节点状态,并返回节点是否执行完毕
     * 如果执行完毕则更新节点状态为COMPLETE，等待执行子节点
     * 如果check异常，则记录异常信息 （如果多次  长时间处于异常信息，判定节点为执行失败）
     */


}
