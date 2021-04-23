package com.java110.dto.auditUser;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 审核人员数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AuditUserDto extends PageDto implements Serializable {

    private String objCode;
    private String auditUserId;
    private String storeId;
    private String userName;
    private String userId;
    private String auditLink;
    private String auditLinkName;
    private String objCodeName;


    private Date createTime;

    private String statusCd = "0";


    public String getObjCode() {
        return objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    public String getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuditLink() {
        return auditLink;
    }

    public void setAuditLink(String auditLink) {
        this.auditLink = auditLink;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getAuditLinkName() {
        return auditLinkName;
    }

    public void setAuditLinkName(String auditLinkName) {
        this.auditLinkName = auditLinkName;
    }

    public String getObjCodeName() {
        return objCodeName;
    }

    public void setObjCodeName(String objCodeName) {
        this.objCodeName = objCodeName;
    }
}
