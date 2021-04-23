package com.java110.api.listener.unit;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.unitAttr.IUnitAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeUnitAttrConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存单元属性侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateUnitAttrListener")
public class UpdateUnitAttrListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IUnitAttrBMO unitAttrBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        unitAttrBMOImpl.updateUnitAttr(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeUnitAttrConstant.UPDATE_UNITATTR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
