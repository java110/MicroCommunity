package com.java110.po.questionAnswerTitleValue;

import java.io.Serializable;
import java.util.Date;

public class QuestionAnswerTitleValuePo implements Serializable {

    private String valueId;
    private String titleId;
    private String objId;
    private String statusCd = "0";
    private String objType;
    private String qaValue;
    private String seq;

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

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getQaValue() {
        return qaValue;
    }

    public void setQaValue(String qaValue) {
        this.qaValue = qaValue;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }


}
