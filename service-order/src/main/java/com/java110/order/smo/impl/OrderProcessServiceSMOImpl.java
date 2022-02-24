package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.context.IOrderDataFlowContext;
import com.java110.core.context.IOrderNotifyDataFlowContext;
import com.java110.core.context.OrderDataFlow;
import com.java110.core.event.center.DataFlowEventPublishing;
import com.java110.core.factory.OrderDataFlowContextFactory;
import com.java110.entity.order.Business;
import com.java110.entity.order.ServiceBusiness;
import com.java110.order.dao.ICenterServiceDAO;
import com.java110.order.smo.IOrderProcessServiceSMO;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.exception.InitConfigDataException;
import com.java110.utils.exception.NoAuthorityException;
import com.java110.utils.exception.NoSupportException;
import com.java110.utils.exception.OrdersException;
import com.java110.utils.exception.RuleException;
import com.java110.utils.exception.SMOException;
import com.java110.utils.kafka.KafkaFactory;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ServiceBusinessUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订单服务处理类
 * Created by wuxw on 2018/4/13.
 */
@Service("orderProcessServiceSMOImpl")
//@Transactional
public class OrderProcessServiceSMOImpl extends AbstractOrderServiceSMOImpl implements IOrderProcessServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(OrderProcessServiceSMOImpl.class);

