package com.java110.dto.fastuser;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FastuserDto
 * @Description 活动数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FastuserDto extends PageDto implements Serializable {

    private String fastUserContext;
    private String fastUserTitle;
    private String userName;
    private String userId;
    private String fastUserId;
    private String startTime;
    private String endTime;
    private String communityId;
    private String state;


    private Date createTime;

    private String statusCd = "0";


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
