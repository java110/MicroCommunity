package com.java110.core.cache;

import com.java110.utils.cache.Jedis;
import com.java110.utils.factory.ApplicationContextFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
public class JedisClientPool implements Jedis {
    @Override
    public String set(String key, String value) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().set(key, value);
    }

    @Override
    public String set(byte[] key, byte[] value) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().set(key, value);
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, int time) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().set(key, value,nxxx,expx,time);
    }


    @Override
    public String get(String key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().get(key);
    }

    @Override
    public byte[] get(byte[] key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().get(key);
    }

    @Override
    public Boolean exists(String key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().exists(key);
    }

    @Override
    public Long expire(String key, int seconds) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().expire(key, seconds);
    }

    @Override
    public Long ttl(String key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().ttl(key);
    }

    @Override
    public Long incr(String key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().incr(key);
    }

    @Override
    public Long hset(String key, String field, String value) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().hset(key, field, value);
    }

    @Override
    public String hget(String key, String field) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().hget(key, field);
    }

    @Override
    public Long hdel(String key, String... field) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().hdel(key, field);
    }

    @Override
    public Long del(String key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().del(key);
    }

    @Override
    public Long del(byte[] key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().del(key);
    }

    @Override
    public void close() {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        jedisPool.getResource().close();
    }

    @Override
    public Set<String> keys(String pattern) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().keys(pattern);
    }

    @Override
    public Object eval(String script, int keyCount, String... params) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().eval(script,keyCount,params);
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> args) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource().eval(script,keys,args);
    }
}
