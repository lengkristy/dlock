/**
 * projectName: dlock
 * fileName: DLockServiceRedisImpl.java
 * packageName: com.moon.dlock.redis
 * date: 2019-11-12 10:08
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.redis;

import com.moon.dlock.core.DLockService;
import com.moon.dlock.entity.DLockInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: DLockServiceRedisImpl
 * @packageName: com.moon.dlock.redis
 * @description: 通过redis实现的分布式锁
 * @data: 2019-11-12 10:08
 **/
@Service
public class DLockServiceRedisImpl implements DLockService{

    //定义全局变量
    /**redis返回的状态值，获取锁成功*/
    private static final String LOCK_SUCCESS = "OK";
    /**写入redis的NX值，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作*/
    private static final String SET_IF_NOT_EXIST = "NX";
    /**给redis设置时间超时,PX为毫秒*/
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    private static Logger LOGGER = LoggerFactory.getLogger(DLockServiceRedisImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean tryGetDistributedLock(DLockInfo dLockInfo) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
        }catch (Exception e){
            LOGGER.error("获取Jedit失败",e);
            return false;
        }
        try {
            String result = jedis.set(dLockInfo.getName(), dLockInfo.getRequestId(), SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, dLockInfo.getExpireTime());

            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            LOGGER.error("获取分布式锁[" + dLockInfo.getName() + "]失败",e);
            return false;
        }finally {
            jedis.close();
        }
    }

    @Override
    public boolean releaseDistributedLock(DLockInfo dLockInfo) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
        }catch (Exception e){
            LOGGER.error("获取Jedit失败",e);
            return false;
        }
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(dLockInfo.getName()), Collections.singletonList(dLockInfo.getRequestId()));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
        }catch (Exception e){
            LOGGER.error("释放分布式锁[" + dLockInfo.getName() + "]失败",e);
        }finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 获取jedis客户端
     * @return
     */
    private Jedis getJedis()throws Exception{
        Field jedisField = JedisConnection.class.getDeclaredField("jedis");
        jedisField.setAccessible(true);
        Jedis jedis = (Jedis)jedisField.get(this.redisTemplate.getConnectionFactory().getConnection());
        return jedis;
    }
}