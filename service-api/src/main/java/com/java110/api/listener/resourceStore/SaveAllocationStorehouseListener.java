package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import com.java110.dto.allocationStorehouseApply.AllocationStorehouseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.intf.common.IAllocationStorehouseUserInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.po.allocationStorehouse.AllocationStorehousePo;
import com.java110.po.allocationStorehouseApply.AllocationStorehouseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeAllocationStorehouseConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveAllocationStorehouseListener")
public class SaveAllocationStorehouseListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseUserInnerServiceSMO allocationStorehouseUserInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "remark", "请求报文中未包含申请信息");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");

        if (!reqJson.containsKey("resourceStores")) {
            throw new IllegalArgumentException("请求报文中未包含物品信息");
        }

        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        if (resourceStores == null || resourceStores.size() < 1) {
            throw new IllegalArgumentException("请求报文中未包含物品信息");
        }

        //校验 物品是否存在
        for (int resIndex = 0; resIndex < resourceStores.size(); resIndex++) {


            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setStoreId(reqJson.getString("storeId"));
            resourceStoreDto.setResId(resourceStores.getJSONObject(resIndex).getString("resId"));
            resourceStoreDto.setShId(resourceStores.getJSONObject(resIndex).getString("shId"));
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);

            Assert.listOnlyOne(resourceStoreDtos, "未包含 物品信息");

            int stockA = Integer.parseInt(resourceStoreDtos.get(0).getStock());
            int stockB = Integer.parseInt(resourceStores.getJSONObject(resIndex).getString("curStock"));

            if (stockA < stockB) {
                throw new IllegalArgumentException("库存不足");
            }
            resourceStores.getJSONObject(resIndex).put("resName", resourceStoreDtos.get(0).getResName());
            resourceStores.getJSONObject(resIndex).put("stockA", stockA);
        }
        reqJson.put("resourceStores", resourceStores);
//        reqJson.put("resName", resourceStoreDtos.get(0).getResName());
//        reqJson.put("stockA", stockA);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = new AllocationStorehouseApplyPo();
        allocationStorehouseApplyPo.setApplyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId));
        allocationStorehouseApplyPo.setApplyCount("0");
        allocationStorehouseApplyPo.setRemark(reqJson.getString("remark"));
        allocationStorehouseApplyPo.setStartUserId(reqJson.getString("userId"));
        allocationStorehouseApplyPo.setStartUserName(reqJson.getString("userName"));
        allocationStorehouseApplyPo.setStoreId(reqJson.getString("storeId"));
        allocationStorehouseApplyPo.setState(AllocationStorehouseDto.STATE_APPLY);

        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        JSONObject resObj = null;
        for (int resIndex = 0; resIndex < resourceStores.size(); resIndex++) {
            resObj = resourceStores.getJSONObject(resIndex);
            AllocationStorehousePo allocationStorehousePo = new AllocationStorehousePo();
            allocationStorehousePo.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
            allocationStorehousePo.setApplyId(allocationStorehouseApplyPo.getApplyId());
            allocationStorehousePo.setResId(resObj.getString("resId"));
            allocationStorehousePo.setResName(resObj.getString("resName"));
            allocationStorehousePo.setShIda(resObj.getString("shId"));
            allocationStorehousePo.setShIdz(resObj.getString("shzId"));
            allocationStorehousePo.setState(AllocationStorehouseDto.STATE_APPLY);
            allocationStorehousePo.setStoreId(reqJson.getString("storeId"));
            allocationStorehousePo.setStock(resObj.getString("curStock"));
            allocationStorehousePo.setOriginalStock(resObj.getString("stock"));
            allocationStorehousePo.setRemark(reqJson.getString("remark"));
            allocationStorehousePo.setStartUserId(reqJson.getString("userId"));
            allocationStorehousePo.setStartUserName(reqJson.getString("userName"));
            super.insert(context, allocationStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE);

            // 减去去库存
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(resObj.getString("resId"));
            resourceStorePo.setStoreId(reqJson.getString("storeId"));
            resourceStorePo.setShId(resObj.getString("shId"));
            int stockA = Integer.parseInt(resObj.getString("stock"));//调拨数量
            int stockB = Integer.parseInt(resObj.getString("curStock"));//现有库存
            resourceStorePo.setStock((stockA - stockB) + "");
            super.update(context, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE);
            int oldCurStore = Integer.parseInt(allocationStorehouseApplyPo.getApplyCount());
            oldCurStore += Integer.parseInt(resObj.getString("curStock"));
            allocationStorehouseApplyPo.setApplyCount(oldCurStore + "");
        }
        super.insert(context, allocationStorehouseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE_APPLY);
        commit(context);

        ResponseEntity<String> responseEntity = context.getResponseEntity();

        //开始流程
        if (HttpStatus.OK == responseEntity.getStatusCode()) {
            AllocationStorehouseApplyDto allocationStorehouseDto = BeanConvertUtil.covertBean(allocationStorehouseApplyPo, AllocationStorehouseApplyDto.class);
            allocationStorehouseDto.setCurrentUserId(reqJson.getString("userId"));
            allocationStorehouseUserInnerServiceSMOImpl.startProcess(allocationStorehouseDto);
        }
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAllocationStorehouseConstant.ADD_ALLOCATIONSTOREHOUSE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
