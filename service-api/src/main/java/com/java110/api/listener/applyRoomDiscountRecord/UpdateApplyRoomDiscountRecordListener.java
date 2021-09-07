package com.java110.api.listener.applyRoomDiscountRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.applyRoomDiscountRecord.IApplyRoomDiscountRecordBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeApplyRoomDiscountRecordConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存验房记录侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateApplyRoomDiscountRecordListener")
public class UpdateApplyRoomDiscountRecordListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IApplyRoomDiscountRecordBMO applyRoomDiscountRecordBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "ardrId", "ardrId不能为空");
        Assert.hasKeyAndValue(reqJson, "ardId", "请求报文中未包含ardId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "createUserId", "请求报文中未包含createUserId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        applyRoomDiscountRecordBMOImpl.updateApplyRoomDiscountRecord(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeApplyRoomDiscountRecordConstant.UPDATE_APPLYROOMDISCOUNTRECORD;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
