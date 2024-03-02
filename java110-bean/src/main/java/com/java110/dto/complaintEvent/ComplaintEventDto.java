package com.java110.dto.complaintEvent;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 投诉事件数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ComplaintEventDto extends PageDto implements Serializable {

    public static final String EVENT_TYPE_SUBMIT = "1000";//1001 投诉处理 2002 评价 3003 评价回复
    public static final String EVENT_TYPE_DO = "1001";//1001 投诉处理 2002 评价 3003 评价回复
    public static final String EVENT_TYPE_APPRAISE = "2002";//1001 投诉处理 2002 评价 3003 评价回复
    public static final String EVENT_TYPE_REPLY = "3003";//1001 投诉处理 2002 评价 3003 评价回复

    private String eventId;
    private String createUserId;
    private String complaintId;
    private String createUserName;
    private String remark;
    private String eventType;
    private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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
