package com.java110.center.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.center.dao.ICenterServiceDAO;
import com.java110.center.smo.ICenterServiceSMO;
import com.java110.common.cache.AppRouteCache;
import com.java110.common.cache.MappingCache;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.KafkaConstant;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.*;
import com.java110.common.factory.AuthenticationFactory;
import com.java110.common.factory.DataFlowFactory;
import com.java110.common.factory.DataTransactionFactory;
import com.java110.common.kafka.KafkaFactory;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.*;
import com.java110.entity.center.*;
import com.java110.event.center.DataFlowEventPublishing;
import com.java110.service.smo.IQueryServiceSMO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * 中心服务处理类
 * Created by wuxw on 2018/4/13.
 */
@Service("centerServiceSMOImpl")
@Transactional
public class CenterServiceSMOImpl extends LoggerEngine implements ICenterServiceSMO {

    @Autowired
    ICenterServiceDAO centerServiceDaoImpl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Override
    public String service(String reqJson, Map<String, String> headers) throws SMOException{

        DataFlow dataFlow = null;

        JSONObject responseJson = null;

        String resJson = "";

        try {
            reqJson = decrypt(reqJson,headers);
            //1.0 创建数据流
            dataFlow = DataFlowFactory.newInstance().builder(reqJson, headers);
            //2.0 加载配置信息
            initConfigData(dataFlow);
            //3.0 校验 APPID是否有权限操作serviceCode
            judgeAuthority(dataFlow);
            //4.0 调用规则校验
            ruleValidate(dataFlow);
            //5.0 保存订单和业务项 c_orders c_order_attrs c_business c_business_attrs
            saveOrdersAndBusiness(dataFlow);
            //6.0 调用下游系统
            invokeBusinessSystem(dataFlow);

            responseJson = DataTransactionFactory.createCommonResponseJson(dataFlow);

        } catch (DecryptException e){ //解密异常
            responseJson =  DataTransactionFactory.createOrderResponseJson(ResponseConstant.NO_TRANSACTION_ID,
                    e.getResult().getCode(), e.getMessage());
        }catch (BusinessException e) {
            try {
                //7.0 作废订单和业务项
                invalidOrderAndBusiness(dataFlow);
                //8.0 广播作废业务系统订单信息
                //想法:这里可以直接不广播，只有在业务返回时才广播,
                // 疑问：在这里部分消息发出去了，如果在receiveBusinessSystemNotifyMessage这个方法中依然没有收到，我们认为是下游系统也是失败了不用处理，
                //目前看逻辑也是对的
                //invalidBusinessSystem(dataFlow);
            } catch (Exception e1) {
                LoggerEngine.error("作废订单失败", e);
                //9.0 将订单状态改为失败，人工处理。
                updateOrderAndBusinessError(dataFlow);
            } finally {
                responseJson = DataTransactionFactory.createOrderResponseJson(dataFlow.getTransactionId(),
                         e.getResult().getCode(), e.getMessage());
            }

        } catch (OrdersException e) {
            responseJson =  DataTransactionFactory.createOrderResponseJson(dataFlow.getTransactionId(),
                     e.getResult().getCode(), e.getMessage());
        } catch (RuleException e) {
            responseJson =  DataTransactionFactory.createOrderResponseJson(dataFlow.getTransactionId(),
                     e.getResult().getCode(), e.getMessage());
        } catch (NoAuthorityException e) {
            responseJson =  DataTransactionFactory.createOrderResponseJson(dataFlow.getTransactionId(),
                     e.getResult().getCode(), e.getMessage());
        } catch (InitConfigDataException e){
            responseJson =  DataTransactionFactory.createOrderResponseJson(dataFlow.getTransactionId(),
                    e.getResult().getCode(), e.getMessage());
        }catch (Exception e) {
            logger.error("内部异常了：",e);
            responseJson =  DataTransactionFactory.createOrderResponseJson(dataFlow == null
                            ? ResponseConstant.NO_TRANSACTION_ID
                            : dataFlow.getTransactionId(),
                     ResponseConstant.RESULT_CODE_INNER_ERROR, "内部异常了：" + e.getMessage() + e.getLocalizedMessage());
        } finally {
            if(dataFlow != null) {
                //这里记录日志
                Date endDate = DateUtil.getCurrentDate();

                dataFlow.setEndDate(endDate);
                dataFlow.setResJson(responseJson);
                //添加耗时
                DataFlowFactory.addCostTime(dataFlow, "service", "业务处理总耗时", dataFlow.getStartDate(), dataFlow.getEndDate());

                //这里保存耗时，以及日志
                saveLogMessage(dataFlow.getReqJson(), dataFlow.getResJson());

                //保存耗时
                saveCostTimeLogMessage(dataFlow);
                //处理返回报文鉴权
                AuthenticationFactory.putSign(dataFlow, responseJson);
            }
            resJson = encrypt(responseJson.toJSONString(),headers);
            return resJson;

        }

    }

