package com.java110.api.bmo.meterWater.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.meterWater.IMeterWaterBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.po.meterWater.MeterWaterPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("meterWaterBMOImpl")
public class MeterWaterBMOImpl extends ApiBaseBMO implements IMeterWaterBMO {

    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addMeterWater(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("waterId", "-1");
        MeterWaterPo meterWaterPo = BeanConvertUtil.covertBean(paramInJson, MeterWaterPo.class);
        super.insert(dataFlowContext, meterWaterPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_METER_WATER);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateMeterWater(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        MeterWaterPo meterWaterPo = BeanConvertUtil.covertBean(paramInJson, MeterWaterPo.class);
        super.update(dataFlowContext, meterWaterPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_METER_WATER);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteMeterWater(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        MeterWaterPo meterWaterPo = BeanConvertUtil.covertBean(paramInJson, MeterWaterPo.class);
        super.update(dataFlowContext, meterWaterPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_METER_WATER);
    }

}
