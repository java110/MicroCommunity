package com.java110.vo.api.fastuser;

import java.io.Serializable;
import java.util.Date;

public class ApiFastuserDataVo implements Serializable {

    private String fastUserContext;
    private String fastUserTitle;
    private String userName;
    private String startTime;
    private String endTime;
    private String fastUserId;
    private Date createTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFastUserContext() {
        return fastUserContext;
    }

    public void setFastUserContext(String fastUserContext) {
        this.fastUserContext = fastUserContext;
    }

    public String getFastUserTitle() {
        return fastUserTitle;
    }

    public void setFastUserTitle(String fastUserTitle) {
        this.fastUserTitle = fastUserTitle;
    }

    public String getFastUserId() {
        return fastUserId;
    }

    public void setFastUserId(String fastUserId) {
        this.fastUserId = fastUserId;
    }
}
