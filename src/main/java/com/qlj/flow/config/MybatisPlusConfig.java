/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.config;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.qlj.flow.util.UUIDUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.config.MybatisPlusConfig.java,  v  0.1  2020/7/10  15:19  49796  Exp  $$
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * id策略
     * @return
     */
    @Bean
    public IdentifierGenerator idGenerator() {
        return new DefaultIdentifierGenerator() {
            @Override
            public String nextUUID(Object entity) {
                return UUIDUtil.randomUuid();
            }
        };
    }
}
