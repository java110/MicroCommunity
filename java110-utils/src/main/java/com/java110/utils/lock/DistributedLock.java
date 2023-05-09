package com.java110.utils.lock;

import com.java110.utils.cache.BaseCache;
import com.java110.utils.cache.Jedis;

import java.util.Collections;
import java.util.UUID;

/**
 * 分布式事务锁
 * add by wuxw 2020-01-11
 */
public class DistributedLock extends BaseCache {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final int DEFAULT_EXPIRE_TIME = 1000;


    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getLockUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 等待获取锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return
     */
    public static boolean waitGetDistributedLock(String lockKey, String requestId) {
        return waitGetDistributedLock(lockKey, requestId, DEFAULT_EXPIRE_TIME);
    }

    public static boolean waitGetDistributedLock(String lockKey, String requestId, int expireTime) {
        Jedis redis = null;
        try {
            redis = getJedis();
            while (true) {
                if (tryGetDistributedLock(redis, lockKey, requestId, expireTime)) {
                    return true;
                }
            }
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }


    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    private static boolean tryGetDistributedLock(Jedis redis, String lockKey, String requestId, int expireTime) {
        String result = redis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis redis = null;
        try {
            redis = getJedis();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = redis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
}
