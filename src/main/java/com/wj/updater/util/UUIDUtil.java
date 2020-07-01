/**
 * www.tuanbangbang.com
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.wj.updater.util;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * UUID
 *  @authorr qiulujin
 *  crate UUIDUtil.java on  2019-08-23 9:27
 */
public class UUIDUtil {

    /**
     * 生成uuid
     * @return
     */
    public static String randomUuid(){
        return StringUtils.replace(UUID.randomUUID().toString(),"-","");
    }
}
