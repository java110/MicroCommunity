package com.java110.dto.payFeeDetailDiscount;

import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeDiscountSpecDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 缴费优惠数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PayFeeDetailDiscountDto extends PageDto implements Serializable {

    private String detailDiscountId;
    private String discountPrice;
    private String detailId;
    private String remark;
    private String communityId;
    private String discountId;
    private String discountName;
    private String discountType;
    private String feeId;
    private String bId;
    private String ruleName;

    private List<FeeDiscountSpecDto> feeDiscountSpecs;


    private Date createTime;

    private String statusCd = "0";


    public String getDetailDiscountId() {
        return detailDiscountId;
    }

    public void setDetailDiscountId(String detailDiscountId) {
        this.detailDiscountId = detailDiscountId;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
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

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
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

    public List<FeeDiscountSpecDto> getFeeDiscountSpecs() {
        return feeDiscountSpecs;
    }

    public void setFeeDiscountSpecs(List<FeeDiscountSpecDto> feeDiscountSpecs) {
        this.feeDiscountSpecs = feeDiscountSpecs;
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

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
