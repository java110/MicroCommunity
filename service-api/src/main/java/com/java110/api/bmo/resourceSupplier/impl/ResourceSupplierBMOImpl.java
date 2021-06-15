package com.java110.api.bmo.resourceSupplier.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.resourceSupplier.IResourceSupplierBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.purchaseApplyDetail.PurchaseApplyDetailDto;
import com.java110.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.intf.store.IResourceSupplierInnerServiceSMO;
import com.java110.po.resourceSupplier.ResourceSupplierPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("resourceSupplierBMOImpl")
public class ResourceSupplierBMOImpl extends ApiBaseBMO implements IResourceSupplierBMO {

    @Autowired
    private IResourceSupplierInnerServiceSMO resourceSupplierInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addResourceSupplier(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("rsId", "-1");
        paramInJson.put("createUserId", paramInJson.getString("userId"));
        paramInJson.put("createUserName", paramInJson.getString("userName"));
        ResourceSupplierPo resourceSupplierPo = BeanConvertUtil.covertBean(paramInJson, ResourceSupplierPo.class);
        super.insert(dataFlowContext, resourceSupplierPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_SUPPLIER);
    }

    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateResourceSupplier(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ResourceSupplierPo resourceSupplierPo = BeanConvertUtil.covertBean(paramInJson, ResourceSupplierPo.class);
        super.update(dataFlowContext, resourceSupplierPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_SUPPLIER);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteResourceSupplier(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        PurchaseApplyDetailDto purchaseApplyDetailDto = new PurchaseApplyDetailDto();
        purchaseApplyDetailDto.setRsId(paramInJson.getString("rsId"));
        List<PurchaseApplyDetailDto> purchaseApplyDetailDtos = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto);
        Assert.listIsNull(purchaseApplyDetailDtos, "该供应商存在物品，不能直接删除！");
        ResourceSupplierPo resourceSupplierPo = BeanConvertUtil.covertBean(paramInJson, ResourceSupplierPo.class);
        super.update(dataFlowContext, resourceSupplierPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_RESOURCE_SUPPLIER);
    }
}
