/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.util.FlowApplication.java,  v  0.1  2020/7/2  15:09  49796  Exp  $$
 */

@SpringBootApplication(scanBasePackages = {"com.qlj.flow","com.gitee.sunchenbin.mybatis.actable.manager.*"})
@MapperScan(basePackages ={"com.qlj.flow.mapper","com.gitee.sunchenbin.mybatis.actable.dao.*"})
public class FlowApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(com.qlj.flow.FlowApplication.class, args);
    }

}
