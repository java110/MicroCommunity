package com.java110.dto.complaintAppraise;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 投诉评价数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ComplaintAppraiseDto extends PageDto implements Serializable {

    private String score;
private String replyContext;
private String createUserId;
private String replyUserId;
private String complaintId;
private String replyUserName;
private String appraiseId;
private String context;
private String createUserName;
private String state;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getScore() {
        return score;
    }
public void setScore(String score) {
        this.score = score;
    }
public String getReplyContext() {
        return replyContext;
    }
public void setReplyContext(String replyContext) {
        this.replyContext = replyContext;
    }
public String getCreateUserId() {
        return createUserId;
    }
public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
public String getReplyUserId() {
        return replyUserId;
    }
public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }
public String getComplaintId() {
        return complaintId;
    }
public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }
public String getReplyUserName() {
        return replyUserName;
    }
public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }
public String getAppraiseId() {
        return appraiseId;
    }
public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
    }
public String getContext() {
        return context;
    }
public void setContext(String context) {
        this.context = context;
    }
public String getCreateUserName() {
        return createUserName;
    }
public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
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
