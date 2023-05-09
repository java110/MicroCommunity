package com.java110.utils.cache;

import com.java110.entity.mapping.Mapping;
import com.java110.utils.constant.DomainContant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.java110.utils.cache.Jedis;

import java.util.List;

/**
 * 映射缓存工具类
 * Created by wuxw on 2018/4/14.
 */
public class MappingCache extends BaseCache {

    private final static Logger logger = LoggerFactory.getLogger(MappingCache.class);


    //后缀 用来刷缓存时删除 所有以这个为后缀的数据
    public final static String _SUFFIX_MAPPING = "_SUFFIX_MAPPING";
    //日志
    public final static String LOG_SERVICE_CODE = "LOG_SERVICE_CODE";
    public final static String CALL_OUT_LOG = "CALL_OUT_LOG";


    /**
     * 获取值
     *
     * @param domain
     * @param key
     * @return
     */
    public static String getValue(String domain, String key) {
        Jedis redis = null;
        long startTime = DateUtil.getCurrentDate().getTime();
        try {
            redis = getJedis();
            Object object = SerializeUtil.unserialize(redis.get((domain + key + _SUFFIX_MAPPING).getBytes()));
            if (object == null) {
                return null;
            }

            Mapping mapping = (Mapping) object;
            return mapping.getValue();
        } finally {
            if (redis != null) {
                redis.close();
            }

            logger.debug(domain + "::" + key + " redis 耗时：" + (DateUtil.getCurrentDate().getTime() - startTime));
        }
    }

    /**
     * 获取公用域下的key值
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        Mapping mapping = getMapping(key);
        return mapping == null ? "" : mapping.getValue();
    }

    public static Mapping getMapping(String key) {
        Jedis redis = null;
        long startTime = DateUtil.getCurrentDate().getTime();

        try {
            redis = getJedis();
            Object obj = SerializeUtil.unserialize(redis.get((DomainContant.COMMON_DOMAIN + key + _SUFFIX_MAPPING).getBytes()));
            if (obj instanceof Mapping) {
                return (Mapping) obj;
            }
        } finally {
            if (redis != null) {
                redis.close();
            }
            logger.debug( key + " redis 耗时：" + (DateUtil.getCurrentDate().getTime() - startTime));

        }
        return null;
    }

    /**
     * 获取 域下的所有数据
     *
     * @param domain
     * @return
     */
    public static List<Mapping> getValueByDomain(String domain) {
        Jedis redis = null;
        try {
            redis = getJedis();
            return SerializeUtil.unserializeList(redis.get((domain + _SUFFIX_MAPPING).getBytes()), Mapping.class);
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 保存数据
     *
     * @param mapping
     */
    public static void setVaule(Mapping mapping) {
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set((mapping.getDomain() + mapping.getKey() + _SUFFIX_MAPPING).getBytes(), SerializeUtil.serialize(mapping));
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 保存list 数据
     *
     * @param mappings
     */
    public static void setValue(List<Mapping> mappings) {
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set((mappings.get(0).getDomain() + _SUFFIX_MAPPING).getBytes(), SerializeUtil.serializeList(mappings));
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 获取值
     *
     * @param domain
     * @param key
     * @return
     */
    public static String getRemark(String domain, String key) {
        Jedis redis = null;
        try {
            redis = getJedis();
            Object object = SerializeUtil.unserialize(redis.get((domain + key + _SUFFIX_MAPPING).getBytes()));
            if (object == null) {
                return null;
            }

            Mapping mapping = (Mapping) object;
            return mapping.getRemark();
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }


}
