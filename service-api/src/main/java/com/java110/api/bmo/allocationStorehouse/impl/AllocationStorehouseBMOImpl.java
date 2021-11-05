package com.java110.api.bmo.allocationStorehouse.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.allocationStorehouse.IAllocationStorehouseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.intf.community.IResourceStoreServiceSMO;
import com.java110.intf.store.IAllocationStorehouseInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.po.allocationStorehouse.AllocationStorehousePo;
import com.java110.po.allocationStorehouseApply.AllocationStorehouseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("allocationStorehouseBMOImpl")
public class AllocationStorehouseBMOImpl extends ApiBaseBMO implements IAllocationStorehouseBMO {

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreServiceSMO resourceStoreServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

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

        AllocationStorehouseDto allocationStorehouseDto = new AllocationStorehouseDto();
        allocationStorehouseDto.setApplyId(paramInJson.getString("applyId"));
        allocationStorehouseDto.setStoreId(paramInJson.getString("storeId"));

        List<AllocationStorehouseDto> allocationStorehouseDtos = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);

        for (AllocationStorehouseDto tmpAllocationStorehouseDto : allocationStorehouseDtos) {
            AllocationStorehousePo allocationStorehousePo = BeanConvertUtil.covertBean(tmpAllocationStorehouseDto, AllocationStorehousePo.class);
            super.delete(dataFlowContext, allocationStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ALLOCATION_STOREHOUSE);
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(allocationStorehousePo.getResId());
            //查询资源物品表
            List<ResourceStorePo> resourceStores = resourceStoreServiceSMOImpl.getResourceStores(resourceStorePo);
            Assert.listOnlyOne(resourceStores, "资源物品信息错误");
            //获取库存数量
            BigDecimal resourceStoreStock = new BigDecimal(resourceStores.get(0).getStock());
            //获取调拨的数量
            BigDecimal storehouseStock = new BigDecimal(allocationStorehousePo.getStock());
            //库存数量
            BigDecimal stock = resourceStoreStock.add(storehouseStock);
            resourceStorePo.setStock(String.valueOf(stock));
            //计算最小计量总数
            if (StringUtil.isEmpty(resourceStores.get(0).getMiniStock())) {
                throw new IllegalArgumentException("最小计量总数不能为空！");
            }
            BigDecimal miniStock = new BigDecimal(resourceStores.get(0).getMiniStock()); //获取物品表的最小计量总数
            if (StringUtil.isEmpty(resourceStores.get(0).getMiniUnitStock())) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            BigDecimal miniUnitStock = new BigDecimal(resourceStores.get(0).getMiniUnitStock()); //获取最小计量单位数量
            BigDecimal stock2 = new BigDecimal(allocationStorehousePo.getStock()); //获取最小计量单位数量
            BigDecimal nowMiniStock = stock2.multiply(miniUnitStock); //计算当前的最小计量总数
            BigDecimal newMiniStock = miniStock.add(nowMiniStock);
            resourceStorePo.setMiniStock(String.valueOf(newMiniStock));
            super.update(dataFlowContext, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE);

            //取消流程审批
            //查询任务
            PurchaseApplyDto purchaseDto = new PurchaseApplyDto();
            purchaseDto.setBusinessKey(tmpAllocationStorehouseDto.getApplyId());
            List<PurchaseApplyDto>  purchaseApplyDtoList=purchaseApplyInnerServiceSMOImpl.getActRuTaskId(purchaseDto);
            if(purchaseApplyDtoList!=null && purchaseApplyDtoList.size()>0){
                PurchaseApplyDto purchaseDto1 = new PurchaseApplyDto();
                purchaseDto1.setActRuTaskId(purchaseApplyDtoList.get(0).getActRuTaskId());
                purchaseDto1.setAssigneeUser("999999");
                purchaseApplyInnerServiceSMOImpl.updateActRuTaskById(purchaseDto1);
            }
        }

        AllocationStorehouseApplyPo allocationStorehouseApplyPo = new AllocationStorehouseApplyPo();
        allocationStorehouseApplyPo.setApplyId(allocationStorehouseDto.getApplyId());
        allocationStorehouseApplyPo.setStoreId(allocationStorehouseDto.getStoreId());
        super.update(dataFlowContext, allocationStorehouseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ALLOCATION_STOREHOUSE_APPLY);

    }
}
