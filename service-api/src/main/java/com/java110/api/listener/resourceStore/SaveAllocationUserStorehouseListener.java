package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.allocationUserStorehouse.IAllocationUserStorehouseBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAllocationUserStorehouseConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import com.java110.core.annotation.Java110Listener;
import org.springframework.http.ResponseEntity;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveAllocationUserStorehouseListener")
public class SaveAllocationUserStorehouseListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IAllocationUserStorehouseBMO allocationUserStorehouseBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "acceptUserId", "请求报文中未包含acceptUserId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        String userId = reqJson.getString("userId");
        String acceptUserId = reqJson.getString("acceptUserId");
        if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(acceptUserId) && acceptUserId.equals(userId)){
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "物品接受人不能是本人，所以无法进行转赠操作！");
            context.setResponseEntity(responseEntity);
            return;
        }
        allocationUserStorehouseBMOImpl.addAllocationUserStorehouse(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAllocationUserStorehouseConstant.ADD_ALLOCATIONUSERSTOREHOUSE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
