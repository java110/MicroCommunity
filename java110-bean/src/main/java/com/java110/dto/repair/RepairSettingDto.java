package com.java110.dto.repair;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 报修设置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RepairSettingDto extends PageDto implements Serializable {

    public static final String NOTIFY_WAY_SMS = "SMS"; //短信
    public static final String NOTIFY_WAY_WECHAT = "WECHAT"; //微信
    private String repairTypeName;
    private String repairType;
    private String remark;
    private String communityId;
    private String repairWay;
    private String repairWayName;
    private String settingId;
    private String publicArea;
    private String payFeeFlag;
    private String priceScope;
    private String returnVisitFlag;
    private String returnVisitFlagName;
    private String repairSettingType;
    private String repairSettingTypeName;
    private Date createTime;
    private String statusCd = "0";
    private String isShow;

    private String notifyWay;

    public String getRepairTypeName() {
        return repairTypeName;
    }

    public void setRepairTypeName(String repairTypeName) {
        this.repairTypeName = repairTypeName;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
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

    public String getRepairWay() {
        return repairWay;
    }

    public void setRepairWay(String repairWay) {
        this.repairWay = repairWay;
    }

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
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

    public String getRepairWayName() {
        return repairWayName;
    }

    public void setRepairWayName(String repairWayName) {
        this.repairWayName = repairWayName;
    }

    public String getPublicArea() {
        return publicArea;
    }

    public void setPublicArea(String publicArea) {
        this.publicArea = publicArea;
    }

    public String getPayFeeFlag() {
        return payFeeFlag;
    }

    public void setPayFeeFlag(String payFeeFlag) {
        this.payFeeFlag = payFeeFlag;
    }

    public String getPriceScope() {
        return priceScope;
    }

    public void setPriceScope(String priceScope) {
        this.priceScope = priceScope;
    }

    public String getReturnVisitFlag() {
        return returnVisitFlag;
    }

    public void setReturnVisitFlag(String returnVisitFlag) {
        this.returnVisitFlag = returnVisitFlag;
    }

    public String getReturnVisitFlagName() {
        return returnVisitFlagName;
    }

    public void setReturnVisitFlagName(String returnVisitFlagName) {
        this.returnVisitFlagName = returnVisitFlagName;
    }

    public String getRepairSettingType() {
        return repairSettingType;
    }

    public void setRepairSettingType(String repairSettingType) {
        this.repairSettingType = repairSettingType;
    }

    public String getRepairSettingTypeName() {
        return repairSettingTypeName;
    }

    public void setRepairSettingTypeName(String repairSettingTypeName) {
        this.repairSettingTypeName = repairSettingTypeName;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getNotifyWay() {
        return notifyWay;
    }

    public void setNotifyWay(String notifyWay) {
        this.notifyWay = notifyWay;
    }
}
