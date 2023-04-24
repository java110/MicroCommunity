package com.java110.dto.reportInfoSetting;

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
public class ReportInfoSettingTitleValueDto extends PageDto implements Serializable {

    private String valueId;
private String titleId;
private String communityId;
private String qaValue;
private String userCount;
private String seq;
    private String[] titleIds;

    private Date createTime;

    private String statusCd = "0";


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
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public String[] getTitleIds() {
        return titleIds;
    }

    public void setTitleIds(String[] titleIds) {
        this.titleIds = titleIds;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }
}
