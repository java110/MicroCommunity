package com.java110.dto.reportInfoSetting;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 进出上报题目设置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReportInfoSettingTitleDto extends PageDto implements Serializable {

    public static final String TITLE_TYPE_SINGLE = "1001"; // 单选题
    public static final String TITLE_TYPE_MULTIPLE = "2002"; // 多选题
    public static final String TITLE_TYPE_QUESTIONS = "3003"; // 简答题

    private String titleType;
    private String titleId;
    private String title;
    private String titleLike;
    private String communityId;
    private String seq;
    private String settingId;

    private Date createTime;

    private String statusCd = "0";

    private List<ReportInfoSettingTitleValueDto> reportInfoSettingTitleValueDtos;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
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

    public String getTitleLike() {
        return titleLike;
    }

    public void setTitleLike(String titleLike) {
        this.titleLike = titleLike;
    }

    public List<ReportInfoSettingTitleValueDto> getReportInfoSettingTitleValueDtos() {
        return reportInfoSettingTitleValueDtos;
    }

    public void setReportInfoSettingTitleValueDtos(List<ReportInfoSettingTitleValueDto> reportInfoSettingTitleValueDtos) {
        this.reportInfoSettingTitleValueDtos = reportInfoSettingTitleValueDtos;
    }
}
