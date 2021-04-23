package com.java110.api.listener.machineRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineRecord.IMachineRecordBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineRecordConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteMachineRecordListener")
public class DeleteMachineRecordListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IMachineRecordBMO machineRecordBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "machineRecordId", "开门记录ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        machineRecordBMOImpl.deleteMachineRecord(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineRecordConstant.DELETE_MACHINERECORD;
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
