package com.java110.dto.questionAnswer;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 答卷数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class QuestionAnswerTitleDto extends PageDto implements Serializable {

    public static final String TITLE_TYPE_SINGLE = "1001"; // 单选题
    public static final String TITLE_TYPE_MULTIPLE = "2002"; // 多选题
    public static final String TITLE_TYPE_QUESTIONS = "3003"; // 简答题


    private String titleType;
    private String titleId;
    private String objId;
    private String[] objIds;
    private String objType;
    private String seq;
    private String qaId;
    private String qaTitle;
    private String userId;


    private Date createTime;

    private String statusCd = "0";

    private List<QuestionAnswerTitleValueDto> questionAnswerTitleValues;


    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
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

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getQaId() {
        return qaId;
    }

    public void setQaId(String qaId) {
        this.qaId = qaId;
    }

    public String getQaTitle() {
        return qaTitle;
    }

    public void setQaTitle(String qaTitle) {
        this.qaTitle = qaTitle;
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

    public String[] getObjIds() {
        return objIds;
    }

    public void setObjIds(String[] objIds) {
        this.objIds = objIds;
    }

    public List<QuestionAnswerTitleValueDto> getQuestionAnswerTitleValues() {
        return questionAnswerTitleValues;
    }

    public void setQuestionAnswerTitleValues(List<QuestionAnswerTitleValueDto> questionAnswerTitleValues) {
        this.questionAnswerTitleValues = questionAnswerTitleValues;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
