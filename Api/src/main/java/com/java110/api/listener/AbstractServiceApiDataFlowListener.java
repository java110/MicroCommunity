package com.java110.api.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.*;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.StringUtil;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.event.service.api.ServiceDataFlowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2018/11/15.
 */
public abstract class AbstractServiceApiDataFlowListener implements ServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractServiceApiDataFlowListener.class);

    protected static final int DEFAULT_ORDER = 1;
    //默认序列
    protected static final int DEFAULT_SEQ = 1;
    protected static final int MAX_ROW = 50;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate restTemplateNoLoadBalanced;

    /**
     * 调用下游服务
     *
     * @param event
     * @return
     */
    protected ResponseEntity<String> callService(ServiceDataFlowEvent event) {

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        return callService(dataFlowContext, service, dataFlowContext.getReqJson());
    }


    /**
     * 调用下游服务
     *
     * @param context
     * @param serviceCode 下游服务
     * @return
     */
    protected ResponseEntity<String> callService(DataFlowContext context, String serviceCode, Map paramIn) {

        ResponseEntity responseEntity = null;
        AppService appService = DataFlowFactory.getService(context.getAppId(), serviceCode);
        if (appService == null) {
            responseEntity = new ResponseEntity<String>("当前没有权限访问" + ServiceCodeConstant.SERVICE_CODE_QUERY_STORE_USERS, HttpStatus.UNAUTHORIZED);
            context.setResponseEntity(responseEntity);
            return responseEntity;
        }
        return callService(context, appService, paramIn);
    }

    /**
     * 调用下游服务
     *
     * @param context
     * @param appService 下游服务
     * @return
     */
    protected ResponseEntity<String> callService(DataFlowContext context, AppService appService, Map paramIn) {

        ResponseEntity responseEntity = null;
        if (paramIn == null || paramIn.isEmpty()) {
            paramIn = context.getReqJson();
        }

        RestTemplate tmpRestTemplate = appService.getServiceCode().startsWith("out.") ? restTemplateNoLoadBalanced : restTemplate;

        String serviceUrl = appService.getUrl();
        HttpEntity<String> httpEntity = null;
        HttpHeaders header = new HttpHeaders();
        for (String key : context.getRequestCurrentHeaders().keySet()) {
            if (CommonConstant.HTTP_SERVICE.toLowerCase().equals(key.toLowerCase())) {
                continue;
            }
            header.add(key, context.getRequestCurrentHeaders().get(key));
        }
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(), appService.getServiceCode());
        try {
            if (CommonConstant.HTTP_METHOD_GET.equals(appService.getMethod())) {
                serviceUrl += "?";
                for (Object key : paramIn.keySet()) {
                    serviceUrl += (key + "=" + paramIn.get(key) + "&");
                }

                if (serviceUrl.endsWith("&")) {
                    serviceUrl = serviceUrl.substring(0, serviceUrl.lastIndexOf("&"));
                }
                httpEntity = new HttpEntity<String>("", header);
                responseEntity = tmpRestTemplate.exchange(serviceUrl, HttpMethod.GET, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_PUT.equals(appService.getMethod())) {
                httpEntity = new HttpEntity<String>(JSONObject.toJSONString(paramIn), header);
                responseEntity = tmpRestTemplate.exchange(serviceUrl, HttpMethod.PUT, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_DELETE.equals(appService.getMethod())) {
                httpEntity = new HttpEntity<String>(JSONObject.toJSONString(paramIn), header);
                responseEntity = tmpRestTemplate.exchange(serviceUrl, HttpMethod.DELETE, httpEntity, String.class);
            } else {
                httpEntity = new HttpEntity<String>(JSONObject.toJSONString(paramIn), header);
                responseEntity = tmpRestTemplate.exchange(serviceUrl, HttpMethod.POST, httpEntity, String.class);
            }
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>("请求下游系统异常，" + e.getResponseBodyAsString(), e.getStatusCode());
        }
        return responseEntity;
    }


    /**
     * 请求落地方
     *
     * @param dataFlowContext
     * @param service
     * @param httpEntity
     */
    protected void doRequest(DataFlowContext dataFlowContext, AppService service, HttpEntity<String> httpEntity) {

        ResponseEntity responseEntity = null;
        //配置c_service 时请注意 如果是以out 开头的调用外部的地址
        RestTemplate tmpRestTemplate = service.getServiceCode().startsWith("out.") ? restTemplateNoLoadBalanced : restTemplate;

        try {
            if (CommonConstant.HTTP_METHOD_GET.equals(service.getMethod())) {
                String requestUrl = dataFlowContext.getRequestHeaders().get("REQUEST_URL");
                if (!StringUtil.isNullOrNone(requestUrl)) {
                    String param = requestUrl.contains("?") ? requestUrl.substring(requestUrl.indexOf("?") + 1, requestUrl.length()) : "";
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
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>("请求下游系统异常，" + e.getResponseBodyAsString(), e.getStatusCode());
        }

        logger.debug("API 服务调用下游服务请求：{}，返回为：{}", httpEntity, responseEntity);

        dataFlowContext.setResponseEntity(responseEntity);
    }


    /**
     * 处理返回报文信息
     *
     * @param dataFlowContext
     */
    protected void doResponse(DataFlowContext dataFlowContext) {
        ResponseEntity<String> responseEntity = dataFlowContext.getResponseEntity();
        ResponseEntity<String> newResponseEntity = null;
        if (responseEntity == null ||
                responseEntity.getStatusCode() != HttpStatus.OK ||
                StringUtil.isNullOrNone(responseEntity.getBody()) ||
                !Assert.isJsonObject(responseEntity.getBody())) { //这里一般进不去
            return;
        }
        JSONObject resJson = JSONObject.parseObject(responseEntity.getBody());

        if (!resJson.containsKey("orders")
                || !ResponseConstant.RESULT_CODE_SUCCESS.equals(resJson.getJSONObject("orders").getJSONObject("response").getString("code"))) {
            return;
        }

        if (resJson.containsKey("business") && resJson.getJSONArray("business").size() == 1) {
            JSONObject busiJson = resJson.getJSONArray("business").getJSONObject(0);
            if (busiJson.containsKey("orderTypeCd")) {
                busiJson.remove("orderTypeCd");
            }
            if (busiJson.containsKey("serviceCode")) {
                busiJson.remove("serviceCode");
            }
            if (busiJson.containsKey("response")) {
                busiJson.remove("response");
            }
            if (busiJson.containsKey("bId")) {
                busiJson.remove("bId");
            }

            if (busiJson.containsKey("businessType")) {
                busiJson.remove("businessType");
            }

            if (busiJson.containsKey("dataFlowId")) {
                busiJson.remove("dataFlowId");
            }
            //这个一般是 center服务和下游系统之间交互的流水可以删掉，返回出去也没有啥意义
            if (busiJson.containsKey("transactionId")) {
                busiJson.remove("transactionId");
            }
            //这里不直接把 下游系统返回的头信息直接扔给ResponseEntity 的原因是 下游系统的 header中的 Context-* 信息导致 客户端调用耗时很长，所以做一下处理
            //newResponseEntity = new ResponseEntity<String>(busiJson.toJSONString(),responseEntity.getHeaders(), HttpStatus.OK);
            Map<String, String> headersMap = responseEntity.getHeaders().toSingleValueMap();
            if (headersMap.containsKey("Content-Disposition")) {
                headersMap.remove("Content-Disposition");
            }
            if (headersMap.containsKey("Content-Type")) {
                headersMap.remove("Content-Type");
            }
            if (headersMap.containsKey("Content-Length")) {
                headersMap.remove("Content-Length");
            }
            if (headersMap.containsKey("Accept-Charset")) {
                headersMap.remove("Accept-Charset");
            }
            if (headersMap.containsKey("X-Application-Context")) {
                headersMap.remove("X-Application-Context");
            }

            HttpHeaders header = new HttpHeaders();
            header.setAll(headersMap);
            newResponseEntity = new ResponseEntity<String>(busiJson.toJSONString(), header, HttpStatus.OK);


            dataFlowContext.setResponseEntity(newResponseEntity);
        }
    }


    /**
     * 将rest 协议转为 订单协议
     *
     * @param business
     * @return
     */
    protected JSONObject restToCenterProtocol(JSONObject business, Map<String, String> headers) {

        JSONObject centerProtocol = JSONObject.parseObject("{\"orders\":{},\"business\":[]}");
        freshOrderProtocol(centerProtocol.getJSONObject("orders"), headers);
        centerProtocol.getJSONArray("business").add(business);
        return centerProtocol;
    }

    /**
     * 将rest 协议转为 订单协议
     *
     * @param businesses 多个业务
     * @param headers    订单头信息
     * @return
     */
    protected JSONObject restToCenterProtocol(JSONArray businesses, Map<String, String> headers) {

        JSONObject centerProtocol = JSONObject.parseObject("{\"orders\":{},\"business\":[]}");
        freshOrderProtocol(centerProtocol.getJSONObject("orders"), headers);
        centerProtocol.put("business", businesses);
        return centerProtocol;
    }

    /**
     * 刷入order信息
     *
     * @param orders  订单信息
     * @param headers 头部信息
     */
    protected void freshOrderProtocol(JSONObject orders, Map<String, String> headers) {
        for (String key : headers.keySet()) {

            if (CommonConstant.HTTP_APP_ID.equals(key)) {
                orders.put("appId", headers.get(key));
            }
            if (CommonConstant.HTTP_TRANSACTION_ID.equals(key)) {
                orders.put("transactionId", headers.get(key));
            }
            if (CommonConstant.HTTP_SIGN.equals(key)) {
                orders.put("sign", headers.get(key));
            }

            if (CommonConstant.HTTP_REQ_TIME.equals(key)) {
                orders.put("requestTime", headers.get(key));
            }
            if (CommonConstant.HTTP_ORDER_TYPE_CD.equals(key)) {
                orders.put("orderTypeCd", headers.get(key));
            }
            if (CommonConstant.HTTP_USER_ID.equals(key)) {
                orders.put("userId", headers.get(key));
            }
        }

    }

    /**
     * 刷入order信息
     *
     * @param httpHeaders http 头信息
     * @param headers     头部信息
     */
    protected void freshHttpHeader(HttpHeaders httpHeaders, Map<String, String> headers) {
        for (String key : headers.keySet()) {

            if (CommonConstant.HTTP_APP_ID.equals(key)) {
                httpHeaders.add("app_id", headers.get(key));
            }
            if (CommonConstant.HTTP_TRANSACTION_ID.equals(key)) {
                httpHeaders.add("transaction_id", headers.get(key));
            }

            if (CommonConstant.HTTP_REQ_TIME.equals(key)) {
                httpHeaders.add("req_time", headers.get(key));
            }

            if (CommonConstant.HTTP_USER_ID.equals(key)) {
                httpHeaders.add("user_id", headers.get(key));
            }
        }

    }

    /**
     * 校验小区和业主是否有关系
     *
     * @param paramInJson                  请求报文
     * @param communityInnerServiceSMOImpl 小区内部调用接口
     */
    protected void communityHasOwner(JSONObject paramInJson, ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberId(paramInJson.getString("ownerId"));
        communityMemberDto.setCommunityId(paramInJson.getString("communityId"));
        communityMemberDto.setStatusCd(StatusConstant.STATUS_CD_VALID);
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.OWNER);
        List<CommunityMemberDto> communityMemberDtoList = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        if (communityMemberDtoList == null || communityMemberDtoList.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "业主和小区存在关系存在异常，请检查");
        }
    }

    /**
     * 分页信息校验
     * @param reqJson
     */
    protected void validatePageInfo(JSONObject reqJson){
        Assert.jsonObjectHaveKey(reqJson, "feeTypeCd", "请求中未包含feeTypeCd信息");
        Assert.jsonObjectHaveKey(reqJson, "roomId", "请求中未包含roomId信息");
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
