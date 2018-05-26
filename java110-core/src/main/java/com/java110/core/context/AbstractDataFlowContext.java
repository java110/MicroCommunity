package com.java110.core.context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.entity.center.DataFlowLinksCost;
import com.java110.entity.center.DataFlowLog;

import java.util.*;

/**
 * 数据流上下文
 * Created by wuxw on 2018/5/18.
 */
public abstract class AbstractDataFlowContext implements DataFlowContext,Orders{

    private String dataFlowId;

    private String businessType;

    //交易流水
    private String transactionId;


    private String requestTime;

    private String orderTypeCd;

    private String responseTime;

    private JSONArray resBusiness;


    private String code;

    private String message;

    private String reqData;

    private JSONObject reqJson;

    private JSONObject resJson;

    private String resData;

    protected List<Business> businesses;

    private Business currentBusiness;

    private List<DataFlowLinksCost> linksCostDates = new ArrayList<DataFlowLinksCost>();

    private List<DataFlowLog> logDatas = new ArrayList<DataFlowLog>();

    protected Map<String,String> headers = new HashMap<String,String>();

    //请求开始时间
    private Date startDate;

    //请求完成时间
    private Date endDate;

    /**
     * 构建 对象信息
     * @param reqInfo
     * @param headerAll
     * @return
     * @throws Exception
     */
    public abstract DataFlowContext builder(String reqInfo, Map<String,String> headerAll) throws Exception;

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getDataFlowId() {
        return dataFlowId;
    }

    public void setDataFlowId(String dataFlowId) {
        this.dataFlowId = dataFlowId;
    }


    public List<Business> getBusinesses() {
        return businesses;
    }


    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public void setOrderTypeCd(String orderTypeCd) {
        this.orderTypeCd = orderTypeCd;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderTypeCd() {
        return orderTypeCd;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
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


    public List<DataFlowLog> getLogDatas(){
        return logDatas;
    }

    public void setResBusiness(JSONArray resBusiness) {
        this.resBusiness = resBusiness;
    }

    public JSONArray getResBusiness() {
        return resBusiness;
    }

    public Business getCurrentBusiness() {
        return currentBusiness;
    }

    public void setCurrentBusiness(Business currentBusiness) {
        this.currentBusiness = currentBusiness;
    }

    /**
     * 添加各个环节的日志
     * @param dataFlowLog
     */
    public void addLogDatas(DataFlowLog dataFlowLog){
        this.logDatas.add(dataFlowLog);
    }

    /**
     * 添加各个环节的耗时
     * @param dataFlowLinksCost
     */
    public void addLinksCostDates(DataFlowLinksCost dataFlowLinksCost){
        this.linksCostDates.add(dataFlowLinksCost);
    }


    public String getAppId(){return null;}

    public String getUserId(){return null;};

    public String getRemark(){return null;};

    public String getReqSign(){return null;};

    public JSONArray getAttrs(){return null;};

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Map<String, Object> getParamOut() {
        return null;
    }

    public String getResData() {
        return resData;
    }

    public void setResData(String resData) {
        this.resData = resData;
    }

    public JSONObject getReqJson() {
        return reqJson;
    }

    public void setReqJson(JSONObject reqJson) {
        this.reqJson = reqJson;
    }

    public void addParamOut(String key, Object value) {

    }

    public String getbId(){
        return null;
    }

    public abstract Orders getOrder();

    protected AbstractDataFlowContext(Date startDate, String code){
        this.setStartDate(startDate);
        this.setCode(code);
    }


}
