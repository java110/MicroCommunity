package com.java110.core.cache;

import com.java110.utils.cache.Jedis;
import com.java110.utils.factory.ApplicationContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * spring 内置 redis
 */
@Component
public class JedisClientTemplate implements Jedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        return "";
    }

    @Override
    public String set(byte[] key, byte[] value) {
        redisTemplate.opsForValue().set(key, value);
        return "";
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, int time) {
//        redisTemplate.opsForValue().set(key, value,nxxx,expx,time);
//        return "";
        return redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) {
                //redis.clients.jedis.Jedis jedis = (redis.clients.jedis.Jedis) connection.getNativeConnection();
                Object nativeConnection = connection.getNativeConnection();
                // 集群
                if (nativeConnection instanceof JedisCluster) {
                    return ((JedisCluster) nativeConnection).set(key, value, nxxx, expx, time);
                }

                // 单机
                if (nativeConnection instanceof redis.clients.jedis.Jedis) {
                    return ((redis.clients.jedis.Jedis) nativeConnection).set(key, value, nxxx, expx, time);
                }
                return "";
            }
        }, true).toString();
    }


    @Override
    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }

        return value.toString();
    }

    @Override
    public byte[] get(byte[] key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return (byte[]) value;
    }

    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long expire(String key, int seconds) {
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        return redisTemplate.opsForValue().getOperations().getExpire(key);
    }

    @Override
    public Long ttl(String key) {
        return redisTemplate.opsForValue().getOperations().getExpire(key);
    }

    @Override
    public Long incr(String key) {
        return redisTemplate.getConnectionFactory().getConnection().incr(redisTemplate.getKeySerializer().serialize(key));
    }

    @Override
    public Long hset(String key, String field, String value) {
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        redis.clients.jedis.Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hset(key, field, value);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String hget(String key, String field) {
        redisTemplate.opsForZSet().add(key, field, 1);
        return "";
    }

    @Override
    public Long hdel(String key, String... field) {
        return redisTemplate.opsForZSet().remove(key, field);
    }

    @Override
    public Long del(String key) {
        redisTemplate.delete(key);
        return 1L;
    }

    @Override
    public Long del(byte[] key) {
        redisTemplate.delete(key);
        return 1L;
    }

    @Override
    public void close() {
//        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
//        jedisPool.getResource().close();
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public Object eval(String script, int keyCount, String... params) {
        return null;
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> args) {
        Object exeRet = redisTemplate.execute((RedisCallback<Object>) connection -> {
            Object nativeConnection = connection.getNativeConnection();
            // 集群
            if (nativeConnection instanceof JedisCluster) {
                return ((JedisCluster) nativeConnection).eval(script, keys, args);
            }

            // 单机
            if (nativeConnection instanceof redis.clients.jedis.Jedis) {
                return ((redis.clients.jedis.Jedis) nativeConnection).eval(script, keys, args);
            }

            return null;
        });
        //return redisTemplate.execute(redisScript, keys, args);
        return exeRet;
    }
}
