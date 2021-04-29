package com.java110.api.listener.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.repair.IRepairSettingBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.repair.RepairTypeUserDto;
import com.java110.utils.constant.ServiceCodeRepairSettingConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveRepairSettingListener")
public class SaveRepairSettingListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IRepairSettingBMO repairSettingBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "repairTypeName", "请求报文中未包含repairTypeName");
        Assert.hasKeyAndValue(reqJson, "repairWay", "请求报文中未包含repairWay");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "publicArea", "请求报文中未包含公共区域");
        Assert.hasKeyAndValue(reqJson, "payFeeFlag", "请求报文中未包含收费情况");
        Assert.hasKeyAndValue(reqJson, "returnVisitFlag", "请求报文中未包含回访设置");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        repairSettingBMOImpl.addRepairSetting(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairSettingConstant.ADD_REPAIRSETTING;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
