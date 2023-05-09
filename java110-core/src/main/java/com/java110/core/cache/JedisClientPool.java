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
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
           return jedis.set(key, value);
        }finally {
            jedis.close();
        }
    }

    @Override
    public String set(byte[] key, byte[] value) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.set(key, value);
        }finally {
            jedis.close();
        }
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, int time) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.set(key, value,nxxx,expx,time);
        }finally {
            jedis.close();
        }
    }


    @Override
    public String get(String key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.get(key);
        }finally {
            jedis.close();
        }
    }

    @Override
    public byte[] get(byte[] key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.get(key);
        }finally {
            jedis.close();
        }
    }

    @Override
    public Boolean exists(String key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.exists(key);
        }finally {
            jedis.close();
        }
    }

    @Override
    public Long expire(String key, int seconds) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.expire(key, seconds);
        }finally {
            jedis.close();
        }
    }

    @Override
    public Long ttl(String key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.ttl(key);
        }finally {
            jedis.close();
        }
    }

    @Override
    public Long incr(String key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.incr(key);
        }finally {
            jedis.close();
        }
    }

    @Override
    public Long hset(String key, String field, String value) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hset(key, field, value);
        }finally {
            jedis.close();
        }
    }

    @Override
    public String hget(String key, String field) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hget(key, field);
        }finally {
            jedis.close();
        }
    }

    @Override
    public Long hdel(String key, String... field) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hdel(key, field);
        }finally {
            jedis.close();
        }
    }

    @Override
    public Long del(String key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.del(key);
        }finally {
            jedis.close();
        }
    }

    @Override
    public Long del(byte[] key) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.del(key);
        }finally {
            jedis.close();
        }
    }

    @Override
    public void close() {
//        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
//        jedisPool.getResource().close();
    }

    @Override
    public Set<String> keys(String pattern) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.keys(pattern);
        }finally {
            jedis.close();
        }
    }

    @Override
    public Object eval(String script, int keyCount, String... params) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.eval(script,keyCount,params);
        }finally {
            jedis.close();
        }
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> args) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.eval(script,keys,args);
        }finally {
            jedis.close();
        }
    }
}
