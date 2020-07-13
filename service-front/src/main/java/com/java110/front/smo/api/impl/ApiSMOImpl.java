package com.java110.front.smo.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.api.IApiSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service("apiSMOImpl")
public class ApiSMOImpl extends BaseComponentSMO implements IApiSMO {


    private final static Logger logger = LoggerFactory.getLogger(ApiSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected ComponentValidateResult validateStoreStaffCommunityRelationship(IPageData pd, RestTemplate restTemplate) {
        // 校验 员工和商户是否有关系
        ResponseEntity responseEntity = getStoreInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return new ComponentValidateResult("", "", "", pd.getUserId(), pd.getUserName());
        }

        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeId", "根据用户ID查询商户ID失败，未包含storeId节点");
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeTypeCd", "根据用户ID查询商户类型失败，未包含storeTypeCd节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        String communityId = "";
        if (paramIn != null && paramIn.containsKey("communityId") && !StringUtil.isEmpty(paramIn.getString("communityId"))) {
            communityId = paramIn.getString("communityId");
            checkStoreEnterCommunity(pd, storeId, storeTypeCd, communityId, restTemplate);
        }
        return new ComponentValidateResult(storeId, storeTypeCd, communityId, pd.getUserId(), pd.getUserName());
    }

    @Override
    public ResponseEntity<String> doApi(String body, Map<String, String> headers, HttpServletRequest request) {
        HttpHeaders header = new HttpHeaders();
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);

        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);
        header.add("user-id", result.getUserId());
        header.add("user-name", result.getUserName());
        header.add("store-id", result.getStoreId());
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
