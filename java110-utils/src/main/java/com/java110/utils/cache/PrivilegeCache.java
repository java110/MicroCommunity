package com.java110.utils.cache;

import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.utils.util.SerializeUtil;
import com.java110.utils.cache.Jedis;

import java.util.List;

/**
 * 映射缓存工具类
 * Created by wuxw on 2018/4/14.
 */
public class PrivilegeCache extends BaseCache {

    //后缀 用来刷缓存时删除 所有以这个为后缀的数据
    public final static String DEFAULT_PRIVILEGE = "JAVA110_PRIVILEGE";


    /**
     * 获取 基础权限
     *
     * @return
     */
    public static List<BasePrivilegeDto> getPrivileges() {
        Jedis redis = null;
        try {
            redis = getJedis();
            return SerializeUtil.unserializeList(redis.get((DEFAULT_PRIVILEGE).getBytes()), BasePrivilegeDto.class);
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 保存list 数据
     *
     * @param basePrivilegeDtos
     */
    public static void setValue(List<BasePrivilegeDto> basePrivilegeDtos) {
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set((DEFAULT_PRIVILEGE).getBytes(), SerializeUtil.serializeList(basePrivilegeDtos));
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }


}
