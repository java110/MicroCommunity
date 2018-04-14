package com.java110.common.cache;

import com.java110.common.constant.DomainContant;
import com.java110.common.util.SerializeUtil;
import com.java110.entity.mapping.Mapping;

import java.util.List;

/**
 * 映射缓存工具类
 * Created by wuxw on 2018/4/14.
 */
public class MappingCache extends BaseCache {


    /**
     * 获取值
     * @param domain
     * @param key
     * @return
     */
    public static String getValue(String domain,String key){
        Object object = SerializeUtil.unserialize(getJedis().get((domain+key).getBytes()));
        if(object == null){
            return null;
        }

        Mapping mapping = (Mapping) object;
        return mapping.getValue();
    }

    /**
     * 获取公用域下的key值
     * @param key
     * @return
     */
    public static String getValue(String key){
        return getJedis().get(DomainContant.COMMON_DOMAIN+key);
    }

    /**
     * 获取 域下的所有数据
     * @param domain
     * @return
     */
    public static List<Mapping> getValueByDomain(String domain){
        return SerializeUtil.unserializeList(getJedis().get(domain.getBytes()),Mapping.class);
    }

    /**
     * 保存数据
     * @param mapping
     */
    public static void setVaule(Mapping mapping){
        getJedis().set((mapping.getDomain()+mapping.getKey()).getBytes(),SerializeUtil.serialize(mapping));
    }

    /**
     * 保存list 数据
     * @param mappings
     */
    public static void setValue(List<Mapping> mappings){
        getJedis().set((mappings.get(0).getDomain()+mappings.get(0).getKey()).getBytes(),SerializeUtil.serializeList(mappings));
    }




}
