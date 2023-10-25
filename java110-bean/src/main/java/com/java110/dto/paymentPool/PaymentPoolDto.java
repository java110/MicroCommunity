package com.java110.dto.paymentPool;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 支付厂家数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PaymentPoolDto extends PageDto implements Serializable {

    private String certPath;
private String remark;
private String state;
private String communityId;
private String paymentName;
private String ppId;
private String paymentType;


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
}
