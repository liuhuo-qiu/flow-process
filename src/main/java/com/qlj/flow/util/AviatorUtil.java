/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.util;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 49796
 * @version :  com.qlj.flow.util.AviatorUtil.java,  v  0.1  2020/7/9  10:14  49796  Exp  $$
 */
public class AviatorUtil {
    /**
     *
     */
    private static final Logger logger= LoggerFactory.getLogger(AviatorUtil.class);
    /**
     * 引擎实例
     */
    private static final AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();

    /**
     * 初始化引擎参数
     */
    static {
        //运行时优先
        instance.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.COMPILE);
        //将所有浮点数解析为 Decimal 类型
        instance.setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
        //防止死循环，限制循环最大次数
        instance.setOption(Options.MAX_LOOP_COUNT, 9999);
        try{
            AviatorEvaluator.importFunctions(DateUtil.class);
            AviatorEvaluator.importFunctions(StringUtils.class);
            AviatorEvaluator.importFunctions(NumberUtils.class);
        }catch (Exception e){
            logger.warn("表达式引擎加载函数方法失败",e);
        }
    }

    /**
     * 获取执行引擎
     * @return
     */
    public static AviatorEvaluatorInstance getInstance(){
        return instance;
    }
}
