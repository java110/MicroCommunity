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
    private String purchaseTotalPrice;
    private String endUserName;
    private String endUserTel;
    private List<PurchaseApplyDetailVo> purchaseApplyDetailVo;
    private String description;
    private String staffId;
    private String staffName;
    private String staffTel;
    private String warehousingWay;
    private String createUserId;
    private String createUserName;
    private String unitCodeName;
    private String miniUnitCodeName;
    private String isFixed;
    private String isFixedName;


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

    public String getPurchaseTotalPrice() {
        return purchaseTotalPrice;
    }

    public void setPurchaseTotalPrice(String purchaseTotalPrice) {
        this.purchaseTotalPrice = purchaseTotalPrice;
    }

    public String getWarehousingWay() {
        return warehousingWay;
    }

    public void setWarehousingWay(String warehousingWay) {
        this.warehousingWay = warehousingWay;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUnitCodeName() {
        return unitCodeName;
    }

    public void setUnitCodeName(String unitCodeName) {
        this.unitCodeName = unitCodeName;
    }

    public String getMiniUnitCodeName() {
        return miniUnitCodeName;
    }

    public void setMiniUnitCodeName(String miniUnitCodeName) {
        this.miniUnitCodeName = miniUnitCodeName;
    }

    public String getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(String isFixed) {
        this.isFixed = isFixed;
    }

    public String getIsFixedName() {
        return isFixedName;
    }

    public void setIsFixedName(String isFixedName) {
        this.isFixedName = isFixedName;
    }
}
