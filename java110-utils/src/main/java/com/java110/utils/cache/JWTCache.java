package com.java110.utils.cache;

import com.java110.utils.cache.Jedis;

/**
 * Created by wuxw on 2018/5/5.
 */
public class JWTCache extends BaseCache{

    /**
     * 获取值(用户ID)
     * @returne
     */
    public static String getValue(String jdi){
        Jedis redis = null;
        try {
            redis = getJedis();
            return redis.get(jdi);
        }finally {
            if(redis != null){
                redis.close();
            }
        }
    }

    /**
     * 保存数据
     * @param jdi
     */
    public static void setValue(String jdi,String userId,int expireTime){
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set(jdi,userId);
            redis.expire(jdi,expireTime);
        }finally {
            if(redis != null){
                redis.close();
            }
        }

    }

    /**
     * 删除记录
     * @param jdi
     */
    public static void removeValue(String jdi){
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.del(jdi);
        }finally {
            if(redis != null){
                redis.close();
            }
        }
    }

    /**
     * 重设超时间
     * @param jdi
     * @param expireTime
     */
    public static void resetExpireTime(String jdi,int expireTime){

        Jedis redis = null;
        try {
            redis = getJedis();
            redis.expire(jdi,expireTime);
        }finally {
            if(redis != null){
                redis.close();
            }
        }
    }
}
