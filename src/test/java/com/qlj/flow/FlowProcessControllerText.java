/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow;

import com.alibaba.fastjson.JSONObject;
import com.qlj.flow.entity.FlowProcess;
import com.qlj.flow.entity.ProcessRecord;
import com.qlj.flow.service.FlowProcessService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.FlowProcessText.java,  v  0.1  2020/7/13  09:00  49796  Exp  $$
 */
@SpringBootTest
@Transactional
public class FlowProcessControllerText {

    /**
     * 流程服务类
     */
    @Resource
    private FlowProcessService flowProcessService;

    /**
     * 测试创建和删除流程
     */
    @Test
    public void testFlowProcess(){
        FlowProcess flowProcess = new FlowProcess();
        flowProcess.setStatus(FlowProcess.STATUS_ENABLE);
        flowProcess.setName("testProcess");
        int process = flowProcessService.createProcess(flowProcess);
        Assert.isTrue(process>0,"创建流程失败!");
        Assert.isTrue(StringUtils.isNoneBlank(flowProcess.getId()),"创建流程失败!");
    }
}
