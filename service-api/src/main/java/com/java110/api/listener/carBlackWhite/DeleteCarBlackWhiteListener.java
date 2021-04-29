package com.java110.api.listener.carBlackWhite;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.carBlackWhite.ICarBlackWhiteBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;

import com.java110.core.annotation.Java110Listener;
import com.java110.utils.constant.ServiceCodeCarBlackWhiteConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteCarBlackWhiteListener")
public class DeleteCarBlackWhiteListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ICarBlackWhiteBMO carBlackWhiteBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写车牌号");

        Assert.hasKeyAndValue(reqJson, "bwId", "黑白名单ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        carBlackWhiteBMOImpl.deleteCarBlackWhite(reqJson, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeCarBlackWhiteConstant.DELETE_CARBLACKWHITE;
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
