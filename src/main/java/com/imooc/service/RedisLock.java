package com.imooc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author 张进文
 * @ClassName RedisLock
 * @Description TODO
 * @Date 2019/3/13 16:54
 * @Version 1.0
 */
@Component
@Slf4j
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;


    /** *
     * @description: 加锁
     * @author: 张进文
     * @param key
     * @param value
     * @return: boolean
     * @date: 2019/3/13 16:57
     * @version: 1.0
     */
    public boolean lock(String key,String value){
       if( redisTemplate.opsForValue().setIfAbsent(key,value)){
           return true;
       }

       String currentValue=redisTemplate.opsForValue().get(key);
       log.info("当前时间......."+currentValue);
       //如果锁过期
        if(!StringUtils.isEmpty(currentValue)&&Long.parseLong(currentValue)<System.currentTimeMillis()){
            //获取上一个锁的时间
            String oldValue=redisTemplate.opsForValue().getAndSet(key, value);
            if(!StringUtils.isEmpty(oldValue)&&oldValue.equals(currentValue)){
                return true;
            }
        }

        return false;
    }


    public void unlock(String key,String value){
        String currentValue=redisTemplate.opsForValue().get(key);
        if(!StringUtils.isEmpty(currentValue)&&currentValue.equals(value)){
            redisTemplate.opsForValue().getOperations().delete(key);
        }

    }

}