    /**
     * 解密
     * @param reqJson
     * @return
     */
    private String decrypt(String reqJson,Map<String,String> headers) throws DecryptException{
        try {
            if (MappingConstant.VALUE_ON.equals(headers.get(CommonConstant.ENCRYPT))) {
                logger.debug("解密前字符：" + reqJson);
                reqJson = new String(AuthenticationFactory.decrypt(reqJson.getBytes("UTF-8"), AuthenticationFactory.loadPrivateKey(MappingConstant.KEY_PRIVATE_STRING)
                        , NumberUtils.isNumber(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) ? Integer.parseInt(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) :
                                Integer.parseInt(MappingCache.getValue(MappingConstant.KEY_DEFAULT_DECRYPT_KEY_SIZE))),"UTF-8");
                logger.debug("解密后字符：" + reqJson);
            }
        }catch (Exception e){
            throw new DecryptException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"解密失败");
        }

        return reqJson;
    }

    /**
     * 加密
     * @param resJson
     * @param headers
     * @return
     */
    private String encrypt(String resJson,Map<String,String> headers){
        try {
            if (MappingConstant.VALUE_ON.equals(headers.get(CommonConstant.ENCRYPT))) {
                logger.debug("加密前字符：" + resJson);
                resJson = new String(AuthenticationFactory.encrypt(resJson.getBytes("UTF-8"), AuthenticationFactory.loadPubKey(MappingConstant.KEY_PUBLIC_STRING)
                        , NumberUtils.isNumber(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) ? Integer.parseInt(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) :
                                Integer.parseInt(MappingCache.getValue(MappingConstant.KEY_DEFAULT_DECRYPT_KEY_SIZE))),"UTF-8");
                logger.debug("加密后字符：" + resJson);
            }
        }catch (Exception e){
            logger.error("加密失败：",e);
        }
        return resJson;
    }



    /**
     * 2.0初始化配置信息
     *
     * @param dataFlow
     */
    private void initConfigData(DataFlow dataFlow) {
        Date startDate = DateUtil.getCurrentDate();
        //查询配置信息，并将配置信息封装到 dataFlow 对象中
        List<AppRoute> appRoutes = AppRouteCache.getAppRoute(dataFlow.getAppId());

        if (appRoutes == null) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "initConfigData", "加载配置耗时", startDate);
            throw new InitConfigDataException(ResponseConstant.RESULT_CODE_INNER_ERROR,"当前没有获取到AppId对应的信息，appId = "+dataFlow.getAppId());
        }
        for(AppRoute appRoute: appRoutes) {
            dataFlow.addAppRoutes(appRoute);
        }
        //
        if("-1".equals(dataFlow.getDataFlowId()) || StringUtil.isNullOrNone(dataFlow.getDataFlowId())){
            dataFlow.setDataFlowId(SequenceUtil.getDataFlowId());
        }

        //添加耗时
        DataFlowFactory.addCostTime(dataFlow, "initConfigData", "加载配置耗时", startDate);
    }

    /**
     * 3.0判断 AppId 是否 有serviceCode权限
     *
     * @param dataFlow
     * @throws RuntimeException
     */
    private void judgeAuthority(DataFlow dataFlow) throws NoAuthorityException {
        Date startDate = DateUtil.getCurrentDate();

        if (StringUtil.isNullOrNone(dataFlow.getAppId()) || dataFlow.getAppRoutes().size() == 0 ) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "appId 为空或不正确");
        }

        if (StringUtil.isNullOrNone(dataFlow.getTransactionId())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "transactionId 不能为空");
        }

        if(!StringUtil.isNullOrNone(dataFlow.getAppRoutes().get(0).getSecurityCode())){
            String sign = AuthenticationFactory.dataFlowMd5(dataFlow);
            if(!sign.equals(dataFlow.getReqSign().toLowerCase())){
                throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "签名失败");
            }
        }

        if (StringUtil.isNullOrNone(dataFlow.getUserId())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "userId 不能为空");
        }

        if (StringUtil.isNullOrNone(dataFlow.getRequestTime()) || !DateUtil.judgeDate(dataFlow.getRequestTime(), DateUtil.DATE_FORMATE_STRING_DEFAULT)) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "requestTime 格式不对，遵循yyyyMMddHHmmss格式");
        }

        if (StringUtil.isNullOrNone(dataFlow.getOrderTypeCd())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "orderTypeCd 不能为空");
        }

        //判断 AppId 是否有权限操作相应的服务
        if (dataFlow.getBusinesses() != null && dataFlow.getBusinesses().size() > 0) {
            for (Business business : dataFlow.getBusinesses()) {

                AppService appService = DataFlowFactory.getService(dataFlow, business.getServiceCode());

                //这里调用缓存 查询缓存信息
                if (appService == null) {
                    //添加耗时
                    DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
                    throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "AppId 没有权限访问 serviceCod = " + business.getServiceCode());
                }
            }
        }

        //检验白名单
        List<String> whileListIp = dataFlow.getAppRoutes().get(0).getWhileListIp();
        if (whileListIp != null && whileListIp.size() > 0 && !whileListIp.contains(dataFlow.getIp())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "当前IP被限制不能访问服务");
        }

        //检查黑名单
        List<String> backListIp = dataFlow.getAppRoutes().get(0).getBackListIp();
        if (backListIp != null && backListIp.size() > 0&& backListIp.contains(dataFlow.getIp())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "当前IP被限制不能访问服务");
        }
        //添加耗时
        DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
    }

    /**
     * 4.0规则校验
     *
     * @param dataFlow
     * @throws RuleException
     */
    private void ruleValidate(DataFlow dataFlow) throws RuleException {
        Date startDate = DateUtil.getCurrentDate();
        try {

            if (MappingConstant.VALUE_OFF.equals(MappingCache.getValue(MappingConstant.KEY_RULE_ON_OFF))
                    || (MappingCache.getValue(MappingConstant.KEY_NO_NEED_RULE_VALDATE_ORDER) != null
                    && MappingCache.getValue(MappingConstant.KEY_NO_NEED_RULE_VALDATE_ORDER).contains(dataFlow.getOrderTypeCd()))) {
                //不做校验
                //添加耗时
                DataFlowFactory.addCostTime(dataFlow, "ruleValidate", "规则校验耗时", startDate);
                return ;
            }

            //调用规则

        } catch (Exception e) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "ruleValidate", "规则校验耗时", startDate);
            throw new RuleException(ResponseConstant.RESULT_CODE_RULE_ERROR, "规则校验异常失败：" + e.getMessage());
        }

        DataFlowFactory.addCostTime(dataFlow, "ruleValidate", "规则校验耗时", startDate);

    }

    /**
     * 5.0 保存订单和业务项 c_orders c_order_attrs c_business c_business_attrs
     *
     * @param dataFlow
     * @throws OrdersException
     */
    private void saveOrdersAndBusiness(DataFlow dataFlow) throws OrdersException {
        Date startDate = DateUtil.getCurrentDate();
        if(MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER) != null
                &&MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER).contains(dataFlow.getOrderTypeCd())){
            //不保存订单信息
            DataFlowFactory.addCostTime(dataFlow, "saveOrdersAndBusiness", "保存订单和业务项耗时", startDate);
            return ;
        }


        //1.0 保存 orders信息
        centerServiceDaoImpl.saveOrder(DataFlowFactory.getOrder(dataFlow));


        centerServiceDaoImpl.saveOrderAttrs(DataFlowFactory.getOrderAttrs(dataFlow));

        //2.0 保存 business信息

        centerServiceDaoImpl.saveBusiness(DataFlowFactory.getBusiness(dataFlow));

        centerServiceDaoImpl.saveBusinessAttrs(DataFlowFactory.getBusinessAttrs(dataFlow));

        DataFlowFactory.addCostTime(dataFlow, "saveOrdersAndBusiness", "保存订单和业务项耗时", startDate);
    }

    /**
     * 6.0 调用下游系统
     *
     * @param dataFlow
     * @throws BusinessException
     */
    private void invokeBusinessSystem(DataFlow dataFlow) throws BusinessException {
        Date startDate = DateUtil.getCurrentDate();
        if(MappingCache.getValue(MappingConstant.KEY_NO_INVOKE_BUSINESS_SYSTEM) != null
                &&MappingCache.getValue(MappingConstant.KEY_NO_INVOKE_BUSINESS_SYSTEM).contains(dataFlow.getOrderTypeCd())){
            //不用调用 下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)
            DataFlowFactory.addCostTime(dataFlow, "invokeBusinessSystem", "调用下游系统耗时", startDate);
            dataFlow.setResponseBusinessJson(DataTransactionFactory.createCommonResponseJson(dataFlow.getTransactionId(),
                    ResponseConstant.RESULT_CODE_SUCCESS, "成功",null));
            return ;
        }

        //6.1 先处理同步方式的服务，每一同步后发布事件广播

        doSynchronousBusinesses(dataFlow);


        //6.2 处理异步服务
        doAsynchronousBusinesses(dataFlow);


        DataFlowFactory.addCostTime(dataFlow, "invokeBusinessSystem", "调用下游系统耗时", startDate);
    }



    /**
     * 7.0 作废订单和业务项
     *
     * @param dataFlow
     */
    private void invalidOrderAndBusiness(DataFlow dataFlow) {
        Date startDate = DateUtil.getCurrentDate();
        if(MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER) != null
                &&MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER).contains(dataFlow.getOrderTypeCd())){
            //不用作废订单信息
            DataFlowFactory.addCostTime(dataFlow, "invalidOrderAndBusiness", "作废订单和业务项耗时", startDate);
            return ;
        }

        //作废 订单
        centerServiceDaoImpl.updateOrder(DataFlowFactory.getNeedInvalidOrder(dataFlow));

        //作废订单项
        centerServiceDaoImpl.updateBusiness(DataFlowFactory.getNeedInvalidOrder(dataFlow));


        DataFlowFactory.addCostTime(dataFlow, "invalidOrderAndBusiness", "作废订单和业务项耗时", startDate);
    }


    /**
     * 8.0 广播作废已经完成业务系统订单信息
     *
     * @param dataFlow
     */
    private void invalidCompletedBusinessSystem(DataFlow dataFlow) throws Exception{
        // 根据 c_business 表中的字段business_type_cd 找到对应的消息队列名称
        List<Map> completedBusinesses = centerServiceDaoImpl.getCommonOrderCompledBusinessByBId(dataFlow.getBusinesses().get(0).getbId());
        for(AppRoute appRoute :dataFlow.getAppRoutes()){
            for(Map completedBusiness : completedBusinesses){
                if(completedBusiness.get("business_type_cd").equals(appRoute.getAppService().getBusinessTypeCd())){
                    KafkaFactory.sendKafkaMessage(appRoute.getAppService().getMessageQueueName(),"",
                            DataFlowFactory.getCompletedBusinessErrorJson(dataFlow,completedBusiness,appRoute.getAppService()));
                    saveLogMessage(DataFlowFactory.getCompletedBusinessErrorJson(dataFlow,completedBusiness,appRoute.getAppService()),null);
                }
            }
        }

    }

    /**
     * 9.0 将订单状态改为失败，人工处理。
     *
     * @param dataFlow
     */
    private void updateOrderAndBusinessError(DataFlow dataFlow) {

        Date startDate = DateUtil.getCurrentDate();

        //作废 订单
        centerServiceDaoImpl.updateOrder(DataFlowFactory.getNeedErrorOrder(dataFlow));

        //作废订单项
        centerServiceDaoImpl.updateBusiness(DataFlowFactory.getNeedErrorOrder(dataFlow));


        DataFlowFactory.addCostTime(dataFlow, "updateOrderAndBusinessError", "订单状态改为失败耗时", startDate);

    }


    /**
     * 接受业务系统通知消息
     * @param receiveJson 接受报文
     * @throws SMOException
     */
    @Override
    public void receiveBusinessSystemNotifyMessage(String receiveJson) throws SMOException{
        Date startDate = DateUtil.getCurrentDate();
        DataFlow dataFlow = null;
        try {
            //1.0 创建数据流
            dataFlow = DataFlowFactory.newInstance().builder(receiveJson, null);
            //如果订单都没有保存，则再不要处理
            if(MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER) != null
                    &&MappingCache.getValue(MappingConstant.KEY_NO_SAVE_ORDER).contains(dataFlow.getOrderTypeCd())){
                //不保存订单信息
                return ;
            }

            //2.0加载数据，没有找到appId 及配置信息 则抛出InitConfigDataException
            reloadOrderInfoAndConfigData(dataFlow);

            //3.0 判断是否成功,失败会抛出BusinessStatusException异常
            judgeBusinessStatus(dataFlow);

            //4.0 修改业务为成功,如果发现业务项已经是作废或失败状态（D或E）则抛出BusinessException异常
            completeBusiness(dataFlow);
            //5.0当所有业务动作是否都是C，将订单信息改为 C 并且发布竣工消息，这里在广播之前确认
            completeOrderAndNotifyBusinessSystem(dataFlow);

        }catch (BusinessStatusException e){
            try {
                //6.0 作废订单和所有业务项
                invalidOrderAndBusiness(dataFlow);
                //7.0 广播作废业务系统订单信息
                //想法，这里只广播已经完成的订单项
                invalidCompletedBusinessSystem(dataFlow);
            } catch (Exception e1) {
                LoggerEngine.error("作废订单失败", e1);
                //8.0 将订单状态改为失败，人工处理。
                updateOrderAndBusinessError(dataFlow);
            }
        }catch (BusinessException e) {
            //9.0说明这个订单已经失败了，再不需要
            //想法，这里广播当前失败业务
            try {
                notifyBusinessSystemErrorMessage(dataFlow);
            }catch (Exception e1){
                //这里记录日志
            }
        }catch (InitConfigDataException e){ //这种一般不会出现，除非人工改了数据
            LoggerEngine.error("加载配置数据出错", e);
            try {
                //6.0 作废订单和所有业务项
                invalidOrderAndBusiness(dataFlow);
                //7.0 广播作废业务系统订单信息
                //想法，这里只广播已经完成的订单项
                invalidCompletedBusinessSystem(dataFlow);
            } catch (Exception e1) {
                LoggerEngine.error("作废订单失败", e1);
                //8.0 将订单状态改为失败，人工处理。
                updateOrderAndBusinessError(dataFlow);
            }

        }catch (Exception e){
            LoggerEngine.error("作废订单失败", e);
            //10.0 成功的情况下通知下游系统失败将状态改为NE，人工处理。
            updateBusinessNotifyError(dataFlow);
        }finally{
            DataFlowFactory.addCostTime(dataFlow, "receiveBusinessSystemNotifyMessage", "接受业务系统通知消息耗时", startDate);
            saveLogMessage(dataFlow.getReqJson(),null);
        }
    }


    /**
     * 2.0重新加载订单信息到dataFlow 中
     *
     * @param dataFlow
     */
    private void reloadOrderInfoAndConfigData(DataFlow dataFlow) {

        Map order = centerServiceDaoImpl.getOrderInfoByBId(dataFlow.getBusinesses().get(0).getbId());
        dataFlow.setoId(order.get("o_id").toString());

        if("-1".equals(dataFlow.getDataFlowId()) || StringUtil.isNullOrNone(dataFlow.getDataFlowId())){
            throw new InitConfigDataException(ResponseConstant.RESULT_CODE_ERROR,"请求报文中没有包含 dataFlowId 节点");
        }
        //重新加载配置
        initConfigData(dataFlow);
    }

    /**
     * 9.0 成功的情况下通知下游系统失败将状态改为NE，人工处理。
     *
     * @param dataFlow
     */
    private void updateBusinessNotifyError(DataFlow dataFlow) {

        Date startDate = DateUtil.getCurrentDate();
            //完成订单项
        centerServiceDaoImpl.updateBusinessByBId(DataFlowFactory.getNeedNotifyErrorBusiness(dataFlow));

        DataFlowFactory.addCostTime(dataFlow, "updateBusinessNotifyError", "订单状态改为失败耗时", startDate);

    }

    /**
     * 判断是否都成功了
     * @param dataFlow
     */
    private void judgeBusinessStatus(DataFlow dataFlow) throws BusinessStatusException{

        List<Business> businesses = dataFlow.getBusinesses();

        for(Business business: businesses){
            if(!ResponseConstant.RESULT_CODE_SUCCESS.equals(business.getCode())){
                throw new BusinessStatusException(business.getCode(),"业务bId= "+business.getbId() + " 处理失败，需要作废订单");
            }
        }

    }

    /**
     * 3.0 修改业务为成功,如果发现业务项已经是作废或失败状态（D或E）则抛出BusinessException异常
     *
     * @param dataFlow
     */
    private void completeBusiness(DataFlow dataFlow) throws BusinessException{
        try {
            //完成订单项
            centerServiceDaoImpl.updateBusinessByBId(DataFlowFactory.getNeedCompleteBusiness(dataFlow));

        }catch (DAOException e){
            throw new BusinessException(e.getResult(),e);
        }
    }

    /**
     * //4.0当所有业务动作是否都是C，将订单信息改为 C 并且发布竣工消息，这里在广播之前确认
     * @param dataFlow
     */
    private void completeOrderAndNotifyBusinessSystem(DataFlow dataFlow) throws Exception{
        try {
            centerServiceDaoImpl.completeOrderByBId(DataFlowFactory.getMoreBId(dataFlow));
            //通知成功消息
            notifyBusinessSystemSuccessMessage(dataFlow);
        }catch (DAOException e){
            //这里什么都不做，说明订单没有完成
        }


    }

    /**
     * 通知 订单已经完成，后端需要完成数据
     * @param dataFlow
     */
    private void notifyBusinessSystemSuccessMessage(DataFlow dataFlow) throws Exception{

        //拼装报文通知业务系统
        KafkaFactory.sendKafkaMessage(
                DataFlowFactory.getService(dataFlow,dataFlow.getBusinesses().get(0).getServiceCode()).getMessageQueueName(),"",DataFlowFactory.getNotifyBusinessSuccessJson(dataFlow).toJSONString());

        saveLogMessage(DataFlowFactory.getNotifyBusinessSuccessJson(dataFlow),null);
    }

    /**
     * 8.0 广播作废业务系统订单信息
     *
     * @param dataFlow
     */
    private void notifyBusinessSystemErrorMessage(DataFlow dataFlow) throws Exception{

        //拼装报文通知业务系统
        KafkaFactory.sendKafkaMessage(
                DataFlowFactory.getService(dataFlow,dataFlow.getBusinesses().get(0).getServiceCode()).getMessageQueueName(),"",
                DataFlowFactory.getNotifyBusinessErrorJson(dataFlow).toJSONString());
        saveLogMessage(DataFlowFactory.getNotifyBusinessErrorJson(dataFlow),null);
    }

    /**
     * 处理同步业务
     * @param dataFlow
     */
    private void doSynchronousBusinesses(DataFlow dataFlow) throws BusinessException{
        Date startDate = DateUtil.getCurrentDate();
        Date businessStartDate = null;
        List<Business> synchronousBusinesses = DataFlowFactory.getSynchronousBusinesses(dataFlow);

        if(synchronousBusinesses == null || synchronousBusinesses.size() == 0){
            return ;
        }
        AppService service = null;
        JSONObject requestBusinessJson = null;
        JSONArray responseBusinesses = new JSONArray();
        String responseMessage = "";
        //6.2处理同步服务
        for(Business business : synchronousBusinesses) {
            businessStartDate = DateUtil.getCurrentDate();
            service = DataFlowFactory.getService(dataFlow,business.getServiceCode());
            requestBusinessJson = DataFlowFactory.getRequestBusinessJson(dataFlow,business);
            dataFlow.setRequestBusinessJson(requestBusinessJson);

            if(service.getMethod() == null || "".equals(service.getMethod())) {//post方式
                //http://user-service/test/sayHello
                responseMessage = restTemplate.postForObject(service.getUrl(),dataFlow.getRequestBusinessJson().toJSONString(),String.class);
            }else{//webservice方式
                responseMessage = (String) WebServiceAxisClient.callWebService(service.getUrl(),service.getMethod(),
                        new Object[]{dataFlow.getRequestBusinessJson().toJSONString()},
                        service.getTimeOut());
            }

            if(StringUtil.isNullOrNone(responseMessage) || !Assert.isJsonObject(responseMessage)){
                throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR,"下游系统返回格式不正确，请按协议规范处理");
            }
            dataFlow.setResponseBusinessJson(JSONObject.parseObject(responseMessage));
            //发布事件
            DataFlowEventPublishing.multicastEvent(service.getServiceCode(),dataFlow);

            responseBusinesses.add(dataFlow.getResponseBusinessJson());

            DataFlowFactory.addCostTime(dataFlow, business.getServiceCode(), "调用"+business.getServiceName()+"耗时", businessStartDate);
            saveLogMessage(dataFlow.getRequestBusinessJson(),dataFlow.getResponseBusinessJson());
        }

        dataFlow.setRequestBusinessJson(dataFlow.getReqJson());

        dataFlow.setResponseBusinessJson(DataTransactionFactory.createCommonResponseJson(dataFlow.getTransactionId(),
                 ResponseConstant.RESULT_CODE_SUCCESS, "成功",responseBusinesses));

        DataFlowFactory.addCostTime(dataFlow, "doSynchronousBusinesses", "同步调用业务系统总耗时", startDate);
}

    /**
     * 处理异步业务
     * @param
     */
    private void doAsynchronousBusinesses(DataFlow dataFlow) throws BusinessException{
        Date startDate = DateUtil.getCurrentDate();
        //6.3 处理异步，按消息队里处理
        List<Business> asynchronousBusinesses = DataFlowFactory.getAsynchronousBusinesses(dataFlow);

        if(asynchronousBusinesses == null || asynchronousBusinesses.size() == 0){
            return ;
        }

        try {
            for (Business business : asynchronousBusinesses) {
                KafkaFactory.sendKafkaMessage(DataFlowFactory.getService(dataFlow, business.getServiceCode()).getMessageQueueName(), "",
                        DataFlowFactory.getRequestBusinessJson(dataFlow,business).toJSONString());
            }
        }catch (Exception e){
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR,e.getMessage());
        }

        dataFlow.setRequestBusinessJson(dataFlow.getReqJson());

        dataFlow.setResponseBusinessJson(DataTransactionFactory.createOrderResponseJson(dataFlow.getTransactionId(),
                 ResponseConstant.RESULT_CODE_SUCCESS, "成功"));
        DataFlowFactory.addCostTime(dataFlow, "doSynchronousBusinesses", "异步调用业务系统总耗时", startDate);
        saveLogMessage(dataFlow.getRequestBusinessJson(),dataFlow.getResponseBusinessJson());
    }


    /**
     * 保存日志信息
     * @param requestJson
     */
    private void saveLogMessage(JSONObject requestJson,JSONObject responseJson){

        try{
            if(MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_LOG_ON_OFF))){
                JSONObject log = new JSONObject();
                log.put("request",requestJson);
                log.put("response",responseJson);
                KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_LOG_NAME,"",log.toJSONString());
            }
        }catch (Exception e){
            logger.error("报错日志出错了，",e);
        }
    }

    /**
     * 保存耗时信息
     * @param dataFlow
     */
    private void saveCostTimeLogMessage(DataFlow dataFlow){
        try{
            if(MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_COST_TIME_ON_OFF))){
                List<DataFlowLinksCost> dataFlowLinksCosts = dataFlow.getLinksCostDates();
                JSONObject costDate = new JSONObject();
                JSONArray costDates = new JSONArray();
                JSONObject newObj = null;
                for(DataFlowLinksCost dataFlowLinksCost : dataFlowLinksCosts){
                    newObj = JSONObject.parseObject(JSONObject.toJSONString(dataFlowLinksCost));
                    newObj.put("dataFlowId",dataFlow.getDataFlowId());
                    newObj.put("transactionId",dataFlow.getTransactionId());
                    costDates.add(newObj);
                }
                costDate.put("costDates",costDates);

                KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_COST_TIME_LOG_NAME,"",costDate.toJSONString());
            }
        }catch (Exception e){
            logger.error("报错日志出错了，",e);
        }
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
