package com.java110.po.allocationStorehouseApply;

import java.io.Serializable;
import java.util.Date;

public class AllocationStorehouseApplyPo implements Serializable {

    private String applyId;
private String startUserId;
private String startUserName;
private String applyCount;
private String remark;
private String state;
private String storeId;
public String getApplyId() {
        return applyId;
    }
public void setApplyId(String applyId) {
        this.applyId = applyId;
    }
public String getStartUserId() {
        return startUserId;
    }
public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }
public String getStartUserName() {
        return startUserName;
    }
public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }
public String getApplyCount() {
        return applyCount;
    }
public void setApplyCount(String applyCount) {
        this.applyCount = applyCount;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }



}
