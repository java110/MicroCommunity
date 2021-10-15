package com.java110.dto.carInoutPayment;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 车辆支付数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CarInoutPaymentDto extends PageDto implements Serializable {

    private String realCharge;
private String inoutId;
private String payType;
private String paymentId;
private String paId;
private String communityId;
private String payCharge;


    private Date createTime;

    private String statusCd = "0";


    public String getRealCharge() {
        return realCharge;
    }
public void setRealCharge(String realCharge) {
        this.realCharge = realCharge;
    }
public String getInoutId() {
        return inoutId;
    }
public void setInoutId(String inoutId) {
        this.inoutId = inoutId;
    }
public String getPayType() {
        return payType;
    }
public void setPayType(String payType) {
        this.payType = payType;
    }
public String getPaymentId() {
        return paymentId;
    }
public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
public String getPaId() {
        return paId;
    }
public void setPaId(String paId) {
        this.paId = paId;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getPayCharge() {
        return payCharge;
    }
public void setPayCharge(String payCharge) {
        this.payCharge = payCharge;
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
