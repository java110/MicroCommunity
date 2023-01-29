package com.java110.core.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.cache.AppRouteCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.core.context.AbstractDataFlowContext;
import com.java110.core.context.ApiDataFlow;
import com.java110.core.context.DataFlow;
import com.java110.core.context.OrderDataFlow;
import com.java110.entity.center.AppRoute;
import com.java110.entity.center.AppService;
import com.java110.entity.center.Business;
import com.java110.entity.center.DataFlowLinksCost;
import com.java110.entity.order.BusinessAttrs;
import com.java110.entity.order.Orders;
import com.java110.entity.order.OrdersAttrs;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * 数据流工厂类
 * Created by wuxw on 2018/4/13.
 */
public class DataFlowFactory {

    /**
     * 初始化
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Class<T> clazz) throws BeanInstantiationException {
        Assert.notNull(clazz, "Class 不能为空");
        if (clazz.isInterface()) {
            throw new BeanInstantiationException(clazz, "指定类是一个接口");
        }
        //DateUtil.getCurrentDate(), ResponseConstant.RESULT_CODE_SUCCESS
        try {
            return clazz.getConstructor(Date.class,String.class).newInstance(DateUtil.getCurrentDate(), ResponseConstant.RESULT_CODE_SUCCESS);
        }
        catch (InstantiationException ex) {
            throw new BeanInstantiationException(clazz, "是一个抽象类?", ex);
        }catch (Exception ex){
            throw new BeanInstantiationException(clazz, "构造函数不能访问?", ex);

        }
    }


    /**
     * 添加耗时
     * @param dataFlow 数据流
     * @param linksCode 环节编码
     * @param linksName 环节名称
     * @param startDate 开始时间
     * @return
     */
    public static void addCostTime(AbstractDataFlowContext dataFlow, String linksCode, String linksName, Date startDate){
        if(MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,MappingConstant.KEY_COST_TIME_ON_OFF))) {
            DataFlowLinksCost dataFlowLinksCost = new DataFlowLinksCost().builder(linksCode, linksName, startDate, DateUtil.getCurrentDate());
            dataFlow.addLinksCostDates(dataFlowLinksCost);
        }
    }


    /**
     * 添加耗时
     * @param dataFlow 数据流
     * @param linksCode 环节编码
     * @param linksName 环节名称
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    public static void addCostTime(AbstractDataFlowContext dataFlow, String linksCode, String linksName, Date startDate, Date endDate){
        if(MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,MappingConstant.KEY_COST_TIME_ON_OFF))) {
            DataFlowLinksCost dataFlowLinksCost = new DataFlowLinksCost().builder(linksCode, linksName, startDate, endDate);
            dataFlow.addLinksCostDates(dataFlowLinksCost);
        }
    }

    /**
     * 获取单个路由
     * @param dataFlow
     * @param serviceCode
     * @return
     */
    public static AppRoute getRoute(DataFlow dataFlow, String serviceCode){
        if (dataFlow.getAppRoutes().size() == 0){
            throw new RuntimeException("当前没有获取到AppId对应的信息");
        }

        List<AppRoute> appRoutes = dataFlow.getAppRoutes();
        for(AppRoute appRoute : appRoutes) {
            if (StatusConstant.STATUS_CD_VALID.equals(appRoute.getStatusCd())
                    &&appRoute.getAppService().getServiceCode().equals(serviceCode)){
                return appRoute;
            }
        }
        return null;
    }

    /**
     * 获取单个路由
     * @param dataFlow
     * @param serviceCode
     * @return
     */
    public static AppRoute getRoute(ApiDataFlow dataFlow, String serviceCode){
        if (dataFlow.getAppRoutes().size() == 0){
            throw new RuntimeException("当前没有获取到AppId对应的信息");
        }

        List<AppRoute> appRoutes = dataFlow.getAppRoutes();
        for(AppRoute appRoute : appRoutes) {
            if (StatusConstant.STATUS_CD_VALID.equals(appRoute.getStatusCd())
                    &&appRoute.getAppService().getServiceCode().equals(serviceCode)){
                return appRoute;
            }
        }
        return null;
    }

    /**
     * 根据AppId 和serviceCode 查询AppRoute
     * @param appId
     * @param serviceCode
     * @return
     */
    public static AppRoute getRoute(String appId,String serviceCode){
        List<AppRoute> appRoutes = AppRouteCache.getAppRoute(appId);
        for(AppRoute appRoute : appRoutes) {
            if (StatusConstant.STATUS_CD_VALID.equals(appRoute.getStatusCd())
                    &&appRoute.getAppService().getServiceCode().equals(serviceCode)){
                return appRoute;
            }
        }
        return null;
    }
    /**
     * 获取单个服务
     * @param dataFlow
     * @param serviceCode
     * @return
     */
    public static AppService getService(DataFlow dataFlow, String serviceCode){
        AppRoute route = getRoute(dataFlow, serviceCode);
        if(route == null){
            return null;
        }
        return route.getAppService();
    }

    /**
     * 获取单个服务
     * @param dataFlow
     * @param serviceCode
     * @return
     */
    public static AppService getService(ApiDataFlow dataFlow, String serviceCode){
        AppRoute route = getRoute(dataFlow, serviceCode);
        if(route == null){
            return null;
        }
        return route.getAppService();
    }

    /**
     * 根据appid 和 serviceCode 查询相应的 appservice
     * @param appId
     * @param serviceCode
     * @return
     */
    public static AppService getService(String appId,String serviceCode){
        AppRoute route = getRoute(appId, serviceCode);
        if(route == null){
            return null;
        }
        return route.getAppService();
    }

    /**
     * 获取Order信息
     * @param dataFlow
     * @return
     */
    public static Map getOrder(DataFlow dataFlow){
        Map order = new HashMap();
        dataFlow.setoId(GenerateCodeFactory.getOId());
        order.put("oId",dataFlow.getoId());
        order.put("appId",dataFlow.getAppId());
        order.put("extTransactionId",dataFlow.getTransactionId());
        order.put("userId",dataFlow.getUserId());
        order.put("requestTime",dataFlow.getRequestTime());
        order.put("orderTypeCd",dataFlow.getOrderTypeCd());
        order.put("remark",dataFlow.getRemark());
        order.put("statusCd",StatusConstant.STATUS_CD_SAVE);
        return order ;
    }

    /**
     * 获取Order信息
     * @param orders 订单信息
     * @return
     */
    public static Map getOrder(Orders orders){
        Map order = new HashMap();
        orders.setoId(GenerateCodeFactory.getOId());
        order.put("oId",orders.getoId());
        order.put("appId",orders.getAppId());
        order.put("extTransactionId",orders.getExtTransactionId());
        order.put("userId",orders.getUserId());
        order.put("requestTime",orders.getRequestTime());
        order.put("orderTypeCd",orders.getOrderTypeCd());
        order.put("remark",orders.getRemark());
        order.put("statusCd",StatusConstant.STATUS_CD_SAVE);
        return order ;
    }


    /**
     * 获取订单属性
     * @param dataFlow
     * @return
     */
    public static List<Map> getOrderAttrs(DataFlow dataFlow){
        List<Map> orderAttrs = new ArrayList<Map>();
        JSONObject reqOrders = dataFlow.getReqOrders();
        if(!reqOrders.containsKey("attrs") || reqOrders.getJSONArray("attrs").size() ==0){
            return orderAttrs;
        }
        JSONArray attrs = reqOrders.getJSONArray("attrs");
        Map attrMap = null;
        for(int attrIndex = 0;attrIndex <attrs.size();attrIndex ++ )
        {
            attrMap = new HashMap();
            attrMap.put("oId",dataFlow.getoId());
            attrMap.put("attrId", GenerateCodeFactory.getAttrId());
            attrMap.put("specCd",attrs.getJSONObject(attrIndex).getString("specCd"));
            attrMap.put("value",attrs.getJSONObject(attrIndex).getString("value"));
            orderAttrs.add(attrMap);
        }
        return orderAttrs;
    }

    /**
     * 获取订单属性
     * @param orders
     * @return
     */
    public static List<Map> getOrderAttrs(Orders orders){
        List<Map> orderAttrs = new ArrayList<Map>();

        List<OrdersAttrs> attrs = orders.getOrdersAttrs();
        Map attrMap = null;
        for(OrdersAttrs ordersAttr:attrs)
        {
            ordersAttr.setAttrId(GenerateCodeFactory.getAttrId());
            attrMap = new HashMap();
            attrMap.put("oId",orders.getoId());
            attrMap.put("attrId", ordersAttr.getAttrId());
            attrMap.put("specCd",ordersAttr.getSpecCd());
            attrMap.put("value",ordersAttr.getValue());
            orderAttrs.add(attrMap);
        }
        return orderAttrs;
    }
    /**
     * 获取订单项
     * @param dataFlow
     * @return
     */
    public static List<Map> getBusiness(DataFlow dataFlow){
        List<Map> businesss = new ArrayList<Map>();
        List<Business> businesses= dataFlow.getBusinesses();
        Map busiMap = null;
        for(Business business : businesses) {
            if(business == null){
                continue;
            }
            business.setbId(GenerateCodeFactory.getBId());
            busiMap = new HashMap();
            busiMap.put("oId",dataFlow.getoId());
            busiMap.put("businessTypeCd",getService(dataFlow,business.getServiceCode()).getBusinessTypeCd());
            busiMap.put("remark",business.getRemark());
            busiMap.put("statusCd",StatusConstant.STATUS_CD_SAVE);
            busiMap.put("bId",business.getbId());
            businesss.add(busiMap);
        }
        return businesss;
    }


    /**
     * 获取订单项
     * @param dataFlow
     * @return
     */
    public static List<Map> getBusiness(OrderDataFlow dataFlow){
        List<Map> businesss = new ArrayList<Map>();
        Map busiMap = null;
        List<com.java110.entity.order.Business> businessList = dataFlow.getBusinessList();
        for(com.java110.entity.order.Business business : businessList) {
            if(business == null){
                continue;
            }
            business.setbId(GenerateCodeFactory.getBId());
            busiMap = new HashMap();
            busiMap.put("oId",dataFlow.getOrders().getoId());
            busiMap.put("businessTypeCd",business.getBusinessTypeCd());
            busiMap.put("remark",business.getRemark());
            busiMap.put("statusCd",StatusConstant.STATUS_CD_SAVE);
            busiMap.put("bId",business.getbId());
            businesss.add(busiMap);
        }
        return businesss;
    }


    /**
     * 组装撤单数据
     * @param dataFlow
     * @param message
     * @return
     */
    public static List<Map> getDeleteOrderBusiness(DataFlow dataFlow,String message){
        List<Map> business = new ArrayList<Map>();
        Map busiMap = new HashMap();
        busiMap.put("oId",dataFlow.getoId());
        busiMap.put("businessTypeCd",StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        busiMap.put("remark",message);
        busiMap.put("statusCd",StatusConstant.STATUS_CD_DELETE_ORDER);
        busiMap.put("bId", GenerateCodeFactory.getBId());
        business.add(busiMap);
        return business;
    }

    /**
     * 组装撤单数据
     * @param dataFlow
     * @param message
     * @return
     */
    public static List<Map> getDeleteOrderBusiness(OrderDataFlow dataFlow,String message){
        List<Map> business = new ArrayList<Map>();
        Map busiMap = new HashMap();
        busiMap.put("oId",dataFlow.getOrders().getoId());
        busiMap.put("businessTypeCd",StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        busiMap.put("remark",message);
        busiMap.put("statusCd",StatusConstant.STATUS_CD_DELETE_ORDER);
        busiMap.put("bId", GenerateCodeFactory.getBId());
        business.add(busiMap);
        return business;
    }

    /**
     * 获取订单属性
     * @param dataFlow
     * @return
     */
    public static List<Map> getBusinessAttrs(DataFlow dataFlow){
        List<Map> businessAttrs = new ArrayList<Map>();
        List<Business> businesses = dataFlow.getBusinesses();
        for(Business business :businesses) {
            if (business.getAttrs() == null || business.getAttrs().size() ==0) {
                continue;
            }
            JSONArray attrs = business.getAttrs();
            Map attrMap = null;
            for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
                attrMap = new HashMap();
                attrMap.put("bId", business.getbId());
                attrMap.put("attrId", GenerateCodeFactory.getAttrId());
                attrMap.put("specCd", attrs.getJSONObject(attrIndex).getString("specCd"));
                attrMap.put("value", attrs.getJSONObject(attrIndex).getString("value"));
                businessAttrs.add(attrMap);
            }
        }
        return businessAttrs;
    }


    /**
     * 获取订单属性
     * @param dataFlow
     * @return
     */
    public static List<Map> getBusinessAttrs(OrderDataFlow dataFlow){
        List<Map> businessAttrs = new ArrayList<Map>();
        List<com.java110.entity.order.Business> businesses = dataFlow.getBusinessList();
        for(com.java110.entity.order.Business business :businesses) {
            if (business.getBusinessAttrs() == null || business.getBusinessAttrs().size() ==0) {
                continue;
            }
            List<BusinessAttrs> attrs = business.getBusinessAttrs();
            Map attrMap = null;
            for (BusinessAttrs businessAttrs1 : attrs) {
                attrMap = new HashMap();
                businessAttrs1.setAttrId(GenerateCodeFactory.getAttrId());
                attrMap.put("bId", business.getbId());
                attrMap.put("attrId", businessAttrs1.getAttrId());
                attrMap.put("specCd", businessAttrs1.getSpecCd());
                attrMap.put("value", businessAttrs1.getValue());
                businessAttrs.add(attrMap);
            }
        }
        return businessAttrs;
    }

    /**
     * 获取将要作废的订单
     * @param dataFlow
     * @return
     */
    public static Map getNeedInvalidOrder(DataFlow dataFlow){
        Map order = new HashMap();
        order.put("oId",dataFlow.getoId());
       // order.put("finishTime",DateUtil.getCurrentDate());
        order.put("statusCd",StatusConstant.STATUS_CD_DELETE);
        return order;
    }

    /**
     * 获取将要作废的订单
     * @param dataFlow
     * @return
     */
    public static Map getNeedInvalidOrder(OrderDataFlow dataFlow){
        Map order = new HashMap();
        order.put("oId",dataFlow.getOrders().getoId());
        // order.put("finishTime",DateUtil.getCurrentDate());
        order.put("statusCd",StatusConstant.STATUS_CD_DELETE);
        return order;
    }

    /**
     * 获取将要作废的订单
     * @param dataFlow
     * @return
     */
    public static Map getNeedErrorOrder(DataFlow dataFlow){
        Map order = new HashMap();
        order.put("oId",dataFlow.getoId());
        //order.put("finishTime",DateUtil.getCurrentDate());
        order.put("statusCd",StatusConstant.STATUS_CD_ERROR);
        return order;
    }

    public static Map getNeedCompleteBusiness(DataFlow dataFlow){
        Map business = new HashMap();
        String bId = "";
        for(Business busi:dataFlow.getBusinesses()){
            bId += busi.getbId()+",";
        }
        business.put("bId",bId.substring(0,bId.length()-1));
        business.put("finishTime",DateUtil.getCurrentDate());
        business.put("statusCd",StatusConstant.STATUS_CD_COMPLETE);
        return business;
    }

    /**
     * Business 过程完成
     * @param dataFlow
     * @return
     */
    public static Map getNeedBusinessComplete(DataFlow dataFlow){
        Map business = new HashMap();
        String bId = "";
        for(Business busi:dataFlow.getBusinesses()){
            bId += busi.getbId()+",";
        }
        business.put("bId",bId.substring(0,bId.length()-1));
        business.put("finishTime",DateUtil.getCurrentDate());
        business.put("statusCd",StatusConstant.STATUS_CD_BUSINESS_COMPLETE);
        return business;
    }

    /**
     * Business 过程完成
     * @param dataFlow
     * @return
     */
    public static Map getNeedDeleteBusiness(DataFlow dataFlow){
        Map business = new HashMap();
        String bId = "";
        for(Business busi:dataFlow.getBusinesses()){
            bId += busi.getbId()+",";
        }
        business.put("bId",bId.substring(0,bId.length()-1));
        business.put("finishTime",DateUtil.getCurrentDate());
        business.put("statusCd",StatusConstant.STATUS_CD_DELETE_ORDER);
        return business;
    }

    /**
     * Business 过程完成
     * @param dataFlow
     * @return
     */
    public static Map getNeedDeleteBusiness(OrderDataFlow dataFlow){
        Map business = new HashMap();
        String bId = "";
        for(com.java110.entity.order.Business busi:dataFlow.getBusinessList()){
            bId += busi.getbId()+",";
        }
        business.put("bId",bId.substring(0,bId.length()-1));
        business.put("finishTime",DateUtil.getCurrentDate());
        business.put("statusCd",StatusConstant.STATUS_CD_DELETE_ORDER);
        return business;
    }

    public static Map getNeedNotifyErrorBusiness(DataFlow dataFlow){
        Map business = new HashMap();
        String bId = getMoreBId(dataFlow);
        business.put("bId",bId.substring(0,bId.length()-1));
        business.put("finishTime",DateUtil.getCurrentDate());
        business.put("statusCd",StatusConstant.STATUS_CD_NOTIFY_ERROR);
        return business;
    }

    /**
     * 获取DataFlow 对象中的所有bId
     * @param dataFlow
     * @return
     */
    public static String getMoreBId(DataFlow dataFlow){
        String bId = "";
        for(Business busi:dataFlow.getBusinesses()){
            bId += busi.getbId()+",";
        }
        return bId;
    }


    /**
     * 获取将要完成的订单
     * @param dataFlow
     * @return
     */
    public static Map getNeedCompleteOrder(DataFlow dataFlow){
        Map order = new HashMap();
        order.put("oId",dataFlow.getoId());
        order.put("finishTime",DateUtil.getCurrentDate());
        order.put("statusCd",StatusConstant.STATUS_CD_COMPLETE);
        return order;
    }

    /**
     * 获取竣工消息的报文（订单完成后通知业务系统）
     * @param dataFlow
     * @return
     */
    public static JSONObject getNotifyBusinessSuccessJson(DataFlow dataFlow){
        JSONObject notifyMessage = getTransactionBusinessBaseJson(dataFlow,StatusConstant.NOTIFY_BUSINESS_TYPE);
        JSONArray businesses = notifyMessage.getJSONArray("business");

        JSONObject busi = null;
        JSONObject response = null;
        for(Business business :dataFlow.getBusinesses()){
            busi = new JSONObject();
            busi.put("bId",business.getbId());
            busi.put("serviceCode",business.getServiceCode());
            response = new JSONObject();
            response.put("code",ResponseConstant.RESULT_CODE_SUCCESS);
            response.put("message","成功");
            busi.put("response",response);
            businesses.add(busi);
        }
        return notifyMessage;
    }

    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     * @param dataFlow
     * @return
     */
    public static JSONObject getNotifyBusinessErrorJson(DataFlow dataFlow){

        JSONObject notifyMessage = getTransactionBusinessBaseJson(dataFlow,StatusConstant.NOTIFY_BUSINESS_TYPE);
        JSONArray businesses = notifyMessage.getJSONArray("business");

        JSONObject busi = null;
        JSONObject response = null;
        for(Business business :dataFlow.getBusinesses()){
            busi = new JSONObject();
            busi.put("bId",business.getbId());
            busi.put("serviceCode",business.getServiceCode());
            response = new JSONObject();
            response.put("code",ResponseConstant.RESULT_CODE_INNER_ERROR);
            response.put("message","失败");
            busi.put("response",response);
            businesses.add(busi);
        }
        return notifyMessage;
    }


    public static JSONObject getCompletedBusinessErrorJson(DataFlow dataFlow,Map business,AppService appService){
        JSONObject notifyMessage = getTransactionBusinessBaseJson(dataFlow,StatusConstant.NOTIFY_BUSINESS_TYPE);
        JSONArray businesses = notifyMessage.getJSONArray("business");

        JSONObject busi = null;
        JSONObject response = null;
        busi = new JSONObject();
        busi.put("bId",business.get("b_id"));
        busi.put("serviceCode",appService.getServiceCode());
        response = new JSONObject();
        response.put("code",ResponseConstant.RESULT_CODE_INNER_ERROR);
        response.put("message","失败");
        busi.put("response",response);
        businesses.add(busi);
        return notifyMessage;

    }



    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     * @param business
     * @return
     */
    public static JSONObject getBusinessTableDataInfoToInstanceTableJson(DataFlow dataFlow,Business business){

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow,StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE);
        JSONObject busi = null;
            busi = new JSONObject();
            busi.put("bId",business.getbId());
            busi.put("serviceCode",business.getServiceCode());
            busi.put("serviceName",business.getServiceName());
            //busi.put("isInstance",CommonConstant.INSTANCE_Y);
        //busi.put("datas",business.getDatas());
        requestMessage.put("business",busi);
        return requestMessage;
    }

    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     * @param business
     * @return
     */
    public static JSONObject getBusinessTableDataInfoToInstanceTableJson(OrderDataFlow dataFlow,com.java110.entity.order.Business business){

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow,StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE);
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId",business.getbId());
        busi.put("businessTypeCd",business.getBusinessTypeCd());
        requestMessage.put("business",busi);
        return requestMessage;
    }

    /**
     * 发起撤单请求报文
     * @param business
     * @return
     */
    public static JSONObject getDeleteInstanceTableJson(DataFlow dataFlow,Business business){

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow,StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId",business.getbId());
        busi.put("serviceCode",business.getServiceCode());
        busi.put("serviceName",business.getServiceName());
        //busi.put("datas",business.getDatas());
        requestMessage.put("business",busi);
        return requestMessage;
    }

    /**
     * 发起撤单请求报文
     * @param business
     * @return
     */
    public static JSONObject getDeleteInstanceTableJson(OrderDataFlow dataFlow,com.java110.entity.order.Business business){

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow,StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId",business.getbId());
        busi.put("businessTypeCd",business.getBusinessTypeCd());
        //busi.put("datas",business.getDatas());
        requestMessage.put("business",busi);
        return requestMessage;
    }

    public static JSONObject getDeleteInstanceTableJson(DataFlow dataFlow,Map business,AppService appService){
        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow,StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId",business.get("b_id"));
        busi.put("serviceCode",appService.getServiceCode());
        //busi.put("datas",business.getDatas());
        requestMessage.put("business",busi);
        return requestMessage;

    }

    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     * @param business
     * @return
     */
    public static JSONObject getCompleteInstanceDataJson(DataFlow dataFlow,Business business){

        JSONObject notifyMessage = getTransactionBusinessBaseJson(dataFlow,StatusConstant.REQUEST_BUSINESS_TYPE);
        //JSONObject businesses = notifyMessage.getJSONObject("business");
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId",business.getbId());
        busi.put("serviceCode",business.getServiceCode());
        busi.put("serviceName",business.getServiceName());
        busi.put("isInstance",CommonConstant.INSTANCE_Y);
        notifyMessage.put("business",busi);
        return notifyMessage;
    }

    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     * @param business
     * @return
     */
    public static JSONObject getRequestBusinessJson(DataFlow dataFlow,Business business){

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow,StatusConstant.REQUEST_BUSINESS_TYPE_BUSINESS);
        //JSONObject businesses = notifyMessage.getJSONObject("business");
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId",business.getbId());
        busi.put("serviceCode",business.getServiceCode());
        busi.put("serviceName",business.getServiceName());
        busi.put("remark",business.getRemark());
        busi.put("datas",business.getDatas());
        requestMessage.put("business",busi);
        return requestMessage;
    }


    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     * @param business
     * @return
     */
    public static JSONObject getRequestBusinessJson(OrderDataFlow dataFlow,com.java110.entity.order.Business business){

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow,StatusConstant.REQUEST_BUSINESS_TYPE_BUSINESS);
        //JSONObject businesses = notifyMessage.getJSONObject("business");
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId",business.getbId());
        busi.put("businessTypeCd",business.getBusinessTypeCd());
        busi.put("remark",business.getRemark());
        busi.put("datas",business.getData());
        requestMessage.put("business",busi);
        return requestMessage;
    }

    /**
     * 业务系统交互
     * @return
     */
    private static JSONObject getTransactionBusinessBaseJson(DataFlow dataFlow,String businessType){
        JSONObject notifyMessage = JSONObject.parseObject("{\"orders\":{},\"business\":{}}");
        JSONObject orders = notifyMessage.getJSONObject("orders");
        orders.put("transactionId", UUID.randomUUID().toString().replace("-",""));
        orders.put("dataFlowId",dataFlow.getDataFlowId());
        orders.put("orderTypeCd",dataFlow.getOrderTypeCd());
        orders.put("requestTime",DateUtil.getyyyyMMddhhmmssDateString());
        orders.put("businessType",businessType);
        return notifyMessage;
    }

    /**
     * 业务系统交互
     * @return
     */
    private static JSONObject getTransactionBusinessBaseJson(OrderDataFlow dataFlow,String businessType){
        JSONObject notifyMessage = JSONObject.parseObject("{\"orders\":{},\"business\":{}}");
        JSONObject orders = notifyMessage.getJSONObject("orders");
        orders.put("transactionId", UUID.randomUUID().toString().replace("-",""));
        orders.put("dataFlowId",dataFlow.getDataFlowId());
        orders.put("orderTypeCd",dataFlow.getOrders().getOrderTypeCd());
        orders.put("requestTime",DateUtil.getyyyyMMddhhmmssDateString());
        orders.put("businessType",businessType);
        return notifyMessage;
    }

    /**
     * 获取同步处理业务
     * @param dataFlow
     * @return
     */
    public static List<Business> getSynchronousBusinesses(DataFlow dataFlow){
        AppService service = null;
        AppRoute route = null;
        List<Business> syschronousBusinesses = new ArrayList<Business>();
        for(Business business :dataFlow.getBusinesses()){
            route = DataFlowFactory.getRoute(dataFlow,business.getServiceCode());
            service = route.getAppService();
            if(CommonConstant.ORDER_INVOKE_METHOD_SYNCHRONOUS.equals(route.getInvokeModel())){
                business.setSeq(service.getSeq());
                syschronousBusinesses.add(business);
            }
        }
        if(syschronousBusinesses.size() > 0) {
            Collections.sort(syschronousBusinesses);
        }

        return syschronousBusinesses;
    }


    /**
     * 获取同步处理业务
     * @param dataFlow
     * @return
     */
    public static List<com.java110.entity.order.Business> getSynchronousBusinesses(OrderDataFlow dataFlow){
        AppService service = null;
        AppRoute route = null;
        List<com.java110.entity.order.Business> syschronousBusinesses = new ArrayList<com.java110.entity.order.Business>();
        for(com.java110.entity.order.Business business :dataFlow.getBusinessList()){

            if(CommonConstant.ORDER_INVOKE_METHOD_SYNCHRONOUS.equals(business.getInvokeModel()) || StringUtil.isEmpty(business.getInvokeModel())){
                business.setSeq(service.getSeq());
                syschronousBusinesses.add(business);
            }
        }
        if(syschronousBusinesses.size() > 0) {
            Collections.sort(syschronousBusinesses);
        }

        return syschronousBusinesses;
    }


    /**
     * 获取异步处理业务
     * @param dataFlow
     * @return
     */
    public static List<Business> getAsynchronousBusinesses(DataFlow dataFlow){
        AppService service = null;
        AppRoute route = null;
        List<Business> syschronousBusinesses = new ArrayList<Business>();
        for(Business business :dataFlow.getBusinesses()){
            route = DataFlowFactory.getRoute(dataFlow,business.getServiceCode());
            service = route.getAppService();
            if(CommonConstant.ORDER_INVOKE_METHOD_ASYNCHRONOUS.equals(route.getInvokeModel())){

                syschronousBusinesses.add(business);
            }
        }

        return syschronousBusinesses;
    }

    /**
     * 获取异步处理业务
     * @param dataFlow
     * @return
     */
    public static List<com.java110.entity.order.Business> getAsynchronousBusinesses(OrderDataFlow dataFlow){

        List<com.java110.entity.order.Business> asynchronousBusinesses = new ArrayList<com.java110.entity.order.Business>();
        for(com.java110.entity.order.Business business :dataFlow.getBusinessList()){
            if(CommonConstant.ORDER_INVOKE_METHOD_ASYNCHRONOUS.equals(business.getInvokeModel())){

                asynchronousBusinesses.add(business);
            }
        }

        return asynchronousBusinesses;
    }


    /**
     * hashmap 转MultiValueMap
     * @param httpHeaders
     * @return
     */
    public static MultiValueMap<String, String> hashMap2MultiValueMap(Map<String,String> httpHeaders){
        MultiValueMap<String, String> multiValueMap = new HttpHeaders();
        for(String key:httpHeaders.keySet()) {
            multiValueMap.add(key,httpHeaders.get(key));
        }

        return multiValueMap;
    }


}
