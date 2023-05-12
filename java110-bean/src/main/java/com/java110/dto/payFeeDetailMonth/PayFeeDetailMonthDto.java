package com.java110.dto.payFeeDetailMonth;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 月缴费表数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PayFeeDetailMonthDto extends PageDto implements Serializable {

    public static final String STATE_W = "W";//待缴费
    public static final String STATE_C = "C";//已缴费
    public static final String STATE_R = "R";//已退费

    private String detailMonth;
    private String detailYear;
    private String detailId;
    private String receivableAmount;
    private String discountAmount;
    private String remark;
    private String receivedAmount;
    private String communityId;
    private String feeId;
    private String monthId;


    private Date createTime;

    private String statusCd = "0";

    private String objName;
    private String objId;
    private String ownerName;
    private String ownerId;
    private String link;
    private String state;
    private String payFeeTime;

    private String curMonthTime;

    private String feeName;
    private String configId;



    public String getDetailMonth() {
        return detailMonth;
    }

    public void setDetailMonth(String detailMonth) {
        this.detailMonth = detailMonth;
    }

    public String getDetailYear() {
        return detailYear;
    }

    public void setDetailYear(String detailYear) {
        this.detailYear = detailYear;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getMonthId() {
        return monthId;
    }

    public void setMonthId(String monthId) {
        this.monthId = monthId;
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

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPayFeeTime() {
        return payFeeTime;
    }

    public void setPayFeeTime(String payFeeTime) {
        this.payFeeTime = payFeeTime;
    }

    public String getCurMonthTime() {
        return curMonthTime;
    }

    public void setCurMonthTime(String curMonthTime) {
        this.curMonthTime = curMonthTime;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }
}
