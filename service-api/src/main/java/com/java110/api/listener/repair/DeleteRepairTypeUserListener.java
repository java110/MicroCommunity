package com.java110.api.listener.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.repair.IRepairTypeUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeRepairTypeUserConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteRepairTypeUserListener")
public class DeleteRepairTypeUserListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IRepairTypeUserBMO repairTypeUserBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "typeUserId", "typeUserId不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        repairTypeUserBMOImpl.deleteRepairTypeUser(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairTypeUserConstant.DELETE_REPAIRTYPEUSER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
