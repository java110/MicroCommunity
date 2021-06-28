package com.java110.po.owner;

import java.io.Serializable;

/**
 * @ClassName RepairUserPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 14:17
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class RepairUserPo implements Serializable {
    private String ruId;
    private String repairId;
    private String userId;
    private String communityId;
    private String state;
    private String context;
    private String staffId;
    private String staffName;
    private String preStaffId;
    private String preStaffName;
    private String preRuId;
    private String startTime;
    private String endTime;
    private String repairEvent;
    private String createTime;
    private String bId;
    private String statusCd = "0";
    private String flag = "0";

    public String getRuId() {
        return ruId;
    }

    public void setRuId(String ruId) {
        this.ruId = ruId;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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

    public String getRepairEvent() {
        return repairEvent;
    }

    public void setRepairEvent(String repairEvent) {
        this.repairEvent = repairEvent;
    }

    public String getPreRuId() {
        return preRuId;
    }

    public void setPreRuId(String preRuId) {
        this.preRuId = preRuId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
