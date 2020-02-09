package com.java110.api.listener.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataFlowFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

/**
 * 修改员工 2018年12月6日
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("modifyStaffServiceListener")
public class ModifyStaffServiceListener extends AbstractServiceApiDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(ModifyStaffServiceListener.class);



    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_USER_STAFF_MODIFY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public int getOrder() {
        return 0;
    }


    /**
     * 添加员工信息
     *
     *
     *
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        //获取数据上下文对象
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
        Assert.isJsonObject(paramIn,"添加员工时请求参数有误，不是有效的json格式 "+paramIn);
        JSONObject paramInJson = JSONObject.parseObject(paramIn);
        Assert.jsonObjectHaveKey(paramInJson,"userId","请求参数中未包含userId 节点，请确认");
        JSONArray businesses = new JSONArray();
        //判断请求报文中包含 userId 并且 不为-1时 将已有用户添加为员工，反之，则添加用户再将用户添加为员工
        JSONObject staffBusiness = modifyStaff(paramInJson,dataFlowContext);
        businesses.add(staffBusiness);

        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_USER_ID,paramInJson.getString("userId"));
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD,"D");

        String paramInObj = super.restToCenterProtocol(businesses,dataFlowContext.getRequestCurrentHeaders()).toJSONString();

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header,dataFlowContext.getRequestCurrentHeaders());

        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj, header);
        //http://user-service/test/sayHello
        super.doRequest(dataFlowContext, service, httpEntity);

        super.doResponse(dataFlowContext);
    }



    private JSONObject modifyStaff(JSONObject paramObj,DataFlowContext dataFlowContext){
        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(paramObj,"name","请求参数中未包含name 节点，请确认");
        Assert.jsonObjectHaveKey(paramObj,"tel","请求参数中未包含tel 节点，请确认");


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_MODIFY_USER_INFO);
        business.put(CommonConstant.HTTP_SEQ,1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL,CommonConstant.HTTP_INVOKE_MODEL_S);

        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessUser",builderStaffInfo(paramObj,dataFlowContext));

        return business;
    }

    /**
     *  构建员工信息
     * @param paramObj
     * @param dataFlowContext
     * @return
     */
    private JSONObject builderStaffInfo(JSONObject paramObj, DataFlowContext dataFlowContext) {

        //首先根据员工ID查询员工信息，根据员工信息修改相应的数据
        ResponseEntity responseEntity= null;
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);
        if(appService == null){
            throw new ListenerExecuteException(1999,"当前没有权限访问"+ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);

        }
        String requestUrl = appService.getUrl() + "?userId="+paramObj.getString("userId");
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(),ServiceCodeConstant.SERVICE_CODE_QUERY_USER_USERINFO);
        dataFlowContext.getRequestHeaders().put("REQUEST_URL",requestUrl);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", header);
        doRequest(dataFlowContext,appService,httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            dataFlowContext.setResponseEntity(responseEntity);
        }

        JSONObject userInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        userInfo.putAll(paramObj);

        return userInfo;
    }

}
