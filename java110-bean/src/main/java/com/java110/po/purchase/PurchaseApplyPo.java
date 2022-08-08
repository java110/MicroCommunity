package com.java110.po.purchase;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PurchaseApply
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 20:37
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class PurchaseApplyPo implements Serializable {

    private String applyOrderId;

    private String storeId;
    private String userId;
    private String userName;
    private String description;

    private String resOrderType;
    private String state;
    private String endUserName;
    private String endUserTel;
    private String createTime;
    private String warehousingWay;
    private String createUserId;
    private String createUserName;
    private String communityId;

    private String statusCd = "0";

    private ResourceStorePo[] resourceStores;

    List<PurchaseApplyDetailPo> purchaseApplyDetailPos;

    public String getApplyOrderId() {
        return applyOrderId;
    }

    public void setApplyOrderId(String applyOrderId) {
        this.applyOrderId = applyOrderId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public ResourceStorePo[] getResourceStores() {
        return resourceStores;
    }

    public void setResourceStores(ResourceStorePo[] resourceStores) {
        this.resourceStores = resourceStores;
    }

    public List<PurchaseApplyDetailPo> getPurchaseApplyDetailPos() {
        return purchaseApplyDetailPos;
    }

    public void setPurchaseApplyDetailPos(List<PurchaseApplyDetailPo> purchaseApplyDetailPos) {
        this.purchaseApplyDetailPos = purchaseApplyDetailPos;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
