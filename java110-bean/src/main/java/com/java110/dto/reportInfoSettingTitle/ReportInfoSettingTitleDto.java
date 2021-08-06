package com.java110.dto.reportInfoSettingTitle;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 进出上报题目设置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReportInfoSettingTitleDto extends PageDto implements Serializable {

    private String titleType;
private String titleId;
private String title;
private String communityId;
private String seq;
private String settingId;


    private Date createTime;

    private String statusCd = "0";


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
}
