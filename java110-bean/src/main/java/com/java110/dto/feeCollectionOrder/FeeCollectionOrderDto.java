package com.java110.dto.feeCollectionOrder;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 催缴单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeCollectionOrderDto extends PageDto implements Serializable {

    private String orderId;
private String staffName;
private String state;
private String communityId;
private String collectionWay;
private String staffId;
private String remarks;
private String collectionName;


    private Date createTime;

    private String statusCd = "0";


    public String getOrderId() {
        return orderId;
    }
public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
public String getStaffName() {
        return staffName;
    }
public void setStaffName(String staffName) {
        this.staffName = staffName;
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
public String getCollectionWay() {
        return collectionWay;
    }
public void setCollectionWay(String collectionWay) {
        this.collectionWay = collectionWay;
    }
public String getStaffId() {
        return staffId;
    }
public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
public String getRemarks() {
        return remarks;
    }
public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
public String getCollectionName() {
        return collectionName;
    }
public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
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
