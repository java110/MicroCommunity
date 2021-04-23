package com.java110.api.listener.property;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

/**
 * @author wux
 * @create 2019-02-10 下午11:11
 * @desc 检查员工是否有物业信息
 **/

@Java110Listener("checkStaffHasPropertyListener")
public class CheckStaffHasPropertyListener extends AbstractServiceApiDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(CheckStaffHasPropertyListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_CHECK_PROPERTY_STAFFHASPROPERTY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {

        logger.debug("ServiceDataFlowEvent : {}",event);

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
       /* Assert.isJsonObject(paramIn,"用户注册请求参数有误，不是有效的json格式 "+paramIn);
        Assert.jsonObjectHaveKey(paramIn,"userId","请求报文中未包含userId 节点请检查 " +paramIn);
        JSONObject paramObj = JSONObject.parseObject(paramIn);*/
        ResponseEntity responseEntity= null;

        HttpHeaders header = new HttpHeaders();
        for(String key : dataFlowContext.getRequestCurrentHeaders().keySet()){
            header.add(key,dataFlowContext.getRequestCurrentHeaders().get(key));
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>(paramIn, header);
        super.doRequest(dataFlowContext, service, httpEntity);
        responseEntity = dataFlowContext.getResponseEntity();
        dataFlowContext.setResponseEntity(responseEntity);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return ;
        }
        String resObj = responseEntity.getBody().toString();

        Assert.isJsonObject(resObj,"下游服务返回格式错误，不是有效json格式"+resObj);

        JSONObject resJson = JSONObject.parseObject(resObj);

        Assert.jsonObjectHaveKey(resJson,"count","下游服务返回格式错误，返回报文中未包含count"+resObj);

        long count = resJson.getLongValue("count");

        if(count < 1){
            throw  new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"当前员工没有相关物业信息，数据异常请检查");
        }

        responseEntity = new ResponseEntity<String>("成功",HttpStatus.OK);


    }

    @Override
    public int getOrder() {
        return 0;
    }
}
