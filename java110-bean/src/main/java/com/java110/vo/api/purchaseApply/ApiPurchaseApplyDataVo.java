package com.java110.vo.api.purchaseApply;

import java.io.Serializable;
import java.util.Date;

public class ApiPurchaseApplyDataVo implements Serializable {

    private String applyOrderId;
    private String state;
    private String userName;
    //申请时间
    private Date createTime;
    //物品名称是
    private String resourceNames;
    //累计价格
    private String totalPrice;


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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
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
}
