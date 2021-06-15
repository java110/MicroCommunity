package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ownerAttr.IOwnerAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeOwnerAttrConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteOwnerAttrListener")
public class DeleteOwnerAttrListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IOwnerAttrBMO ownerAttrBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ownerAttrBMOImpl.deleteOwnerAttr(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeOwnerAttrConstant.DELETE_OWNERATTR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
