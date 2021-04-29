package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineTranslate.IMachineTranslateBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;


import com.java110.core.annotation.Java110Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveMachineTranslateListener")
public class SaveMachineTranslateListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IMachineTranslateBMO machineTranslateBMOImpl;
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "machineId", "必填，请填写设备版本号");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择对象类型");
        Assert.hasKeyAndValue(reqJson, "objName", "必填，请填写设备名称");
        Assert.hasKeyAndValue(reqJson, "objId", "必填，请填写对象Id");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请选择状态");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {



        //添加单元信息
        machineTranslateBMOImpl.addMachineTranslate(reqJson, context);


    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.ADD_MACHINETRANSLATE;
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
