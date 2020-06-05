package com.java110.api.bmo.resourceStore.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.resourceStore.IResourceStoreBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.store.IResourceStoreInnerServiceSMO;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ResourceStoreBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:40
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("resourceStoreBMOImpl")
public class ResourceStoreBMOImpl extends ApiBaseBMO implements IResourceStoreBMO {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteResourceStore(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        ResourceStorePo resourceStorePo = BeanConvertUtil.covertBean(paramInJson, ResourceStorePo.class);
        super.delete(dataFlowContext, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_RESOURCE_STORE);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addResourceStore(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessResourceStore = new JSONObject();
        businessResourceStore.putAll(paramInJson);
        businessResourceStore.put("resId", "-1");
        businessResourceStore.put("stock", "0");
        ResourceStorePo resourceStorePo = BeanConvertUtil.covertBean(businessResourceStore, ResourceStorePo.class);
        super.insert(dataFlowContext, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_STORE);
    }

    /**
     * 添加物品管理信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateResourceStore(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
        resourceStoreDto.setResId(paramInJson.getString("resId"));
        resourceStoreDto.setStoreId(paramInJson.getString("storeId"));

        List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);

        Assert.isOne(resourceStoreDtos, "查询到多条物品 或未查到物品，resId=" + resourceStoreDto.getResId());

        JSONObject businessResourceStore = new JSONObject();
        businessResourceStore.putAll(paramInJson);
        businessResourceStore.put("stock", resourceStoreDtos.get(0).getStock());
        ResourceStorePo resourceStorePo = BeanConvertUtil.covertBean(businessResourceStore, ResourceStorePo.class);
        super.update(dataFlowContext, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE);
    }
}
