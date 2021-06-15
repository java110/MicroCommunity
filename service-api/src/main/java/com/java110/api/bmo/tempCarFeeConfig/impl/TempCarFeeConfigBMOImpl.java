package com.java110.api.bmo.tempCarFeeConfig.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.tempCarFeeConfig.ITempCarFeeConfigBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.java110.po.tempCarFeeConfig.TempCarFeeConfigPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tempCarFeeConfigBMOImpl")
public class TempCarFeeConfigBMOImpl extends ApiBaseBMO implements ITempCarFeeConfigBMO {

    @Autowired
    private ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addTempCarFeeConfig(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        TempCarFeeConfigPo tempCarFeeConfigPo = BeanConvertUtil.covertBean(paramInJson, TempCarFeeConfigPo.class);
        super.insert(dataFlowContext, tempCarFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_TEMP_CAR_FEE_CONFIG_INFO);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateTempCarFeeConfig(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        TempCarFeeConfigPo tempCarFeeConfigPo = BeanConvertUtil.covertBean(paramInJson, TempCarFeeConfigPo.class);
        super.update(dataFlowContext, tempCarFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_TEMP_CAR_FEE_CONFIG_INFO);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteTempCarFeeConfig(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        TempCarFeeConfigPo tempCarFeeConfigPo = BeanConvertUtil.covertBean(paramInJson, TempCarFeeConfigPo.class);
        super.update(dataFlowContext, tempCarFeeConfigPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_TEMP_CAR_FEE_CONFIG_INFO);
    }

}
