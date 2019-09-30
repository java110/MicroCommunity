package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.DateUtil;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * 页面请求数据封装
 * Created by wuxw on 2018/5/2.
 */
public class PageData implements IPageData,Serializable {



    public PageData(){

        this.setTransactionId(UUID.randomUUID().toString());
    }


    private String userId ;

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
     * @return
     */
    public static IPageData newInstance(){
        return new PageData();
    }

    public IPageData builder(Map param) throws IllegalArgumentException{
        JSONObject reqJson = null;

        return this;
    }

    public IPageData builder(String userId,String token,String reqData,String componentCode,String componentMethod,String url,String sessionId)
            throws IllegalArgumentException{
        this.setComponentCode(componentCode);
        this.setComponentMethod(componentMethod);
        this.setReqData(reqData);
        this.setRequestTime(DateUtil.getyyyyMMddhhmmssDateString());
        this.setUserId(userId);
        this.setToken(token);
        this.setUrl(url);
        this.setSessionId(sessionId);

        return this;
    }

    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
