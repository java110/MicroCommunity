package com.java110.common.cache;

import com.java110.common.util.SerializeUtil;
import com.java110.entity.center.AppRoute;

import java.util.List;

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
    public static List<AppRoute> getAppRoute(String appId){
        List<AppRoute> appRoutes = null;

            appRoutes = SerializeUtil.unserializeList(getJedis().get(appId.getBytes()),AppRoute.class);
            if(appRoutes == null || appRoutes.size() ==0) {
                return null;
            }

        return appRoutes;
    }


    /**
     * 保存路由信息
     * @param appRoutes
     */
    public static void setAppRoute(List<AppRoute> appRoutes){
        getJedis().set(appRoutes.get(0).getAppId().getBytes(),SerializeUtil.serializeList(appRoutes));
    }
}
