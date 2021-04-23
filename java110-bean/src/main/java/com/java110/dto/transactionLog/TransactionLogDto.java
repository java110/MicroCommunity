package com.java110.dto.transactionLog;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 交互日志数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class TransactionLogDto extends PageDto implements Serializable {

    private String srcIp;
private String serviceCode;
private String costTime;
private String ip;
private String appId;
private String logId;
private String state;
private String userId;
private String transactionId;
private String timestamp;


    private Date createTime;

    private String statusCd = "0";


    public String getSrcIp() {
        return srcIp;
    }
public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }
public String getServiceCode() {
        return serviceCode;
    }
public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
public String getCostTime() {
        return costTime;
    }
public void setCostTime(String costTime) {
        this.costTime = costTime;
    }
public String getIp() {
        return ip;
    }
public void setIp(String ip) {
        this.ip = ip;
    }
public String getAppId() {
        return appId;
    }
public void setAppId(String appId) {
        this.appId = appId;
    }
public String getLogId() {
        return logId;
    }
public void setLogId(String logId) {
        this.logId = logId;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getUserId() {
        return userId;
    }
public void setUserId(String userId) {
        this.userId = userId;
    }
public String getTransactionId() {
        return transactionId;
    }
public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
public String getTimestamp() {
        return timestamp;
    }
public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
}
