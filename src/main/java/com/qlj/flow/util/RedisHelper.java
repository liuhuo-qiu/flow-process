/**
 * hpx.com Inc.
 * Copyright (c) 2012-YEARAll Rights Reserved.
 */
package com.qlj.flow.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**  
 * @author qlj  
 * @version $Id: RedisHelper.java, v 0.1 2019-10-30 17:00 legend Exp $$ 
 */
@Component
public class RedisHelper {

    /**
     * 日志对象
     **/
    private static final Logger logger = LoggerFactory.getLogger(RedisHelper.class);

    /**
     * rediTemplate
     */
     @Autowired
     private StringRedisTemplate redisTemplate;

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time){
        try {
            if(time>0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常",e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("redis操作异常",e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }
    /**
     * 获取redis缓存
     * @param key
     * @return
     */
    public String getValue(String key){
        Object o = redisTemplate.opsForValue().get(key);
        if(null!=o){
            return o.toString();
        }
        return "";
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常",e);
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, String value, long time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常",e);
            return false;
        }
    }

    /**
     * 添加一个hashMap到redis
     * @param key
     * @param map
     * @return
     */
    public boolean setHash(String key, Map<String, String> map){
        try{
            redisTemplate.opsForHash().putAll(key,map);
            return true;
        }catch(Exception e){
            logger.error("redis操作异常",e);
        }
        return false;
    }

    /**
     * 添加一个value到hash
     * @param key
     * @param filed
     * @param value
     * @return
     */
    public boolean setHash(String key, String filed, String value){
        try{
            redisTemplate.opsForHash().put(key,filed,value);
            return true;
        }catch(Exception e){
            logger.error("redis操作异常",e);
        }
        return false;
    }

    /**
     * 获取一个hash的所有value
     * @param key
     * @return
     */
    public List<Object> getAllHashValue(String key){
       return redisTemplate.opsForHash().values(key);
    }

    /**
     * 获取hash中的指定value
     * @param key
     * @param filed
     * @return
     */
    public Object getHashValue(String key, String filed){
        return redisTemplate.opsForHash().get(key,filed);
    }

    /**
     *  获取整个hash
     * @param key
     * @return
     */
    public Map<Object, Object> getHashEntry(String key){
       return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 移除hash中的value
     * @param key
     * @param fileds
     * @return
     */
    public boolean removeHash(String key, String fileds){
        try{
            redisTemplate.opsForHash().delete(key,fileds);
            return true;
        }catch(Exception e){
            logger.error("redis操作异常",e);
        }
        return false;
    }

    /**
     * 移除hash中的value
     * @param key
     * @param fileds
     * @return
     */
    public boolean removeHash(String key, List<String> fileds){
        try{
            redisTemplate.opsForHash().delete(key,fileds.toArray());
            return true;
        }catch(Exception e){
            logger.error("redis操作异常",e);
        }
        return false;
    }

    /**
     *  对BigDecimal类型的value 进行加操作， 仅支持8位小数的数值运算
     * @param key
     * @param v
     * @return  累加之后的值
     */
    public BigDecimal increment(String key, BigDecimal v){
        Object o = getValue(key);
        BigDecimal add= BigDecimal.ZERO;
        if(null!=o){
            BigDecimal rv = new BigDecimal(o.toString());
            add= MathUtil.add(rv, v, 12);
        }else{
            add=v;
        }
        set(key,add.toPlainString());
        return add;
    }

    /**
     *  对数值类型的value 进行加操作
     * @param key
     * @param v
     * @return
     */
    public long increment(String key, long v){
        Object o = getValue(key);
        Long add=0L;
        if(null!=o){
            Long rv = Long.valueOf(o.toString());
            add =rv+v;
        }else{
            add=v;
        }
        set(key,add.toString());
        return add;
    }

    /**
     *  对BigDecimal类型的value 进行加操作， 仅支持8位小数的数值运算
     * @param key
     * @param v
     * @return  累加之后的值
     */
    public BigDecimal increment(String key, String filed, BigDecimal v){
        Object o = getHashValue(key,filed);
        BigDecimal add= BigDecimal.ZERO;
        if(null!=o){
            BigDecimal rv = new BigDecimal(o.toString());
            add= MathUtil.add(rv, v, 12);
        }else{
            add=v;
        }
        setHash(key,filed,add.toPlainString());
        return add;
    }

    /**
     *  对数值类型的value 进行加操作
     * @param key
     * @param v
     * @return
     */
    public long increment(String key, String filed, long v){
        Object o = getHashValue(key,filed);
        Long add=0L;
        if(null!=o){
            Long rv = Long.valueOf(o.toString());
            add =rv+v;
        }else{
            add=v;
        }
        setHash(key,filed,add.toString());
        return add;
    }


    /**
     *  对BigDecimal类型的value 进行加操作， 仅支持8位小数的数值运算
     * @param key
     * @param v
     * @return  累加之后的值
     */
    public BigDecimal increment(String key, String filed, BigDecimal v, int scale){
        Object o = getHashValue(key,filed);
        BigDecimal add= BigDecimal.ZERO;
        if(null!=o){
            BigDecimal rv = new BigDecimal(o.toString());
            add= MathUtil.add(rv, v, scale);
        }else{
            v.setScale(scale, BigDecimal.ROUND_HALF_UP);
            add=v;
        }
        setHash(key,filed,add.toPlainString());
        return add;
    }

    /**
     * 增加一个有序集合
     * @param key    key
     * @param value  value  唯一
     * @param source   排序分数
     */
    public void addZSet(String key, String value, double source){
        redisTemplate.opsForZSet().add(key,value,source);
    }

    /**
     * 从有序集合中移除一个元素
     * @param key
     * @param value
     */
    public void removeZSet(String key, String value){
        redisTemplate.opsForZSet().remove(key,value);
    }

    /**
     * 返回指定区间内的元素，顺序由小到大
     * @param key    key
     * @param start	 开始坐标
     * @param end	结束坐标
     * @return
     */
    public Set<String> getZSet(String key, long start, long end){
        Set<String> range = redisTemplate.opsForZSet().range(key, start, end);
        return range;
    }

    /**
     * 查询当前集合中的记录数
     * @param key
     * @return
     */
    public Long countZSet(String key){
        Long count = redisTemplate.opsForZSet().zCard(key);
        return count;
    }


    /**
     * 返回指定区间内的元素，顺序由大到小
     * @param key    key
     * @param start	 开始坐标
     * @param end	结束坐标
     * @return
     */
    public Set<String> getReverseZSet(String key, long start, long end){
        Set<String> range = redisTemplate.opsForZSet().reverseRange(key, start, end);
        return range;
    }


    /**
     *  在列表头部增加一个元素，如果列表不存在则创建一个新列表
     * @param key   列表的key
     * @param value  插入的value
     * @return  插入后的列表长度
     */
    public Long leftPush(String key, String value){
        Long count = redisTemplate.opsForList().leftPush(key, value);
        return count;
    }

    /**
     *  在列表尾部增加一个元素，如果列表不存在则创建一个新列表
     * @param key   列表的key
     * @param value  插入的value
     * @return  插入后的列表长度
     */
    public Long rightPush(String key, String value){
        Long count = redisTemplate.opsForList().rightPush(key, value);
        return count;
    }

    /**
     * 弹出列表最左边的一个元素，弹出之后，此元素不再存在列表中
     * @param key  列表的key
     * @return
     */
    public String leftPop(String key){
        Object value = redisTemplate.opsForList().leftPop(key);
        if(null!=value){
            return value.toString();
        }
        return "";
    }



    /**
     * 弹出列表最右边的一个元素，弹出之后，此元素不再存在列表中
     * @param key  列表的key
     * @return
     */
    public String rightPop(String key){
        Object value = redisTemplate.opsForList().rightPop(key);
        if(null!=value){
            return value.toString();
        }
        return "";
    }

    /**
     * 获取列表指定位置的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> getList(String key, int start, int end){
         return redisTemplate.opsForList().range(key, start, end);
    }


    /**
     * 获取列表类型数据的长度
     * @param key
     * @return
     */
    public Long countArray(String key){
        Long size = redisTemplate
                .opsForList().size(key);
        return size;
    }




}
