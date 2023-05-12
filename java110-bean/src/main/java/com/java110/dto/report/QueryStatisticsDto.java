package com.java110.dto.report;

import java.io.Serializable;

public class QueryStatisticsDto implements Serializable {

    private String communityId;
    private String queryDate;


    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }
}
