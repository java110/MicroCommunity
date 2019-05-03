package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.cache.MappingCache;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.KafkaConstant;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceBusinessConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.BusinessException;
import com.java110.common.exception.BusinessStatusException;
import com.java110.common.exception.ConfigDataException;
import com.java110.common.exception.DAOException;
import com.java110.common.exception.DecryptException;
import com.java110.common.exception.InitConfigDataException;
import com.java110.common.exception.NoAuthorityException;
import com.java110.common.exception.NoSupportException;
import com.java110.common.exception.OrdersException;
import com.java110.common.exception.RuleException;
import com.java110.common.exception.SMOException;
import com.java110.common.factory.ApplicationContextFactory;
import com.java110.common.kafka.KafkaFactory;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.DateUtil;
import com.java110.common.util.ServiceBusinessUtil;
import com.java110.common.util.StringUtil;
import com.java110.common.util.WebServiceAxisClient;
import com.java110.core.client.RestTemplate;
import com.java110.core.context.DataFlow;
import com.java110.core.context.IOrderDataFlowContext;
import com.java110.core.context.IOrderNotifyDataFlowContext;
import com.java110.core.context.IOrderResponse;
import com.java110.core.context.OrderDataFlow;
import com.java110.core.context.OrderNotifyDataFlow;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.OrderDataFlowContextFactory;
import com.java110.entity.center.AppService;
import com.java110.entity.center.DataFlowLinksCost;
import com.java110.entity.order.Business;
import com.java110.entity.order.ServiceBusiness;
import com.java110.event.center.DataFlowEventPublishing;
import com.java110.log.agent.LogAgent;
import com.java110.order.dao.ICenterServiceDAO;
import com.java110.order.smo.IOrderServiceSMO;
import com.java110.service.init.ServiceInfoListener;
import com.java110.service.smo.IQueryServiceSMO;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
@Service("orderServiceSMOImpl")
//@Transactional
public class OrderServiceSMOImpl implements IOrderServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceSMOImpl.class);


    @Autowired
    ICenterServiceDAO centerServiceDaoImpl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate restTemplateNoLoadBalanced;

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;


    /**
     * 业务统一处理服务方法
     *
     * @param reqJson 请求报文json
     * @return
     */
    public ResponseEntity<String> service(String reqJson, Map<String, String> headers) throws SMOException {
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

            if (responseEntity == null) {
                responseEntity = new ResponseEntity<String>(dataFlow.getResJson().getJSONArray("msg").toJSONString(), OrderDataFlowContextFactory.hashMap2MultiValueMap(dataFlow.getResHeaders()), HttpStatus.OK);
            }
            if (dataFlow != null) {
                //添加耗时
                //OrderDataFlowContextFactory.addCostTime(dataFlow, "service", "业务处理总耗时", dataFlow.getStartDate(), dataFlow.getEndDate());
                //保存耗时
                //saveCostTimeLogMessage(dataFlow);
//                saveLogMessage(dataFlow,LogAgent.createLogMessage(dataFlow.getRequestHeaders(),dataFlow.getReqJson().toJSONString()),
//                        LogAgent.createLogMessage(dataFlow.getResponseHeaders(),responseEntity.getBody()),endDate.getTime()-dataFlow.getStartDate().getTime());
//                DataFlowEventPublishing.dataResponse(dataFlow,reqJson,headers);
            }


            //这里保存耗时，以及日志
            return responseEntity;

        }
    }

    /**
     * 刷返回值
     *
     * @param dataFlow
     */
    private void refreshOrderDataFlowResJson(IOrderDataFlowContext dataFlow) {

//        if(dataFlow.getResJson() == null || dataFlow.getResJson().isEmpty()){
//            JSONObject resJson = new JSONObject();
//            resJson.put("msg","成功");
//            dataFlow.setResJson(resJson);
//        }

    }

    /**
     * 抒写返回头信息
     *
     * @param dataFlow
     */
    private void putResponseHeader(DataFlow dataFlow, Map<String, String> headers) {
        headers.put("responseTime", DateUtil.getDefaultFormateTimeString(new Date()));
        headers.put("transactionId", dataFlow.getTransactionId());
    }

    /**
     * 解密
     *
     * @param reqJson
     * @return
     */
    private String decrypt(String reqJson, Map<String, String> headers) throws DecryptException {
        try {
            if (MappingConstant.VALUE_ON.equals(headers.get(CommonConstant.ENCRYPT))) {
                logger.debug("解密前字符：" + reqJson);
                reqJson = new String(AuthenticationFactory.decrypt(reqJson.getBytes("UTF-8"), AuthenticationFactory.loadPrivateKey(MappingConstant.KEY_PRIVATE_STRING)
                        , NumberUtils.isNumber(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) ? Integer.parseInt(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) :
                                Integer.parseInt(MappingCache.getValue(MappingConstant.KEY_DEFAULT_DECRYPT_KEY_SIZE))), "UTF-8");
                logger.debug("解密后字符：" + reqJson);
            }
        } catch (Exception e) {
            throw new DecryptException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "解密失败");
        }

        return reqJson;
    }

    /**
     * 加密
     *
     * @param resJson
     * @param headers
     * @return
     */
    private String encrypt(String resJson, Map<String, String> headers) {
        try {
            if (MappingConstant.VALUE_ON.equals(headers.get(CommonConstant.ENCRYPT))) {
                logger.debug("加密前字符：" + resJson);
                resJson = new String(AuthenticationFactory.encrypt(resJson.getBytes("UTF-8"), AuthenticationFactory.loadPubKey(MappingConstant.KEY_PUBLIC_STRING)
                        , NumberUtils.isNumber(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) ? Integer.parseInt(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) :
                                Integer.parseInt(MappingCache.getValue(MappingConstant.KEY_DEFAULT_DECRYPT_KEY_SIZE))), "UTF-8");
                logger.debug("加密后字符：" + resJson);
            }
        } catch (Exception e) {
            logger.error("加密失败：", e);
        }
        return resJson;
    }


    /**
     * 2.0初始化配置信息
     *
     * @param dataFlow
     */
    private void initConfigData(IOrderDataFlowContext dataFlow) {

    }

    /**
     * 4.0规则校验
     *
     * @param dataFlow
     * @throws RuleException
     */
    private void ruleValidate(IOrderDataFlowContext dataFlow) throws RuleException {
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
    private void saveOrdersAndBusiness(IOrderDataFlowContext dataFlow) throws OrdersException {
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
     * 6.0 调用下游系统
     *
     * @param dataFlow
     * @throws BusinessException
     */
    private void invokeBusinessSystem(IOrderDataFlowContext dataFlow) throws BusinessException {
        Date startDate = DateUtil.getCurrentDate();
        //6.1 先处理同步方式的服务，每一同步后发布事件广播

        doSynchronousBusinesses(dataFlow);

        //6.2 处理异步服务
        doAsynchronousBusinesses(dataFlow);


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
     * 9.0 将订单状态改为失败，人工处理。
     *
     * @param dataFlow
     */
    private void updateOrderAndBusinessError(IOrderNotifyDataFlowContext dataFlow) {

        Date startDate = DateUtil.getCurrentDate();

        //作废 订单
        centerServiceDaoImpl.updateOrder(OrderDataFlowContextFactory.getNeedErrorOrder(dataFlow));

        //作废订单项
        centerServiceDaoImpl.updateBusiness(OrderDataFlowContextFactory.getNeedErrorOrder(dataFlow));


        //OrderDataFlowContextFactory.addCostTime(dataFlow, "updateOrderAndBusinessError", "订单状态改为失败耗时", startDate);

    }

    /**
     * 将订单状态改为作废状态。
     *
     * @param dataFlow
     */
    private void updateOrderAndBusinessDelete(IOrderDataFlowContext dataFlow) {

        Date startDate = DateUtil.getCurrentDate();

        //作废 订单
        centerServiceDaoImpl.updateOrder(OrderDataFlowContextFactory.getNeedInvalidOrder(dataFlow));

        //作废订单项
        centerServiceDaoImpl.updateBusiness(OrderDataFlowContextFactory.getNeedDeleteBusiness(dataFlow));

        //加入撤单记录
        //doAddDeleteOrderBusinessData(dataFlow);


        OrderDataFlowContextFactory.addCostTime(dataFlow, "updateOrderAndBusinessError", "订单状态改为失败耗时", startDate);

    }

    /**
     * 加入撤单记录
     *
     * @param dataFlow
     */
    private void doAddDeleteOrderBusinessData(IOrderDataFlowContext dataFlow) {
       /* Map business = new HashMap();
        business.put("bId",SequenceUtil.getBId());
        business.put("oId",dataFlow.getoId());
        business.put("businessTypeCd",StatusConstant.REQUEST_BUSINESS_TYPE_DELETE);
        business.put("remark","发起撤单");
        business.put("statusCd",StatusConstant.STATUS_CD_DELETE_ORDER);*/
        centerServiceDaoImpl.saveBusiness(OrderDataFlowContextFactory.getDeleteOrderBusiness(dataFlow, "订单失败，加入撤单"));
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
     * 接受业务系统通知消息
     *
     * @param receiveJson 接受报文
     * @throws SMOException
     */
    @Override
    public void receiveBusinessSystemNotifyMessage(String receiveJson) throws SMOException {
        Date startDate = DateUtil.getCurrentDate();
        IOrderNotifyDataFlowContext dataFlow = null;
        try {
            //1.0 创建数据流
            dataFlow = OrderDataFlowContextFactory.newInstance(OrderNotifyDataFlow.class).builder(receiveJson, null);
            //如果订单都没有保存，则再不要处理
            if (MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER) != null
                    && MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER).contains(dataFlow.getOrderTypeCd())) {
                //不保存订单信息
                return;
            }

            //如果不是 business 和instance 过程 则直接跳出
            judgeBusinessOrInstance(dataFlow);

            //2.0加载数据，没有找到appId 及配置信息 则抛出InitConfigDataException
            reloadOrderInfo(dataFlow);

            //3.0 判断是否成功,失败会抛出BusinessStatusException异常
            judgeBusinessStatusAndCompleteBusiness(dataFlow);

            //4.0 修改业务为成功,如果发现业务项已经是作废或失败状态（D或E）则抛出BusinessException异常
            //completeBusiness(dataFlow);

            //5.0 判断 发起 Instance 条件是否满足，如果满足 发起 Instance过程
            judgeSendToInstance(dataFlow);

            //7.0 判断撤单条件是否满足，如果满足发起撤单
            invalidCompletedBusinessSystem(dataFlow);

        } catch (BusinessStatusException e) {

            logger.error("订单失败:", e);
            //8.0 将订单状态改为失败，人工处理。
            updateOrderAndBusinessError(dataFlow);

        } catch (BusinessException e) {
            //9.0说明这个订单已经失败了，再不需要
            //想法，这里广播当前失败业务
            logger.error("修改业务数据失败", e);
        }/*catch (InitConfigDataException e){ //这种一般不会出现，除非人工改了数据
            LoggerEngine.error("加载配置数据出错", e);
            try {
                //6.0 作废订单和所有业务项
                invalidOrderAndBusiness(dataFlow);
                //7.0 广播作废业务系统订单信息,这里只有 Instance 失败后才发起 撤单
                if(StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE.equals(dataFlow.getBusinessType())) {
                    invalidCompletedBusinessSystem(dataFlow);
                }
            } catch (Exception e1) {
                LoggerEngine.error("作废订单失败", e1);
                //8.0 将订单状态改为失败，人工处理。
                updateOrderAndBusinessError(dataFlow);
            }

        }*/ catch (NoSupportException e) {
            LoggerEngine.error("当前业务不支持", e);
        } catch (Exception e) {
            LoggerEngine.error("作废订单失败", e);
            //10.0 成功的情况下通知下游系统失败将状态改为NE，人工处理。
            updateBusinessNotifyError(dataFlow);
        } finally {
            /*OrderDataFlowContextFactory.addCostTime(dataFlow, "receiveBusinessSystemNotifyMessage", "接受业务系统通知消息耗时", startDate);
            saveLogMessage(dataFlow,LogAgent.createLogMessage(dataFlow.getRequestCurrentHeaders(),dataFlow.getReqJson().toJSONString()),
                    LogAgent.createLogMessage(dataFlow.getResponseCurrentHeaders(),ResponseConstant.RESULT_CODE_SUCCESS),
                    DateUtil.getCurrentDate().getTime() - dataFlow.getStartDate().getTime());*/
        }
    }

    /**
     * Instance过程
     *
     * @param dataFlow
     */
    private void doSendInstance(IOrderNotifyDataFlowContext dataFlow) {
        if (dataFlow == null || !StatusConstant.REQUEST_BUSINESS_TYPE_BUSINESS.equals(dataFlow.getBusinessType())) {
            return;
        }
        try {
            ServiceBusiness serviceBusiness = ServiceBusinessUtil.getServiceBusiness(dataFlow.getBusinessTypeCd());
            KafkaFactory.sendKafkaMessage(serviceBusiness.getMessageTopic(), "",
                    OrderDataFlowContextFactory.getBusinessTableDataInfoToInstanceTableJson(dataFlow).toJSONString());
        } catch (Exception e) {

        }

    }

    /**
     * 判断是否是 business 或者 instance过程
     *
     * @param dataFlow
     * @throws NoSupportException
     */
    private void judgeBusinessOrInstance(IOrderNotifyDataFlowContext dataFlow) throws NoSupportException {

        if (dataFlow == null || StatusConstant.REQUEST_BUSINESS_TYPE_BUSINESS.equals(dataFlow.getBusinessType()) ||
                StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE.equals(dataFlow.getBusinessType())) {
            return;
        }

        throw new NoSupportException(ResponseConstant.RESULT_PARAM_ERROR, "当前只支持 Business 和 Instance过程");
    }


    /**
     * 2.0重新加载订单信息到dataFlow 中
     *
     * @param dataFlow
     */
    private void reloadOrderInfoAndConfigData(DataFlow dataFlow) {

        Map order = centerServiceDaoImpl.getOrderInfoByBId(dataFlow.getBusinesses().get(0).getbId());
        dataFlow.setoId(order.get("o_id").toString());
        dataFlow.setAppId(order.get("app_id").toString());
        if ("-1".equals(dataFlow.getDataFlowId()) || StringUtil.isNullOrNone(dataFlow.getDataFlowId())) {
            throw new InitConfigDataException(ResponseConstant.RESULT_CODE_ERROR, "请求报文中没有包含 dataFlowId 节点");
        }
        //重新刷端口信息
        ServiceInfoListener serviceInfoListener = ApplicationContextFactory.getBean("serviceInfoListener", ServiceInfoListener.class);
        if (serviceInfoListener != null) {
            dataFlow.setPort(serviceInfoListener.getServerPort() + "");
        }
        //重新加载配置
        //initConfigData(dataFlow);
    }


    /**
     * 2.0重新加载订单信息到dataFlow 中
     *
     * @param dataFlow
     */
    private void reloadOrderInfo(IOrderNotifyDataFlowContext dataFlow) {

        Map order = centerServiceDaoImpl.getOrderInfoByBId(dataFlow.getbId());
        dataFlow.setoId(order.get("o_id").toString());
        if ("-1".equals(dataFlow.getDataFlowId()) || StringUtil.isNullOrNone(dataFlow.getDataFlowId())) {
            throw new InitConfigDataException(ResponseConstant.RESULT_CODE_ERROR, "请求报文中没有包含 dataFlowId 节点");
        }
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
     * 判断是否都成功了
     *
     * @param dataFlow
     */
    private void judgeBusinessStatusAndCompleteBusiness(IOrderNotifyDataFlowContext dataFlow) throws BusinessStatusException {

        //List<Business> businesses = dataFlow.getBusinesses();

        //1.0 判断是否存在撤单，如果是撤单则将当前 bId 标记为撤单状态
        if (StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE.equals(dataFlow.getBusinessType())) {
            Map businessMap = centerServiceDaoImpl.getDeleteOrderBusinessByOId(dataFlow.getoId());
            if (businessMap != null && !businessMap.isEmpty()) {
                centerServiceDaoImpl.updateBusinessByBId(OrderDataFlowContextFactory.getNeedDeleteBusiness(dataFlow));
                return;
            }
        }

        //Business business = dataFlow.getCurrentBusiness();
        if (!ResponseConstant.RESULT_CODE_SUCCESS.equals(((IOrderResponse) dataFlow).getCode())) {
            //throw new BusinessStatusException(business.getCode(),"业务bId= "+business.getbId() + " 处理失败，需要作废订单");
            //作废订单和业务项 插入撤单记录 等待撤单
            invalidOrderAndBusiness(dataFlow);
        } else {
            completeBusiness(dataFlow);
        }

    }

    /**
     * 3.0 修改业务为成功,如果发现业务项已经是作废或失败状态（D或E）则抛出BusinessException异常
     *
     * @param dataFlow
     */
    private void completeBusiness(IOrderNotifyDataFlowContext dataFlow) throws BusinessException {
        try {
            if (StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE.equals(dataFlow.getBusinessType())) {
                //完成订单项
                centerServiceDaoImpl.updateBusinessByBId(OrderDataFlowContextFactory.getNeedCompleteBusiness(dataFlow));

                //如果业务都完成，则将 订单改为完成状态
                centerServiceDaoImpl.completeOrderByBId(dataFlow.getbId());
            } else if (StatusConstant.REQUEST_BUSINESS_TYPE_BUSINESS.equals(dataFlow.getBusinessType())) {
                centerServiceDaoImpl.updateBusinessByBId(OrderDataFlowContextFactory.getNeedBusinessComplete(dataFlow));
            } else { //这里到不了，前面做了校验
                throw new BusinessException(ResponseConstant.RESULT_PARAM_ERROR, "当前不支持 业务类型为 businessType" + dataFlow.getBusinessType());
            }

        } catch (DAOException e) {
            throw new BusinessException(e.getResult(), e);
        }
    }

    /**
     * //4.0当所有业务动作是否都是C，将订单信息改为 C 并且发布竣工消息，这里在广播之前确认
     *
     * @param dataFlow
     */
    private void judgeSendToInstance(IOrderNotifyDataFlowContext dataFlow) throws Exception {
        try {
            if (centerServiceDaoImpl.judgeAllBusinessCompleted(dataFlow.getoId(), StatusConstant.STATUS_CD_BUSINESS_COMPLETE) > 0) {
                //通知成功消息
                doSendInstance(dataFlow);
            }
        } catch (DAOException e) {
            //这里什么都不做，说明订单没有完成
        }


    }

    /**
     * 通知 订单已经完成，后端需要完成数据
     *
     * @param dataFlow
     */
    private void notifyBusinessSystemSuccessMessage(IOrderNotifyDataFlowContext dataFlow) throws Exception {

        long startTime = DateUtil.getCurrentDate().getTime();

        ServiceBusiness serviceBusiness = ServiceBusinessUtil.getServiceBusiness(dataFlow.getBusinessTypeCd());
        //拼装报文通知业务系统
        KafkaFactory.sendKafkaMessage(serviceBusiness.getMessageTopic(), "",
                OrderDataFlowContextFactory.getNotifyBusinessSuccessJson(dataFlow).toJSONString());

        /*saveLogMessage(dataFlow,LogAgent.createLogMessage(dataFlow.getRequestCurrentHeaders(),OrderDataFlowContextFactory.getNotifyBusinessSuccessJson(dataFlow).toJSONString()),
                LogAgent.createLogMessage(dataFlow.getResponseCurrentHeaders(),ResponseConstant.RESULT_CODE_SUCCESS),
                DateUtil.getCurrentDate().getTime() - startTime);*/
    }

    /**
     * 8.0 广播作废业务系统订单信息
     *
     * @param dataFlow
     */
    private void notifyBusinessSystemErrorMessage(IOrderNotifyDataFlowContext dataFlow) throws Exception {
        long startTime = DateUtil.getCurrentDate().getTime();

        ServiceBusiness serviceBusiness = ServiceBusinessUtil.getServiceBusiness(dataFlow.getBusinessTypeCd());

        //拼装报文通知业务系统
        KafkaFactory.sendKafkaMessage(serviceBusiness.getMessageTopic(), "",
                OrderDataFlowContextFactory.getNotifyBusinessErrorJson(dataFlow).toJSONString());
        /*saveLogMessage(dataFlow,LogAgent.createLogMessage(dataFlow.getRequestCurrentHeaders(),OrderDataFlowContextFactory.getNotifyBusinessErrorJson(dataFlow).toJSONString()),
                LogAgent.createLogMessage(dataFlow.getResponseCurrentHeaders(),ResponseConstant.RESULT_CODE_ERROR),
                DateUtil.getCurrentDate().getTime() - startTime);*/
    }

    /**
     * 处理同步业务
     *
     * @param dataFlow
     */
    private void doSynchronousBusinesses(IOrderDataFlowContext dataFlow) throws BusinessException {
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
     * 发起撤单业务
     *
     * @param dataFlow
     * @param deleteBusinesses
     */
    private void doDeleteOrderAndInstanceData(IOrderDataFlowContext dataFlow, List<Business> deleteBusinesses) {

        if (deleteBusinesses == null || deleteBusinesses.size() == 0) {
            return;
        }

        //1.0 在c_business 表中加入 撤单记录
        centerServiceDaoImpl.saveBusiness(OrderDataFlowContextFactory.getDeleteOrderBusiness(dataFlow, "业务系统实例失败，发起撤单"));
        //2.0 作废 c_orders 和 c_business 数据
        updateOrderAndBusinessDelete(dataFlow);
        //3.0 发起 撤单业务
        doDeleteBusinessSystemInstanceData(dataFlow, deleteBusinesses);
    }

    /**
     * 完成订单状态
     * @param synchronousBusinesses
     */
    /*private void doComplateOrderAndBusiness(DataFlow dataFlow,List<Business> synchronousBusinesses) {

        //Complete Order and business
        Map order = new HashMap();
        order.put("oId",dataFlow.getoId());
        order.put("statusCd", StatusConstant.STATUS_CD_COMPLETE);
        order.put("finishTime",DateUtil.getCurrentDate());
        centerServiceDaoImpl.updateOrder(order);
        centerServiceDaoImpl.updateBusiness(order);
        Date businessStartDate;
        AppService service;
        JSONObject requestBusinessJson;
        for(Business business : synchronousBusinesses){
            businessStartDate = DateUtil.getCurrentDate();
            service = OrderDataFlowContextFactory.getService(dataFlow,business.getServiceCode());
            if(!CommonConstant.INSTANCE_Y.equals(service.getIsInstance())){
                continue;
            }
            requestBusinessJson = OrderDataFlowContextFactory.getCompleteInstanceDataJson(dataFlow,business);
            JSONObject responseJson = doRequestBusinessSystem(dataFlow, service, requestBusinessJson);

            OrderDataFlowContextFactory.addCostTime(dataFlow, business.getServiceCode(), "调用"+business.getServiceName()+"-doComplete耗时", businessStartDate);
            saveLogMessage(dataFlow,LogAgent.createLogMessage(dataFlow.getRequestCurrentHeaders(),requestBusinessJson.toJSONString()),
                    LogAgent.createLogMessage(dataFlow.getResponseCurrentHeaders(),responseJson.toJSONString()),
                    DateUtil.getCurrentDate().getTime() - businessStartDate.getTime());
        }

    }*/

    /**
     * 将BusinessTable 中的数据保存到 InstanceTable
     *
     * @param dataFlow
     * @param synchronousBusinesses
     */
    private void doBusinessTableDataInfoToInstanceTable(IOrderDataFlowContext dataFlow, List<Business> synchronousBusinesses, List<Business> deleteBusinesses) {
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
     * 业务系统撤单
     *
     * @param dataFlow
     * @param deleteBusinesses
     */
    private void doDeleteBusinessSystemInstanceData(IOrderDataFlowContext dataFlow, List<Business> deleteBusinesses) {
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

    /**
     * 调用下游系统
     *
     * @param dataFlow
     * @param serviceBusiness
     * @param requestBusinessJson 请求报文
     * @return
     */
    private JSONObject doRequestBusinessSystem(IOrderDataFlowContext dataFlow, ServiceBusiness serviceBusiness, JSONObject requestBusinessJson) {
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
            responseMessage = restTemplateNoLoadBalanced.postForObject(httpUrl, requestBusinessJson.toJSONString(), String.class);
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

    private String doTransferRequestBusinessSystem(DataFlow dataFlow, AppService service, String reqData) {
        String responseMessage;
        if (service.getMethod() == null || "".equals(service.getMethod())) {//post方式
            //http://user-service/test/sayHello
            HttpHeaders header = new HttpHeaders();
            for (String key : dataFlow.getRequestCurrentHeaders().keySet()) {
                header.add(key, dataFlow.getRequestCurrentHeaders().get(key));
            }
            HttpEntity<String> httpEntity = new HttpEntity<String>(reqData, header);
            responseMessage = restTemplateNoLoadBalanced.postForObject(service.getUrl(), httpEntity, String.class);
        } else {//webservice方式
            responseMessage = (String) WebServiceAxisClient.callWebService(service.getUrl(), service.getMethod(),
                    new Object[]{dataFlow.getRequestBusinessJson().toJSONString()},
                    service.getTimeOut());
        }
        return responseMessage;
    }

    /**
     * 数据保存到BusinessTable 中
     *
     * @param dataFlow
     * @param synchronousBusinesses
     * @param responseBusinesses
     */
    private void doSaveDataInfoToBusinessTable(IOrderDataFlowContext dataFlow, List<Business> synchronousBusinesses, JSONArray responseBusinesses) {
        Date businessStartDate;
        ServiceBusiness serviceBusiness;
        JSONObject requestBusinessJson;
        for (Business business : synchronousBusinesses) {
            businessStartDate = DateUtil.getCurrentDate();
            //发起Business过程
            updateBusinessStatusCdByBId(business.getbId(), StatusConstant.STATUS_CD_BUSINESS);

            serviceBusiness = ServiceBusinessUtil.getServiceBusiness(business.getBusinessTypeCd());
            requestBusinessJson = OrderDataFlowContextFactory.getRequestBusinessJson(dataFlow, business);

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
     * 处理异步业务
     *
     * @param
     */
    private void doAsynchronousBusinesses(IOrderDataFlowContext dataFlow) throws BusinessException {
        Date startDate = DateUtil.getCurrentDate();
        //6.3 处理异步，按消息队里处理
        List<Business> asynchronousBusinesses = OrderDataFlowContextFactory.getAsynchronousBusinesses(dataFlow);

        if (asynchronousBusinesses == null || asynchronousBusinesses.size() == 0) {
            return;
        }
        ServiceBusiness serviceBusiness;
        try {
            for (Business business : asynchronousBusinesses) {
                serviceBusiness = ServiceBusinessUtil.getServiceBusiness(business.getBusinessTypeCd());
                KafkaFactory.sendKafkaMessage(serviceBusiness.getMessageTopic(), "",
                        OrderDataFlowContextFactory.getRequestBusinessJson(dataFlow, business).toJSONString());
            }
        } catch (Exception e) {
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, e.getMessage());
        }

        OrderDataFlowContextFactory.addCostTime(dataFlow, "doSynchronousBusinesses", "异步调用业务系统总耗时", startDate);
//        saveLogMessage(dataFlow,LogAgent.createLogMessage(dataFlow.getRequestCurrentHeaders(),dataFlow.getRequestBusinessJson().toJSONString()),
//                LogAgent.createLogMessage(dataFlow.getRequestCurrentHeaders(),dataFlow.getResponseBusinessJson().toJSONString()),
//                DateUtil.getCurrentDate().getTime()-startDate.getTime());
    }


    /**
     * 保存日志信息
     *
     * @param dataFlow     数据流对象 封装用户请求的信息
     * @param requestJson  请求报文 格式为
     *                     {"headers":"",
     *                     "body":""
     *                     }
     * @param responseJson 请求报文 格式为
     *                     {"headers":"",
     *                     "body":""
     *                     }
     */
    private void saveLogMessage(DataFlow dataFlow, JSONObject requestJson, JSONObject responseJson, long costTime) {
        LogAgent.sendLog(dataFlow, requestJson, responseJson, costTime);
    }

    /**
     * 保存日志信息
     *
     * @param requestJson
     */
    private void saveLogMessage(String requestJson, String responseJson) {

        try {
            if (MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_LOG_ON_OFF))) {
                JSONObject log = new JSONObject();
                log.put("request", requestJson);
                log.put("response", responseJson);
                KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_LOG_NAME, "", log.toJSONString());
            }
        } catch (Exception e) {
            logger.error("报错日志出错了，", e);
        }
    }

    /**
     * 保存耗时信息
     *
     * @param dataFlow
     */
    private void saveCostTimeLogMessage(DataFlow dataFlow) {
        try {
            if (MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_COST_TIME_ON_OFF))) {
                List<DataFlowLinksCost> dataFlowLinksCosts = dataFlow.getLinksCostDates();
                JSONObject costDate = new JSONObject();
                JSONArray costDates = new JSONArray();
                JSONObject newObj = null;
                for (DataFlowLinksCost dataFlowLinksCost : dataFlowLinksCosts) {
                    newObj = JSONObject.parseObject(JSONObject.toJSONString(dataFlowLinksCost));
                    newObj.put("dataFlowId", dataFlow.getDataFlowId());
                    newObj.put("transactionId", dataFlow.getTransactionId());
                    costDates.add(newObj);
                }
                costDate.put("costDates", costDates);

                KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_COST_TIME_LOG_NAME, "", costDate.toJSONString());
            }
        } catch (Exception e) {
            logger.error("报错日志出错了，", e);
        }
    }

    /**
     * 修改c_business状态
     *
     * @param bId
     * @param statusCd
     */
    private void updateBusinessStatusCdByBId(String bId, String statusCd) {
        Map business = new HashMap();
        business.put("bId", bId);
        business.put("statusCd", statusCd);
        business.put("finishTime", DateUtil.getCurrentDate());
        centerServiceDaoImpl.updateBusinessByBId(business);
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

    public RestTemplate getRestTemplateNoLoadBalanced() {
        return restTemplateNoLoadBalanced;
    }

    public void setRestTemplateNoLoadBalanced(RestTemplate restTemplateNoLoadBalanced) {
        this.restTemplateNoLoadBalanced = restTemplateNoLoadBalanced;
    }
}
