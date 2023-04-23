package com.java110.dto.payFeeConfigDiscount;

import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeDiscountSpecDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 费用项折扣数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PayFeeConfigDiscountDto extends PageDto implements Serializable {

    private String configDiscountId;
    private String configId;
    private String discountId;
    private String communityId;

    private String  discountName;
    private String discountType;
    private List<FeeDiscountSpecDto> feeDiscountSpecs;

    private Date createTime;

    private Date startTime;

    private Date endTime;

    private Date payMaxEndTime;

    private Date currentTime;

    private String statusCd = "0";


    public String getConfigDiscountId() {
        return configDiscountId;
    }

    public void setConfigDiscountId(String configDiscountId) {
        this.configDiscountId = configDiscountId;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
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

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public List<FeeDiscountSpecDto> getFeeDiscountSpecs() {
        return feeDiscountSpecs;
    }

    public void setFeeDiscountSpecs(List<FeeDiscountSpecDto> feeDiscountSpecs) {
        this.feeDiscountSpecs = feeDiscountSpecs;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getPayMaxEndTime() {
        return payMaxEndTime;
    }

    public void setPayMaxEndTime(Date payMaxEndTime) {
        this.payMaxEndTime = payMaxEndTime;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }
}
