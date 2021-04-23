package com.java110.po.repairReturnVisit;

import java.io.Serializable;
import java.util.Date;

public class RepairReturnVisitPo implements Serializable {

    private String visitId;
private String context;
private String repairId;
private String statusCd = "0";
private String communityId;
private String visitPersonName;
private String visitPersonId;
private String visitType;
public String getVisitId() {
        return visitId;
    }
public void setVisitId(String visitId) {
        this.visitId = visitId;
    }
public String getContext() {
        return context;
    }
public void setContext(String context) {
        this.context = context;
    }
public String getRepairId() {
        return repairId;
    }
public void setRepairId(String repairId) {
        this.repairId = repairId;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getVisitPersonName() {
        return visitPersonName;
    }
public void setVisitPersonName(String visitPersonName) {
        this.visitPersonName = visitPersonName;
    }
public String getVisitPersonId() {
        return visitPersonId;
    }
public void setVisitPersonId(String visitPersonId) {
        this.visitPersonId = visitPersonId;
    }
public String getVisitType() {
        return visitType;
    }
public void setVisitType(String visitType) {
        this.visitType = visitType;
    }



}
