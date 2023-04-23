package com.java110.dto.reportInfoAnswer;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 批量操作日志详情数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReportInfoAnswerValueDto extends PageDto implements Serializable {

    public static final String TITLETYPE_RADIO = "1001";
    public static final String TITLETYPE_CHECKBOX = "2002";
    public static final String ITLETYPE_TEXTAREA = "3003";

    private String valueId;
    private String userAnId;
    private String titleId;
    private String anValueId;
    private String valueContent;
    private String communityId;
    private String settingId;

    private String userName;
    private String repName;
    private String repTitle;
    private String repValue;
    private String reType;
    private String repTypeName;
    private String personName;
    private String reportType;

    private Date createTime;

    private String statusCd = "0";

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getUserAnId() {
        return userAnId;
    }

    public void setUserAnId(String userAnId) {
        this.userAnId = userAnId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getAnValueId() {
        return anValueId;
    }

    public void setAnValueId(String anValueId) {
        this.anValueId = anValueId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public String getRepTitle() {
        return repTitle;
    }

    public void setRepTitle(String repTitle) {
        this.repTitle = repTitle;
    }

    public String getRepValue() {
        return repValue;
    }

    public void setRepValue(String repValue) {
        this.repValue = repValue;
    }

    public String getReType() {
        return reType;
    }

    public void setReType(String reType) {
        this.reType = reType;
    }

    public String getRepTypeName() {
        return repTypeName;
    }

    public void setRepTypeName(String repTypeName) {
        this.repTypeName = repTypeName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
}
