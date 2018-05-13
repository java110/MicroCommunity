package com.java110.center.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.center.dao.ICenterServiceDAO;
import com.java110.center.smo.ICenterServiceCacheSMO;
import com.java110.common.cache.AppRouteCache;
import com.java110.common.cache.MappingCache;
import com.java110.common.cache.ServiceSqlCache;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.factory.DataTransactionFactory;
import com.java110.entity.center.AppRoute;
import com.java110.entity.mapping.Mapping;
import com.java110.entity.service.DataQuery;
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
    public void flush(DataQuery dataQuery) throws SMOException{



        //1.0 封装 AppRoute
        flushAppRoute(dataQuery);

        //2.0 分装 Mapping
        flushMapping(dataQuery);

        //3.0 分装 ServiceSql
        flushServiceSql(dataQuery);

        dataQuery.setResponseInfo(DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_SUCCESS,"刷新成功"));
    }

    /**
     * 用来系统启动刷新
     */
    @Override
    public void startFlush() {
        //1.0 封装 AppRoute
        doFlushAppRoute();

        //2.0 分装 Mapping
        doFlushMapping();

        //3.0 分装 ServiceSql
        doFlushServiceSql();
    }

    private void checkCacheParam(DataQuery dataQuery) throws SMOException{
        JSONObject params = dataQuery.getRequestParams();
        if(params == null || !params.containsKey(CommonConstant.CACHE_PARAM_NAME)){
            throw new SMOException(ResponseConstant.RESULT_PARAM_ERROR,"请求报文错误，未包含字段 "+CommonConstant.CACHE_PARAM_NAME);
        }
    }
    /**
     * 3.0 分装 ServiceSql
     */
    private void flushServiceSql(DataQuery dataQuery) {

        JSONObject params = dataQuery.getRequestParams();
        if(!CommonConstant.CACHE_SERVICE_SQL.equals(params.getString(CommonConstant.CACHE_PARAM_NAME))){
            return ;
        }
        // 刷新
        doFlushServiceSql();
    }

    private void doFlushServiceSql() {
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
    private void flushMapping(DataQuery dataQuery) {

        JSONObject params = dataQuery.getRequestParams();

        if(!CommonConstant.CACHE_MAPPING.equals(params.getString(CommonConstant.CACHE_PARAM_NAME))){
            return ;
        }

        doFlushMapping();
    }

    private void doFlushMapping() {
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
    private void flushAppRoute(DataQuery dataQuery){

        JSONObject params = dataQuery.getRequestParams();

        if(!CommonConstant.CACHE_APP_ROUTE_SERVICE.equals(params.getString(CommonConstant.CACHE_PARAM_NAME))){
            return ;
        }
        doFlushAppRoute();

    }

    private void doFlushAppRoute() {
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
