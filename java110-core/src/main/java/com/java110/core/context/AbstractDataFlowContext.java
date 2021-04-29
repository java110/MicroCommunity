package com.java110.core.context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.entity.center.DataFlowLinksCost;
import com.java110.entity.center.DataFlowLog;
import org.springframework.http.ResponseEntity;

import java.util.*;

/**
 * 数据流上下文
 * Created by wuxw on 2018/5/18.
 */
public abstract class AbstractDataFlowContext extends AbstractTransactionLog implements DataFlowContext, IOrders, TransactionLog {

    private String dataFlowId;

    private String businessType;

    //交易流水
    private String transactionId;


    private String requestTime;

    private String orderTypeCd;

    private String responseTime;

    private JSONArray resBusiness;

    private JSONArray serviceBusiness;


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

    protected Map<String, String> requestHeaders = new HashMap<String, String>();
    protected Map<String, String> requestCurrentHeaders = new HashMap<String, String>();
    protected Map<String, String> responseHeaders = new HashMap<String, String>();
    protected Map<String, String> responseCurrentHeaders = new HashMap<String, String>();

    //请求开始时间
    private Date startDate;

    //请求完成时间
    private Date endDate;

    /**
     * 构建 对象信息
     *
     * @param reqInfo
     * @param headerAll
     * @return
     * @throws Exception
     */
    public <T> T builder(String reqInfo, Map<String, String> headerAll) throws Exception {
        //预处理
        preBuilder(reqInfo, headerAll);
        //调用builder
        T dataFlowContext = (T) doBuilder(reqInfo, headerAll);
        //后处理
        afterBuilder((DataFlowContext) dataFlowContext);
        return dataFlowContext;
    }


    /**
     * 预处理
     *
     * @param reqInfo
     * @param headerAll
     */
    protected void preBuilder(String reqInfo, Map<String, String> headerAll) {
        super.preBuilder(reqInfo, headerAll);
    }

    /**
     * 构建对象
     *
     * @param reqInfo
     * @param headerAll
     * @return
     * @throws Exception
     */
    public abstract DataFlowContext doBuilder(String reqInfo, Map<String, String> headerAll) throws Exception;

    protected void afterBuilder(DataFlowContext dataFlowContext) {

    }

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

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public Map<String, String> getRequestCurrentHeaders() {
        return requestHeaders;
    }

    public Map<String, String> getResponseCurrentHeaders() {
        return responseHeaders;
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
        this.setResData(resJson.toJSONString());
    }

    public List<DataFlowLinksCost> getLinksCostDates() {
        return linksCostDates;
    }


    public List<DataFlowLog> getLogDatas() {
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
     *
     * @param dataFlowLog
     */
    public void addLogDatas(DataFlowLog dataFlowLog) {
        this.logDatas.add(dataFlowLog);
    }

    /**
     * 添加各个环节的耗时
     *
     * @param dataFlowLinksCost
     */
    public void addLinksCostDates(DataFlowLinksCost dataFlowLinksCost) {
        this.linksCostDates.add(dataFlowLinksCost);
    }


    public String getAppId() {
        return null;
    }

    public String getUserId() {
        return null;
    }

    ;

    @Override
    public void setAppId(String appId) {

    }

    @Override
    public void setUserId(String userId) {

    }

    public String getRemark() {
        return null;
    }

    ;

    public String getReqSign() {
        return null;
    }

    ;

    public JSONArray getAttrs() {
        return null;
    }

    ;

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

    public void setPort(String port) {
        this.port = port;
    }

    public String getbId() {
        return null;
    }

    public String getLogId() {
        return getbId();
    }


    /**
     * 业务编码 当前需要处理的业务编码，可以写将要请求服务提供方的方法名
     * 主要用于 日志端展示
     *
     * @return 当前服务编码
     */
    public String getServiceCode() {
        if (this.currentBusiness != null) {
            return currentBusiness.getServiceCode();
        }
        return "";
    }

    /**
     * 业务名称 当前需要处理的业务名称，可以当前调用的业务名称 如 商品购买 等
     * 主要用于 日志端展示
     *
     * @return 当前服务名称
     */
    public String getServiceName() {
        if (this.currentBusiness != null) {
            return currentBusiness.getServiceName();
        }
        return "";
    }


    public abstract IOrders getOrder();

    protected AbstractDataFlowContext(Date startDate, String code) {
        this.setStartDate(startDate);
        this.setCode(code);
    }


    public void setResponseEntity(ResponseEntity responseEntity) {

    }

    public ResponseEntity getResponseEntity() {
        return null;
    }

    public JSONArray getServiceBusiness() {
        return serviceBusiness;
    }

    public void addServiceBusiness(JSONObject serviceBusiness) {
        if (this.serviceBusiness == null) {
            this.serviceBusiness = new JSONArray();
        }
        this.serviceBusiness.add(serviceBusiness);
    }

    public void setServiceBusiness(JSONArray serviceBusinesses) {
        this.serviceBusiness = serviceBusinesses;
    }
}
