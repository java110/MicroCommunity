package com.java110.dto.payFeeQrcode;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 支付二维码数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PayFeeQrcodeDto extends PageDto implements Serializable {

    private String createStaffName;
    private String queryWay;
    private String createStaffId;
    private String preFee;
    private String customFee;
    private String pfqId;
    private String qrcodeName;
    private String communityId;
    private String smsValidate;
    private String content;

    private String qrCodeUrl;


    private Date createTime;

    private String state;

    private String statusCd = "0";

    private String feeType;


    public String getCreateStaffName() {
        return createStaffName;
    }

    public void setCreateStaffName(String createStaffName) {
        this.createStaffName = createStaffName;
    }

    public String getQueryWay() {
        return queryWay;
    }

    public void setQueryWay(String queryWay) {
        this.queryWay = queryWay;
    }

    public String getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(String createStaffId) {
        this.createStaffId = createStaffId;
    }

    public String getPreFee() {
        return preFee;
    }

    public void setPreFee(String preFee) {
        this.preFee = preFee;
    }

    public String getCustomFee() {
        return customFee;
    }

    public void setCustomFee(String customFee) {
        this.customFee = customFee;
    }

    public String getPfqId() {
        return pfqId;
    }

    public void setPfqId(String pfqId) {
        this.pfqId = pfqId;
    }

    public String getQrcodeName() {
        return qrcodeName;
    }

    public void setQrcodeName(String qrcodeName) {
        this.qrcodeName = qrcodeName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSmsValidate() {
        return smsValidate;
    }

    public void setSmsValidate(String smsValidate) {
        this.smsValidate = smsValidate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
}
