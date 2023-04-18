package com.java110.dto.machine;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description IOT同步错误日志记录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MachineTranslateErrorDto extends PageDto implements Serializable {

    public static final String REQ_TYPE_URL = "2222";

    private String reqHeader;
    private String machineTranslateId;
    private String resBody;
    private String reqBody;
    private String logId;
    private String communityId;

    private String reqPath;
    private String reqType;


    private Date createTime;

    private String statusCd = "0";


    public String getReqHeader() {
        return reqHeader;
    }

    public void setReqHeader(String reqHeader) {
        this.reqHeader = reqHeader;
    }

    public String getMachineTranslateId() {
        return machineTranslateId;
    }

    public void setMachineTranslateId(String machineTranslateId) {
        this.machineTranslateId = machineTranslateId;
    }

    public String getResBody() {
        return resBody;
    }

    public void setResBody(String resBody) {
        this.resBody = resBody;
    }

    public String getReqBody() {
        return reqBody;
    }

    public void setReqBody(String reqBody) {
        this.reqBody = reqBody;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public String getReqPath() {
        return reqPath;
    }

    public void setReqPath(String reqPath) {
        this.reqPath = reqPath;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }
}
