package com.java110.core.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlow;
import com.java110.core.context.IOrderDataFlowContext;
import com.java110.core.context.IOrderNotifyDataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.entity.center.Business;
import com.java110.entity.center.DataFlowLinksCost;
import com.java110.entity.order.BusinessAttrs;
import com.java110.entity.order.Orders;
import com.java110.entity.order.OrdersAttrs;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * 数据流工厂类
 * Created by wuxw on 2018/4/13.
 */
public class OrderDataFlowContextFactory {

    /**
     * 初始化
     *
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
            return clazz.getConstructor(Date.class, String.class).newInstance(DateUtil.getCurrentDate(), ResponseConstant.RESULT_CODE_SUCCESS);
        } catch (InstantiationException ex) {
            throw new BeanInstantiationException(clazz, "是一个抽象类?", ex);
        } catch (Exception ex) {
            throw new BeanInstantiationException(clazz, "构造函数不能访问?", ex);

        }
    }


    /**
     * 添加耗时
     *
     * @param dataFlow  数据流
     * @param linksCode 环节编码
     * @param linksName 环节名称
     * @param startDate 开始时间
     * @return
     */
    public static void addCostTime(IOrderDataFlowContext dataFlow, String linksCode, String linksName, Date startDate) {
        if (MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,MappingConstant.KEY_COST_TIME_ON_OFF))) {
            DataFlowLinksCost dataFlowLinksCost = new DataFlowLinksCost().builder(linksCode, linksName, startDate, DateUtil.getCurrentDate());
            //dataFlow.addLinksCostDates(dataFlowLinksCost);
        }
    }


    /**
     * 获取将要作废的订单
     *
     * @param dataFlow
     * @return
     */
    public static Map getNeedErrorOrder(IOrderDataFlowContext dataFlow) {
        Map order = new HashMap();
        order.put("oId", dataFlow.getOrders().getoId());
        //order.put("finishTime",DateUtil.getCurrentDate());
        order.put("statusCd", StatusConstant.STATUS_CD_ERROR);
        return order;
    }

    /**
     * 获取将要作废的订单
     *
     * @param dataFlow
     * @return
     */
    public static Map getNeedErrorOrder(IOrderNotifyDataFlowContext dataFlow) {
        Map order = new HashMap();
        order.put("oId", dataFlow.getoId());
        //order.put("finishTime",DateUtil.getCurrentDate());
        order.put("statusCd", StatusConstant.STATUS_CD_ERROR);
        return order;
    }


    /**
     * 添加耗时
     *
     * @param dataFlow  数据流
     * @param linksCode 环节编码
     * @param linksName 环节名称
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public static void addCostTime(IOrderDataFlowContext dataFlow, String linksCode, String linksName, Date startDate, Date endDate) {
        if (MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,MappingConstant.KEY_COST_TIME_ON_OFF))) {
            DataFlowLinksCost dataFlowLinksCost = new DataFlowLinksCost().builder(linksCode, linksName, startDate, endDate);
            //dataFlow.addLinksCostDates(dataFlowLinksCost);
        }
    }

    /**
     * 获取Order信息
     *
     * @param orders 订单信息
     * @return
     */
    public static Map getOrder(Orders orders) {
        Map order = new HashMap();
        if (StringUtil.isEmpty(orders.getoId()) || orders.getoId().startsWith("-")) {
            orders.setoId(GenerateCodeFactory.getOId());
        }
        order.put("oId", orders.getoId());
        order.put("appId", orders.getAppId());
        order.put("extTransactionId", orders.getExtTransactionId());
        order.put("userId", orders.getUserId());
        order.put("requestTime", orders.getRequestTime());
        order.put("orderTypeCd", orders.getOrderTypeCd());
        order.put("remark", orders.getRemark());
        order.put("statusCd", StatusConstant.STATUS_CD_SAVE);
        return order;
    }


    /**
     * 获取订单项
     *
     * @param dataFlow
     * @return
     */
    public static List<Map> getBusiness(IOrderDataFlowContext dataFlow) {
        List<Map> businesss = new ArrayList<Map>();
        Map busiMap = null;
        List<com.java110.entity.order.Business> businessList = dataFlow.getBusinessList();
        for (com.java110.entity.order.Business business : businessList) {
            if (business == null) {
                continue;
            }
            business.setbId(GenerateCodeFactory.getBId());
            busiMap = new HashMap();
            busiMap.put("oId", dataFlow.getOrders().getoId());
            busiMap.put("businessTypeCd", business.getBusinessTypeCd());
            busiMap.put("remark", business.getRemark());
            busiMap.put("statusCd", StatusConstant.STATUS_CD_SAVE);
            busiMap.put("bId", business.getbId());
            businesss.add(busiMap);
        }
        return businesss;
    }

    /**
     * 组装撤单数据
     *
     * @param dataFlow
     * @param message
     * @return
     */
    public static List<Map> getDeleteOrderBusiness(IOrderDataFlowContext dataFlow, String message) {
        List<Map> business = new ArrayList<Map>();
        Map busiMap = new HashMap();
        busiMap.put("oId", dataFlow.getOrders().getoId());
        busiMap.put("businessTypeCd", StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        busiMap.put("remark", message);
        busiMap.put("statusCd", StatusConstant.STATUS_CD_DELETE_ORDER);
        busiMap.put("bId", GenerateCodeFactory.getBId());
        business.add(busiMap);
        return business;
    }


    /**
     * 组装撤单数据
     *
     * @param dataFlow
     * @param message
     * @return
     */
    public static List<Map> getDeleteOrderBusiness(IOrderNotifyDataFlowContext dataFlow, String message) {
        List<Map> business = new ArrayList<Map>();
        Map busiMap = new HashMap();
        busiMap.put("oId", dataFlow.getoId());
        busiMap.put("businessTypeCd", StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        busiMap.put("remark", message);
        busiMap.put("statusCd", StatusConstant.STATUS_CD_DELETE_ORDER);
        busiMap.put("bId", GenerateCodeFactory.getBId());
        business.add(busiMap);
        return business;
    }


    /**
     * 获取订单属性
     *
     * @param dataFlow
     * @return
     */
    public static List<Map> getBusinessAttrs(IOrderDataFlowContext dataFlow) {
        List<Map> businessAttrs = new ArrayList<Map>();
        List<com.java110.entity.order.Business> businesses = dataFlow.getBusinessList();
        for (com.java110.entity.order.Business business : businesses) {
            if (business.getBusinessAttrs() == null || business.getBusinessAttrs().size() == 0) {
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
     *
     * @param dataFlow
     * @return
     */
    public static Map getNeedInvalidOrder(IOrderDataFlowContext dataFlow) {
        Map order = new HashMap();
        order.put("oId", dataFlow.getOrders().getoId());
        // order.put("finishTime",DateUtil.getCurrentDate());
        order.put("statusCd", StatusConstant.STATUS_CD_DELETE);
        return order;
    }


    /**
     * 获取将要作废的订单
     *
     * @param dataFlow
     * @return
     */
    public static Map getNeedInvalidOrder(IOrderNotifyDataFlowContext dataFlow) {
        Map order = new HashMap();
        order.put("oId", dataFlow.getoId());
        // order.put("finishTime",DateUtil.getCurrentDate());
        order.put("statusCd", StatusConstant.STATUS_CD_DELETE);
        return order;
    }

    /**
     * Business 过程完成
     *
     * @param dataFlow
     * @return
     */
    public static Map getNeedDeleteBusiness(IOrderDataFlowContext dataFlow, List<com.java110.entity.order.Business> deleteBusinesses) {
        Map business = new HashMap();
        String bId = "";
        for (com.java110.entity.order.Business busi : deleteBusinesses) {
            bId += busi.getbId() + ",";
        }
        business.put("bId", bId.substring(0, bId.length() - 1));
        business.put("finishTime", DateUtil.getCurrentDate());
        business.put("statusCd", StatusConstant.STATUS_CD_DELETE_ORDER);
        return business;
    }

    /**
     * Business 过程完成
     *
     * @param dataFlow
     * @return
     */
    public static Map getNeedDeleteBusiness(IOrderNotifyDataFlowContext dataFlow) {
        Map business = new HashMap();
        String bId = "";

        business.put("bId", dataFlow.getbId());
        business.put("finishTime", DateUtil.getCurrentDate());
        business.put("statusCd", StatusConstant.STATUS_CD_DELETE_ORDER);
        return business;
    }

    public static Map getNeedNotifyErrorBusiness(DataFlow dataFlow) {
        Map business = new HashMap();
        String bId = getMoreBId(dataFlow);
        business.put("bId", bId.substring(0, bId.length() - 1));
        business.put("finishTime", DateUtil.getCurrentDate());
        business.put("statusCd", StatusConstant.STATUS_CD_NOTIFY_ERROR);
        return business;
    }

    public static Map getNeedNotifyErrorBusiness(IOrderNotifyDataFlowContext dataFlow) {
        Map business = new HashMap();
        business.put("bId", dataFlow.getbId());
        business.put("finishTime", DateUtil.getCurrentDate());
        business.put("statusCd", StatusConstant.STATUS_CD_NOTIFY_ERROR);
        return business;
    }

    /**
     * 获取DataFlow 对象中的所有bId
     *
     * @param dataFlow
     * @return
     */
    public static String getMoreBId(DataFlow dataFlow) {
        String bId = "";
        for (Business busi : dataFlow.getBusinesses()) {
            bId += busi.getbId() + ",";
        }
        return bId;
    }


    /**
     * 获取将要完成的订单
     *
     * @param dataFlow
     * @return
     */
    public static Map getNeedCompleteOrder(DataFlow dataFlow) {
        Map order = new HashMap();
        order.put("oId", dataFlow.getoId());
        order.put("finishTime", DateUtil.getCurrentDate());
        order.put("statusCd", StatusConstant.STATUS_CD_COMPLETE);
        return order;
    }

    /**
     * 获取竣工消息的报文（订单完成后通知业务系统）
     *
     * @param dataFlow
     * @return
     */
    public static JSONObject getNotifyBusinessSuccessJson(DataFlow dataFlow) {
        JSONObject notifyMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.NOTIFY_BUSINESS_TYPE);
        JSONArray businesses = notifyMessage.getJSONArray("business");

        JSONObject busi = null;
        JSONObject response = null;
        for (Business business : dataFlow.getBusinesses()) {
            busi = new JSONObject();
            busi.put("bId", business.getbId());
            busi.put("serviceCode", business.getServiceCode());
            response = new JSONObject();
            response.put("code", ResponseConstant.RESULT_CODE_SUCCESS);
            response.put("message", "成功");
            busi.put("response", response);
            businesses.add(busi);
        }
        return notifyMessage;
    }

    /**
     * 获取竣工消息的报文（订单完成后通知业务系统）
     *
     * @param dataFlow
     * @return
     */
    public static JSONObject getNotifyBusinessSuccessJson(IOrderNotifyDataFlowContext dataFlow) {
        JSONObject notifyMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.NOTIFY_BUSINESS_TYPE);
        JSONArray businesses = notifyMessage.getJSONArray("business");

        JSONObject busi = null;
        JSONObject response = null;
        busi = new JSONObject();
        busi.put("bId", dataFlow.getbId());
        busi.put("businessTypeCd", dataFlow.getBusinessTypeCd());
        response = new JSONObject();
        response.put("code", ResponseConstant.RESULT_CODE_SUCCESS);
        response.put("message", "成功");
        busi.put("response", response);
        businesses.add(busi);
        return notifyMessage;
    }

    /**
     * 获取订单属性
     *
     * @param orders
     * @return
     */
    public static List<Map> getOrderAttrs(Orders orders) {
        List<Map> orderAttrs = new ArrayList<Map>();

        List<OrdersAttrs> attrs = orders.getOrdersAttrs();

        if (attrs == null || attrs.size() == 0) {
            return orderAttrs;
        }

        Map attrMap = null;
        for (OrdersAttrs ordersAttr : attrs) {
            ordersAttr.setAttrId(GenerateCodeFactory.getAttrId());
            attrMap = new HashMap();
            attrMap.put("oId", orders.getoId());
            attrMap.put("attrId", ordersAttr.getAttrId());
            attrMap.put("specCd", ordersAttr.getSpecCd());
            attrMap.put("value", ordersAttr.getValue());
            orderAttrs.add(attrMap);
        }
        return orderAttrs;
    }

    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     *
     * @param dataFlow
     * @return
     */
    public static JSONObject getNotifyBusinessErrorJson(DataFlow dataFlow) {

        JSONObject notifyMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.NOTIFY_BUSINESS_TYPE);
        JSONArray businesses = notifyMessage.getJSONArray("business");

        JSONObject busi = null;
        JSONObject response = null;
        for (Business business : dataFlow.getBusinesses()) {
            busi = new JSONObject();
            busi.put("bId", business.getbId());
            busi.put("serviceCode", business.getServiceCode());
            response = new JSONObject();
            response.put("code", ResponseConstant.RESULT_CODE_INNER_ERROR);
            response.put("message", "失败");
            busi.put("response", response);
            businesses.add(busi);
        }
        return notifyMessage;
    }

    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     *
     * @param dataFlow
     * @return
     */
    public static JSONObject getNotifyBusinessErrorJson(IOrderNotifyDataFlowContext dataFlow) {

        JSONObject notifyMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.NOTIFY_BUSINESS_TYPE);
        JSONArray businesses = notifyMessage.getJSONArray("business");

        JSONObject busi = null;
        JSONObject response = null;
        busi = new JSONObject();
        busi.put("bId", dataFlow.getbId());
        busi.put("businessTypeCd", dataFlow.getBusinessTypeCd());
        response = new JSONObject();
        response.put("code", ResponseConstant.RESULT_CODE_INNER_ERROR);
        response.put("message", "失败");
        busi.put("response", response);
        businesses.add(busi);
        return notifyMessage;
    }

    public static JSONObject getCompletedBusinessErrorJson(DataFlow dataFlow, Map business, AppService appService) {
        JSONObject notifyMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.NOTIFY_BUSINESS_TYPE);
        JSONArray businesses = notifyMessage.getJSONArray("business");

        JSONObject busi = null;
        JSONObject response = null;
        busi = new JSONObject();
        busi.put("bId", business.get("b_id"));
        busi.put("serviceCode", appService.getServiceCode());
        response = new JSONObject();
        response.put("code", ResponseConstant.RESULT_CODE_INNER_ERROR);
        response.put("message", "失败");
        busi.put("response", response);
        businesses.add(busi);
        return notifyMessage;

    }


    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     *
     * @param business
     * @return
     */
    public static JSONObject getBusinessTableDataInfoToInstanceTableJson(DataFlow dataFlow, Business business) {

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE);
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId", business.getbId());
        busi.put("serviceCode", business.getServiceCode());
        busi.put("serviceName", business.getServiceName());
        //busi.put("isInstance",CommonConstant.INSTANCE_Y);
        //busi.put("datas",business.getDatas());
        requestMessage.put("business", busi);
        return requestMessage;
    }

    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     *
     * @param business
     * @return
     */
    public static JSONObject getBusinessTableDataInfoToInstanceTableJson(IOrderDataFlowContext dataFlow, com.java110.entity.order.Business business) {

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE);
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId", business.getbId());
        busi.put("businessTypeCd", business.getBusinessTypeCd());
        requestMessage.put("business", busi);
        return requestMessage;
    }

    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     *
     * @param dataFlow
     * @return
     */
    public static JSONObject getBusinessTableDataInfoToInstanceTableJson(IOrderNotifyDataFlowContext dataFlow) {

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE);
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId", dataFlow.getbId());
        busi.put("businessTypeCd", dataFlow.getBusinessTypeCd());
        requestMessage.put("business", busi);
        return requestMessage;
    }


    /**
     * 发起撤单请求报文
     *
     * @param business
     * @return
     */
    public static JSONObject getDeleteInstanceTableJson(DataFlow dataFlow, Business business) {

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId", business.getbId());
        busi.put("serviceCode", business.getServiceCode());
        busi.put("serviceName", business.getServiceName());
        //busi.put("datas",business.getDatas());
        requestMessage.put("business", busi);
        return requestMessage;
    }

    /**
     * 发起撤单请求报文
     *
     * @param business
     * @return
     */
    public static JSONObject getDeleteInstanceTableJson(IOrderDataFlowContext dataFlow, com.java110.entity.order.Business business) {

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId", business.getbId());
        busi.put("businessTypeCd", business.getBusinessTypeCd());
        //busi.put("datas",business.getDatas());
        requestMessage.put("business", busi);
        return requestMessage;
    }

    public static JSONObject getDeleteInstanceTableJson(DataFlow dataFlow, Map business, AppService appService) {
        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId", business.get("b_id"));
        busi.put("serviceCode", appService.getServiceCode());
        //busi.put("datas",business.getDatas());
        requestMessage.put("business", busi);
        return requestMessage;

    }

    public static JSONObject getDeleteInstanceTableJson(IOrderNotifyDataFlowContext dataFlow, Map business) {
        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId", business.get("b_id"));
        busi.put("businessTypeCd", business.get("business_type_cd").toString());
        //busi.put("datas",business.getDatas());
        requestMessage.put("business", busi);
        return requestMessage;

    }

    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     *
     * @param business
     * @return
     */
    public static JSONObject getCompleteInstanceDataJson(DataFlow dataFlow, Business business) {

        JSONObject notifyMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.REQUEST_BUSINESS_TYPE);
        //JSONObject businesses = notifyMessage.getJSONObject("business");
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId", business.getbId());
        busi.put("serviceCode", business.getServiceCode());
        busi.put("serviceName", business.getServiceName());
        busi.put("isInstance", CommonConstant.INSTANCE_Y);
        notifyMessage.put("business", busi);
        return notifyMessage;
    }

    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     *
     * @param business
     * @return
     */
    public static JSONObject getRequestBusinessJson(DataFlow dataFlow, Business business) {

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.REQUEST_BUSINESS_TYPE_BUSINESS);
        //JSONObject businesses = notifyMessage.getJSONObject("business");
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId", business.getbId());
        busi.put("serviceCode", business.getServiceCode());
        busi.put("serviceName", business.getServiceName());
        busi.put("remark", business.getRemark());
        busi.put("datas", business.getDatas());
        requestMessage.put("business", busi);
        return requestMessage;
    }


    /**
     * 获取失败消息的报文（订单失败后通知业务系统）
     *
     * @param business
     * @return
     */
    public static JSONObject getRequestBusinessJson(IOrderDataFlowContext dataFlow, com.java110.entity.order.Business business) {

        JSONObject requestMessage = getTransactionBusinessBaseJson(dataFlow, StatusConstant.REQUEST_BUSINESS_TYPE_BUSINESS);
        //JSONObject businesses = notifyMessage.getJSONObject("business");
        JSONObject busi = null;
        busi = new JSONObject();
        busi.put("bId", business.getbId());
        busi.put("businessTypeCd", business.getBusinessTypeCd());
        busi.put("remark", business.getRemark());
        busi.put("datas", business.getData());
        requestMessage.put("business", busi);
        return requestMessage;
    }

    /**
     * 业务系统交互
     *
     * @return
     */
    private static JSONObject getTransactionBusinessBaseJson(DataFlow dataFlow, String businessType) {
        JSONObject notifyMessage = JSONObject.parseObject("{\"orders\":{},\"business\":{}}");
        JSONObject orders = notifyMessage.getJSONObject("orders");
        orders.put("transactionId", UUID.randomUUID().toString().replace("-", ""));
        orders.put("dataFlowId", dataFlow.getDataFlowId());
        orders.put("orderTypeCd", dataFlow.getOrderTypeCd());
        orders.put("requestTime", DateUtil.getyyyyMMddhhmmssDateString());
        orders.put("businessType", businessType);
        return notifyMessage;
    }

    /**
     * 业务系统交互
     *
     * @return
     */
    private static JSONObject getTransactionBusinessBaseJson(IOrderDataFlowContext dataFlow, String businessType) {
        JSONObject notifyMessage = JSONObject.parseObject("{\"orders\":{},\"business\":{}}");
        JSONObject orders = notifyMessage.getJSONObject("orders");
        orders.put("transactionId", UUID.randomUUID().toString().replace("-", ""));
        orders.put("dataFlowId", dataFlow.getDataFlowId());
        orders.put("orderTypeCd", dataFlow.getOrders().getOrderTypeCd());
        orders.put("requestTime", DateUtil.getyyyyMMddhhmmssDateString());
        orders.put("businessType", businessType);
        return notifyMessage;
    }

    /**
     * 业务系统交互
     *
     * @return
     */
    private static JSONObject getTransactionBusinessBaseJson(IOrderNotifyDataFlowContext dataFlow, String businessType) {
        JSONObject notifyMessage = JSONObject.parseObject("{\"orders\":{},\"business\":{}}");
        JSONObject orders = notifyMessage.getJSONObject("orders");
        orders.put("transactionId", UUID.randomUUID().toString().replace("-", ""));
        orders.put("dataFlowId", dataFlow.getDataFlowId());
        orders.put("orderTypeCd", dataFlow.getOrderTypeCd());
        orders.put("requestTime", DateUtil.getyyyyMMddhhmmssDateString());
        orders.put("businessType", businessType);
        return notifyMessage;
    }


    /**
     * 获取同步处理业务
     *
     * @param dataFlow
     * @return
     */
    public static List<com.java110.entity.order.Business> getSynchronousBusinesses(IOrderDataFlowContext dataFlow) {
        List<com.java110.entity.order.Business> syschronousBusinesses = new ArrayList<com.java110.entity.order.Business>();
        for (com.java110.entity.order.Business business : dataFlow.getBusinessList()) {

            if (CommonConstant.ORDER_INVOKE_METHOD_SYNCHRONOUS.equals(business.getInvokeModel()) || StringUtil.isEmpty(business.getInvokeModel())) {
                syschronousBusinesses.add(business);
            }
        }
        if (syschronousBusinesses.size() > 0) {
            Collections.sort(syschronousBusinesses);
        }

        return syschronousBusinesses;
    }


    /**
     * 获取异步处理业务
     *
     * @param dataFlow
     * @return
     */
    public static List<com.java110.entity.order.Business> getAsynchronousBusinesses(IOrderDataFlowContext dataFlow) {

        List<com.java110.entity.order.Business> asynchronousBusinesses = new ArrayList<com.java110.entity.order.Business>();
        for (com.java110.entity.order.Business business : dataFlow.getBusinessList()) {
            if (CommonConstant.ORDER_INVOKE_METHOD_ASYNCHRONOUS.equals(business.getInvokeModel())) {

                asynchronousBusinesses.add(business);
            }
        }

        return asynchronousBusinesses;
    }


    /**
     * hashmap 转MultiValueMap
     *
     * @param httpHeaders
     * @return
     */
    public static MultiValueMap<String, String> hashMap2MultiValueMap(Map<String, String> httpHeaders) {
        MultiValueMap<String, String> multiValueMap = new HttpHeaders();
        for (String key : httpHeaders.keySet()) {
            multiValueMap.add(key, httpHeaders.get(key));
        }

        return multiValueMap;
    }


    public static Map getNeedCompleteBusiness(IOrderNotifyDataFlowContext dataFlow) {
        Map business = new HashMap();
        business.put("bId", dataFlow.getbId());
        business.put("finishTime", DateUtil.getCurrentDate());
        business.put("statusCd", StatusConstant.STATUS_CD_COMPLETE);
        return business;
    }

    /**
     * Business 过程完成
     *
     * @param dataFlow
     * @return
     */
    public static Map getNeedBusinessComplete(IOrderNotifyDataFlowContext dataFlow) {
        Map business = new HashMap();

        business.put("bId", dataFlow.getbId());
        business.put("finishTime", DateUtil.getCurrentDate());
        business.put("statusCd", StatusConstant.STATUS_CD_BUSINESS_COMPLETE);
        return business;
    }

}
