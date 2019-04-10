package com.java110.api.listener.users;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.StringUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataFlowFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.http.*;

import java.util.Map;

/**
 * 查询员工信息
 * Created by Administrator on 2019/4/2.
 */
@Java110Listener("queryStaffByUserNameServiceListener")
public class QueryStaffByUserNameServiceListener extends AbstractServiceApiDataFlowListener {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_STAFF_BY_NAME;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    /**
     *
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {


        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        JSONObject data = dataFlowContext.getReqJson();

        Assert.hasKeyAndValue(data,"page","请求报文中未包含page节点");
        Assert.hasKeyAndValue(data,"rows","请求报文中未包含rows节点");
        Assert.hasKeyAndValue(data,"storeId","请求报文中未包含storeId节点");
        Assert.hasKeyAndValue(data,"name","请求报文中未包含name节点");
        ResponseEntity<String> responseEntity = null;
        //根据名称查询用户信息
        responseEntity = super.callService(dataFlowContext,ServiceCodeConstant.SERVICE_CODE_QUERY_USER_BY_NAME,data);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }

        JSONArray resultInfo = JSONObject.parseObject(responseEntity.getBody().toString()).getJSONArray("users");

        if(resultInfo != null || resultInfo.size() < 1){
            responseEntity = new ResponseEntity<String>(new JSONArray().toJSONString(),HttpStatus.OK);
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }


    }

    /**
     * 查询用户信息
     * @param tmpObj
     */
    private void queryUserInfoByUserId( DataFlowContext dataFlowContext,JSONObject tmpObj,AppService appService){

        String userId = tmpObj.getString("userId");

        if(StringUtil.isEmpty(userId)){
            return ;
        }

        ResponseEntity responseEntity= null;

        String requestUrl = appService.getUrl();
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(),ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);

        //先查询商户服务查询员工userId
        requestUrl = requestUrl + "?userId="+userId;
        dataFlowContext.getRequestHeaders().put("REQUEST_URL",requestUrl);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", header);
        doRequest(dataFlowContext,appService,httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            throw new ListenerExecuteException(1999,"查询用户信息异常 "+responseEntity.getBody());
        }
        tmpObj.putAll(JSONObject.parseObject(responseEntity.getBody().toString()));
    }
}
