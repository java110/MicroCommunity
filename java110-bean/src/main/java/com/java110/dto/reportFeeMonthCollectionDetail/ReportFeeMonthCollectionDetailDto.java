package com.java110.dto.reportFeeMonthCollectionDetail;

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
public class ReportFeeMonthCollectionDetailDto extends PageDto implements Serializable {

    private String collectionYear;
private String cdId;
private String collectionMonth;
private String detailId;
private String receivableAmount;
private String discountAmount;
private String receivedAmount;
private String communityId;
private String collectionId;


    private Date createTime;

    private String statusCd = "0";


    public String getCollectionYear() {
        return collectionYear;
    }
public void setCollectionYear(String collectionYear) {
        this.collectionYear = collectionYear;
    }
public String getCdId() {
        return cdId;
    }
public void setCdId(String cdId) {
        this.cdId = cdId;
    }
public String getCollectionMonth() {
        return collectionMonth;
    }
public void setCollectionMonth(String collectionMonth) {
        this.collectionMonth = collectionMonth;
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
public String getCollectionId() {
        return collectionId;
    }
public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
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
