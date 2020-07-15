package com.java110.po.purchase;

import java.io.Serializable;

/**
 * @ClassName PurchaseApplyDetailPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 20:45
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class PurchaseApplyDetailPo implements Serializable {

    private String applyOrderId;
    private String resId;
    private String quantity;
    private String remark;
    private String price;
    private String id;

    private String purchaseQuantity;

    private String purchaseRemark;

    public String getApplyOrderId() {
        return applyOrderId;
    }

    public void setApplyOrderId(String applyOrderId) {
        this.applyOrderId = applyOrderId;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(String purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public String getPurchaseRemark() {
        return purchaseRemark;
    }

    public void setPurchaseRemark(String purchaseRemark) {
        this.purchaseRemark = purchaseRemark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
