package com.java110.dto.basePrivilege;

import com.java110.dto.PageDto;

import java.io.Serializable;

public class HasPrivilegeDto extends PageDto implements Serializable {
    private String pId;
    private String userId;
    private String pgId;
    private String pgName;
    private String description;
    private String createTime;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getPgId() {
        return pgId;
    }

    public void setPgId(String pgId) {
        this.pgId = pgId;
    }

    public String getPgName() {
        return pgName;
    }

    public void setPgName(String pgName) {
        this.pgName = pgName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
