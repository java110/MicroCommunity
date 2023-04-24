package com.java110.api.bmo.allocationStorehouseApply.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.allocationStorehouseApply.IAllocationStorehouseApplyBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.store.IAllocationStorehouseApplyInnerServiceSMO;
import com.java110.po.allocationStorehouseApply.AllocationStorehouseApplyPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("allocationStorehouseApplyBMOImpl")
public class AllocationStorehouseApplyBMOImpl extends ApiBaseBMO implements IAllocationStorehouseApplyBMO {

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAllocationStorehouseApply(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("applyId", "-1");
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = BeanConvertUtil.covertBean(paramInJson, AllocationStorehouseApplyPo.class);
        super.insert(dataFlowContext, allocationStorehouseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE_APPLY);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateAllocationStorehouseApply(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = BeanConvertUtil.covertBean(paramInJson, AllocationStorehouseApplyPo.class);
        super.update(dataFlowContext, allocationStorehouseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ALLOCATION_STOREHOUSE_APPLY);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteAllocationStorehouseApply(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        AllocationStorehouseApplyPo allocationStorehouseApplyPo = BeanConvertUtil.covertBean(paramInJson, AllocationStorehouseApplyPo.class);
        super.update(dataFlowContext, allocationStorehouseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ALLOCATION_STOREHOUSE_APPLY);
    }

}
