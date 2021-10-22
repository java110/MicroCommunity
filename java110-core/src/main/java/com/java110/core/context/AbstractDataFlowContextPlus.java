package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据流上下文
 * Created by wuxw on 2018/5/18.
 */
public abstract class AbstractDataFlowContextPlus implements IDataFlowContextPlus{


    private String dataFlowId;

    /**
     * 请求头信息
     */
    private Map<String,String> reqHeaders;

    /**
     * 请求体信息（只支持json）
     */
    private JSONObject reqJson;

    private String reqData;

    /**
     * 返回头信息
     */
    private Map<String,String> resHeaders;

    /**
     * 返回体信息 （只支持json）
     */
    private JSONObject resJson;



    @Override
    public String getDataFlowId() {
        return dataFlowId;
    }

    public void setDataFlowId(String dataFlowId) {
        this.dataFlowId = dataFlowId;
    }

    @Override
    public Map<String, String> getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(Map<String, String> reqHeaders) {
        if(reqHeaders == null){
            reqHeaders = new HashMap<>();
        }
        this.reqHeaders = reqHeaders;
    }

    @Override
    public JSONObject getReqJson() {
        return reqJson;
    }

    public void setReqJson(JSONObject reqJson) {
        this.reqJson = reqJson;
    }

    @Override
    public Map<String, String> getResHeaders() {
        return resHeaders;
    }

    @Override
    public void setResHeaders(Map<String, String> resHeaders) {
        this.resHeaders = resHeaders;
    }

    @Override
    public JSONObject getResJson() {
        return resJson;
    }

    @Override
    public void setResJson(JSONObject resJson) {
        this.resJson = resJson;
    }


    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }
}
