package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.order.OrderDto;
import com.java110.utils.constant.CommonConstant;
import com.java110.entity.center.AppRoute;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 主要用于离散成对象 httpApi service 请求过程消息记录和返回记录
 * Created by wuxw on 2018/4/13.
 */
public class ApiDataFlow extends AbstractDataFlowContext {

    private String appId;

    private String ip;

    private String apiCurrentService;

    private String userId;

    private String reqSign;

    private String resSign;

    private String requestURL;


    private List<AppRoute> appRoutes = new ArrayList<AppRoute>();
    //请求业务系统报文
    private JSONObject requestBusinessJson;

    //业务系统返回报文
    private JSONObject responseBusinessJson;
    //rest 返回对象
    private ResponseEntity responseEntity;





    public ApiDataFlow(Date startDate, String code){
        super(startDate,code);
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }



    public void setReqSign(String reqSign) {
        this.reqSign = reqSign;
    }



    public void setResSign(String resSign) {
        this.resSign = resSign;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getAppId() {
        return appId;
    }

    public String getUserId() {
        return userId;
    }

    public String getReqSign() {
        return reqSign;
    }

    @Override
    public IOrders getOrder() {
        return this;
    }

    public String getResSign() {
        return resSign;
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

    public ApiDataFlow doBuilder(String reqInfo, Map<String,String> headerAll) throws Exception{

        try{
            JSONObject reqInfoObj = JSONObject.parseObject(reqInfo);

            this.setReqData(reqInfo);
            this.setReqJson(reqInfoObj);
            this.setDataFlowId("-1");
            this.setAppId(headerAll.get(CommonConstant.HTTP_APP_ID));
            this.setTransactionId(headerAll.get(CommonConstant.HTTP_TRANSACTION_ID));
            this.setReqSign(headerAll.get(CommonConstant.HTTP_SIGN));
            this.setRequestTime(headerAll.get(CommonConstant.HTTP_REQ_TIME));
            this.setUserId(headerAll.get(CommonConstant.HTTP_USER_ID));

            if (headerAll.containsKey(CommonConstant.APP_ID)) {
                this.setAppId(headerAll.get(CommonConstant.APP_ID));
            }
            if (headerAll.containsKey(CommonConstant.TRANSACTION_ID)) {
                this.setTransactionId(headerAll.get(CommonConstant.TRANSACTION_ID));
            }
            if (headerAll.containsKey(CommonConstant.REQUEST_TIME)) {
                this.setRequestTime(headerAll.get(CommonConstant.REQUEST_TIME));
            }
            if (headerAll.containsKey(CommonConstant.USER_ID)) {
                this.setUserId(headerAll.get(CommonConstant.USER_ID));
            }

            if (headerAll != null){
                this.requestHeaders.putAll(headerAll);
                this.requestCurrentHeaders.putAll(headerAll);
                this.setRequestURL(requestHeaders.get("REQUEST_URL"));
                this.setIp(requestHeaders.get("IP"));
            }


        }catch (Exception e){

            throw e;
        }
        return this;
    }

    /**
     * 透传时构建对象
     * @param reqInfo
     * @param headerAll
     * @return
     * @throws Exception
     */
    public ApiDataFlow builderTransfer(String reqInfo, Map<String,String> headerAll) throws Exception{

        try{
            this.setReqData(reqInfo);
            this.setDataFlowId("-1");
            this.setAppId(headerAll.get(CommonConstant.HTTP_APP_ID));
            this.setTransactionId(headerAll.get(CommonConstant.HTTP_TRANSACTION_ID));
            this.setReqSign(headerAll.get(CommonConstant.HTTP_SIGN));
            this.setRequestTime(headerAll.get(CommonConstant.HTTP_REQ_TIME));

            if (headerAll != null){
                this.requestHeaders.putAll(headerAll);
                this.requestCurrentHeaders.putAll(headerAll);
                this.setRequestURL(requestHeaders.get("REQUEST_URL"));
                this.setIp(requestHeaders.get("IP"));
            }


        }catch (Exception e){

            throw e;
        }
        return this;
    }

    @Override
    public String getServiceCode() {
        return apiCurrentService;
    }

    public void setApiCurrentService(String apiCurrentService) {
        this.apiCurrentService = apiCurrentService;
    }


    @Override
    public void setResponseEntity(ResponseEntity responseEntity){
        this.responseEntity = responseEntity;
    }


    public ResponseEntity getResponseEntity(){
        return responseEntity;
    }
}
