package com.java110.entity.center;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 主要用于离散成对象 httpApi service 请求过程消息记录和返回记录
 * Created by wuxw on 2018/4/13.
 */
public class DataFlow {

    private String oId;

    //交易流水
    private String transactionId;

    private String appId;

    private String userId;

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

    private String reqJson;

    private String resJson;

    private List<Business> businesses;

    private String requestURL;

    private List<DataFlowLinksCost> linksCostDatas = new ArrayList<DataFlowLinksCost>();

    private Map<String,String> headers = new HashMap<String,String>();

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public List<Business> getBusinesses() {
        return businesses;
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

    public String getReqJson() {
        return reqJson;
    }

    public void setReqJson(String reqJson) {
        this.reqJson = reqJson;
    }

    public String getResJson() {
        return resJson;
    }

    public void setResJson(String resJson) {
        this.resJson = resJson;
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

    /**
     * 添加各个环节的耗时
     * @param dataFlowLinksCost
     */
    public void addLinksCostDatas(DataFlowLinksCost dataFlowLinksCost){
        this.linksCostDatas.add(dataFlowLinksCost);
    }

    public DataFlow builder(String reqInfo, HttpServletRequest request) throws Exception{

        try{
            Business business = null;
            JSONObject reqInfoObj = JSONObject.parseObject(reqInfo);
            JSONObject orderObj = reqInfoObj.getJSONObject("orders");
            JSONArray businessArray = reqInfoObj.getJSONArray("business");
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

            //将url参数写到header map中
            initUrlParam(request);

            //将 header 写到header map中
            initHeadParam(request);
        }catch (Exception e){
            throw e;
        }
        return this;
    }


    /**
     * 将url参数写到header map中
     * @param request
     */
    private void initUrlParam(HttpServletRequest request) {
		/*put real ip address*/



        Map readOnlyMap = request.getParameterMap();

        StringBuffer queryString = new StringBuffer(request.getRequestURL()!=null?request.getRequestURL():"");

        if (readOnlyMap != null && !readOnlyMap.isEmpty()) {
            queryString.append("?");
            Set<String> keys = readOnlyMap.keySet();
            for (Iterator it = keys.iterator(); it.hasNext();) {
                String key = (String) it.next();
                String[] value = (String[]) readOnlyMap.get(key);
//                String[] value = (String[]) readOnlyMap.get(key);
                if(value.length>1) {
                    headers.put(key, value[0]);
                    for(int j =0 ;j<value.length;j++){
                        queryString.append(key);
                        queryString.append("=");
                        queryString.append(value[j]);
                        queryString.append("&");
                    }

                } else {
                    headers.put(key, value[0]);
                    queryString.append(key);
                    queryString.append("=");
                    queryString.append(value[0]);
                    queryString.append("&");
                }
            }
        }

		/*put requst url*/
        if (readOnlyMap != null && !readOnlyMap.isEmpty()){
            this.setRequestURL(queryString.toString().substring(0, queryString.toString().length() - 1));
        }else{
            this.setRequestURL(queryString.toString());
        }

    }

    private void initHeadParam(HttpServletRequest request) {

        headers.put("IP",getIpAddr(request));

        Enumeration reqHeaderEnum = request.getHeaderNames();

        while( reqHeaderEnum.hasMoreElements() ) {
            String headerName = (String)reqHeaderEnum.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
    }

    /**
     * 获取IP地址
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
