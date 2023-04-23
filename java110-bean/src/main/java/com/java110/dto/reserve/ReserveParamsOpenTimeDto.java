package com.java110.dto.reserve;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 预约时间数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReserveParamsOpenTimeDto extends PageDto implements Serializable {

    private String hours;
    private String paramsId;
    private String[] paramsIds;
    private String isOpen;
    private String timeId;
    private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getParamsId() {
        return paramsId;
    }

    public void setParamsId(String paramsId) {
        this.paramsId = paramsId;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public String[] getParamsIds() {
        return paramsIds;
    }

    public void setParamsIds(String[] paramsIds) {
        this.paramsIds = paramsIds;
    }
}
