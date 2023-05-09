package com.java110.utils.cache;

import com.java110.dto.businessTableHis.BusinessTableHisDto;
import com.java110.utils.util.SerializeUtil;
import com.java110.utils.cache.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * 映射缓存工具类
 * Created by wuxw on 2018/4/14.
 */
public class BusinessTableHisCache extends BaseCache {

    //后缀 用来刷缓存时删除 所有以这个为后缀的数据
    public final static String DEFAULT_BUSINESS_TABLE_HIS = "JAVA110_BUSINESS_TABLE_HIS";


    /**
     * 获取  databus
     *
     * @return
     */
    public static List<BusinessTableHisDto> getBusinessTableHiss() {
        Jedis redis = null;
        try {
            redis = getJedis();
            return SerializeUtil.unserializeList(redis.get((DEFAULT_BUSINESS_TABLE_HIS).getBytes()), BusinessTableHisDto.class);
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
    public static List<BusinessTableHisDto> getBusinessTableHisDtos(String action, String actionObj) {
        List<BusinessTableHisDto> businessTableHisDtoDtoDtos = getBusinessTableHiss();

        List<BusinessTableHisDto> tmpBusinessTableHisDtos = new ArrayList<>();

        for (BusinessTableHisDto businessTableHisDtoDto : businessTableHisDtoDtoDtos) {
            if (action.equals(businessTableHisDtoDto.getAction()) && actionObj.equals(businessTableHisDtoDto.getActionObj())) {
                tmpBusinessTableHisDtos.add(businessTableHisDtoDto);
            }
        }
        return tmpBusinessTableHisDtos;
    }

    /**
     * 获取  databus
     *
     * @return
     */
    public static BusinessTableHisDto getBusinessTableHisDto(String action, String actionObj) {
        List<BusinessTableHisDto> businessTableHisDtoDtoDtos = getBusinessTableHiss();
        if(businessTableHisDtoDtoDtos == null){
            return null;
        }

        for (BusinessTableHisDto businessTableHisDtoDto : businessTableHisDtoDtoDtos) {
            if (action.equals(businessTableHisDtoDto.getAction()) && actionObj.equals(businessTableHisDtoDto.getActionObj())) {
                return businessTableHisDtoDto;
            }
        }
        return null;
    }

    /**
     * 保存list 数据
     *
     * @param businessTableHisDtoDtoDtos
     */
    public static void setValue(List<BusinessTableHisDto> businessTableHisDtoDtoDtos) {
        if (businessTableHisDtoDtoDtos == null || businessTableHisDtoDtoDtos.size() < 1) {
            return;
        }
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set((DEFAULT_BUSINESS_TABLE_HIS).getBytes(), SerializeUtil.serializeList(businessTableHisDtoDtoDtos));
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }


}
