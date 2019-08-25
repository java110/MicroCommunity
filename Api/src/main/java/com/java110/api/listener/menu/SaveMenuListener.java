package com.java110.api.listener.menu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ServiceCodeMenuConstant;




import com.java110.core.annotation.Java110Listener;
/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveMenuListener")
public class SaveMenuListener extends AbstractServiceApiListener {
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

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
        return ServiceCodeMenuConstant.ADD_MENU;
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
