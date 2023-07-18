package com.java110.dto.questionTitle;

import com.java110.dto.PageDto;
import com.java110.dto.questionTitleValue.QuestionTitleValueDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 问卷题目数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class QuestionTitleDto extends PageDto implements Serializable {

    public static final String TITLE_TYPE_SINGLE = "1001"; // 单选题
    public static final String TITLE_TYPE_MULTIPLE = "2002"; // 多选题
    public static final String TITLE_TYPE_QUESTIONS = "3003"; // 简答题

    private String titleType;
    private String titleId;
    private String communityId;
    private String qaTitle;

    private List<QuestionTitleValueDto> titleValues;

    private Date createTime;

    private String statusCd = "0";

    private String qaId;


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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public List<QuestionTitleValueDto> getTitleValues() {
        return titleValues;
    }

    public void setTitleValues(List<QuestionTitleValueDto> titleValues) {
        this.titleValues = titleValues;
    }

    public String getQaId() {
        return qaId;
    }

    public void setQaId(String qaId) {
        this.qaId = qaId;
    }
}
