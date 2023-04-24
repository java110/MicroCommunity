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
public class TransactionLogMessageDto extends PageDto implements Serializable {

    private String requestMessage;
private String responseHeader;
private String logId;
private String requestHeader;
private String responseMessage;


    private Date createTime;

    private String statusCd = "0";


    public String getRequestMessage() {
        return requestMessage;
    }
public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
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
}
