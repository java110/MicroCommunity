package com.java110.vo.api.purchaseApply;

import java.io.Serializable;
import java.util.List;

public class ApiPurchaseApplyDataVo implements Serializable {

    private String applyOrderId;
    private String resOrderType;
    private String state;
    private String userName;
    private String stateName;
    //申请时间
    private String createTime;
    //物品名称是
    private String resourceNames;
    //累计价格
    private String totalPrice;
    private String endUserName;
    private String endUserTel;
    private List<PurchaseApplyDetailVo> purchaseApplyDetailVo;
    private String description;
    private String staffId;
    private String staffName;
    private String staffTel;


    public String getApplyOrderId() {
        return applyOrderId;
    }

    public void setApplyOrderId(String applyOrderId) {
        this.applyOrderId = applyOrderId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getResourceNames() {
        return resourceNames;
    }

    public void setResourceNames(String resourceNames) {
        this.resourceNames = resourceNames;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<PurchaseApplyDetailVo> getPurchaseApplyDetailVo() {
        return purchaseApplyDetailVo;
    }

    public void setPurchaseApplyDetailVo(List<PurchaseApplyDetailVo> purchaseApplyDetailVo) {
        this.purchaseApplyDetailVo = purchaseApplyDetailVo;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResOrderType() {
        return resOrderType;
    }

    public void setResOrderType(String resOrderType) {
        this.resOrderType = resOrderType;
    }

    public String getEndUserName() {
        return endUserName;
    }

    public void setEndUserName(String endUserName) {
        this.endUserName = endUserName;
    }

    public String getEndUserTel() {
        return endUserTel;
    }

    public void setEndUserTel(String endUserTel) {
        this.endUserTel = endUserTel;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffTel() {
        return staffTel;
    }

    public void setStaffTel(String staffTel) {
        this.staffTel = staffTel;
    }
}
