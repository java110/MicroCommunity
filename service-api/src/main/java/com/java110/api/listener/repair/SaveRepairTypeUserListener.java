package com.java110.api.listener.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.repair.IRepairTypeUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.repair.RepairTypeUserDto;
import com.java110.utils.constant.ServiceCodeRepairTypeUserConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveRepairTypeUserListener")
public class SaveRepairTypeUserListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IRepairTypeUserBMO repairTypeUserBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含userId");
        Assert.hasKeyAndValue(reqJson, "userName", "请求报文中未包含userName");
        Assert.hasKeyAndValue(reqJson, "repairType", "请求报文中未包含repairType");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        reqJson.put("state", RepairTypeUserDto.STATE_ONLINE);

        repairTypeUserBMOImpl.addRepairTypeUser(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairTypeUserConstant.ADD_REPAIRTYPEUSER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
