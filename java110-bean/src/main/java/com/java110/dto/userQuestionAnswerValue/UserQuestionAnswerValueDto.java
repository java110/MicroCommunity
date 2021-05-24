package com.java110.dto.userQuestionAnswerValue;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 答卷答案数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class UserQuestionAnswerValueDto extends PageDto implements Serializable {

    private String score;
    private String valueId;
    private String titleId;
    private String answerType;
    private String objId;
    private String[] objIds;
    private String userQaId;
    private String valueContent;
    private String personId;
    private String objType;
    private String userTitleId;
    private String qaId;
    private String qaTypeName;
    private String userName;
    private String qaName;
    private String qaTitle;
    private String qaValue;
    private String qaType;
    private String startTime;
    private String endTime;


    private Date createTime;

    private String statusCd = "0";


    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

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

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
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

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
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

    public String getQaTypeName() {
        return qaTypeName;
    }

    public void setQaTypeName(String qaTypeName) {
        this.qaTypeName = qaTypeName;
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

    public String getQaTitle() {
        return qaTitle;
    }

    public void setQaTitle(String qaTitle) {
        this.qaTitle = qaTitle;
    }

    public String getQaValue() {
        return qaValue;
    }

    public void setQaValue(String qaValue) {
        this.qaValue = qaValue;
    }

    public String getQaType() {
        return qaType;
    }

    public void setQaType(String qaType) {
        this.qaType = qaType;
    }

    public String[] getObjIds() {
        return objIds;
    }

    public void setObjIds(String[] objIds) {
        this.objIds = objIds;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
