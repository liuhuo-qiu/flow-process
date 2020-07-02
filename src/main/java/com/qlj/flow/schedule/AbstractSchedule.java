/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.schedule.AbstractSchedule.java,  v  0.1  2020/6/29  16:24  49796  Exp  $$
 */
public abstract class AbstractSchedule<T> implements ISchedule<T> {
    /**
     * 日志对象
     **/
    private static final Logger logger = LoggerFactory.getLogger(AbstractSchedule.class);


    /**
     * 阻塞队列对象,队列长度设定为1000
     */
    private final LinkedBlockingQueue<T> queue=new LinkedBlockingQueue<T>(1000);



    /**
     * 初始化方法
     */
    @PostConstruct
    public void init(){
        logger.warn("-----队列初始化开始------------------");
        new Thread(()->{
            while (true){
                try {
                    T poll = queue.take();
                    if(null!=poll){
                        execute(poll);
                    }
                }catch (Exception e){
                    logger.error("队列执行失败:"+e.getMessage(),e);
                }
            }
        }).start();
    }

    /**
     * 往队列添加数据
     * @param t
     */
    @Override
    public boolean addData(T t){
        try {
            queue.put(t);
            return true;
        }catch (Throwable e){
            return false;
        }
    }

    /**
     * 具体业务执行方法
     * @param t
     */
    @Override
    public abstract void execute(T t);
}
