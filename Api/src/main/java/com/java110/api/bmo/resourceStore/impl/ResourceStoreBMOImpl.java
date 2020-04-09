package com.java110.api.bmo.resourceStore.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.resourceStore.IResourceStoreBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.resourceStore.IResourceStoreInnerServiceSMO;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
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
    public JSONObject deleteResourceStore(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_RESOURCE_STORE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessResourceStore = new JSONObject();
        businessResourceStore.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessResourceStore", businessResourceStore);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addResourceStore(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_STORE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessResourceStore = new JSONObject();
        businessResourceStore.putAll(paramInJson);
        businessResourceStore.put("resId", "-1");
        businessResourceStore.put("stock", "0");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessResourceStore", businessResourceStore);
        return business;
    }

    /**
     * 添加物品管理信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateResourceStore(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
        resourceStoreDto.setResId(paramInJson.getString("resId"));
        resourceStoreDto.setStoreId(paramInJson.getString("storeId"));

        List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);

        Assert.isOne(resourceStoreDtos, "查询到多条物品 或未查到物品，resId=" + resourceStoreDto.getResId());


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessResourceStore = new JSONObject();
        businessResourceStore.putAll(paramInJson);
        businessResourceStore.put("stock", resourceStoreDtos.get(0).getStock());
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessResourceStore", businessResourceStore);
        return business;
    }
}
