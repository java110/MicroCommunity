package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.context.IOrderDataFlowContext;
import com.java110.core.context.SecureInvocation;
import com.java110.core.factory.OrderDataFlowContextFactory;
import com.java110.entity.order.Business;
import com.java110.entity.order.ServiceBusiness;
import com.java110.core.event.center.DataFlowEventPublishing;
import com.java110.order.dao.ICenterServiceDAO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceBusinessConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.exception.ConfigDataException;
import com.java110.utils.exception.OrdersException;
import com.java110.utils.exception.RuleException;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class AbstractOrderServiceSMOImpl {

    private static Logger logger = LoggerFactory.getLogger(AbstractOrderServiceSMOImpl.class);


    @Autowired
    protected ICenterServiceDAO centerServiceDaoImpl;


    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected RestTemplate outRestTemplate;

    /**
     * 4.0规则校验
     *
     * @param dataFlow
     * @throws RuleException
     */
    protected void ruleValidate(IOrderDataFlowContext dataFlow) throws RuleException {
        Date startDate = DateUtil.getCurrentDate();
        try {

            if (MappingConstant.VALUE_OFF.equals(MappingCache.getValue(MappingConstant.KEY_RULE_ON_OFF))
                    || (MappingCache.getValue(MappingConstant.KEY_NO_NEED_RULE_VALDATE_ORDER) != null
                    && MappingCache.getValue(MappingConstant.KEY_NO_NEED_RULE_VALDATE_ORDER).contains(dataFlow.getOrders().getOrderTypeCd()))) {
                //不做校验
                //添加耗时
                OrderDataFlowContextFactory.addCostTime(dataFlow, "ruleValidate", "规则校验耗时", startDate);
                return;
            }

            //调用规则

        } catch (Exception e) {
            //添加耗时
            OrderDataFlowContextFactory.addCostTime(dataFlow, "ruleValidate", "规则校验耗时", startDate);
            throw new RuleException(ResponseConstant.RESULT_CODE_RULE_ERROR, "规则校验异常失败：" + e.getMessage());
        }

        OrderDataFlowContextFactory.addCostTime(dataFlow, "ruleValidate", "规则校验耗时", startDate);

    }


    /**
     * 5.0 保存订单和业务项 c_orders c_order_attrs c_business c_business_attrs
     *
     * @param dataFlow
     * @throws OrdersException
     */
    protected void saveOrdersAndBusiness(IOrderDataFlowContext dataFlow) throws OrdersException {
        Date startDate = DateUtil.getCurrentDate();
        if (MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER) != null
                && MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER).contains(dataFlow.getOrders().getOrderTypeCd())) {
            //不保存订单信息
            OrderDataFlowContextFactory.addCostTime(dataFlow, "saveOrdersAndBusiness", "保存订单和业务项耗时", startDate);
            return;
        }


        //1.0 保存 orders信息
        centerServiceDaoImpl.saveOrder(OrderDataFlowContextFactory.getOrder(dataFlow.getOrders()));


        centerServiceDaoImpl.saveOrderAttrs(OrderDataFlowContextFactory.getOrderAttrs(dataFlow.getOrders()));

        //2.0 保存 business信息

        centerServiceDaoImpl.saveBusiness(OrderDataFlowContextFactory.getBusiness(dataFlow));

        centerServiceDaoImpl.saveBusinessAttrs(OrderDataFlowContextFactory.getBusinessAttrs(dataFlow));

        OrderDataFlowContextFactory.addCostTime(dataFlow, "saveOrdersAndBusiness", "保存订单和业务项耗时", startDate);
    }

    /**
     * 刷返回值
     *
     * @param dataFlow
     */
    protected void refreshOrderDataFlowResJson(IOrderDataFlowContext dataFlow) {

//        if(dataFlow.getResJson() == null || dataFlow.getResJson().isEmpty()){
//            JSONObject resJson = new JSONObject();
//            resJson.put("msg","成功");
//            dataFlow.setResJson(resJson);
//        }

    }


    /**
     * 6.0 调用下游系统
     *
     * @param dataFlow
     * @throws BusinessException
     */
    protected void invokeBusinessSystem(IOrderDataFlowContext dataFlow) throws BusinessException {
        Date startDate = DateUtil.getCurrentDate();
        //6.1 先处理同步方式的服务，每一同步后发布事件广播

        doSynchronousBusinesses(dataFlow);

        //6.2 处理异步服务
        doAsynchronousBusinesses(dataFlow);


        OrderDataFlowContextFactory.addCostTime(dataFlow, "invokeBusinessSystem", "调用下游系统耗时", startDate);
    }

    protected  void doAsynchronousBusinesses(IOrderDataFlowContext dataFlow){

    }


    /**
     * 处理同步业务
     *
     * @param dataFlow
     */
    protected void doSynchronousBusinesses(IOrderDataFlowContext dataFlow) throws BusinessException {
        Date startDate = DateUtil.getCurrentDate();
        List<Business> synchronousBusinesses = OrderDataFlowContextFactory.getSynchronousBusinesses(dataFlow);

        List<Business> deleteBusinesses = new ArrayList<Business>();

        if (synchronousBusinesses == null || synchronousBusinesses.size() == 0) {
            return;
        }
        JSONArray responseBusinesses = new JSONArray();

        //6.1处理同步服务 发起Business
        doSaveDataInfoToBusinessTable(dataFlow, synchronousBusinesses, responseBusinesses);

        try {
            //6.2发起Instance
            doBusinessTableDataInfoToInstanceTable(dataFlow, synchronousBusinesses, deleteBusinesses);
        } catch (Exception e) {
            try {
                //这里发起撤单逻辑
                doDeleteOrderAndInstanceData(dataFlow, deleteBusinesses);
            } catch (Exception e1) {
                logger.error("撤单失败", e1);
                //这里记录撤单失败的信息
            }
            throw new BusinessException(ResponseConstant.RESULT_PARAM_ERROR, e.getMessage());
        }
        //6.3 c_business 数据修改为完成
        /*List<Business> asynchronousBusinesses = OrderDataFlowContextFactory.getAsynchronousBusinesses(dataFlow);
        if(asynchronousBusinesses == null || asynchronousBusinesses.size() == 0){
            doComplateOrderAndBusiness(dataFlow,synchronousBusinesses);
        }*/
        OrderDataFlowContextFactory.addCostTime(dataFlow, "doSynchronousBusinesses", "同步调用业务系统总耗时", startDate);
    }


    /**
     * 数据保存到BusinessTable 中
     *
     * @param dataFlow
     * @param synchronousBusinesses
     * @param responseBusinesses
     */
    protected void doSaveDataInfoToBusinessTable(IOrderDataFlowContext dataFlow, List<Business> synchronousBusinesses, JSONArray responseBusinesses) {
        Date businessStartDate;
        ServiceBusiness serviceBusiness;
        JSONObject requestBusinessJson;
        for (Business business : synchronousBusinesses) {
            businessStartDate = DateUtil.getCurrentDate();
            //发起Business过程
            updateBusinessStatusCdByBId(business.getbId(), StatusConstant.STATUS_CD_BUSINESS);

            serviceBusiness = ServiceBusinessUtil.getServiceBusiness(business.getBusinessTypeCd());
            requestBusinessJson = OrderDataFlowContextFactory.getRequestBusinessJson(dataFlow, business);
            //调用下游中心
            JSONObject responseJson = doRequestBusinessSystem(dataFlow, serviceBusiness, requestBusinessJson);

            //发布事件
            DataFlowEventPublishing.invokeBusinessBSuccess(dataFlow, business, responseJson);

            responseBusinesses.add(responseJson);

            OrderDataFlowContextFactory.addCostTime(dataFlow, business.getBusinessTypeCd(), "调用" + business.getBusinessTypeCd() + "耗时", businessStartDate);
   /*         saveLogMessage(null,LogAgent.createLogMessage(dataFlow.getRequestCurrentHeaders(),requestBusinessJson.toJSONString()),
                    LogAgent.createLogMessage(dataFlow.getResponseCurrentHeaders(),responseJson.toJSONString()),
                    DateUtil.getCurrentDate().getTime()-businessStartDate.getTime());*/
        }
    }


    /**
     * 将BusinessTable 中的数据保存到 InstanceTable
     *
     * @param dataFlow
     * @param synchronousBusinesses
     */
    protected void doBusinessTableDataInfoToInstanceTable(IOrderDataFlowContext dataFlow, List<Business> synchronousBusinesses, List<Business> deleteBusinesses) {
        Date businessStartDate;
        ServiceBusiness serviceBusiness;
        JSONObject requestBusinessJson;
        for (Business business : synchronousBusinesses) {
            businessStartDate = DateUtil.getCurrentDate();
            serviceBusiness = ServiceBusinessUtil.getServiceBusiness(business.getBusinessTypeCd());
            //添加需要撤单的业务信息
            deleteBusinesses.add(business);

            requestBusinessJson = OrderDataFlowContextFactory.getBusinessTableDataInfoToInstanceTableJson(dataFlow, business);
            JSONObject responseJson = doRequestBusinessSystem(dataFlow, serviceBusiness, requestBusinessJson);
            //发布事件
            DataFlowEventPublishing.invokeBusinessISuccess(dataFlow, business);
            updateBusinessStatusCdByBId(business.getbId(), StatusConstant.STATUS_CD_COMPLETE);
            OrderDataFlowContextFactory.addCostTime(dataFlow, business.getBusinessTypeCd(), "调用" + business.getBusinessTypeCd() + "耗时", businessStartDate);
          /*  saveLogMessage(dataFlow,LogAgent.createLogMessage(dataFlow.getRequestCurrentHeaders(),requestBusinessJson.toJSONString()),
                    LogAgent.createLogMessage(dataFlow.getResponseCurrentHeaders(),responseJson.toJSONString()),
                    DateUtil.getCurrentDate().getTime() - businessStartDate.getTime());*/
        }

      /*  if(dataFlow.getCurrentBusiness() == null){
            return ;
        }*/

        //判断业务动作是否都竣工，主要考虑 请求报文中 有异步也有同步的情况
        //如果业务都完成，则将 订单改为完成状态
        centerServiceDaoImpl.completeOrderByOId(dataFlow.getOrders().getoId());
    }


    /**
     * 发起撤单业务
     *
     * @param dataFlow
     * @param deleteBusinesses
     */
    protected void doDeleteOrderAndInstanceData(IOrderDataFlowContext dataFlow, List<Business> deleteBusinesses) {

        if (deleteBusinesses == null || deleteBusinesses.size() == 0) {
            return;
        }

        //1.0 在c_business 表中加入 撤单记录
        centerServiceDaoImpl.saveBusiness(OrderDataFlowContextFactory.getDeleteOrderBusiness(dataFlow, "业务系统实例失败，发起撤单"));
        //2.0 作废 c_orders 和 c_business 数据
        updateOrderAndBusinessDelete(dataFlow,deleteBusinesses);
        //3.0 发起 撤单业务
        doDeleteBusinessSystemInstanceData(dataFlow, deleteBusinesses);
    }

    /**
     * 修改c_business状态
     *
     * @param bId
     * @param statusCd
     */
    protected void updateBusinessStatusCdByBId(String bId, String statusCd) {
        Map business = new HashMap();
        business.put("bId", bId);
        business.put("statusCd", statusCd);
        business.put("finishTime", DateUtil.getCurrentDate());
        centerServiceDaoImpl.updateBusinessByBId(business);
    }


    /**
     * 调用下游系统
     *
     * @param dataFlow
     * @param serviceBusiness
     * @param requestBusinessJson 请求报文
     * @return
     */
    protected JSONObject doRequestBusinessSystem(IOrderDataFlowContext dataFlow, ServiceBusiness serviceBusiness, JSONObject requestBusinessJson) {
        String responseMessage;

        Assert.notNull(serviceBusiness, "在表c_service_business中未配置当前业务类型");

        Assert.hasLength(serviceBusiness.getInvokeType(), "c_service_business表配置出错，invoke_type 不能为空" + serviceBusiness.getBusinessTypeCd());
        String httpUrl = "";
        if (ServiceBusinessConstant.INVOKE_TYPE_WEBSERVICE.equals(serviceBusiness.getInvokeType())) {//webservice方式
            String url = serviceBusiness.getUrl();
            String[] urls = url.split("#");

            if (urls.length != 2) {
                throw new ConfigDataException(ResponseConstant.RESULT_CODE_CONFIG_ERROR, "配置错误：c_service_business配置url字段错误" + serviceBusiness.getBusinessTypeCd());
            }
            httpUrl = MappingCache.getValue(urls[0]);
            String method = MappingCache.getValue(urls[1]);
            responseMessage = (String) WebServiceAxisClient.callWebService(httpUrl, method,
                    new Object[]{requestBusinessJson.toJSONString()},
                    serviceBusiness.getTimeout());
        } else if (ServiceBusinessConstant.INVOKE_TYPE_HTTP_POST.equals(serviceBusiness.getInvokeType())) {
            //http://user-service/test/sayHello
            httpUrl = MappingCache.getValue(serviceBusiness.getUrl());
            responseMessage = restTemplate.postForObject(httpUrl, requestBusinessJson.toJSONString(), String.class);
        } else if (ServiceBusinessConstant.INVOKE_TYPE_OUT_HTTP_POST.equals(serviceBusiness.getInvokeType())) {
            httpUrl = MappingCache.getValue(serviceBusiness.getUrl());
            responseMessage = outRestTemplate.postForObject(httpUrl, requestBusinessJson.toJSONString(), String.class);
        } else {//post方式
            throw new ConfigDataException(ResponseConstant.RESULT_CODE_CONFIG_ERROR, "配置错误：c_service_business配置url字段错误,当前无法识别" + serviceBusiness.getBusinessTypeCd());
        }


        logger.debug("调用地址：{}, 订单服务调用下游服务请求报文：{}，返回报文：{}", httpUrl, requestBusinessJson, responseMessage);

        if (StringUtil.isNullOrNone(responseMessage) || !Assert.isJsonObject(responseMessage)) {
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "下游系统返回格式不正确，请按协议规范处理");
        }
        JSONObject responseJson = JSONObject.parseObject(responseMessage);

        Assert.jsonObjectHaveKey(responseJson, "response", "下游返回报文格式错误，没有包含responseJson节点【" + serviceBusiness.getBusinessTypeCd() + "】");

        JSONObject responseInfo = responseJson.getJSONObject("response");

        Assert.jsonObjectHaveKey(responseInfo, "code", "下游返回报文格式错误，response 节点中没有包含code节点【" + serviceBusiness.getBusinessTypeCd() + "】");

        if (!ResponseConstant.RESULT_CODE_SUCCESS.equals(responseInfo.getString("code"))) {
            throw new BusinessException(ResponseConstant.RESULT_PARAM_ERROR, "业务系统处理失败，" + responseInfo.getString("message"));
        }
        return responseJson;
    }

    /**
     * 将订单状态改为作废状态。
     *
     * @param dataFlow
     */
    protected void updateOrderAndBusinessDelete(IOrderDataFlowContext dataFlow,List<Business> deleteBusinesses) {

        Date startDate = DateUtil.getCurrentDate();

        //作废 订单
        centerServiceDaoImpl.updateOrder(OrderDataFlowContextFactory.getNeedInvalidOrder(dataFlow));

        //作废订单项
        centerServiceDaoImpl.updateBusinessByBId(OrderDataFlowContextFactory.getNeedDeleteBusiness(dataFlow,deleteBusinesses));

        //加入撤单记录
        //doAddDeleteOrderBusinessData(dataFlow);


        OrderDataFlowContextFactory.addCostTime(dataFlow, "updateOrderAndBusinessError", "订单状态改为失败耗时", startDate);

    }

    /**
     * 业务系统撤单
     *
     * @param dataFlow
     * @param deleteBusinesses
     */
    protected void doDeleteBusinessSystemInstanceData(IOrderDataFlowContext dataFlow, List<Business> deleteBusinesses) {
        Date businessStartDate;
        JSONObject requestBusinessJson;
        ServiceBusiness serviceBusiness;
        for (Business business : deleteBusinesses) {
            businessStartDate = DateUtil.getCurrentDate();
            requestBusinessJson = OrderDataFlowContextFactory.getDeleteInstanceTableJson(dataFlow, business);
            serviceBusiness = ServiceBusinessUtil.getServiceBusiness(business.getBusinessTypeCd());
            JSONObject responseJson = doRequestBusinessSystem(dataFlow, serviceBusiness, requestBusinessJson);
            OrderDataFlowContextFactory.addCostTime(dataFlow, business.getBusinessTypeCd(), "调用" + business.getBusinessTypeCd() + "-撤单 耗时", businessStartDate);
//            saveLogMessage(dataFlow,LogAgent.createLogMessage(dataFlow.getRequestCurrentHeaders(),requestBusinessJson.toJSONString()),
//                    LogAgent.createLogMessage(dataFlow.getResponseCurrentHeaders(),responseJson.toJSONString()),
//                    DateUtil.getCurrentDate().getTime() - businessStartDate.getTime());
        }
    }


}
