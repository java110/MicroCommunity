package com.java110.dto.report;

import java.io.Serializable;

public class QueryStatisticsDto implements Serializable {

    private String communityId;
    private String startDate;

    /**
     * 查询历史欠费
     */
    private String endDate;


    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
