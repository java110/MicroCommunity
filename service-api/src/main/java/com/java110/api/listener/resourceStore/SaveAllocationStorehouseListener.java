package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.allocationStorehouse.IAllocationStorehouseBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.po.allocationStorehouse.AllocationStorehousePo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeAllocationStorehouseConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveAllocationStorehouseListener")
public class SaveAllocationStorehouseListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IAllocationStorehouseBMO allocationStorehouseBMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "shIda", "请求报文中未包含shIda");
        Assert.hasKeyAndValue(reqJson, "shIdz", "请求报文中未包含shIdz");
        Assert.hasKeyAndValue(reqJson, "resId", "请求报文中未包含resId");
        Assert.hasKeyAndValue(reqJson, "stock", "请求报文中未包含stock");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");

        //校验 物品是否存在

        ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
        resourceStoreDto.setStoreId(reqJson.getString("storeId"));
        resourceStoreDto.setResId(reqJson.getString("resId"));
        resourceStoreDto.setShId(reqJson.getString("shIda"));
        List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);

        Assert.listOnlyOne(resourceStoreDtos, "未包含 物品信息");

        int stockA = Integer.parseInt(resourceStoreDtos.get(0).getStock());
        int stockB = Integer.parseInt(reqJson.getString("stock"));

        if (stockA < stockB) {
            throw new IllegalArgumentException("库存不足");
        }

        reqJson.put("resName", resourceStoreDtos.get(0).getResName());
        reqJson.put("stockA", stockA);


    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        reqJson.put("asId", "-1");
        AllocationStorehousePo allocationStorehousePo = BeanConvertUtil.covertBean(reqJson, AllocationStorehousePo.class);
        allocationStorehousePo.setRemark(reqJson.getString("remark"));
        allocationStorehousePo.setStartUserId(reqJson.getString("userId"));
        allocationStorehousePo.setStartUserName(reqJson.getString("userName"));
        allocationStorehousePo.setState(AllocationStorehouseDto.STATE_AUDIT);
        super.insert(context, allocationStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE);

        // 前去库存

        ResourceStorePo resourceStorePo = new ResourceStorePo();
        resourceStorePo.setResId(reqJson.getString("resId"));
        resourceStorePo.setStoreId(reqJson.getString("storeId"));
        resourceStorePo.setShId(reqJson.getString("shIda"));
        int stockA = Integer.parseInt(reqJson.getString("stockA"));
        int stockB = Integer.parseInt(reqJson.getString("stock"));
        resourceStorePo.setStock((stockA - stockB) + "");
        super.update(context, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE);
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
