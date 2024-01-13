package com.java110.dto.user;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 问卷答案数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class UserQuestionAnswerValueDto extends PageDto implements Serializable {
    private String valueId;
    private String titleId;
    private String userQaId;
    private String valueContent;
    private String communityId;
    private String userTitleId;
    private String qaId;

    private String qaValue;

    private Date createTime;

    private String statusCd = "0";

    private String userName;
    private String qaName;
    private String qaTypeName;
    private String qaTitle;

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getUserQaId() {
        return userQaId;
    }

    public void setUserQaId(String userQaId) {
        this.userQaId = userQaId;
    }

    public String getValueContent() {
        return valueContent;
    }

    public void setValueContent(String valueContent) {
        this.valueContent = valueContent;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getUserTitleId() {
        return userTitleId;
    }

    public void setUserTitleId(String userTitleId) {
        this.userTitleId = userTitleId;
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

    public String getQaValue() {
        return qaValue;
    }

    public void setQaValue(String qaValue) {
        this.qaValue = qaValue;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQaName() {
        return qaName;
    }

    public void setQaName(String qaName) {
        this.qaName = qaName;
    }

    public String getQaTypeName() {
        return qaTypeName;
    }

    public void setQaTypeName(String qaTypeName) {
        this.qaTypeName = qaTypeName;
    }

    public String getQaTitle() {
        return qaTitle;
    }

    public void setQaTitle(String qaTitle) {
        this.qaTitle = qaTitle;
    }
}
