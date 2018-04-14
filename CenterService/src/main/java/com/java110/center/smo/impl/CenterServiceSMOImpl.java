package com.java110.center.smo.impl;

import com.java110.common.cache.AppRouteCache;
import com.java110.center.smo.ICenterServiceSMO;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.NoAuthorityException;
import com.java110.common.factory.DataFlowFactory;
import com.java110.common.util.DateUtil;
import com.java110.common.util.ResponseTemplateUtil;
import com.java110.common.util.StringUtil;
import com.java110.entity.center.AppRoute;
import com.java110.entity.center.AppService;
import com.java110.entity.center.Business;
import com.java110.entity.center.DataFlow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * 中心服务处理类
 * Created by wuxw on 2018/4/13.
 */
@Service("centerServiceSMOImpl")
@Transactional
public class CenterServiceSMOImpl implements ICenterServiceSMO {

    @Override
    public String service(String reqJson, Map<String,String> headers) {

        DataFlow dataFlow = null;

        try {
            //1.0 创建数据流
            dataFlow = DataFlowFactory.newInstance().builder(reqJson, headers);
            //2.0 加载配置信息
            initConfigData(dataFlow);
            //2.0 校验 APPID是否有权限操作serviceCode
            judgeAuthority(dataFlow);


        }catch(NoAuthorityException e){

            return ResponseTemplateUtil.createOrderResponseJson(dataFlow.getTransactionId(),
                    ResponseConstant.NO_NEED_SIGN,e.getResult().getCode(),e.getMessage());
        }catch (Exception e){
            return ResponseTemplateUtil.createOrderResponseJson(dataFlow == null
                            ?ResponseConstant.NO_TRANSACTION_ID
                            :dataFlow.getTransactionId(),
                    ResponseConstant.NO_NEED_SIGN,ResponseConstant.RESULT_CODE_INNER_ERROR,"内部异常了："+e.getMessage()+e.getLocalizedMessage());
        }finally {
            //这里记录日志
            Date endDate = DateUtil.getCurrentDate();
            if(dataFlow == null){ //说明异常了
                return null;
            }
            dataFlow.setEndDate(endDate);
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow,"service","业务处理总耗时",dataFlow.getStartDate(),dataFlow.getEndDate());

            //这里保存耗时，以及日志

            return ResponseTemplateUtil.createCommonResponseJson(dataFlow);
        }

    }

    /**
     * 初始化配置信息
     * @param dataFlow
     */
    private void initConfigData(DataFlow dataFlow){

        //查询配置信息，并将配置信息封装到 dataFlow 对象中
        AppRoute appRoute = AppRouteCache.getAppRoute(dataFlow.getAppId());

        if(appRoute == null){
            throw new RuntimeException("当前没有获取到AppId对应的信息");
        }
        dataFlow.setAppRoute(appRoute);
    }
    /**
     * 判断 AppId 是否 有serviceCode权限
     * @param dataFlow
     * @throws RuntimeException
     */
    private void judgeAuthority(DataFlow dataFlow) throws NoAuthorityException{


        if(StringUtil.isNullOrNone(dataFlow.getAppId()) || dataFlow.getAppRoute() == null){
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"appId 为空或不正确");
        }

        if(StringUtil.isNullOrNone(dataFlow.getTransactionId())){
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"transactionId 不能为空");
        }

        if(StringUtil.isNullOrNone(dataFlow.getUserId())){
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"userId 不能为空");
        }

        if(StringUtil.isNullOrNone(dataFlow.getRequestTime()) || DateUtil.judgeDate(dataFlow.getRequestTime(),DateUtil.DATE_FORMATE_STRING_DEFAULT)){
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"requestTime 格式不对，遵循yyyyMMddHHmmss格式");
        }

        if(StringUtil.isNullOrNone(dataFlow.getOrderTypeCd())){
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"orderTypeCd 不能为空");
        }

        //判断 AppId 是否有权限操作相应的服务
        if(dataFlow.getBusinesses() != null && dataFlow.getBusinesses().size() > 0){
            for (Business business : dataFlow.getBusinesses()){

               AppService appService = DataFlowFactory.getService(dataFlow,business.getServiceCode());

                //这里调用缓存 查询缓存信息
                if(appService == null){
                    throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"AppId 没有权限访问 serviceCod = "+business.getServiceCode());
                }
            }
        }
    }

}
