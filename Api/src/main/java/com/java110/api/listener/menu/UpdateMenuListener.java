package com.java110.api.listener.menu;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.constant.ServiceCodeMenuConstant;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.http.HttpMethod;

/**
 * 编辑菜单侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateMenuListener")
public class UpdateMenuListener extends AbstractServiceApiListener {
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "mId", "菜单ID不能为空");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写菜单名称");
        Assert.hasKeyAndValue(reqJson, "url", "必填，请菜单菜单地址");
        Assert.hasKeyAndValue(reqJson, "seq", "必填，请填写序列");
        Assert.hasKeyAndValue(reqJson, "isShow", "必填，请选择是否显示菜单");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMenuConstant.UPDATE_MENU;
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
