/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author qiulujin
 * @version :  com.wj.rule2.util.LambdaUtil.java,  v  0.1  2020/5/11  8:55  qiulujin  Exp  $$
 */
public class LambdaUtil {

    /**
     * stream中的lambda表达式，带index遍历
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T> Consumer<T> lambdaWithIndex(BiConsumer<T, Integer> consumer){
        final AtomicInteger index=new AtomicInteger(0);
        return t->{
            consumer.accept(t,index.getAndAdd(1));
        };
    }
}
