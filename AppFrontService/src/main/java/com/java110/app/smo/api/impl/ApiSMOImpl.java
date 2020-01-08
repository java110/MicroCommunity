package com.java110.app.smo.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.app.smo.api.IApiSMO;
import com.java110.core.component.BaseComponentSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service("apiSMOImpl")
public class ApiSMOImpl extends BaseComponentSMO implements IApiSMO {


    private final static Logger logger = LoggerFactory.getLogger(ApiSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> doApi(String body, Map<String, String> headers) {

        logger.debug("api请求头" + headers + ";请求内容：" + body);
        HttpMethod method = null;
        String url = ServiceConstant.SERVICE_API_URL + "/api/" + headers.get(CommonConstant.HTTP_SERVICE);
        if (CommonConstant.HTTP_METHOD_POST.equals(headers.get(CommonConstant.HTTP_METHOD))) {
            method = HttpMethod.POST;
        } else if (CommonConstant.HTTP_METHOD_GET.equals(headers.get(CommonConstant.HTTP_METHOD))) {
            method = HttpMethod.GET;
            url += super.mapToUrlParam(JSONObject.parseObject(body));
        } else if (CommonConstant.HTTP_METHOD_DELETE.equals(headers.get(CommonConstant.HTTP_METHOD))) {
            method = HttpMethod.DELETE;
        } else if (CommonConstant.HTTP_METHOD_PUT.equals(headers.get(CommonConstant.HTTP_METHOD))) {
            method = HttpMethod.PUT;

        } else {
            throw new IllegalArgumentException("不支持的请求方式" + headers.get(CommonConstant.HTTP_METHOD));
        }

        HttpHeaders header = new HttpHeaders();
        for (String key : headers.keySet()
        ) {
            header.add(key, headers.get(key));
        }

        HttpEntity<String> httpEntity = new HttpEntity<String>(body, header);
        logger.debug("请求后端url" + url);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, method, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("api返回信息" + responseEntity);
            return responseEntity;
        }
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
