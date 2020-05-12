package com.java110.api.listener.returnPayFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.returnPayFee.IReturnPayFeeBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeReturnPayFeeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveReturnPayFeeListener")
public class SaveReturnPayFeeListener extends AbstractServiceApiListener {

    @Autowired
    private IReturnPayFeeBMO returnPayFeeBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含configId");
        Assert.hasKeyAndValue(reqJson, "feeId", "请求报文中未包含feeId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "detailId", "请求报文中未包含detailId");
        Assert.hasKeyAndValue(reqJson, "reason", "请求报文中未包含reason");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        businesses.add(returnPayFeeBMOImpl.addReturnPayFee(reqJson, context));
        reqJson.put("state","1000");
        businesses.add(returnPayFeeBMOImpl.updateFeeDetail(reqJson, context));

        ResponseEntity<String> responseEntity = returnPayFeeBMOImpl.callService(context, service.getServiceCode(), businesses);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeReturnPayFeeConstant.ADD_RETURNPAYFEE;
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
