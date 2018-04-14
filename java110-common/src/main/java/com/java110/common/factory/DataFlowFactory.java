package com.java110.common.factory;

import com.java110.common.cache.MappingCache;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.util.DateUtil;
import com.java110.entity.center.AppService;
import com.java110.entity.center.AppServiceStatus;
import com.java110.entity.center.DataFlow;
import com.java110.entity.center.DataFlowLinksCost;

import java.util.Date;
import java.util.List;

/**
 * 数据流工厂类
 * Created by wuxw on 2018/4/13.
 */
public class DataFlowFactory {

    public static DataFlow newInstance(){
        return new DataFlow(DateUtil.getCurrentDate(), ResponseConstant.RESULT_CODE_SUCCESS);
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
    public static DataFlow addCostTime(DataFlow dataFlow, String linksCode, String linksName, Date startDate, Date endDate){
        if(MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_COST_TIME_ON_OFF))) {
            DataFlowLinksCost dataFlowLinksCost = new DataFlowLinksCost().builder(linksCode, linksName, startDate, endDate);
            dataFlow.addLinksCostDatas(dataFlowLinksCost);
        }
        return dataFlow;
    }

    /**
     * 获取单个服务
     * @param dataFlow
     * @param serviceCode
     * @return
     */
    public static AppService getService(DataFlow dataFlow, String serviceCode){
        if (dataFlow.getAppRoute() == null){
            throw new RuntimeException("当前没有获取到AppId对应的信息");
        }

        List<AppServiceStatus> serviceStatuses = dataFlow.getAppRoute().getAppServices();
        for(AppServiceStatus serviceStatus : serviceStatuses) {
            if (StatusConstant.STATUS_CD_VALID.equals(serviceStatus.getStatusCd())
                    &&serviceStatus.getAppService().getServiceCode().equals(serviceCode)){
                return serviceStatus.getAppService();
            }
        }
        return null;
    }



}
