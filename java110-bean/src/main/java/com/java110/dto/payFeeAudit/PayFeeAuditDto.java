package com.java110.dto.payFeeAudit;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 缴费审核数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PayFeeAuditDto extends PageDto implements Serializable {

    private String auditUserId;
private String auditUserName;
private String feeDetailId;
private String state;
private String message;
private String communityId;
private String feeId;


    private Date createTime;

    private String statusCd = "0";


    public String getAuditUserId() {
        return auditUserId;
    }
public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }
public String getAuditUserName() {
        return auditUserName;
    }
public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }
public String getFeeDetailId() {
        return feeDetailId;
    }
public void setFeeDetailId(String feeDetailId) {
        this.feeDetailId = feeDetailId;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getMessage() {
        return message;
    }
public void setMessage(String message) {
        this.message = message;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getFeeId() {
        return feeId;
    }
public void setFeeId(String feeId) {
        this.feeId = feeId;
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
}
