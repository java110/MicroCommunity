package com.java110.dto.data;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class ImportDataDto implements Serializable {

    private String businessAdapt;

    private String logId;

    private String communityId;


    public String getBusinessAdapt() {
        return businessAdapt;
    }

    public void setBusinessAdapt(String businessAdapt) {
        this.businessAdapt = businessAdapt;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
