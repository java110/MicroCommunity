package com.java110.entity.center;

import java.util.Date;

/**
 * Created by wuxw on 2018/4/13.
 */
public class DataFlowLinksCost {

    //环节编码
    private String LinksCode;

    private String LinksName;

    private Date startDate;

    private Date endDate;



    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public String getLinksCode() {
        return LinksCode;
    }

    public void setLinksCode(String linksCode) {
        LinksCode = linksCode;
    }

    public String getLinksName() {
        return LinksName;
    }

    public void setLinksName(String linksName) {
        LinksName = linksName;
    }

    public DataFlowLinksCost builder(String LinksCode, String LinksName, Date startDate, Date endDate){
        this.LinksCode = LinksCode;
        this.LinksName = LinksName;
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }
}
