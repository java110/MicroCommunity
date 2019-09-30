package com.java110.utils.cache;


import com.java110.utils.factory.ApplicationContextFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 缓存基类
 * Created by wuxw on 2018/4/14.
 */
public class BaseCache /*extends LoggerEngine*/{

    protected static Jedis getJedis(){
        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
        return jedisPool.getResource();
    }

    /**
     * 删除数据
     * @param pattern
     */
    public static void removeData(String pattern){
        Jedis redis = null;
        try {
            redis = getJedis();
            Set<String> keys = redis.keys("*"+pattern);
            if(keys == null || keys.size() == 0){
                return ;
            }
            for(String key : keys){
                redis.del(key);
            }
        }finally {
            if(redis != null){
                redis.close();
            }
        }
    }

}
