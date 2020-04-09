package com.java110.api.bmo.purchaseApplyDetail.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.purchaseApplyDetail.IPurchaseApplyDetailBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.purchaseApplyDetail.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.dto.purchaseApplyDetail.PurchaseApplyDetailDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("purchaseApplyDetailBMOImpl")
public class PurchaseApplyDetailBMOImpl extends ApiBaseBMO implements IPurchaseApplyDetailBMO {

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addPurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PURCHASE_APPLY_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessPurchaseApplyDetail = new JSONObject();
        businessPurchaseApplyDetail.putAll(paramInJson);
        businessPurchaseApplyDetail.put("id", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessPurchaseApplyDetail", businessPurchaseApplyDetail);
        return business;
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updatePurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        PurchaseApplyDetailDto purchaseApplyDetailDto = new PurchaseApplyDetailDto();
        //purchaseApplyDetailDto.getApplyOrderId(paramInJson.getString("purchaseApplyDetailId"));
        //purchaseApplyDetailDto.setCommunityId(paramInJson.getString("communityId"));
        List<PurchaseApplyDetailDto> purchaseApplyDetailDtos = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto);

        Assert.listOnlyOne(purchaseApplyDetailDtos, "未找到需要修改的活动 或多条数据");


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PURCHASE_APPLY_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessPurchaseApplyDetail = new JSONObject();
        businessPurchaseApplyDetail.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessPurchaseApplyDetail", businessPurchaseApplyDetail);
        return business;
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject deletePurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PURCHASE_APPLY_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessPurchaseApplyDetail = new JSONObject();
        businessPurchaseApplyDetail.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessPurchaseApplyDetail", businessPurchaseApplyDetail);
        return business;
    }

}
