package com.java110.dto.log;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 调用外系统日志数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class TransactionOutLogDto extends PageDto implements Serializable {

    public static final String STATE_S = "S";//成功
    public static final String STATE_F = "F";//失败
    // 微信支付
    public static final String LOG_TYPE_WECHAT_PAY = "WECHAT_PAY";
    // 微信
    public static final String LOG_TYPE_WECHAT = "WECHAT";
    // 物联网
    public static final String LOG_TYPE_IOT = "IOT"; // 物联网

    // 物联网
    public static final String LOG_TYPE_SMS = "SMS"; // 短信验证码

    // oss
    public static final String LOG_TYPE_OSS = "OSS"; // OSS
    public static final String LOG_TYPE_DEFAULT = "DEFAULT"; // 默认
    private String requestMessage;
    private String costTime;
    private String requestUrl;
    private String requestMethod;
    private String responseHeader;
    private String logId;
    private String requestHeader;
    private String state;
    private String responseMessage;


    private Date createTime;

    private String statusCd = "0";

    private String logType;

    private String startTime;
    private String endTime;


    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(String responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
