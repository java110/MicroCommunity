package com.java110.entity.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * 数据查询对象
 * Created by wuxw on 2018/4/19.
 */
public class DataQuery {

    //服务编码
    private String serviceCode;

    private String templateKey;

    //请求参数
    private JSONObject requestParams;

    //返回信息
    private JSONObject responseInfo;

    private ServiceSql serviceSql;

    //rest 返回对象
    private ResponseEntity responseEntity;



    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }


    public JSONObject getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(JSONObject requestParams) {
        this.requestParams = requestParams;
    }

    public JSONObject getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(JSONObject responseInfo) {
        this.responseInfo = responseInfo;
    }

    public ServiceSql getServiceSql() {
        return serviceSql;
    }

    public void setServiceSql(ServiceSql serviceSql) {
        this.serviceSql = serviceSql;
    }

    public ResponseEntity getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }

    /**
     * {
     "bId":"12345678",
     "serviceCode": "querycustinfo",
     "serviceName": "查询客户",
     "remark": "备注",
     "datas": {
     "params": {
     //这个做查询时的参数
     }
     //这里是具体业务
     }
     }
     * 构建DataQuery
     * @return
     */
    public DataQuery builder(String businessInfo){
        JSONObject businessInfoObj = JSONObject.parseObject(businessInfo);
        JSONObject currentBusinessInfo = null;
        //这里为了兼容 centerService
        if(businessInfoObj.containsKey("business")){
            if(businessInfoObj.get("business") instanceof JSONObject){
                currentBusinessInfo = (JSONObject) businessInfoObj.get("business");
            }/*else if(businessInfoObj.get("business") instanceof JSONArray){
                currentBusinessInfo =  ((JSONArray) businessInfoObj.get("business")).getJSONObject(0);
            }*/else {
                return null;
            }
        }else{
            currentBusinessInfo = businessInfoObj;
        }
        this.setServiceCode(currentBusinessInfo.getString("serviceCode"));
        this.setRequestParams(currentBusinessInfo.getJSONObject("datas").getJSONObject("params"));
        return this;
    }

    public DataQuery builder(Map<String, Object> headers){
        this.setServiceCode(headers.get("service").toString());
        this.setRequestParams(JSONObject.parseObject(JSONObject.toJSONString(headers.get("params"))));
        return this;
    }
}
