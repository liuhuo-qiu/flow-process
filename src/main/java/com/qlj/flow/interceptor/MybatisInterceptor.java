/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.interceptor;

import com.qlj.flow.util.ClassUtil;
import com.qlj.flow.util.ContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * 自定义mybatis拦截器,注入creator  createTime  modifier modifiTime
 * @author 49796
 * @version :  com.qlj.flow.MybatisInterceptor.java,  v  0.1  2020/7/2  15:42  49796  Exp  $$
 */
@Component
@Intercepts({@Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
)})
public class MybatisInterceptor implements Interceptor {

    /**
     * 日志对象
     */
    private static final Logger logger= LoggerFactory.getLogger(MybatisInterceptor.class);

    /**
     * 遍历参数
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (SqlCommandType.INSERT != sqlCommandType &&SqlCommandType.UPDATE != sqlCommandType  ) {
            return invocation.proceed();
        }

        Object parameter = invocation.getArgs()[1];
        //如果insert 和 update时 遇到BaseEntity 则注入创建人、创建时间、修改人、修改时间、id
        List<Field> fieldList = ClassUtil.getFieldList(parameter);
        if (parameter == null) {
            return invocation.proceed();
        }
        if(SqlCommandType.UPDATE == sqlCommandType&&parameter instanceof MapperMethod.ParamMap){
            MapperMethod.ParamMap<?> p = (MapperMethod.ParamMap<?>) parameter;
            if (p.containsKey("et")) {
                parameter = p.get("et");
            } else {
                parameter = p.get("param1");
            }
            if (parameter == null) {
                return invocation.proceed();
            }
            fieldList = ClassUtil.getFieldList(parameter);
        }


        for(Field field:fieldList){
            try{
                if(StringUtils.equalsIgnoreCase(field.getName(),"modifyTime")){
                    setFieldValue(field,parameter,new Date());
                }
                if(StringUtils.equalsIgnoreCase(field.getName(),"modifier")){
                    setFieldValue(field,parameter,ContextUtil.getCurrentUser());
                }
                if(SqlCommandType.UPDATE == sqlCommandType){
                    continue;
                }
                if(StringUtils.equalsIgnoreCase(field.getName(),"creator")){
                    setFieldValue(field,parameter,ContextUtil.getCurrentUser());
                }
                if(StringUtils.equalsIgnoreCase(field.getName(),"createTime")){
                    setFieldValue(field,parameter,new Date());
                }

            }catch (Exception e){
                logger.debug(String.format("mybatis拦截器设置值失败 %s",field.getName()),e);
            }
        }
        return invocation.proceed();
    }

    /**
     * 设置值
     * @param field
     * @param parameter
     * @param value
     * @throws IllegalAccessException
     */
    private void setFieldValue(Field field, Object parameter,Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(parameter, value);
        field.setAccessible(false);
    }
}
