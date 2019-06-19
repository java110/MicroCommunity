package com.java110.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 费用配置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeConfigDto extends PageDto implements Serializable {

    private String squarePrice;
private String feeTypeCd;
private String configId;
private String additionalAmount;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getSquarePrice() {
        return squarePrice;
    }
public void setSquarePrice(String squarePrice) {
        this.squarePrice = squarePrice;
    }
public String getFeeTypeCd() {
        return feeTypeCd;
    }
public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
    }
public String getConfigId() {
        return configId;
    }
public void setConfigId(String configId) {
        this.configId = configId;
    }
public String getAdditionalAmount() {
        return additionalAmount;
    }
public void setAdditionalAmount(String additionalAmount) {
        this.additionalAmount = additionalAmount;
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
}
