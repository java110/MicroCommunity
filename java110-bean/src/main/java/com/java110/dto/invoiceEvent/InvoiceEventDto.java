package com.java110.dto.invoiceEvent;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 发票事件数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class InvoiceEventDto extends PageDto implements Serializable {


    public static final String STATE_COMPLETE = "1001";// 类型 1001 审核成功 2002 上传 3003 审核失败 4004 领用 5005 登记
    public static final String STATE_UPLOAD = "2002";//类型 1001 审核成功 2002 上传 3003 审核失败 4004 领用 5005 登记
    public static final String STATE_FAIL = "3003";//类型 1001 审核成功 2002 上传 3003 审核失败 4004 领用 5005 登记
    public static final String STATE_GET = "4004";//类型 1001 审核成功 2002 上传 3003 审核失败 4004 领用 5005 登记
    public static final String STATE_GET_FINISH = "5005";//类型 1001 审核成功 2002 上传 3003 审核失败 4004 领用 5005 登记

    private String eventId;
    private String applyId;
    private String createUserId;
    private String createUserName;
    private String remark;
    private String eventType;

    private String eventTypeName;
    private String communityId;




    private Date createTime;

    private String statusCd = "0";


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
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

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }
}
