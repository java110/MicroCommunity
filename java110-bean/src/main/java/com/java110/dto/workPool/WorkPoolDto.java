package com.java110.dto.workPool;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 工作单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class WorkPoolDto extends PageDto implements Serializable {

    private String workCycle;
private String createUserId;
private String createUserName;
private String storeId;
private String workName;
private String workId;
private String wtId;
private String createUserTel;
private String startTime;
private String endTime;
private String state;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getWorkCycle() {
        return workCycle;
    }
public void setWorkCycle(String workCycle) {
        this.workCycle = workCycle;
    }
public String getCreateUserId() {
        return createUserId;
    }
public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
public String getCreateUserName() {
        return createUserName;
    }
public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getWorkName() {
        return workName;
    }
public void setWorkName(String workName) {
        this.workName = workName;
    }
public String getWorkId() {
        return workId;
    }
public void setWorkId(String workId) {
        this.workId = workId;
    }
public String getWtId() {
        return wtId;
    }
public void setWtId(String wtId) {
        this.wtId = wtId;
    }
public String getCreateUserTel() {
        return createUserTel;
    }
public void setCreateUserTel(String createUserTel) {
        this.createUserTel = createUserTel;
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
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
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
}