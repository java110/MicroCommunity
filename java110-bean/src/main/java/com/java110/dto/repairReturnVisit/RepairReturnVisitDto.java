package com.java110.dto.repairReturnVisit;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 报修回访数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RepairReturnVisitDto extends PageDto implements Serializable {

    private String visitId;
private String context;
private String repairId;
private String communityId;
private String visitPersonName;
private String visitPersonId;
private String visitType;


    private Date createTime;

    private String statusCd = "0";


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
