package com.java110.common.cache;

import com.java110.common.factory.ApplicationContextFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 缓存基类
 * Created by wuxw on 2018/4/14.
 */
public class BaseCache {

    protected static Jedis getJedis(){
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedis.pool");
        return jedisPool.getResource();
    }

}
