package com.java110.entity.service;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 页面请求数据封装
 * Created by wuxw on 2018/5/2.
 */
public class PageData implements Serializable {

    private String transactionId;

    private String requestTime;

    private String method;

    private JSONObject param;

    private JSONObject meta;

    private JSONObject reqJson;

    private JSONObject resJson;

    private String code;

    private String message;

    private String responseTime;

    private JSONObject data;


    public String getTransactionId() {
        return transactionId;
    }

    public PageData setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JSONObject getParam() {
        return param;
    }

    public void setParam(JSONObject param) {
        this.param = param;
    }

    public JSONObject getMeta() {
        return meta;
    }

    public void setMeta(JSONObject meta) {
        this.meta = meta;
    }

    public JSONObject getReqJson() {
        return reqJson;
    }

    public void setReqJson(JSONObject reqJson) {
        this.reqJson = reqJson;
    }

    public JSONObject getResJson() {
        return resJson;
    }

    public void setResJson(JSONObject resJson) {
        this.resJson = resJson;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public PageData builder(String requestJson) throws IllegalArgumentException{
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.parseObject(requestJson);
        }catch (Exception e){
            throw new IllegalArgumentException("请求参数错误",e);
        }
        this.setMeta(reqJson.getJSONObject("meta"));
        this.setMethod(this.getMeta().getString("method"));
        this.setRequestTime(this.getMeta().getString("requestTime"));
        this.setParam(reqJson.getJSONObject("param"));
        this.setReqJson(reqJson);
        return this;
    }
}
