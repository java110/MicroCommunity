package com.java110.dto.paymentPool;

import com.java110.dto.PageDto;
import com.java110.dto.paymentPoolConfig.PaymentPoolConfigDto;
import com.java110.dto.paymentPoolValue.PaymentPoolValueDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 支付厂家数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PaymentPoolDto extends PageDto implements Serializable {

    public static final String PAY_TYPE_COMMUNITY = "1001";
    public static final String PAY_TYPE_TEMP_CAT = "2002";
    public static final String PAY_TYPE_FEE_CONFIG = "3003";

    private String certPath;
    private String remark;
    private String state;
    private String communityId;
    private String paymentName;
    private String ppId;
    private String paymentType;

    private String paymentTypeName;

    private String payType;

    private String beanJsapi;
    private String beanQrcode;
    private String beanNative;

    private String beanRefund;
    private List<PaymentPoolValueDto> values;

    private List<PaymentPoolConfigDto> configs;


    private Date createTime;

    private String statusCd = "0";


    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPpId() {
        return ppId;
    }

    public void setPpId(String ppId) {
        this.ppId = ppId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
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

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public List<PaymentPoolValueDto> getValues() {
        return values;
    }

    public void setValues(List<PaymentPoolValueDto> values) {
        this.values = values;
    }

    public List<PaymentPoolConfigDto> getConfigs() {
        return configs;
    }

    public void setConfigs(List<PaymentPoolConfigDto> configs) {
        this.configs = configs;
    }

    public String getBeanJsapi() {
        return beanJsapi;
    }

    public void setBeanJsapi(String beanJsapi) {
        this.beanJsapi = beanJsapi;
    }

    public String getBeanQrcode() {
        return beanQrcode;
    }

    public void setBeanQrcode(String beanQrcode) {
        this.beanQrcode = beanQrcode;
    }

    public String getBeanNative() {
        return beanNative;
    }

    public void setBeanNative(String beanNative) {
        this.beanNative = beanNative;
    }

    public String getBeanRefund() {
        return beanRefund;
    }

    public void setBeanRefund(String beanRefund) {
        this.beanRefund = beanRefund;
    }
}
