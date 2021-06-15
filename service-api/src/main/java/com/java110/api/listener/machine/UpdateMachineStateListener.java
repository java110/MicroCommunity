package com.java110.api.listener.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machine.IMachineBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存设备侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateMachineStateListener")
public class UpdateMachineStateListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IMachineBMO machineBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "machineId", "设备ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写设备状态");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        machineBMOImpl.updateMachineState(reqJson, context);


    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineConstant.UPDATE_MACHINE_STATE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMachineInnerServiceSMO getMachineInnerServiceSMOImpl() {
        return machineInnerServiceSMOImpl;
    }

    public void setMachineInnerServiceSMOImpl(IMachineInnerServiceSMO machineInnerServiceSMOImpl) {
        this.machineInnerServiceSMOImpl = machineInnerServiceSMOImpl;
    }
}
