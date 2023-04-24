package com.java110.dto.supplier;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 优惠购买数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class SupplierCouponBuyDto extends PageDto implements Serializable {

    private String createUserId;
private String supplierId;
private String quantity;
private String createUserName;
private String remark;
private String objName;
private String couponId;
private String storeId;
private String valuePrice;
private String createUserTel;
private String name;
private String objId;
private String buyId;
private String storeName;


    private Date createTime;

    private String statusCd = "0";


    public String getCreateUserId() {
        return createUserId;
    }
public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
public String getSupplierId() {
        return supplierId;
    }
public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
public String getQuantity() {
        return quantity;
    }
public void setQuantity(String quantity) {
        this.quantity = quantity;
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
public String getObjName() {
        return objName;
    }
public void setObjName(String objName) {
        this.objName = objName;
    }
public String getCouponId() {
        return couponId;
    }
public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getValuePrice() {
        return valuePrice;
    }
public void setValuePrice(String valuePrice) {
        this.valuePrice = valuePrice;
    }
public String getCreateUserTel() {
        return createUserTel;
    }
public void setCreateUserTel(String createUserTel) {
        this.createUserTel = createUserTel;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getBuyId() {
        return buyId;
    }
public void setBuyId(String buyId) {
        this.buyId = buyId;
    }
public String getStoreName() {
        return storeName;
    }
public void setStoreName(String storeName) {
        this.storeName = storeName;
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
