package com.java110.po.resourceStoreUseRecord;

import java.io.Serializable;

public class ResourceStoreUseRecordPo implements Serializable {

    private String repairId;
    private String unitPrice;
    private String createUserId;
    private String quantity;
    private String rsurId;
    private String createUserName;
    private String remark;
    private String storeId;
    private String resId;
    private String communityId;
    private String resName;
    private String resourceStoreName;
    //1001 报废回收   2002 工单损耗   3003 公用损耗
    private String state;

    private String statusCd = "0";

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRsurId() {
        return rsurId;
    }

    public void setRsurId(String rsurId) {
        this.rsurId = rsurId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResourceStoreName() {
        return resourceStoreName;
    }

    public void setResourceStoreName(String resourceStoreName) {
        this.resourceStoreName = resourceStoreName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
