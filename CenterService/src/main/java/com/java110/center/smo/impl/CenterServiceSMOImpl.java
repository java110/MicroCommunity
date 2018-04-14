package com.java110.center.smo.impl;

import com.java110.center.dao.ICenterServiceDAO;
import com.java110.common.cache.AppRouteCache;
import com.java110.center.smo.ICenterServiceSMO;
import com.java110.common.cache.MappingCache;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.*;
import com.java110.common.factory.DataFlowFactory;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.DateUtil;
import com.java110.common.util.ResponseTemplateUtil;
import com.java110.common.util.StringUtil;
import com.java110.entity.center.AppRoute;
import com.java110.entity.center.AppService;
import com.java110.entity.center.Business;
import com.java110.entity.center.DataFlow;
import com.java110.entity.rule.RuleEntrance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 中心服务处理类
 * Created by wuxw on 2018/4/13.
 */
@Service("centerServiceSMOImpl")
@Transactional
public class CenterServiceSMOImpl implements ICenterServiceSMO {

    @Autowired
    ICenterServiceDAO orderServiceDaoImpl;

    @Override
    public String service(String reqJson, Map<String, String> headers) throws SMOException{

        DataFlow dataFlow = null;

        try {
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

        } catch (BusinessException e) {
            try {
                //7.0 作废订单和业务项
                invalidOrderAndBusiness(dataFlow);
                //8.0 广播作废业务系统订单信息
                invalidBusinessSystem(dataFlow);
            } catch (Exception e1) {
                LoggerEngine.error("作废订单失败", e);
                //9.0 记录作废失败的单子，人工处理。
                saveInvalidBusinessError(dataFlow);
            } finally {
                return ResponseTemplateUtil.createOrderResponseJson(dataFlow.getTransactionId(),
                        ResponseConstant.NO_NEED_SIGN, e.getResult().getCode(), e.getMessage());
            }

        } catch (OrdersException e) {
            return ResponseTemplateUtil.createOrderResponseJson(dataFlow.getTransactionId(),
                    ResponseConstant.NO_NEED_SIGN, e.getResult().getCode(), e.getMessage());
        } catch (RuleException e) {
            return ResponseTemplateUtil.createOrderResponseJson(dataFlow.getTransactionId(),
                    ResponseConstant.NO_NEED_SIGN, e.getResult().getCode(), e.getMessage());
        } catch (NoAuthorityException e) {
            return ResponseTemplateUtil.createOrderResponseJson(dataFlow.getTransactionId(),
                    ResponseConstant.NO_NEED_SIGN, e.getResult().getCode(), e.getMessage());
        } catch (Exception e) {
            return ResponseTemplateUtil.createOrderResponseJson(dataFlow == null
                            ? ResponseConstant.NO_TRANSACTION_ID
                            : dataFlow.getTransactionId(),
                    ResponseConstant.NO_NEED_SIGN, ResponseConstant.RESULT_CODE_INNER_ERROR, "内部异常了：" + e.getMessage() + e.getLocalizedMessage());
        } finally {
            //这里记录日志
            Date endDate = DateUtil.getCurrentDate();
            if (dataFlow == null) { //说明异常了,不能记录耗时

                return null;
            }
            dataFlow.setEndDate(endDate);

            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "service", "业务处理总耗时", dataFlow.getStartDate(), dataFlow.getEndDate());

            //这里保存耗时，以及日志

            return ResponseTemplateUtil.createCommonResponseJson(dataFlow);
        }

    }

    /**
     * 2.0初始化配置信息
     *
     * @param dataFlow
     */
    private void initConfigData(DataFlow dataFlow) {
        Date startDate = DateUtil.getCurrentDate();
        //查询配置信息，并将配置信息封装到 dataFlow 对象中
        AppRoute appRoute = AppRouteCache.getAppRoute(dataFlow.getAppId());

        if (appRoute == null) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "initConfigData", "加载配置耗时", startDate);
            throw new RuntimeException("当前没有获取到AppId对应的信息");
        }
        dataFlow.setAppRoute(appRoute);
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

        if (StringUtil.isNullOrNone(dataFlow.getAppId()) || dataFlow.getAppRoute() == null) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "appId 为空或不正确");
        }

        if (StringUtil.isNullOrNone(dataFlow.getTransactionId())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "transactionId 不能为空");
        }

        if (StringUtil.isNullOrNone(dataFlow.getUserId())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "userId 不能为空");
        }

        if (StringUtil.isNullOrNone(dataFlow.getRequestTime()) || DateUtil.judgeDate(dataFlow.getRequestTime(), DateUtil.DATE_FORMATE_STRING_DEFAULT)) {
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
        List<String> whileListIp = dataFlow.getAppRoute().getWhileListIp();
        if (whileListIp != null && !whileListIp.contains(dataFlow.getIp())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "当前IP被限制不能访问服务");
        }

        //检查黑名单
        List<String> backListIp = dataFlow.getAppRoute().getBackListIp();
        if (backListIp != null && backListIp.contains(dataFlow.getIp())) {
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


        //2.0 保存 business信息


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
            return ;
        }

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

        DataFlowFactory.addCostTime(dataFlow, "invalidOrderAndBusiness", "作废订单和业务项耗时", startDate);
    }

    /**
     * 8.0 广播作废业务系统订单信息
     *
     * @param dataFlow
     */
    private void invalidBusinessSystem(DataFlow dataFlow) {
        Date startDate = DateUtil.getCurrentDate();
        if(MappingCache.getValue(MappingConstant.KEY_NO_INVALID_BUSINESS_SYSTEM) != null
                &&MappingCache.getValue(MappingConstant.KEY_NO_INVALID_BUSINESS_SYSTEM).contains(dataFlow.getOrderTypeCd())){
            //不用调用 下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)
            DataFlowFactory.addCostTime(dataFlow, "invalidBusinessSystem", "作废业务耗时", startDate);
            return ;
        }


        DataFlowFactory.addCostTime(dataFlow, "invalidBusinessSystem", "作废业务耗时", startDate);
    }

    /**
     * 9.0 记录作废失败的单子，人工处理。
     *
     * @param dataFlow
     */
    private void saveInvalidBusinessError(DataFlow dataFlow) {

        Date startDate = DateUtil.getCurrentDate();



        DataFlowFactory.addCostTime(dataFlow, "saveInvalidBusinessError", "保存作废业务失败耗时", startDate);

    }


    public ICenterServiceDAO getOrderServiceDaoImpl() {
        return orderServiceDaoImpl;
    }

    public void setOrderServiceDaoImpl(ICenterServiceDAO orderServiceDaoImpl) {
        this.orderServiceDaoImpl = orderServiceDaoImpl;
    }
}
