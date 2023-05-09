package com.java110.utils.cache;

import com.java110.utils.util.SerializeUtil;
import com.java110.entity.center.AppRoute;
import com.java110.utils.cache.Jedis;

import java.util.List;

/**
 * 路由配置
 * Created by wuxw on 2018/4/14.
 */
public class AppRouteCache extends BaseCache {

    //后缀 用来刷缓存时删除 所有以这个为后缀的数据
    public final static String _SUFFIX_APP_ROUTE = "_SUFFIX_APP_ROUTE";

    /**
     * 获取 路由配置
     * @param appId
     * @return
     */
    public static List<AppRoute> getAppRoute(String appId){
        List<AppRoute> appRoutes = null;
        Jedis redis = null;
        try {
            redis = getJedis();
            appRoutes = SerializeUtil.unserializeList(redis.get((appId+_SUFFIX_APP_ROUTE).getBytes()),AppRoute.class);
            if(appRoutes == null || appRoutes.size() ==0) {
                return null;
            }
        }finally {
            if(redis != null){
                redis.close();
            }
        }
        return appRoutes;
    }


    /**
     * 保存路由信息
     * @param appRoutes
     */
    public static void setAppRoute(List<AppRoute> appRoutes){
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set((appRoutes.get(0).getAppId()+_SUFFIX_APP_ROUTE).getBytes(),SerializeUtil.serializeList(appRoutes));
        }finally {
            if(redis != null){
                redis.close();
            }
        }
    }


}
