package com.java110.api.bmo.purchaseApplyDetail.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.purchaseApplyDetail.IPurchaseApplyDetailBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.dto.purchaseApplyDetail.PurchaseApplyDetailDto;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
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
    public void addPurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessPurchaseApplyDetail = new JSONObject();
        businessPurchaseApplyDetail.putAll(paramInJson);
        businessPurchaseApplyDetail.put("id", "-1");
        PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(businessPurchaseApplyDetail, PurchaseApplyDetailPo.class);
        super.insert(dataFlowContext, purchaseApplyDetailPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PURCHASE_APPLY_DETAIL);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updatePurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        PurchaseApplyDetailDto purchaseApplyDetailDto = new PurchaseApplyDetailDto();
        //purchaseApplyDetailDto.getApplyOrderId(paramInJson.getString("purchaseApplyDetailId"));
        //purchaseApplyDetailDto.setCommunityId(paramInJson.getString("communityId"));
        List<PurchaseApplyDetailDto> purchaseApplyDetailDtos = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto);

        Assert.listOnlyOne(purchaseApplyDetailDtos, "未找到需要修改的活动 或多条数据");


        PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(paramInJson, PurchaseApplyDetailPo.class);
        super.update(dataFlowContext, purchaseApplyDetailPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PURCHASE_APPLY_DETAIL);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deletePurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(paramInJson, PurchaseApplyDetailPo.class);
        super.delete(dataFlowContext, purchaseApplyDetailPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PURCHASE_APPLY_DETAIL);
    }

}
