package com.java110.po.userQuestionAnswer;

import java.io.Serializable;
import java.util.Date;

public class UserQuestionAnswerPo implements Serializable {

    private String score;
private String evaluationScore;
private String objId;
private String userQaId;
private String personId;
private String statusCd = "0";
private String state;
private String objType;
private String qaId;
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
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
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



}
