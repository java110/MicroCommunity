package com.java110.utils.cache;

import com.java110.utils.util.SerializeUtil;
import com.java110.entity.service.ServiceSql;
import com.java110.utils.cache.Jedis;

/**
 * 缓存
 * Created by wuxw on 2018/4/19.
 */
public class ServiceSqlCache extends BaseCache {

    //后缀 用来刷缓存时删除 所有以这个为后缀的数据
    public final static String _SUFFIX_SERVICE_SQL = "_SUFFIX_SERVICE_SQL";

    /**
     * 查询 服务sql
     * @param serviceCode
     * @return
     */
    public static ServiceSql getServiceSql(String serviceCode){
        Jedis redis = null;
        try {
            redis = getJedis();
            Object obj = SerializeUtil.unserialize(redis.get((serviceCode+_SUFFIX_SERVICE_SQL).getBytes()));
            if(obj instanceof ServiceSql){
                return (ServiceSql) obj;
            }
        }finally {
            if(redis != null){
                redis.close();
            }
        }
        return null;
    }

    /**
     * 存储对象
     * @param serviceSql
     */
    public static void setServiceSql(ServiceSql serviceSql){
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set((serviceSql.getServiceCode()+_SUFFIX_SERVICE_SQL).getBytes(),SerializeUtil.serialize(serviceSql));
        }finally {
            if(redis != null){
                redis.close();
            }
        }
    }
}
