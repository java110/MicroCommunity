package com.java110.dto.meterMachine;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 智能水电表明细数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MeterMachineDetailDto extends PageDto implements Serializable {

    public static final String STATE_W ="W"; // 等待抄表
    public static final String STATE_F ="F"; // 抄表失败
    public static final String STATE_C ="C"; // 抄表完成

    private String detailType;
    private String curDegrees;
    private String machineId;
    private String prestoreDegrees;
    private String detailId;
    private String remark;
    private String communityId;
    private String curReadingTime;

    private String state;
    private Date createTime;

    private String statusCd = "0";


    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public String getCurDegrees() {
        return curDegrees;
    }

    public void setCurDegrees(String curDegrees) {
        this.curDegrees = curDegrees;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getPrestoreDegrees() {
        return prestoreDegrees;
    }

    public void setPrestoreDegrees(String prestoreDegrees) {
        this.prestoreDegrees = prestoreDegrees;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCurReadingTime() {
        return curReadingTime;
    }

    public void setCurReadingTime(String curReadingTime) {
        this.curReadingTime = curReadingTime;
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
}
