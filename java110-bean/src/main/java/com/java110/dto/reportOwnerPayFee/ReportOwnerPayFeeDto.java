package com.java110.dto.reportOwnerPayFee;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 业主缴费明细数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReportOwnerPayFeeDto extends PageDto implements Serializable {

    private String configName;
private String amount;
private String pfId;
private String detailId;
private String objName;
private String ownerId;
private String feeId;
private String pfMonth;
private String ownerName;
private String pfYear;
private String configId;
private String pfDate;
private String objId;
private String feeName;
private String communityId;
private String objType;


    private Date createTime;

    private String statusCd = "0";


    public String getConfigName() {
        return configName;
    }
public void setConfigName(String configName) {
        this.configName = configName;
    }
public String getAmount() {
        return amount;
    }
public void setAmount(String amount) {
        this.amount = amount;
    }
public String getPfId() {
        return pfId;
    }
public void setPfId(String pfId) {
        this.pfId = pfId;
    }
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
public String getObjName() {
        return objName;
    }
public void setObjName(String objName) {
        this.objName = objName;
    }
public String getOwnerId() {
        return ownerId;
    }
public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
public String getFeeId() {
        return feeId;
    }
public void setFeeId(String feeId) {
        this.feeId = feeId;
    }
public String getPfMonth() {
        return pfMonth;
    }
public void setPfMonth(String pfMonth) {
        this.pfMonth = pfMonth;
    }
public String getOwnerName() {
        return ownerName;
    }
public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
public String getPfYear() {
        return pfYear;
    }
public void setPfYear(String pfYear) {
        this.pfYear = pfYear;
    }
public String getConfigId() {
        return configId;
    }
public void setConfigId(String configId) {
        this.configId = configId;
    }
public String getPfDate() {
        return pfDate;
    }
public void setPfDate(String pfDate) {
        this.pfDate = pfDate;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getFeeName() {
        return feeName;
    }
public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getObjType() {
        return objType;
    }
public void setObjType(String objType) {
        this.objType = objType;
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
