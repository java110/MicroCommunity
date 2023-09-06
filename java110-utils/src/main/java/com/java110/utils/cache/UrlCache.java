package com.java110.utils.cache;

import com.java110.utils.constant.MappingConstant;

/**
 * 系统平台url 地址
 *
 */
public class UrlCache {

    /**
     * 获取业主端域名
     * @return
     */
    public static String getOwnerUrl(){

        return MappingCache.getValue(MappingConstant.URL_DOMAIN,"OWNER_WECHAT_URL");

    }
    /**
     * 获取员工地址
     * @return
     */
    public static String getPropertyPhoneUrl(){
        return MappingCache.getValue(MappingConstant.URL_DOMAIN,"STAFF_WECHAT_URL");
    }

    /**
     * 获取 商城手机端地址
     * @return
     */
    public static String getMallAppUrl(){
        return MappingCache.getValue(MappingConstant.URL_DOMAIN,"MALL_URL");
    }
}
