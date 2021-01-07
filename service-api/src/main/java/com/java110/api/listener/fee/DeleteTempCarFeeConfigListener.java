package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.tempCarFeeConfig.ITempCarFeeConfigBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeTempCarFeeConfigConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteTempCarFeeConfigListener")
public class DeleteTempCarFeeConfigListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ITempCarFeeConfigBMO tempCarFeeConfigBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "configId", "configId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        tempCarFeeConfigBMOImpl.deleteTempCarFeeConfig(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeTempCarFeeConfigConstant.DELETE_TEMPCARFEECONFIG;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
