/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.schedule;

import com.qlj.flow.entity.ProcessNodeRecord;
import org.springframework.stereotype.Component;

/**
 * 节点定时任务,从节点队列取出一个待执行节点执行
 * @author 49796
 * @version :  com.wj.updater.schedule.NodeSchedule.java,  v  0.1  2020/6/29  11:37  49796  Exp  $$
 */
@Component
public class NodeSchedule extends AbstractSchedule<ProcessNodeRecord> {

    /**
     * 执行节点
     * 每个节点执行完毕之后都判断一次
     *  当前节点是否还有子节点
     *  如果有子节点，则将子节点加入执行队列
     *  如果没有子节点，则判断当前流程是否还有其他节点还在执行
     *      如果没有其他节点还在执行，则表示当前流程已经执行结束，更改流程状态
     * @param processNodeRecord
     */
    @Override
    public void execute(ProcessNodeRecord processNodeRecord) {

    }

    /**
     * 定时任务， 扫描running状态的，并且最近更新时间为5分钟以前的任务
     * 调用handler的checkStatus方法检查节点状态,并返回节点是否执行完毕
     * 如果执行完毕则更新节点状态为COMPLETE，等待执行子节点
     * 如果check异常，则记录异常信息 （如果多次  长时间处于异常信息，判定节点为执行失败）
     */
}
