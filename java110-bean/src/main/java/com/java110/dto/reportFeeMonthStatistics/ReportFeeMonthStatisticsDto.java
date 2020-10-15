package com.java110.dto.reportFeeMonthStatistics;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 费用月统计数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReportFeeMonthStatisticsDto extends PageDto implements Serializable {

    private String receivableAmount;
private String statisticsId;
private String updateTime;
private String remark;
private String objName;
private String receivedAmount;
private String feeYear;
private String feeMonth;
private String feeId;
private String configId;
private String objId;
private String feeName;
private String oweAmount;
private String communityId;
private String feeCreateTime;
private String objType;


    private Date createTime;

    private String statusCd = "0";


    public String getReceivableAmount() {
        return receivableAmount;
    }
public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }
public String getStatisticsId() {
        return statisticsId;
    }
public void setStatisticsId(String statisticsId) {
        this.statisticsId = statisticsId;
    }
public String getUpdateTime() {
        return updateTime;
    }
public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getObjName() {
        return objName;
    }
public void setObjName(String objName) {
        this.objName = objName;
    }
public String getReceivedAmount() {
        return receivedAmount;
    }
public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }
public String getFeeYear() {
        return feeYear;
    }
public void setFeeYear(String feeYear) {
        this.feeYear = feeYear;
    }
public String getFeeMonth() {
        return feeMonth;
    }
public void setFeeMonth(String feeMonth) {
        this.feeMonth = feeMonth;
    }
public String getFeeId() {
        return feeId;
    }
public void setFeeId(String feeId) {
        this.feeId = feeId;
    }
public String getConfigId() {
        return configId;
    }
public void setConfigId(String configId) {
        this.configId = configId;
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
public String getOweAmount() {
        return oweAmount;
    }
public void setOweAmount(String oweAmount) {
        this.oweAmount = oweAmount;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getFeeCreateTime() {
        return feeCreateTime;
    }
public void setFeeCreateTime(String feeCreateTime) {
        this.feeCreateTime = feeCreateTime;
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
