package com.java110.common.factory;

import com.java110.common.constant.ResponseConstant;
import com.java110.common.util.DateUtil;
import com.java110.entity.center.DataFlow;
import com.java110.entity.center.DataFlowLinksCost;

import java.util.Date;

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
        DataFlowLinksCost dataFlowLinksCost = new DataFlowLinksCost().builder(linksCode, linksName, startDate, endDate);
        dataFlow.addLinksCostDatas(dataFlowLinksCost);
        return dataFlow;
    }
}
