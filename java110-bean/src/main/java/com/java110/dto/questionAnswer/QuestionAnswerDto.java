package com.java110.dto.questionAnswer;

import com.java110.dto.PageDto;
import com.java110.dto.questionTitleValue.QuestionTitleValueDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 问卷投票数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class QuestionAnswerDto extends PageDto implements Serializable {

    public static final String QA_TYPE_VOTE = "3003";// 业主投票
    public static final String STATE_WAIT = "W"; // 待发布
    public static final String STATE_COMPLETE = "C"; //发布完成

    private String qaName;
    private String qaType;
    private String startTime;
    private String remark;
    private String endTime;
    private String communityId;
    private String content;
    private String qaId;

    private String titleType;

    private Date createTime;

    private String statusCd = "0";

    private long voteCount; // 总投票人数

    private long votedCount; // 已投票人数

    private String ownerName;

    private String link;

    private String roomName;

    private List<QuestionTitleValueDto> titleValues;

    private String state;

    private String userQaId;

    public String getQaName() {
        return qaName;
    }

    public void setQaName(String qaName) {
        this.qaName = qaName;
    }

    public String getQaType() {
        return qaType;
    }

    public void setQaType(String qaType) {
        this.qaType = qaType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQaId() {
        return qaId;
    }

    public void setQaId(String qaId) {
        this.qaId = qaId;
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

    public List<QuestionTitleValueDto> getTitleValues() {
        return titleValues;
    }

    public void setTitleValues(List<QuestionTitleValueDto> titleValues) {
        this.titleValues = titleValues;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public long getVotedCount() {
        return votedCount;
    }

    public void setVotedCount(long votedCount) {
        this.votedCount = votedCount;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserQaId() {
        return userQaId;
    }

    public void setUserQaId(String userQaId) {
        this.userQaId = userQaId;
    }
}
