package com.java110.api.bmo.tempCarFeeConfigAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.tempCarFeeConfigAttr.ITempCarFeeConfigAttrBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.fee.ITempCarFeeConfigAttrInnerServiceSMO;
import com.java110.po.tempCarFeeConfigAttr.TempCarFeeConfigAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tempCarFeeConfigAttrBMOImpl")
public class TempCarFeeConfigAttrBMOImpl extends ApiBaseBMO implements ITempCarFeeConfigAttrBMO {

    @Autowired
    private ITempCarFeeConfigAttrInnerServiceSMO tempCarFeeConfigAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addTempCarFeeConfigAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        TempCarFeeConfigAttrPo tempCarFeeConfigAttrPo = BeanConvertUtil.covertBean(paramInJson, TempCarFeeConfigAttrPo.class);
        super.insert(dataFlowContext, tempCarFeeConfigAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_TEMP_CAR_FEE_CONFIG_ATTR_INFO);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateTempCarFeeConfigAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        TempCarFeeConfigAttrPo tempCarFeeConfigAttrPo = BeanConvertUtil.covertBean(paramInJson, TempCarFeeConfigAttrPo.class);
        super.update(dataFlowContext, tempCarFeeConfigAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_TEMP_CAR_FEE_CONFIG_ATTR_INFO);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteTempCarFeeConfigAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        TempCarFeeConfigAttrPo tempCarFeeConfigAttrPo = BeanConvertUtil.covertBean(paramInJson, TempCarFeeConfigAttrPo.class);
        super.update(dataFlowContext, tempCarFeeConfigAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_TEMP_CAR_FEE_CONFIG_ATTR_INFO);
    }

}
