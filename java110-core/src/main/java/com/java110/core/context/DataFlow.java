package com.java110.core.context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.AppRoute;
import com.java110.entity.center.Business;


import java.util.*;

/**
 * 主要用于离散成对象 httpApi service 请求过程消息记录和返回记录
 * Created by wuxw on 2018/4/13.
 */
public class DataFlow extends AbstractDataFlowContext {

    private String oId;

    private String appId;

    private String userId;

    private String ip;

    private String remark;

    private String reqSign;

    private JSONObject reqOrders;

    private JSONArray reqBusiness;

    private String resSign;

    private JSONObject resOrders;



    private String requestURL;

    /*private AppRoute appRoute;*/

    private List<AppRoute> appRoutes = new ArrayList<AppRoute>();
    //请求业务系统报文
    private JSONObject requestBusinessJson;

    //业务系统返回报文
    private JSONObject responseBusinessJson;

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    //public DataFlow(){}

    public DataFlow(Date startDate,String code){
        super(startDate,code);
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setReqSign(String reqSign) {
        this.reqSign = reqSign;
    }

    public void setReqOrders(JSONObject reqOrders) {
        this.reqOrders = reqOrders;
    }

    public void setReqBusiness(JSONArray reqBusiness) {
        this.reqBusiness = reqBusiness;
    }

    public void setResSign(String resSign) {
        this.resSign = resSign;
    }

    public void setResOrders(JSONObject resOrders) {
        this.resOrders = resOrders;
    }



    public String getAppId() {
        return appId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRemark() {
        return remark;
    }

    public String getReqSign() {
        return reqSign;
    }

    @Override
    public Orders getOrder() {
        return this;
    }

    public JSONObject getReqOrders() {
        return reqOrders;
    }

    public JSONArray getReqBusiness() {
        return reqBusiness;
    }

    public String getResSign() {
        return resSign;
    }

    public JSONObject getResOrders() {
        return resOrders;
    }



    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }


    public List<AppRoute> getAppRoutes() {
        return appRoutes;
    }

    public void addAppRoutes(AppRoute appRoute) {
        this.appRoutes.add(appRoute);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public JSONObject getRequestBusinessJson() {
        return requestBusinessJson;
    }

    public void setRequestBusinessJson(JSONObject requestBusinessJson) {
        this.requestBusinessJson = requestBusinessJson;
    }

    public JSONObject getResponseBusinessJson() {
        return responseBusinessJson;
    }

    public void setResponseBusinessJson(JSONObject responseBusinessJson) {
        this.responseBusinessJson = responseBusinessJson;
    }

    public DataFlow builderByBusiness(String businessInfo) throws Exception{
        try{
            Business business = null;
            JSONObject reqInfoObj = JSONObject.parseObject(businessInfo);
            this.setReqJson(reqInfoObj);
            this.setDataFlowId(reqInfoObj.containsKey("dataFlowId")?reqInfoObj.getString("dataFlowId"):"-1");
            //this.setAppId(orderObj.getString("appId"));
            this.setTransactionId(reqInfoObj.getString("transactionId"));
            this.setOrderTypeCd(reqInfoObj.getString("orderTypeCd"));
            this.setRequestTime(reqInfoObj.getString("responseTime"));
            this.setBusinessType(reqInfoObj.getString("businessType"));
            //this.setReqOrders(orderObj);
            JSONObject businessObj = new JSONObject();
            businessObj.put("bId",reqInfoObj.getString("bId"));
            businessObj.put("serviceCode",reqInfoObj.getString("serviceCode"));
            JSONArray reqBusinesses = new JSONArray();
            reqBusinesses.add(businessInfo);
            this.setReqBusiness(reqBusinesses);
            JSONObject response = reqInfoObj.getJSONObject("response");
            this.setCode(response.getString("code"));
            this.setMessage(response.getString("message"));
            businessObj.put("response",response);
            this.businesses = new ArrayList<Business>();
            business = new Business().builder(businessObj);
            businesses.add(business);

        }catch (Exception e){

            throw e;
        }
        return this;
    }

    public DataFlow builder(String reqInfo, Map<String,String> headerAll) throws Exception{

        try{
            Business business = null;
            JSONObject reqInfoObj = JSONObject.parseObject(reqInfo);
            JSONObject orderObj = reqInfoObj.getJSONObject("orders");
            JSONArray businessArray = reqInfoObj.getJSONArray("business");
            this.setReqJson(reqInfoObj);
            this.setDataFlowId(orderObj.containsKey("dataFlowId")?orderObj.getString("dataFlowId"):"-1");
            this.setAppId(orderObj.getString("appId"));
            this.setTransactionId(orderObj.getString("transactionId"));
            this.setUserId(orderObj.getString("userId"));
            this.setOrderTypeCd(orderObj.getString("orderTypeCd"));
            this.setRemark(orderObj.getString("remark"));
            this.setReqSign(orderObj.getString("sign"));
            this.setRequestTime(orderObj.getString("requestTime"));
            this.setReqOrders(orderObj);
            this.setReqBusiness(businessArray);
            this.businesses = new ArrayList<Business>();
            if(businessArray != null && businessArray.size() > 0){
                for(int businessIndex = 0;businessIndex < businessArray.size();businessIndex++) {
                    business = new Business().builder(businessArray.getJSONObject(businessIndex));
                    businesses.add(business);
                }
            }

            if (headerAll != null){
                this.headers.putAll(headerAll);
                this.setRequestURL(headers.get("REQUEST_URL"));
                this.setIp(headers.get("IP"));
            }


        }catch (Exception e){

            throw e;
        }
        return this;
    }

}
