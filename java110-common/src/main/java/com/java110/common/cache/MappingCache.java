package com.java110.common.cache;

import com.java110.common.constant.DomainContant;
import com.java110.common.util.SerializeUtil;
import com.java110.entity.mapping.Mapping;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * 映射缓存工具类
 * Created by wuxw on 2018/4/14.
 */
public class MappingCache extends BaseCache {

    //后缀 用来刷缓存时删除 所有以这个为后缀的数据
    public final static String _SUFFIX_MAPPING = "_SUFFIX_MAPPING";

    /**
     * 获取值
     * @param domain
     * @param key
     * @return
     */
    public static String getValue(String domain,String key){
        Jedis redis = null;
        try {
            redis = getJedis();
            Object object = SerializeUtil.unserialize(redis.get((domain + key+_SUFFIX_MAPPING).getBytes()));
            if (object == null) {
                return null;
            }

            Mapping mapping = (Mapping) object;
            return mapping.getValue();
        }finally {
            if(redis != null){
                redis.close();
            }
        }
    }

    /**
     * 获取公用域下的key值
     * @param key
     * @return
     */
    public static String getValue(String key){
        Mapping mapping = getMapping(key);
        return mapping == null ? "":mapping.getValue();
    }

    public static Mapping getMapping(String key){
        Jedis redis = null;
        try {
            redis = getJedis();
            Object obj = SerializeUtil.unserialize(redis.get((DomainContant.COMMON_DOMAIN+key+_SUFFIX_MAPPING).getBytes()));
            if(obj instanceof Mapping){
                return (Mapping) obj;
            }
        }finally {
            if(redis != null){
                redis.close();
            }
        }
        return null;
    }

    /**
     * 获取 域下的所有数据
     * @param domain
     * @return
     */
    public static List<Mapping> getValueByDomain(String domain){
        Jedis redis = null;
        try {
            redis = getJedis();
            return SerializeUtil.unserializeList(redis.get((domain+_SUFFIX_MAPPING).getBytes()),Mapping.class);
        }finally {
            if(redis != null){
                redis.close();
            }
        }
    }

    /**
     * 保存数据
     * @param mapping
     */
    public static void setVaule(Mapping mapping){
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set((mapping.getDomain()+mapping.getKey()+_SUFFIX_MAPPING).getBytes(),SerializeUtil.serialize(mapping));
        }finally {
            if(redis != null){
                redis.close();
            }
        }
    }

    /**
     * 保存list 数据
     * @param mappings
     */
    public static void setValue(List<Mapping> mappings){
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set((mappings.get(0).getDomain()+_SUFFIX_MAPPING).getBytes(),SerializeUtil.serializeList(mappings));
        }finally {
            if(redis != null){
                redis.close();
            }
        }
    }




}
