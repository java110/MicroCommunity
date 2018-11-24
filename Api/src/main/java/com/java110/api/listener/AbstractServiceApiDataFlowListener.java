package com.java110.api.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.CommonConstant;
import com.java110.common.util.StringUtil;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by wuxw on 2018/11/15.
 */
public abstract class AbstractServiceApiDataFlowListener implements ServiceDataFlowListener {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate restTemplateNoLoadBalanced;

    /**
     * 请求落地方
     * @param dataFlowContext
     * @param service
     * @param httpEntity
     */
    protected void doRequest(DataFlowContext dataFlowContext, AppService service, HttpEntity<String> httpEntity) {

        ResponseEntity responseEntity= null;
        //配置c_service 时请注意 如果是以out 开头的调用外部的地址
        RestTemplate tmpRestTemplate = service.getServiceCode().startsWith("out.")?restTemplateNoLoadBalanced:restTemplate;

        try {
            if (CommonConstant.HTTP_METHOD_GET.equals(service.getMethod())) {
                String requestUrl = dataFlowContext.getRequestHeaders().get("REQUEST_URL");
                if (!StringUtil.isNullOrNone(requestUrl)) {
                    String param = requestUrl.contains("?") ? requestUrl.substring(requestUrl.indexOf("?")+1, requestUrl.length()) : "";
                    if (service.getUrl().contains("?")) {
                        requestUrl = service.getUrl() + "&" + param;
                    } else {
                        requestUrl = service.getUrl() + "?" + param;
                    }
                }
                responseEntity = tmpRestTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_PUT.equals(service.getMethod())) {
                responseEntity = tmpRestTemplate.exchange(service.getUrl(), HttpMethod.PUT, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_DELETE.equals(service.getMethod())) {
                String requestUrl = dataFlowContext.getRequestHeaders().get("REQUEST_URL");
                if (!StringUtil.isNullOrNone(requestUrl)) {
                    String param = requestUrl.contains("?") ? requestUrl.substring(requestUrl.indexOf("?"), requestUrl.length()) : "";
                    if (service.getUrl().contains("?")) {
                        requestUrl = service.getUrl() + "&" + param;
                    } else {
                        requestUrl = service.getUrl() + "?" + param;
                    }
                }
                responseEntity = tmpRestTemplate.exchange(requestUrl, HttpMethod.DELETE, httpEntity, String.class);
            } else {
                responseEntity = tmpRestTemplate.exchange(service.getUrl(), HttpMethod.POST, httpEntity, String.class);
            }
        }catch (HttpServerErrorException e){ //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>("请求下游系统异常异常，"+e.getResponseBodyAsString(),e.getStatusCode());

        }

        dataFlowContext.setResponseEntity(responseEntity);
    }


    /**
     * 将rest 协议转为 订单协议
     * @param business
     * @return
     */
    protected JSONObject restToCenterProtocol(JSONObject business, Map<String,String> headers){

        JSONObject centerProtocol = JSONObject.parseObject("{\"orders\":{},\"business\":[]}");
        freshOrderProtocol(centerProtocol.getJSONObject("orders"),headers);
        centerProtocol.getJSONArray("business").add(business);
        return centerProtocol;
    }

    /**
     * 刷入order信息
     * @param orders 订单信息
     * @param headers 头部信息
     */
    protected void freshOrderProtocol(JSONObject orders, Map<String, String> headers) {
        for(String key : headers.keySet()){

            if(CommonConstant.HTTP_APP_ID.equals(key)) {
                orders.put("appId", headers.get(key));
            }
            if(CommonConstant.HTTP_TRANSACTION_ID.equals(key)) {
                orders.put("transactionId", headers.get(key));
            }
            if(CommonConstant.HTTP_SIGN.equals(key)) {
                orders.put("sign", headers.get(key));
            }

            if(CommonConstant.HTTP_REQ_TIME.equals(key)) {
                orders.put("requestTime", headers.get(key));
            }
            if(CommonConstant.HTTP_ORDER_TYPE_CD.equals(key)){
                orders.put("orderTypeCd",headers.get(key));
            }
            if(CommonConstant.HTTP_USER_ID.equals(key)){
                orders.put("userId",headers.get(key));
            }
        }

    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestTemplate getRestTemplateNoLoadBalanced() {
        return restTemplateNoLoadBalanced;
    }

    public void setRestTemplateNoLoadBalanced(RestTemplate restTemplateNoLoadBalanced) {
        this.restTemplateNoLoadBalanced = restTemplateNoLoadBalanced;
    }
}
