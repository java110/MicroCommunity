package com.java110.api.listener.@@templateCode@@;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.@@templateCode@@.I@@TemplateCode@@BMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.ServiceCode@@TemplateCode@@Constant;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.java110.core.annotation.Java110Listener;
/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("save@@TemplateCode@@Listener")
public class Save@@TemplateCode@@Listener extends AbstractServiceApiPlusListener {

    @Autowired
    private I@@TemplateCode@@BMO @@templateCode@@BMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        @@validateTemplateColumns@@
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        @@templateCode@@BMOImpl.add@@TemplateCode@@(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCode@@TemplateCode@@Constant.ADD_@@TEMPLATECODE@@;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
