package com.java110.dto.userQuestionAnswer;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 答卷数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class UserQuestionAnswerDto extends PageDto implements Serializable {

    private String score;
    private String evaluationScore;
    private String objId;
    private String userQaId;
    private String personId;
    private String personName;
    private String state;
    private String objType;
    private String qaId;
    private String qaType;
    private String[] qaTypes;
    private String startTime;
    private String qaName;
    private String endTime;
    private String qaTypeName;

    private Date createTime;

    private String statusCd = "0";


    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationScore(String evaluationScore) {
        this.evaluationScore = evaluationScore;
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

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
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

    public String getQaName() {
        return qaName;
    }

    public void setQaName(String qaName) {
        this.qaName = qaName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getQaTypeName() {
        return qaTypeName;
    }

    public void setQaTypeName(String qaTypeName) {
        this.qaTypeName = qaTypeName;
    }

    public String[] getQaTypes() {
        return qaTypes;
    }

    public void setQaTypes(String[] qaTypes) {
        this.qaTypes = qaTypes;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
