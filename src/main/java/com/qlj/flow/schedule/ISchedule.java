/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.schedule;

/**
 * 本地阻塞队列接口
 * @author 49796
 * @version :  com.wj.updater.schedule.IShedule.java,  v  0.1  2020/6/29  16:23  49796  Exp  $$
 */
public interface ISchedule<T> {


    /**
     * 往队列里添加数据
     * @param t
     * @return
     */
    boolean addData(T t);

    /**
     * 队列具体执行方法
     * @param t
     */
    void execute(T t);
}
