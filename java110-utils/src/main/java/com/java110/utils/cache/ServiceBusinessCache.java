package com.java110.utils.cache;

import com.java110.utils.util.SerializeUtil;
import com.java110.entity.order.ServiceBusiness;
import com.java110.utils.cache.Jedis;

import java.util.List;

/**
 * 业务信息 缓存类
 * Created by wuxw on 2018/4/14.
 */
public class ServiceBusinessCache extends BaseCache {

    //后缀 用来刷缓存时删除 所有以这个为后缀的数据
    public final static String _KEY_SERVICE_BUSINESS = "_KEY_ALL_SERVICE_BUSINESS";

    /**
     * 获取 业务信息
     * @return
     */
    public static List<ServiceBusiness> getServiceBusiness(){
        List<ServiceBusiness> serviceBusinesses = null;
        Jedis redis = null;
        try {
            redis = getJedis();
            serviceBusinesses = SerializeUtil.unserializeList(redis.get((_KEY_SERVICE_BUSINESS).getBytes()),ServiceBusiness.class);
            if(serviceBusinesses == null || serviceBusinesses.size() ==0) {
                return null;
            }
        }finally {
            if(redis != null){
                redis.close();
            }
        }
        return serviceBusinesses;
    }


    /**
     * 保存路由信息
     * @param serviceBusinesses
     */
    public static void setServiceBusiness(List<ServiceBusiness> serviceBusinesses){
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set((_KEY_SERVICE_BUSINESS).getBytes(),SerializeUtil.serializeList(serviceBusinesses));
        }finally {
            if(redis != null){
                redis.close();
            }
        }
    }


}
