package com.java110.dto.repair;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 报修派单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RepairUserDto extends PageDto implements Serializable {

    //开始用户
    public static final String REPAIR_EVENT_START_USER = "startUser";
    //审核用户
    public static final String REPAIR_EVENT_AUDIT_USER = "auditUser";

    public static final String REPAIR_EVENT_PAY_USER = "payUser";

    public  static final String STATE_DOING = "10001";// 处理中
    public  static final String STATE_CLOSE = "10002";// 结单
    public  static final String STATE_BACK = "10003";// 退单
    public  static final String STATE_TRANSFER = "10004";// 转单
    public  static final String STATE_SUBMIT = "10005";// 提交



    private String context;
    private String repairId;
    private String[] repairIds;
    private String ruId;
    private String state;
    private String stateName;
    private String communityId;
    private String userId;
    private String userName;

    private String staffId;
    private String staffName;
    private String preStaffId;
    private String preStaffName;
    private Date startTime;
    private Date endTime;
    private String repairEvent;
    private String duration;


    private Date createTime;

    private String statusCd = "0";


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getRuId() {
        return ruId;
    }

    public void setRuId(String ruId) {
        this.ruId = ruId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String[] getRepairIds() {
        return repairIds;
    }

    public void setRepairIds(String[] repairIds) {
        this.repairIds = repairIds;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getPreStaffId() {
        return preStaffId;
    }

    public void setPreStaffId(String preStaffId) {
        this.preStaffId = preStaffId;
    }

    public String getPreStaffName() {
        return preStaffName;
    }

    public void setPreStaffName(String preStaffName) {
        this.preStaffName = preStaffName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRepairEvent() {
        return repairEvent;
    }

    public void setRepairEvent(String repairEvent) {
        this.repairEvent = repairEvent;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
