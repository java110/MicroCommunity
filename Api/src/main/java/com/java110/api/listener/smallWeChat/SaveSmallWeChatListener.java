package com.java110.api.listener.smallWeChat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.smallWeChat.ISmallWeChatBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeSmallWeChatConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存小程序配置
 */
@Java110Listener("saveSmallWeChatListener")
public class SaveSmallWeChatListener extends AbstractServiceApiListener {

    @Autowired
    private ISmallWeChatBMO smallWeChatBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        Assert.hasKeyAndValue(reqJson, "appId", "请求报文中未包含appId");
        Assert.hasKeyAndValue(reqJson, "appSecret", "请求报文中未包含appSecret");
        Assert.hasKeyAndValue(reqJson, "payPassword", "请求报文中未包含payPassword");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        AppService service = event.getAppService();
        businesses.add(smallWeChatBMOImpl.addSmallWeChat(reqJson, context));
        ResponseEntity<String> responseEntity = smallWeChatBMOImpl.callService(context, service.getServiceCode(), businesses);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeSmallWeChatConstant.ADD_SMALL_WE_CHAT;
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
