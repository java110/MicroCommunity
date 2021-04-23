package com.java110.vo.api.purchaseApplyDetail;

import java.io.Serializable;
import java.util.Date;

public class ApiPurchaseApplyDetailDataVo implements Serializable {

    private String operate;
private String applyOrderId;
private String statusCd;
private String id;
private String bId;
private String resId;
public String getOperate() {
        return operate;
    }
public void setOperate(String operate) {
        this.operate = operate;
    }
public String getApplyOrderId() {
        return applyOrderId;
    }
public void setApplyOrderId(String applyOrderId) {
        this.applyOrderId = applyOrderId;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getId() {
        return id;
    }
public void setId(String id) {
        this.id = id;
    }
public String getBId() {
        return bId;
    }
public void setBId(String bId) {
        this.bId = bId;
    }
public String getResId() {
        return resId;
    }
public void setResId(String resId) {
        this.resId = resId;
    }



}
