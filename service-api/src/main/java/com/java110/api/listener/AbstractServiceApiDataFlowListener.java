package com.java110.api.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowListener;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.entity.center.AppService;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.CommunityMemberTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
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
    protected static final int MAX_ROW = 10000;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;


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
        RestTemplate tmpRestTemplate = service.getServiceCode().startsWith("out.") ? outRestTemplate : restTemplate;

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
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
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
     *
     * @param reqJson
     */
    protected void validatePageInfo(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
