package com.java110.api.listener.system;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.entity.center.AppService;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Java110Listener("transferListener")
public class TransferListener extends AbstractServiceApiListener {
    private final static Logger logger = LoggerFactory.getLogger(TransferListener.class);


    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AppService service = event.getAppService();
        Map<String, String> reqHeader = context.getRequestCurrentHeaders();
        HttpHeaders header = new HttpHeaders();
        for (String key : context.getRequestCurrentHeaders().keySet()) {
            header.add(key, reqHeader.get(key));
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>(reqJson.toJSONString(), header);
        String orgRequestUrl = context.getRequestHeaders().get("REQUEST_URL");


        String requestUrl = service.getUrl() + "/" + reqHeader.get(CommonConstant.HTTP_RESOURCE) + "/" + reqHeader.get(CommonConstant.HTTP_ACTION);

        ResponseEntity responseEntity = null;
        if (!StringUtil.isNullOrNone(orgRequestUrl)) {
            String param = orgRequestUrl.contains("?") ? orgRequestUrl.substring(requestUrl.indexOf("?") + 1, orgRequestUrl.length()) : "";
            requestUrl += ("?" + param);
        }
        try {
            if (CommonConstant.HTTP_METHOD_GET.equals(service.getMethod())) {
                responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_PUT.equals(service.getMethod())) {
                responseEntity = restTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_DELETE.equals(service.getMethod())) {
                responseEntity = restTemplate.exchange(requestUrl, HttpMethod.DELETE, httpEntity, String.class);
            } else {
                responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
            }
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            logger.error("请求下游服务【" + requestUrl + "】异常，参数为" + httpEntity + e.getResponseBodyAsString(), e);
            String body = e.getResponseBodyAsString();

            if(StringUtil.isJsonObject(body)){
                JSONObject bodyObj = JSONObject.parseObject(body);
                if(bodyObj.containsKey("message") && !StringUtil.isEmpty(bodyObj.getString("message"))){
                    body = bodyObj.getString("message");
                }
            }
            responseEntity = new ResponseEntity<String>(body, e.getStatusCode());
        }

        logger.debug("API 服务调用下游服务请求：{}，返回为：{}", httpEntity, responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, String.valueOf(responseEntity.getBody()));
            context.setResponseEntity(responseEntity);

            return;
        }
        if (StringUtils.isEmpty(responseEntity.getBody() + "")) {
            responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "处理失败");
            context.setResponseEntity(responseEntity);
            return;
        }
        JSONObject resParam = JSONObject.parseObject(responseEntity.getBody() + "");
        if (resParam.containsKey("code") && resParam.containsKey("msg")) { // 说明微服务返回的是 resultVo 对象直接返回就可以
            context.setResponseEntity(responseEntity);
            return;
        }
        responseEntity = ResultVo.createResponseEntity(resParam);
        context.setResponseEntity(responseEntity);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SYSTEM_TRANSFER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }
}
