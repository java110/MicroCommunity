package com.java110.po.owner;

import java.io.Serializable;

/**
 * @ClassName RepairUserPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 14:17
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class RepairUserPo implements Serializable {
    private String ruId;
    private String repairId;
    private String userId;
    private String communityId;
    private String state;
    private String context;

    public String getRuId() {
        return ruId;
    }

    public void setRuId(String ruId) {
        this.ruId = ruId;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
