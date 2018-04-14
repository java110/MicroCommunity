package com.java110.common.cache;

import com.java110.common.util.SerializeUtil;
import com.java110.entity.center.AppRoute;

/**
 * 路由配置
 * Created by wuxw on 2018/4/14.
 */
public class AppRouteCache extends BaseCache {

    /**
     * 获取 路由配置
     * @param appId
     * @return
     */
    public static AppRoute getAppRoute(String appId){
        AppRoute appRoute = null;

            Object object = SerializeUtil.unserialize(getJedis().get(appId.getBytes()));
            if(object == null)
            {
                return null;
            }
            appRoute = (AppRoute) object;


        return appRoute;
    }


    /**
     * 保存路由信息
     * @param appRoute
     */
    public static void setAppRoute(AppRoute appRoute){
        getJedis().set(appRoute.getAppId().getBytes(),SerializeUtil.serialize(appRoute));
    }
}
