package com.java110.vo.api.org;

import java.io.Serializable;
import java.util.Date;

public class ApiOrgDataVo implements Serializable {

    private String orgId;
private String orgName;
private String orgLevel;
private String parentOrgId;
private String description;
public String getOrgId() {
        return orgId;
    }
public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
public String getOrgName() {
        return orgName;
    }
public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
public String getOrgLevel() {
        return orgLevel;
    }
public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }
public String getParentOrgId() {
        return parentOrgId;
    }
public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }
public String getDescription() {
        return description;
    }
public void setDescription(String description) {
        this.description = description;
    }



}
