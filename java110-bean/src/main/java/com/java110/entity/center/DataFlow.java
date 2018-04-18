package com.java110.entity.center;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * 主要用于离散成对象 httpApi service 请求过程消息记录和返回记录
 * Created by wuxw on 2018/4/13.
 */
public class DataFlow {


    private String dataFlowId;

    private String oId;

    //交易流水
    private String transactionId;

    private String appId;

    private String userId;

    private String ip;

    private String orderTypeCd;

    private String requestTime;

    private String remark;

    private String reqSign;

    private JSONObject reqOrders;

    private JSONArray reqBusiness;

    private String responseTime;

    private String resSign;

    private String code;

    private String message;

    private JSONObject resOrders;

    private JSONArray resBusiness;

    //请求开始时间
    private Date startDate;

    //请求完成时间
    private Date endDate;

    private JSONObject reqJson;

    private JSONObject resJson;

    private List<Business> businesses;

    private String requestURL;

    private List<DataFlowLinksCost> linksCostDates = new ArrayList<DataFlowLinksCost>();

    private Map<String,String> headers = new HashMap<String,String>();

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

    public List<Business> getBusinesses() {
        return businesses;
    }

    public String getDataFlowId() {
        return dataFlowId;
    }

    public void setDataFlowId(String dataFlowId) {
        this.dataFlowId = dataFlowId;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }


    //public DataFlow(){}

    public DataFlow(Date startDate,String code){
        this.setStartDate(startDate);
        this.setCode(code);
    }


    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderTypeCd(String orderTypeCd) {
        this.orderTypeCd = orderTypeCd;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
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

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public void setResSign(String resSign) {
        this.resSign = resSign;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResOrders(JSONObject resOrders) {
        this.resOrders = resOrders;
    }

    public void setResBusiness(JSONArray resBusiness) {
        this.resBusiness = resBusiness;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAppId() {
        return appId;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderTypeCd() {
        return orderTypeCd;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public String getRemark() {
        return remark;
    }

    public String getReqSign() {
        return reqSign;
    }

    public JSONObject getReqOrders() {
        return reqOrders;
    }

    public JSONArray getReqBusiness() {
        return reqBusiness;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public String getResSign() {
        return resSign;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public JSONObject getResOrders() {
        return resOrders;
    }

    public JSONArray getResBusiness() {
        return resBusiness;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }



    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /*public AppRoute getAppRoute() {
        return appRoute;
    }

    public void setAppRoute(AppRoute appRoute) {
        this.appRoute = appRoute;
    }*/

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

    public List<DataFlowLinksCost> getLinksCostDates() {
        return linksCostDates;
    }

    /**
     * 添加各个环节的耗时
     * @param dataFlowLinksCost
     */
    public void addLinksCostDates(DataFlowLinksCost dataFlowLinksCost){
        this.linksCostDates.add(dataFlowLinksCost);
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
            this.setAppId(orderObj.getString("transactionId"));
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
