package com.java110.center.smo.impl;

import com.java110.center.dao.ICenterServiceDAO;
import com.java110.center.smo.ICenterServiceCacheSMO;
import com.java110.common.cache.AppRouteCache;
import com.java110.common.cache.MappingCache;
import com.java110.common.cache.ServiceSqlCache;
import com.java110.entity.center.AppRoute;
import com.java110.entity.mapping.Mapping;
import com.java110.entity.service.ServiceSql;
import com.java110.service.dao.IQueryServiceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 刷新缓存
 * Created by wuxw on 2018/4/18.
 */
@Service("centerServiceCacheSMOImpl")
public class CenterServiceCacheSMOImpl implements ICenterServiceCacheSMO {

    @Autowired
    ICenterServiceDAO centerServiceDAOImpl;

    @Autowired
    IQueryServiceDAO queryServiceDAOImpl;

    @Override
    public void flush() {

        //1.0 封装 AppRoute
        flushAppRoute();

        //2.0 分装 Mapping
        flushMapping();

        //3.0 分装 ServiceSql
        flushServiceSql();
    }

    /**
     * 3.0 分装 ServiceSql
     */
    private void flushServiceSql() {
        List<ServiceSql> serviceSqls = queryServiceDAOImpl.qureyServiceSqlAll();

        if(serviceSqls == null || serviceSqls.size() == 0){
            return;
        }
        for(ServiceSql serviceSql: serviceSqls){
            ServiceSqlCache.setServiceSql(serviceSql);
        }
    }

    /**
     * 刷新 Mapping 数据
     */
    private void flushMapping() {

        List<Mapping> mappings = centerServiceDAOImpl.getMappingInfoAll();

        for(Mapping mapping : mappings){
            MappingCache.setVaule(mapping);
        }

        Map<String,List<Mapping>> mappingMap = new HashMap<String,List<Mapping>>();
        List<Mapping> mappingsNew = null;
        for(Mapping mapping : mappings){
            if(mappingMap.containsKey(mapping.getDomain())){
                mappingsNew = mappingMap.get(mapping.getDomain());
                mappingsNew.add(mapping);
            }else{
                mappingsNew = new ArrayList<Mapping>();
                mappingsNew.add(mapping);
                mappingMap.put(mapping.getDomain(),mappingsNew);
            }
        }

        for (String domain : mappingMap.keySet()) {
            MappingCache.setValue(mappingMap.get(domain));
        }
    }

    /**
     * 刷新AppRoute数据
     */
    private void flushAppRoute(){

        List<Map> appInfos = centerServiceDAOImpl.getAppRouteAndServiceInfoAll();
        Map<String,List<AppRoute>> appRoustsMap = new HashMap<String,List<AppRoute>>();
        List<AppRoute> appRoutes = null;
        for(Map appInfo : appInfos){
            if(appRoustsMap.containsKey(appInfo.get("app_id").toString())){
                appRoutes = appRoustsMap.get(appInfo.get("app_id").toString());
                appRoutes.add(AppRoute.newInstance().builder(appInfo));
            }else{
                appRoutes = new ArrayList<AppRoute>();
                appRoutes.add(AppRoute.newInstance().builder(appInfo));
                appRoustsMap.put(appInfo.get("app_id").toString(),appRoutes);
            }
        }

        for (String appId : appRoustsMap.keySet()) {
            AppRouteCache.setAppRoute(appRoustsMap.get(appId));
        }

    }

    public ICenterServiceDAO getCenterServiceDAOImpl() {
        return centerServiceDAOImpl;
    }

    public void setCenterServiceDAOImpl(ICenterServiceDAO centerServiceDAOImpl) {
        this.centerServiceDAOImpl = centerServiceDAOImpl;
    }

    public IQueryServiceDAO getQueryServiceDAOImpl() {
        return queryServiceDAOImpl;
    }

    public void setQueryServiceDAOImpl(IQueryServiceDAO queryServiceDAOImpl) {
        this.queryServiceDAOImpl = queryServiceDAOImpl;
    }
}
