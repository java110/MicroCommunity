package com.java110.api.bmo.payFeeDetailDiscount.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.payFeeDetailDiscount.IPayFeeDetailDiscountBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.fee.IPayFeeDetailDiscountInnerServiceSMO;
import com.java110.po.payFeeDetailDiscount.PayFeeDetailDiscountPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
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
    public JSONObject addPayFeeDetailDiscount(JSONObject paramInJson, JSONObject discountJson, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_DETAIL_DISCOUNT_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFee = new JSONObject();
        businessFee.put("detailDiscountId", "-1");
        businessFee.put("discountPrice", discountJson.getString("discountPrice"));
        businessFee.put("discountId", discountJson.getString("discountId"));
        businessFee.put("detailId", paramInJson.getString("detailId"));
        businessFee.put("communityId", paramInJson.getString("communityId"));
        businessFee.put("feeId", paramInJson.getString("feeId"));
        //businessFee.putAll(feeMap);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeeDetailDiscountPo.class.getSimpleName(), businessFee);
        return business;

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addPayFeeDetailDiscountTwo(JSONObject paramInJson, JSONObject discountJson, DataFlowContext dataFlowContext) {
        JSONObject businessFee = new JSONObject();
        businessFee.put("detailDiscountId", "-1");
        businessFee.put("discountPrice", discountJson.getString("discountPrice"));
        businessFee.put("discountId", discountJson.getString("discountId"));
        businessFee.put("detailId", paramInJson.containsKey("newDetailId") ? paramInJson.getString("newDetailId") : paramInJson.getString("detailId"));
        businessFee.put("communityId", paramInJson.getString("communityId"));
        businessFee.put("feeId", paramInJson.getString("feeId"));
        PayFeeDetailDiscountPo payFeeDetailDiscountPo = BeanConvertUtil.covertBean(businessFee, PayFeeDetailDiscountPo.class);
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
