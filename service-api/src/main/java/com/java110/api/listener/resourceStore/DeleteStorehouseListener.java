package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.storehouse.IStorehouseBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.community.IResourceStoreServiceSMO;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.utils.constant.ServiceCodeStorehouseConstant;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteStorehouseListener")
public class DeleteStorehouseListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IStorehouseBMO storehouseBMOImpl;

    @Autowired
    private IResourceStoreServiceSMO resourceStoreServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "shId", "shId不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        //获取仓库id
        String shId = reqJson.getString("shId");
        ResourceStorePo resourceStorePo = new ResourceStorePo();
        resourceStorePo.setShId(shId);
        int page = resourceStoreServiceSMOImpl.getResourceStoresCount(resourceStorePo);
        if (page < 1) {
            storehouseBMOImpl.deleteStorehouse(reqJson, context);
        } else {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "该仓库包含物品，不能直接删除！");
            context.setResponseEntity(responseEntity);
            return;
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeStorehouseConstant.DELETE_STOREHOUSE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
