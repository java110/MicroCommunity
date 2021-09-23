package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.DateUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * 页面请求数据封装
 * Created by wuxw on 2018/5/2.
 */
public class PageData implements IPageData, Serializable {


    public PageData() {

        this.setTransactionId(UUID.randomUUID().toString());
    }


    private String userId;

    private String userName;

    private String appId;

    //会话ID
    private String sessionId;

    private String transactionId;

    private String requestTime;

    private String componentCode;

    private String componentMethod;

    private String token;

    private String reqData;

    private String responseTime;

    private String url;

    private String apiUrl;

    private HttpMethod method;
    private Map<String, Object> headers;

    /**
     * 付款方id
     */
    private String payerObjId;

    /**
     * 付款方类型
     */
    private String payerObjType;

    /**
     * 缴费到期时间
     */
    private String endTime;

    private ResponseEntity responseEntity;

    public String getUserId() {
        return userId;
    }

    public String getTransactionId() {
        return transactionId;
    }


    public String getComponentCode() {
        return componentCode;
    }

    public String getComponentMethod() {
        return componentMethod;
    }

    public String getToken() {
        return token;
    }

    public String getReqData() {
        return reqData;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public void setComponentMethod(String componentMethod) {
        this.componentMethod = componentMethod;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    @Override
    public ResponseEntity getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 初始化 PageData
     *
     * @return
     */
    public static IPageData newInstance() {
        return new PageData();
    }

    public IPageData builder(Map param) throws IllegalArgumentException {
        JSONObject reqJson = null;

        return this;
    }

    public IPageData builder(String userId,
                             String userName,
                             String token,
                             String reqData,
                             String componentCode,
                             String componentMethod,
                             String url,
                             String sessionId) {
        return builder(userId,
                userName,
                token,
                reqData,
                componentCode,
                componentMethod,
                url,
                sessionId,
                "",
                null);
    }

    @Override
    public IPageData builder(String userId,
                             String userName,
                             String token,
                             String reqData,
                             String componentCode,
                             String componentMethod,
                             String url,
                             String sessionId,
                             String appId,
                             String payerObjId,
                             String payerObjType,
                             String endTime) throws IllegalArgumentException {
        this.setComponentCode(componentCode);
        this.setComponentMethod(componentMethod);
        this.setReqData(reqData);
        this.setRequestTime(DateUtil.getyyyyMMddhhmmssDateString());
        this.setUserId(userId);
        this.setUserName(userName);
        this.setToken(token);
        this.setUrl(url);
        this.setSessionId(sessionId);
        this.setAppId(appId);
        this.setPayerObjId(payerObjId);
        this.setPayerObjType(payerObjType);
        this.setEndTime(endTime);
        return this;
    }
    public IPageData builder(String userId,
                             String userName,
                             String token,
                             String reqData,
                             String componentCode,
                             String componentMethod,
                             String url,
                             String sessionId,
                             String appId)
            throws IllegalArgumentException {
        return builder(userId,
                userName,
                token,
                reqData,
                componentCode,
                componentMethod,
                url,
                sessionId,
                appId,
                null);
    }


    public IPageData builder(String userId,
                             String userName,
                             String token,
                             String reqData,
                             String componentCode,
                             String componentMethod,
                             String url,
                             String sessionId,
                             String appId,
                             Map<String, Object> headers)
            throws IllegalArgumentException {
        this.setComponentCode(componentCode);
        this.setComponentMethod(componentMethod);
        this.setReqData(reqData);
        this.setRequestTime(DateUtil.getyyyyMMddhhmmssDateString());
        this.setUserId(userId);
        this.setUserName(userName);
        this.setToken(token);
        this.setUrl(url);
        this.setSessionId(sessionId);
        this.setAppId(appId);
        this.setHeaders(headers);

        return this;
    }

    public String toString() {
        return JSONObject.toJSONString(this);
    }

    @Override
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public Map<String, Object> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPayerObjId() {
        return payerObjId;
    }

    public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
    }

    public String getPayerObjType() {
        return payerObjType;
    }

    public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
