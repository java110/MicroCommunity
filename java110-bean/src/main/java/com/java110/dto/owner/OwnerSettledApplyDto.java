package com.java110.dto.owner;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @ClassName FloorDto
 * @Description 业主入驻申请数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OwnerSettledApplyDto extends PageDto implements Serializable {

    //状态 W待审核 D 审核中 C 审核完成 D 审核失败
    public static final String STATE_WAIT = "W"; //W待审核
    public static final String STATE_DOING = "D"; //审核中
    public static final String STATE_COMPLETE = "C"; //审核完成
    public static final String STATE_FAIT = "F"; //审核失败

    private String applyId;
    private String[] applyIds;
    private String createUserId;
    private String remark;
    private String state;
    private String ownerId;
    private String communityId;

    private String stateName;

    private String ownerName;

    private String ownerLink;

    private String roomCount;


    private String createTime;

    private String statusCd = "0";

    private String flowId;
    private String flowName;
    private String auditWay;


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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }



    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerLink() {
        return ownerLink;
    }

    public void setOwnerLink(String ownerLink) {
        this.ownerLink = ownerLink;
    }

    public String getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(String roomCount) {
        this.roomCount = roomCount;
    }

    public String[] getApplyIds() {
        return applyIds;
    }

    public void setApplyIds(String[] applyIds) {
        this.applyIds = applyIds;
    }


    public String getFlowId() {
        return flowId;
    }


    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }


    public String getFlowName() {
        return flowName;
    }


    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }


    public String getAuditWay() {
        return auditWay;
    }


    public void setAuditWay(String auditWay) {
        this.auditWay = auditWay;
    }
}