//
//    @Autowired
//    ICenterServiceDAO centerServiceDaoImpl;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private RestTemplate restTemplateNoLoadBalanced;

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;


    /**
     * 业务统一处理服务方法
     *
     * @param reqJson 请求报文json
     * @return
     */
    public ResponseEntity<String> preService(String reqJson, Map<String, String> headers) throws SMOException {
        IOrderDataFlowContext dataFlow = null;

        JSONObject responseJson = null;

        ResponseEntity<String> responseEntity = null;

        try {
            DataFlowEventPublishing.preValidateData(reqJson, headers);
            //1.0 创建数据流
            dataFlow = OrderDataFlowContextFactory.newInstance(OrderDataFlow.class).builder(reqJson, headers);
            DataFlowEventPublishing.initDataFlowComplete(dataFlow);

            //2.0 调用规则校验
            ruleValidate(dataFlow);
            DataFlowEventPublishing.ruleValidateComplete(dataFlow);

            //3.0 保存订单和业务项 c_orders c_order_attrs c_business c_business_attrs
            saveOrdersAndBusiness(dataFlow);

            //6.0 调用下游系统
            DataFlowEventPublishing.invokeBusinessSystem(dataFlow);
            invokeBusinessSystem(dataFlow);

            //能够执行到这一步 认为是都成功了
            refreshOrderDataFlowResJson(dataFlow);

            responseEntity = new ResponseEntity<String>(dataFlow.getResJson().toJSONString(), OrderDataFlowContextFactory.hashMap2MultiValueMap(dataFlow.getResHeaders()), HttpStatus.OK);

        } catch (BusinessException e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), OrderDataFlowContextFactory.hashMap2MultiValueMap(dataFlow.getResHeaders()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (OrdersException e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), OrderDataFlowContextFactory.hashMap2MultiValueMap(dataFlow.getResHeaders()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuleException e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), OrderDataFlowContextFactory.hashMap2MultiValueMap(dataFlow.getResHeaders()), HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
        } catch (NoAuthorityException e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), OrderDataFlowContextFactory.hashMap2MultiValueMap(dataFlow.getResHeaders()), HttpStatus.UNAUTHORIZED);
        } catch (InitConfigDataException e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), OrderDataFlowContextFactory.hashMap2MultiValueMap(dataFlow.getResHeaders()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("内部异常了：", e);
            responseEntity = new ResponseEntity<String>("内部异常了：" + e.getMessage() + e.getLocalizedMessage(), OrderDataFlowContextFactory.hashMap2MultiValueMap(dataFlow.getResHeaders()), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            //这里保存耗时，以及日志
        }
        return responseEntity;
    }

    /**
     * 刷返回值
     *
     * @param dataFlow
     */
    @Override
    protected void refreshOrderDataFlowResJson(IOrderDataFlowContext dataFlow) {
        JSONObject resJson = new JSONObject();
        resJson.put("oId", dataFlow.getOrders().getoId());
        dataFlow.setResJson(resJson);
    }

    /**
     * 6.0 调用下游系统
     *
     * @param dataFlow
     * @throws BusinessException
     */
    protected void invokeBusinessSystem(IOrderDataFlowContext dataFlow) throws BusinessException {
        Date startDate = DateUtil.getCurrentDate();
        doSynchronousBusinesses(dataFlow);
        OrderDataFlowContextFactory.addCostTime(dataFlow, "invokeBusinessSystem", "调用下游系统耗时", startDate);
    }


    /**
     * 7.0 作废订单和业务项 插入撤单记录 等待撤单
     *
     * @param dataFlow
     */
    private void invalidOrderAndBusiness(IOrderNotifyDataFlowContext dataFlow) {
        Date startDate = DateUtil.getCurrentDate();
        if (MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER) != null
                && MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER).contains(dataFlow.getOrderTypeCd())) {
            //不用作废订单信息
            // OrderDataFlowContextFactory.addCostTime(dataFlow, "invalidOrderAndBusiness", "作废订单和业务项耗时", startDate);
            return;
        }

        //如果已经作废 不存在 或失败，则不做处理

        Map order = centerServiceDaoImpl.getOrderInfoByBId(dataFlow.getbId());

        if (order == null || !order.containsKey("status_cd") || StatusConstant.STATUS_CD_DELETE.equals(order.get("status_cd"))
                || StatusConstant.STATUS_CD_ERROR.equals(order.get("status_cd"))) {
            return;
        }

        //作废 订单
        centerServiceDaoImpl.updateOrder(OrderDataFlowContextFactory.getNeedInvalidOrder(dataFlow));

        //作废订单项
        centerServiceDaoImpl.updateBusiness(OrderDataFlowContextFactory.getNeedInvalidOrder(dataFlow));

        //将当前订单项改为 撤单状态
        centerServiceDaoImpl.updateBusinessByBId(OrderDataFlowContextFactory.getNeedDeleteBusiness(dataFlow));
        //插入撤单记录
        doAddDeleteOrderBusinessData(dataFlow);

        //OrderDataFlowContextFactory.addCostTime(dataFlow, "invalidOrderAndBusiness", "作废订单和业务项耗时", startDate);
    }


    /**
     * 8.0 广播作废已经完成业务系统订单信息
     *
     * @param dataFlow
     */
    private void invalidCompletedBusinessSystem(IOrderNotifyDataFlowContext dataFlow) throws Exception {

        if (!StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE.equals(dataFlow.getBusinessType())) {
            return;
        }

        //判断 订单instance 是否都变成了撤单状态
        if (centerServiceDaoImpl.judgeAllBusinessDeleteOrder(dataFlow.getoId(), StatusConstant.STATUS_CD_DELETE_ORDER) < 1) {
            return;
        }

        // 根据 c_business 表中的字段business_type_cd 找到对应的消息队列名称
        Map paramIn = new HashMap();
        paramIn.put("oId", dataFlow.getoId());
        paramIn.put("statusCd", StatusConstant.STATUS_CD_DELETE_ORDER);
        List<Map> completedBusinesses = centerServiceDaoImpl.getBusinessByOId(paramIn);
        for (Map completedBusiness : completedBusinesses) {
            ServiceBusiness serviceBusiness = ServiceBusinessUtil.getServiceBusiness(completedBusiness.get("business_type_cd").toString());
            long startTime = DateUtil.getCurrentDate().getTime();
            //发起撤单
            KafkaFactory.sendKafkaMessage(serviceBusiness.getMessageTopic(), "",
                    OrderDataFlowContextFactory.getDeleteInstanceTableJson(dataFlow, completedBusiness).toJSONString());
            //saveLogMessage(OrderDataFlowContextFactory.getDeleteInstanceTableJson(dataFlow,completedBusiness,appRoute.getAppService()),null);
        }
    }

    /**
     * 9.0 将订单状态改为失败，人工处理。
     *
     * @param dataFlow
     */
    private void updateOrderAndBusinessError(IOrderDataFlowContext dataFlow) {

        Date startDate = DateUtil.getCurrentDate();

        //作废 订单
        centerServiceDaoImpl.updateOrder(OrderDataFlowContextFactory.getNeedErrorOrder(dataFlow));

        //作废订单项
        centerServiceDaoImpl.updateBusiness(OrderDataFlowContextFactory.getNeedErrorOrder(dataFlow));


        OrderDataFlowContextFactory.addCostTime(dataFlow, "updateOrderAndBusinessError", "订单状态改为失败耗时", startDate);

    }


    /**
     * 加入撤单记录
     *
     * @param dataFlow
     */
    private void doAddDeleteOrderBusinessData(IOrderNotifyDataFlowContext dataFlow) {
       /* Map business = new HashMap();
        business.put("bId",SequenceUtil.getBId());
        business.put("oId",dataFlow.getoId());
        business.put("businessTypeCd",StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        business.put("remark","发起撤单");
        business.put("statusCd",StatusConstant.STATUS_CD_DELETE_ORDER);*/
        centerServiceDaoImpl.saveBusiness(OrderDataFlowContextFactory.getDeleteOrderBusiness(dataFlow, "订单失败，加入撤单"));
    }


    /**
     * 确认提交订单
     * <p>
     * 请求报文：
     * {
     * "orders":{
     * "transactionId":"576dbed0-117e-451e-b80e-88b2021e4002",
     * "oId":"",
     * "requestTime":"20200109114153",
     * "orderTypeCd":"D",
     * "orderProcess":"1005001"
     * }
     * }
     *
     * @param reqJson 接受报文
     * @throws SMOException
     */

    public ResponseEntity confirmService(String reqJson, Map<String, String> headers) throws SMOException {
        IOrderDataFlowContext dataFlow = null;
        ResponseEntity<String> responseEntity = null;
        try {
            DataFlowEventPublishing.preValidateData(reqJson, headers);
            //1.0 创建数据流
            dataFlow = OrderDataFlowContextFactory.newInstance(OrderDataFlow.class).builder(reqJson, headers);

            notifyInstanceOrder(dataFlow, headers);
            // 业务调用完成
            DataFlowEventPublishing.invokeConfirmFinishBusinessSystem(dataFlow);
        } catch (Exception e) {
            LoggerEngine.error("确认提交订单失败", e);
            //10.0 成功的情况下通知下游系统失败将状态改为NE，人工处理。
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } finally {
            responseEntity = new ResponseEntity<>("成功", HttpStatus.OK);
        }
        return responseEntity;
    }

    /**
     * 通知订单 提交到实例中
     *
     * @param dataflow
     * @param headers
     */
    private void notifyInstanceOrder(IOrderDataFlowContext dataflow, Map<String, String> headers) {

        //判断 订单状态是否为B
        Map orderInfo = new HashMap();
        orderInfo.put("oId", dataflow.getOrders().getoId());
        orderInfo.put("statusCd", StatusConstant.STATUS_CD_BUSINESS);
        List<Map> businesses = centerServiceDaoImpl.getBusinessByOId(orderInfo);

        if (businesses == null || businesses.size() < 1) {
            throw new NoSupportException(1999, "未找到有效订单" + dataflow.getOrders().getoId());
        }

        doSendInstanceOrder(dataflow, businesses);

    }

    private void doSendInstanceOrder(IOrderDataFlowContext dataFlow, List<Map> businesses) {
        Date startDate = DateUtil.getCurrentDate();

        List<Business> deleteBusinesses = new ArrayList<Business>();


        JSONArray responseBusinesses = new JSONArray();
        List<Business> synchronousBusinesses = new ArrayList<>();

        Business business = null;
        for (Map busi : businesses) {
            business = new Business();
            business.setbId(busi.get("b_id") + "");
            business.setBusinessTypeCd(busi.get("business_type_cd") + "");
            synchronousBusinesses.add(business);
        }

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
        OrderDataFlowContextFactory.addCostTime(dataFlow, "doSynchronousBusinesses", "同步调用业务系统总耗时", startDate);
    }

    /**
     * 9.0 成功的情况下通知下游系统失败将状态改为NE，人工处理。
     *
     * @param dataFlow
     */
    private void updateBusinessNotifyError(IOrderNotifyDataFlowContext dataFlow) {

        Date startDate = DateUtil.getCurrentDate();
        //完成订单项
        centerServiceDaoImpl.updateBusinessByBId(OrderDataFlowContextFactory.getNeedNotifyErrorBusiness(dataFlow));

        // OrderDataFlowContextFactory.addCostTime(dataFlow, "updateBusinessNotifyError", "订单状态改为失败耗时", startDate);

    }


    /**
     * 处理同步业务
     *
     * @param dataFlow
     */
    @Override
    protected void doSynchronousBusinesses(IOrderDataFlowContext dataFlow) throws BusinessException {
        Date startDate = DateUtil.getCurrentDate();
        List<Business> synchronousBusinesses = OrderDataFlowContextFactory.getSynchronousBusinesses(dataFlow);

        if (synchronousBusinesses == null || synchronousBusinesses.size() == 0) {
            return;
        }
        JSONArray responseBusinesses = new JSONArray();

        //6.1处理同步服务 发起Business
        doSaveDataInfoToBusinessTable(dataFlow, synchronousBusinesses, responseBusinesses);

        OrderDataFlowContextFactory.addCostTime(dataFlow, "doSynchronousBusinesses", "同步调用业务系统总耗时", startDate);
    }


    public ICenterServiceDAO getCenterServiceDaoImpl() {
        return centerServiceDaoImpl;
    }

    public void setCenterServiceDaoImpl(ICenterServiceDAO centerServiceDaoImpl) {
        this.centerServiceDaoImpl = centerServiceDaoImpl;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public IQueryServiceSMO getQueryServiceSMOImpl() {
        return queryServiceSMOImpl;
    }

    public void setQueryServiceSMOImpl(IQueryServiceSMO queryServiceSMOImpl) {
        this.queryServiceSMOImpl = queryServiceSMOImpl;
    }


}
