package com.java110.api.listener.returnPayFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.fee.IFeeBMO;
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
 * 保存退费表侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateReturnPayFeeListener")
public class UpdateReturnPayFeeListener extends AbstractServiceApiListener {

    @Autowired
    private IReturnPayFeeBMO returnPayFeeBMOImpl;
    @Autowired
    private IFeeBMO feeBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "returnFeeId", "returnFeeId不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "state不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        AppService service = event.getAppService();
        businesses.add(returnPayFeeBMOImpl.updateReturnPayFee(reqJson, context));
        //退费审核通过
        if("1100".equals(reqJson.getString("state"))){
            reqJson.put("state","1300");
            businesses.add(returnPayFeeBMOImpl.addFeeDetail(reqJson, context));
            reqJson.put("state","1100");
            String cycles = (String) reqJson.get("cycles");
            String receivableAmount = (String) reqJson.get("receivableAmount");
            String receivedAmount = (String) reqJson.get("receivedAmount");
            reqJson.put("cycles",cycles.split("-")[1]);
            reqJson.put("receivableAmount",receivableAmount.split("-")[1]);
            reqJson.put("receivedAmount",receivedAmount.split("-")[1]);
            reqJson.put("createTime",reqJson.get("payTime"));
            businesses.add(returnPayFeeBMOImpl.updateFeeDetail(reqJson, context));
        }
        //不通过
        if("1200".equals(reqJson.getString("state"))){
            reqJson.put("state","1200");
            businesses.add(returnPayFeeBMOImpl.updateFeeDetail(reqJson, context));
        }



        ResponseEntity<String> responseEntity = returnPayFeeBMOImpl.callService(context, service.getServiceCode(), businesses);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeReturnPayFeeConstant.UPDATE_RETURNPAYFEE;
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
