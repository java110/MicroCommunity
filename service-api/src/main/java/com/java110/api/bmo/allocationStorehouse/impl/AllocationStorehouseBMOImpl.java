package com.java110.api.bmo.allocationStorehouse.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.allocationStorehouse.IAllocationStorehouseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.store.IAllocationStorehouseInnerServiceSMO;
import com.java110.po.allocationStorehouse.AllocationStorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("allocationStorehouseBMOImpl")
public class AllocationStorehouseBMOImpl extends ApiBaseBMO implements IAllocationStorehouseBMO {

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAllocationStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("asId", "-1");
        AllocationStorehousePo allocationStorehousePo = BeanConvertUtil.covertBean(paramInJson, AllocationStorehousePo.class);
        super.insert(dataFlowContext, allocationStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateAllocationStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AllocationStorehousePo allocationStorehousePo = BeanConvertUtil.covertBean(paramInJson, AllocationStorehousePo.class);
        super.update(dataFlowContext, allocationStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ALLOCATION_STOREHOUSE);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteAllocationStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        AllocationStorehousePo allocationStorehousePo = BeanConvertUtil.covertBean(paramInJson, AllocationStorehousePo.class);
        super.update(dataFlowContext, allocationStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ALLOCATION_STOREHOUSE);
    }

}
