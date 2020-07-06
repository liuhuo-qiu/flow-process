/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.util;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.util.ContextUtil.java,  v  0.1  2020/7/2  15:27  49796  Exp  $$
 */
public class ContextUtil {
    /**
     * 当前用户线程变量
     */
    private static final InheritableThreadLocal<String> currentUser=new InheritableThreadLocal<>();

    /**
     * 获取当前用户
     * @return
     */
    public static String  getCurrentUser(){
        return currentUser.get();
    }

    /**
     * 设置当前用户线程变量
     * @param user
     */
    public static void setCurrentUser(String user){
        currentUser.set(user);
    }
}
