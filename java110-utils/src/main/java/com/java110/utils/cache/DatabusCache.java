package com.java110.utils.cache;

import com.java110.dto.businessDatabus.BusinessDatabusDto;
import com.java110.utils.util.SerializeUtil;
import com.java110.utils.cache.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * 映射缓存工具类
 * Created by wuxw on 2018/4/14.
 */
public class DatabusCache extends BaseCache {

    //后缀 用来刷缓存时删除 所有以这个为后缀的数据
    public final static String DEFAULT_DATABUS = "JAVA110_DATABUS";


    /**
     * 获取  databus
     *
     * @return
     */
    public static List<BusinessDatabusDto> getDatabuss() {
        Jedis redis = null;
        try {
            redis = getJedis();
            return SerializeUtil.unserializeList(redis.get((DEFAULT_DATABUS).getBytes()), BusinessDatabusDto.class);
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 获取  databus
     *
     * @return
     */
    public static List<BusinessDatabusDto> getDatabuss(String businessType) {
        List<BusinessDatabusDto> businessDatabusDtos = getDatabuss();

        List<BusinessDatabusDto> tmpBusinessDatabusDtos = new ArrayList<>();

        for (BusinessDatabusDto businessDatabusDto : businessDatabusDtos) {
            if (businessType.equals(businessDatabusDto.getBusinessTypeCd())) {
                tmpBusinessDatabusDtos.add(businessDatabusDto);
            }
        }
        return tmpBusinessDatabusDtos;
    }

    /**
     * 保存list 数据
     *
     * @param businessDatabusDtos
     */
    public static void setValue(List<BusinessDatabusDto> businessDatabusDtos) {
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set((DEFAULT_DATABUS).getBytes(), SerializeUtil.serializeList(businessDatabusDtos));
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }


}
