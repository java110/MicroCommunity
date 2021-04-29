package com.java110.api.listener.resourceStoreUseRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.resourceStoreUseRecord.IResourceStoreUseRecordBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeResourceStoreUseRecordConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.core.annotation.Java110Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteResourceStoreUseRecordListener")
public class DeleteResourceStoreUseRecordListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IResourceStoreUseRecordBMO resourceStoreUseRecordBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "rsurId", "rsurId不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        resourceStoreUseRecordBMOImpl.deleteResourceStoreUseRecord(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceStoreUseRecordConstant.DELETE_RESOURCESTOREUSERECORD;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
