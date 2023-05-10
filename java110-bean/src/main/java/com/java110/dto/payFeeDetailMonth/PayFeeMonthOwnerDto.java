package com.java110.dto.payFeeDetailMonth;

import java.io.Serializable;

/**
 * 缴费月业主数据封装
 * 主要包括：
 * 房屋，合同 ，车辆
 * 业主信息
 * add by wuxw
 */
public class PayFeeMonthOwnerDto implements Serializable {
    private String objName;
    private String objId;
    private String ownerName;
    private String ownerId;
    private String link;
    private String payFeeTime;

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

    public String getPayFeeTime() {
        return payFeeTime;
    }

    public void setPayFeeTime(String payFeeTime) {
        this.payFeeTime = payFeeTime;
    }
}
