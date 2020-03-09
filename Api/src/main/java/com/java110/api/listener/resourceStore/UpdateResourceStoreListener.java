package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.resourceStore.IResourceStoreBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.smo.resourceStore.IResourceStoreInnerServiceSMO;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeResourceStoreConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 保存物品管理侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateResourceStoreListener")
public class UpdateResourceStoreListener extends AbstractServiceApiListener {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreBMO resourceStoreBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "resId", "物品ID不能为空");
        Assert.hasKeyAndValue(reqJson, "resName", "必填，请填写物品名称");
        //Assert.hasKeyAndValue(reqJson, "resCode", "必填，请填写物品编码");
        Assert.hasKeyAndValue(reqJson, "price", "必填，请填写物品价格");
        //Assert.hasKeyAndValue(reqJson, "stock", "必填，请填写物品库存");
        //Assert.hasKeyAndValue(reqJson, "description", "必填，请填写描述");
        Assert.hasKeyAndValue(reqJson, "storeId", "商户信息不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        businesses.add(updateResourceStore(reqJson, context));



        ResponseEntity<String> responseEntity = resourceStoreBMOImpl.callService(context, service.getServiceCode(), businesses);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceStoreConstant.UPDATE_RESOURCESTORE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    /**
     * 添加物品管理信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject updateResourceStore(JSONObject paramInJson, DataFlowContext dataFlowContext) {

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

    public IResourceStoreInnerServiceSMO getResourceStoreInnerServiceSMOImpl() {
        return resourceStoreInnerServiceSMOImpl;
    }

    public void setResourceStoreInnerServiceSMOImpl(IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl) {
        this.resourceStoreInnerServiceSMOImpl = resourceStoreInnerServiceSMOImpl;
    }
}
