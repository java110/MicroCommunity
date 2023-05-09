package com.java110.core.cache;

import com.java110.utils.cache.Jedis;
import com.java110.utils.factory.ApplicationContextFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
public class JedisClientCluster implements Jedis {
    @Override
    public String set(String key, String value) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.set(key, value);
    }

    @Override
    public String set(byte[] key, byte[] value) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.set(key, value);
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, int time) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.set(key, value,nxxx,expx,time);
    }


    @Override
    public String get(String key) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.get(key);
    }

    @Override
    public byte[] get(byte[] key) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.get(key);
    }

    @Override
    public Boolean exists(String key) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.exists(key);
    }

    @Override
    public Long expire(String key, int seconds) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.expire(key, seconds);
    }

    @Override
    public Long ttl(String key) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.ttl(key);
    }

    @Override
    public Long incr(String key) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.incr(key);
    }

    @Override
    public Long hset(String key, String field, String value) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.hset(key, field, value);
    }

    @Override
    public String hget(String key, String field) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.hget(key, field);
    }

    @Override
    public Long hdel(String key, String... field) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.hdel(key, field);
    }

    @Override
    public Long del(String key) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.del(key);
    }

    @Override
    public Long del(byte[] key) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.del(key);
    }

    @Override
    public void close() {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        try {
            jedisCluster.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> keys(String pattern) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.hkeys(pattern);
    }

    @Override
    public Object eval(String script, int keyCount, String... params) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.eval(script,keyCount,params);
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> args) {
        JedisCluster jedisCluster = (JedisCluster) ApplicationContextFactory.getBean("jedisCluster");
        return jedisCluster.eval(script,keys,args);
    }
}
