package com.java110.utils.cache;


import com.java110.utils.factory.ApplicationContextFactory;

import java.util.Set;

/**
 * 缓存基类
 * Created by wuxw on 2018/4/14.
 */
public class BaseCache {

    public final static String JEDIS_DEFAULT_POOL = "jedisClientTemplate"; // 单节点模式 jedisClientPool  集群模式 jedisClientCluster



    protected static Jedis getJedis(){
//        JedisPool jedisPool = (JedisPool) ApplicationContextFactory.getBean("jedisPool");
//        return jedisPool.getResource();
        //Jedis jedis = (Jedis) ApplicationContextFactory.getBean("jedisClientCluster");
        Jedis jedis = (Jedis) ApplicationContextFactory.getBean(JEDIS_DEFAULT_POOL);

        return jedis;
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
