package com.java110.api.listener.task;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.task.ITaskBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeTaskConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveTaskListener")
public class SaveTaskListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ITaskBMO taskBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "taskName", "请求报文中未包含taskName");
        Assert.hasKeyAndValue(reqJson, "templateId", "请求报文中未包含templateId");
        Assert.hasKeyAndValue(reqJson, "taskCron", "请求报文中未包含taskCron");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        taskBMOImpl.addTask(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeTaskConstant.ADD_TASK;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
