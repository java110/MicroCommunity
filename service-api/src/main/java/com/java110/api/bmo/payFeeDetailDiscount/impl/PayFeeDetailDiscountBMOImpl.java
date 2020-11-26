package com.java110.api.bmo.payFeeDetailDiscount.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.payFeeDetailDiscount.IPayFeeDetailDiscountBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.fee.IPayFeeDetailDiscountInnerServiceSMO;
import com.java110.po.payFeeDetailDiscount.PayFeeDetailDiscountPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("payFeeDetailDiscountBMOImpl")
public class PayFeeDetailDiscountBMOImpl extends ApiBaseBMO implements IPayFeeDetailDiscountBMO {

    @Autowired
    private IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addPayFeeDetailDiscount(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("detailDiscountId", "-1");
        PayFeeDetailDiscountPo payFeeDetailDiscountPo = BeanConvertUtil.covertBean(paramInJson, PayFeeDetailDiscountPo.class);
        super.insert(dataFlowContext, payFeeDetailDiscountPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_DETAIL_DISCOUNT_INFO);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updatePayFeeDetailDiscount(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        PayFeeDetailDiscountPo payFeeDetailDiscountPo = BeanConvertUtil.covertBean(paramInJson, PayFeeDetailDiscountPo.class);
        super.update(dataFlowContext, payFeeDetailDiscountPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_DETAIL_DISCOUNT_INFO);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deletePayFeeDetailDiscount(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        PayFeeDetailDiscountPo payFeeDetailDiscountPo = BeanConvertUtil.covertBean(paramInJson, PayFeeDetailDiscountPo.class);
        super.update(dataFlowContext, payFeeDetailDiscountPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_DETAIL_DISCOUNT_INFO);
    }

}
