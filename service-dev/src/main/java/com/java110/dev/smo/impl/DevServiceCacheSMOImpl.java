package com.java110.dev.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.db.dao.IQueryServiceDAO;
import com.java110.dev.dao.IDevServiceDAO;
import com.java110.dev.smo.IDevServiceCacheSMO;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.businessDatabus.BusinessDatabusDto;
import com.java110.dto.businessTableHis.BusinessTableHisDto;
import com.java110.entity.center.AppRoute;
import com.java110.entity.mapping.Mapping;
import com.java110.entity.order.ServiceBusiness;
import com.java110.entity.service.ServiceSql;
import com.java110.service.context.DataQuery;
import com.java110.utils.cache.*;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
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
@Service("devServiceCacheSMOImpl")
public class DevServiceCacheSMOImpl implements IDevServiceCacheSMO {

    private final static Logger logger = LoggerFactory.getLogger(DevServiceCacheSMOImpl.class);

    @Autowired
    IDevServiceDAO devServiceDAOImpl;

    @Autowired
    IQueryServiceDAO queryServiceDAOImpl;

    @Override
    public void flush(DataQuery dataQuery) throws SMOException {


        //1.0 封装 AppRoute
        flushAppRoute(dataQuery);

        //2.0 分装 Mapping
        flushMapping(dataQuery);

        //3.0 分装 ServiceSql
        flushServiceSql(dataQuery);

        //4.0 刷新业务信息
        flushServiceBusiness(dataQuery);

        //5.0 刷新基础权限
        flushPrivilege(dataQuery);

        //刷新databus
        flushDatabus(dataQuery);

//刷新BusinessTableHis
        flushBusinessTableHis(dataQuery);

        dataQuery.setResponseInfo(DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_SUCCESS, "刷新成功"));
    }

    /**
     * 根据缓存类别刷新缓存
     *
     * @param headers 缓存类别
     */
    public void flush(Map<String, String> headers) throws SMOException {

        flushAppRoute(headers);

        flushMapping(headers);

        //3.0 分装 ServiceSql
        flushServiceSql(headers);

        //4.0 刷新业务信息
        flushServiceBusiness(headers);

        //5.0 刷新基础权限
        flushPrivilege(headers);

        flushDatabus(headers);

        flushBusinessTableHis(headers);
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
        //5.0 刷新全新
        doFlushServiceBusiness();


        doFlushPrivilege();

        //刷新databus
        doFlushDatabus();

        //刷新BusinessTableHis
        doFlushBusinessTableHis();
    }


    private void checkCacheParam(DataQuery dataQuery) throws SMOException {
        JSONObject params = dataQuery.getRequestParams();
        if (params == null || !params.containsKey(CommonConstant.CACHE_PARAM_NAME)) {
            throw new SMOException(ResponseConstant.RESULT_PARAM_ERROR, "请求报文错误，未包含字段 " + CommonConstant.CACHE_PARAM_NAME);
        }
    }

    /**
     * 3.0 分装 ServiceSql
     */
    private void flushServiceSql(DataQuery dataQuery) {

        JSONObject params = dataQuery.getRequestParams();
        if (!CommonConstant.CACHE_SERVICE_SQL.equals(params.getString(CommonConstant.CACHE_PARAM_NAME))) {
            return;
        }
        // 刷新
        doFlushServiceSql();
    }

    private void flushServiceBusiness(DataQuery dataQuery) {

        JSONObject params = dataQuery.getRequestParams();
        if (!CommonConstant.CACHE_SERVICE_BUSINESS.equals(params.getString(CommonConstant.CACHE_PARAM_NAME))) {
            return;
        }
        // 刷新
        doFlushServiceBusiness();
    }

    /**
     * 3.0 分装 ServiceSql
     */
    private void flushServiceSql(Map<String, String> headers) {

        Assert.hasKey(headers, CommonConstant.CACHE_PARAM, "未包含cache参数" + headers.toString());
        if (!CommonConstant.CACHE_SERVICE_SQL.equals(headers.get(CommonConstant.CACHE_PARAM))
                && !CommonConstant.CACHE_ALL.equals(headers.get(CommonConstant.CACHE_PARAM))) {
            return;
        }
        // 刷新
        doFlushServiceSql();
    }

    /**
     * 3.0 分装 ServiceSql
     */
    private void flushServiceBusiness(Map<String, String> headers) {

        Assert.hasKey(headers, CommonConstant.CACHE_PARAM, "未包含cache参数" + headers.toString());
        if (!CommonConstant.CACHE_SERVICE_BUSINESS.equals(headers.get(CommonConstant.CACHE_PARAM))
                && !CommonConstant.CACHE_ALL.equals(headers.get(CommonConstant.CACHE_PARAM))) {
            return;
        }
        // 刷新
        doFlushServiceBusiness();
    }

    private void doFlushServiceSql() {

        logger.debug("开始刷新 ServiceSql数据到redis数据库中");

        List<ServiceSql> serviceSqls = queryServiceDAOImpl.qureyServiceSqlAll();

        if (serviceSqls == null || serviceSqls.size() == 0) {
            return;
        }
        //删除原始数据
        ServiceSqlCache.removeData(ServiceSqlCache._SUFFIX_SERVICE_SQL);

        for (ServiceSql serviceSql : serviceSqls) {
            ServiceSqlCache.setServiceSql(serviceSql);
        }
    }

    private void doFlushServiceBusiness() {
        logger.debug("开始刷新 ServiceBusiness数据到redis数据库中");
        List<ServiceBusiness> serviceBusinesses = queryServiceDAOImpl.qureyServiceBusiness();

        if (serviceBusinesses == null || serviceBusinesses.size() == 0) {
            return;
        }
        //删除原始数据
        ServiceBusinessCache.removeData(ServiceBusinessCache._KEY_SERVICE_BUSINESS);

        //设置缓存
        ServiceBusinessCache.setServiceBusiness(serviceBusinesses);
    }


    /**
     * 刷新 Mapping 数据
     */
    private void flushMapping(DataQuery dataQuery) {

        JSONObject params = dataQuery.getRequestParams();

        if (!CommonConstant.CACHE_MAPPING.equals(params.getString(CommonConstant.CACHE_PARAM_NAME))) {
            return;
        }

        doFlushMapping();
    }


    /**
     * 刷新 Mapping 数据
     */
    private void flushPrivilege(DataQuery dataQuery) {

        JSONObject params = dataQuery.getRequestParams();

        if (!CommonConstant.CACHE_PRIVILEGE.equals(params.getString(CommonConstant.CACHE_PARAM_NAME))) {
            return;
        }

        doFlushPrivilege();
    }

    /**
     * 刷新 Mapping 数据
     */
    private void flushDatabus(DataQuery dataQuery) {

        JSONObject params = dataQuery.getRequestParams();

        if (!CommonConstant.CACHE_DATABUS.equals(params.getString(CommonConstant.CACHE_PARAM_NAME))) {
            return;
        }

        doFlushDatabus();
    }

    /**
     * 刷新 doFlushBusinessTableHis 数据
     */
    private void flushBusinessTableHis(DataQuery dataQuery) {

        JSONObject params = dataQuery.getRequestParams();

        if (!CommonConstant.CACHE_BUSINESS_TABLE_HIS.equals(params.getString(CommonConstant.CACHE_PARAM_NAME))) {
            return;
        }

        doFlushBusinessTableHis();
    }


    /**
     * 刷新 Mapping 数据
     */
    private void flushPrivilege(Map<String, String> headers) {

        Assert.hasKey(headers, CommonConstant.CACHE_PARAM, "未包含cache参数" + headers.toString());

        if (!CommonConstant.CACHE_PRIVILEGE.equals(headers.get(CommonConstant.CACHE_PARAM))
                && !CommonConstant.CACHE_ALL.equals(headers.get(CommonConstant.CACHE_PARAM))) {
            return;
        }

        doFlushPrivilege();
    }

    /**
     * 刷新 databus 数据
     */
    private void flushDatabus(Map<String, String> headers) {

        Assert.hasKey(headers, CommonConstant.CACHE_PARAM, "未包含cache参数" + headers.toString());

        if (!CommonConstant.CACHE_DATABUS.equals(headers.get(CommonConstant.CACHE_PARAM))
                && !CommonConstant.CACHE_ALL.equals(headers.get(CommonConstant.CACHE_PARAM))) {
            return;
        }

        doFlushDatabus();
    }

    /**
     * 刷新 databus 数据
     */
    private void flushBusinessTableHis(Map<String, String> headers) {

        Assert.hasKey(headers, CommonConstant.CACHE_PARAM, "未包含cache参数" + headers.toString());

        if (!CommonConstant.CACHE_BUSINESS_TABLE_HIS.equals(headers.get(CommonConstant.CACHE_PARAM))
                && !CommonConstant.CACHE_ALL.equals(headers.get(CommonConstant.CACHE_PARAM))) {
            return;
        }

        doFlushBusinessTableHis();
    }

    /**
     * 刷新 Mapping 数据
     */
    private void flushMapping(Map<String, String> headers) {

        Assert.hasKey(headers, CommonConstant.CACHE_PARAM, "未包含cache参数" + headers.toString());

        if (!CommonConstant.CACHE_MAPPING.equals(headers.get(CommonConstant.CACHE_PARAM))
                && !CommonConstant.CACHE_ALL.equals(headers.get(CommonConstant.CACHE_PARAM))) {
            return;
        }

        doFlushMapping();
    }

    private void doFlushMapping() {
        logger.debug("开始刷新 Mapping数据到redis数据库中");
        List<Mapping> mappings = devServiceDAOImpl.getMappingInfoAll();
        //删除原始数据
        MappingCache.removeData(MappingCache._SUFFIX_MAPPING);
        for (Mapping mapping : mappings) {
            MappingCache.setVaule(mapping);
        }

        Map<String, List<Mapping>> mappingMap = new HashMap<String, List<Mapping>>();
        List<Mapping> mappingsNew = null;
        for (Mapping mapping : mappings) {
            if (mappingMap.containsKey(mapping.getDomain())) {
                mappingsNew = mappingMap.get(mapping.getDomain());
                mappingsNew.add(mapping);
            } else {
                mappingsNew = new ArrayList<Mapping>();
                mappingsNew.add(mapping);
                mappingMap.put(mapping.getDomain(), mappingsNew);
            }
        }

        for (String domain : mappingMap.keySet()) {
            MappingCache.setValue(mappingMap.get(domain));
        }
    }

    private void doFlushPrivilege() {
        logger.debug("开始刷新 Mapping数据到redis数据库中");
        List<BasePrivilegeDto> basePrivilegeDtos = devServiceDAOImpl.getPrivilegeAll();
        //删除原始数据
        PrivilegeCache.removeData(PrivilegeCache.DEFAULT_PRIVILEGE);
        PrivilegeCache.setValue(basePrivilegeDtos);
    }

    private void doFlushDatabus() {
        logger.debug("开始刷新 Mapping数据到redis数据库中");
        List<BusinessDatabusDto> businessDatabusDtos = devServiceDAOImpl.getDatabusAll();
        //删除原始数据
        DatabusCache.removeData(DatabusCache.DEFAULT_DATABUS);
        DatabusCache.setValue(businessDatabusDtos);
    }

    private void doFlushBusinessTableHis() {
        logger.debug("开始刷新 BusinessTableHis数据到redis数据库中");
        List<BusinessTableHisDto> businessTableHisDtos = devServiceDAOImpl.getBusinessTableHisAll();
        //删除原始数据
        BusinessTableHisCache.removeData(BusinessTableHisCache.DEFAULT_BUSINESS_TABLE_HIS);
        BusinessTableHisCache.setValue(businessTableHisDtos);
    }


    /**
     * 刷新AppRoute数据
     */
    private void flushAppRoute(DataQuery dataQuery) {

        JSONObject params = dataQuery.getRequestParams();

        if (!CommonConstant.CACHE_APP_ROUTE_SERVICE.equals(params.getString(CommonConstant.CACHE_PARAM_NAME))) {
            return;
        }
        doFlushAppRoute();

    }

    /**
     * 刷新AppRoute数据
     */
    private void flushAppRoute(Map<String, String> headers) {

        Assert.hasKey(headers, CommonConstant.CACHE_PARAM, "未包含cache参数" + headers.toString());

        if (!CommonConstant.CACHE_APP_ROUTE_SERVICE.equals(headers.get(CommonConstant.CACHE_PARAM))
                && !CommonConstant.CACHE_ALL.equals(headers.get(CommonConstant.CACHE_PARAM))) {
            return;
        }
        doFlushAppRoute();

    }

    private void doFlushAppRoute() {
        logger.debug("开始刷新 AppRoute数据到redis数据库中");
        List<Map> appInfos = devServiceDAOImpl.getAppRouteAndServiceInfoAll();
        Map<String, List<AppRoute>> appRoustsMap = new HashMap<String, List<AppRoute>>();
        List<AppRoute> appRoutes = null;
        for (Map appInfo : appInfos) {
            if (appRoustsMap.containsKey(appInfo.get("app_id").toString())) {
                appRoutes = appRoustsMap.get(appInfo.get("app_id").toString());
                appRoutes.add(AppRoute.newInstance().builder(appInfo));
            } else {
                appRoutes = new ArrayList<AppRoute>();
                appRoutes.add(AppRoute.newInstance().builder(appInfo));
                appRoustsMap.put(appInfo.get("app_id").toString(), appRoutes);
            }
        }
        //删除原始数据
        AppRouteCache.removeData(AppRouteCache._SUFFIX_APP_ROUTE);

        for (String appId : appRoustsMap.keySet()) {
            AppRouteCache.setAppRoute(appRoustsMap.get(appId));
        }
    }


    public IQueryServiceDAO getQueryServiceDAOImpl() {
        return queryServiceDAOImpl;
    }

    public void setQueryServiceDAOImpl(IQueryServiceDAO queryServiceDAOImpl) {
        this.queryServiceDAOImpl = queryServiceDAOImpl;
    }
}
