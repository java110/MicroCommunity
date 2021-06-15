package com.java110.api.listener.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.smallWeChat.ISmallWechatAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeSmallWechatAttrConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存微信属性侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateSmallWechatAttrListener")
public class UpdateSmallWechatAttrListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ISmallWechatAttrBMO smallWechatAttrBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "value", "请求报文中未包含value");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        smallWechatAttrBMOImpl.updateSmallWechatAttr(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeSmallWechatAttrConstant.UPDATE_SMALLWECHATATTR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
