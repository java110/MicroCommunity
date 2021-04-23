package com.java110.api.listener.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ownerRepair.IOwnerRepairBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeOwnerRepairConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存业主报修侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateOwnerRepairListener")
public class UpdateOwnerRepairListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IOwnerRepairBMO ownerRepairBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "repairId", "报修ID不能为空");
        Assert.hasKeyAndValue(reqJson, "repairType", "必填，请选择报修类型");
        Assert.hasKeyAndValue(reqJson, "repairName", "必填，请填写报修人名称");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写报修人手机号");
        Assert.hasKeyAndValue(reqJson, "repairObjType", "必填，请填写报修对象类型");
        Assert.hasKeyAndValue(reqJson, "repairObjId", "必填，请填写报修对象ID");
        Assert.hasKeyAndValue(reqJson, "repairObjName", "必填，请填写报修对象名称");
        Assert.hasKeyAndValue(reqJson, "appointmentTime", "必填，请填写预约时间");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写报修内容");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写报修状态");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ownerRepairBMOImpl.updateOwnerRepair(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeOwnerRepairConstant.UPDATE_OWNERREPAIR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
