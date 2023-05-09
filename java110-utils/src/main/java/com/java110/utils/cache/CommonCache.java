package com.java110.utils.cache;

import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.java110.utils.cache.Jedis;

/**
 * Created by wuxw on 2018/5/5.
 */
public class CommonCache extends BaseCache {

    private final static Logger logger = LoggerFactory.getLogger(MappingCache.class);

    public final static int defaultExpireTime = 5 * 60;
    public final static int RESEND_DEFAULT_EXPIRETIME = 1 * 60;

    //支付默认回话
    public final static int PAY_DEFAULT_EXPIRE_TIME = 2 * 60 * 60;



    /**
     * 获取值(用户ID)
     *
     * @returne
     */
    public static String getValue(String key) {
        Jedis redis = null;
        long startTime = DateUtil.getCurrentDate().getTime();

        try {
            redis = getJedis();
            return redis.get(key);
        } finally {
            if (redis != null) {
                redis.close();
            }
            logger.debug( key + " redis 耗时：" + (DateUtil.getCurrentDate().getTime() - startTime));
        }
    }

    /**
     * 获取值(用户ID)
     *
     * @returne
     */
    public static String getAndRemoveValue(String key) {
        Jedis redis = null;
        String value = "";
        long startTime = DateUtil.getCurrentDate().getTime();

        try {
            redis = getJedis();
            value = redis.get(key);
            removeValue(key);
        } finally {
            if (redis != null) {
                redis.close();
            }
            logger.debug( key + "getAndRemoveValue redis 耗时：" + (DateUtil.getCurrentDate().getTime() - startTime));

        }
        return value;
    }

    /**
     * 保存数据
     *
     * @param key
     */
    public static void setValue(String key, String value, int expireTime) {
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set(key, value);
            redis.expire(key, expireTime);
        } finally {
            if (redis != null) {
                redis.close();
            }
        }

    }


    /**
     * 保存数据
     *
     * @param key
     */
    public static void setValue(String key, String value) {
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set(key, value);
        } finally {
            if (redis != null) {
                redis.close();
            }
        }

    }

    /**
     * 删除记录
     *
     * @param key
     */
    public static void removeValue(String key) {
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.del(key);
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 重设超时间
     *
     * @param jdi
     * @param expireTime
     */
    public static void resetExpireTime(String jdi, int expireTime) {

        Jedis redis = null;
        try {
            redis = getJedis();
            redis.expire(jdi, expireTime);
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
}
