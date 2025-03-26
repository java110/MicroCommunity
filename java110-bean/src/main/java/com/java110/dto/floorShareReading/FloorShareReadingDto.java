package com.java110.dto.floorShareReading;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 楼栋抄表数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FloorShareReadingDto extends PageDto implements Serializable {

    public static final String STATE_W = "W";// 待审核
    public static final String STATE_C = "C";// 审核完成
    public static final String STATE_F = "F";// 审核失败
    private String fsmId;
    private String remark;
    private String readingId;
    private String title;
    private String curReadingTime;
    private String createStaffName;
    private String curDegrees;
    private String preDegrees;
    private String auditStaffName;
    private String preReadingTime;
    private String state;
    private String statsMsg;
    private String shareMsg;
    private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getFsmId() {
        return fsmId;
    }

    public void setFsmId(String fsmId) {
        this.fsmId = fsmId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReadingId() {
        return readingId;
    }

    public void setReadingId(String readingId) {
        this.readingId = readingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurReadingTime() {
        return curReadingTime;
    }

    public void setCurReadingTime(String curReadingTime) {
        this.curReadingTime = curReadingTime;
    }

    public String getCreateStaffName() {
        return createStaffName;
    }

    public void setCreateStaffName(String createStaffName) {
        this.createStaffName = createStaffName;
    }

    public String getCurDegrees() {
        return curDegrees;
    }

    public void setCurDegrees(String curDegrees) {
        this.curDegrees = curDegrees;
    }

    public String getPreDegrees() {
        return preDegrees;
    }

    public void setPreDegrees(String preDegrees) {
        this.preDegrees = preDegrees;
    }

    public String getAuditStaffName() {
        return auditStaffName;
    }

    public void setAuditStaffName(String auditStaffName) {
        this.auditStaffName = auditStaffName;
    }

    public String getPreReadingTime() {
        return preReadingTime;
    }

    public void setPreReadingTime(String preReadingTime) {
        this.preReadingTime = preReadingTime;
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

    public String getStatsMsg() {
        return statsMsg;
    }

    public void setStatsMsg(String statsMsg) {
        this.statsMsg = statsMsg;
    }

    public String getShareMsg() {
        return shareMsg;
    }

    public void setShareMsg(String shareMsg) {
        this.shareMsg = shareMsg;
    }
}
