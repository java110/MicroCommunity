package com.java110.dto.reserve;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 预约参数数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReserveParamsDto extends PageDto implements Serializable {
    public static final String PARAM_WAY_DAY = "1";
    public static final String PARAM_WAY_WEEK = "2";

    private String paramWay;
    private String paramsId;
    private String maxQuantity;
    private String name;
    private String paramWayText;
    private String startTime;
    private String communityId;
    private String hoursMaxQuantity;


    private Date createTime;

    private String statusCd = "0";

    private List<ReserveParamsOpenTimeDto> openTimes;


    public String getParamWay() {
        return paramWay;
    }

    public void setParamWay(String paramWay) {
        this.paramWay = paramWay;
    }

    public String getParamsId() {
        return paramsId;
    }

    public void setParamsId(String paramsId) {
        this.paramsId = paramsId;
    }

    public String getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(String maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParamWayText() {
        return paramWayText;
    }

    public void setParamWayText(String paramWayText) {
        this.paramWayText = paramWayText;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getHoursMaxQuantity() {
        return hoursMaxQuantity;
    }

    public void setHoursMaxQuantity(String hoursMaxQuantity) {
        this.hoursMaxQuantity = hoursMaxQuantity;
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

    public List<ReserveParamsOpenTimeDto> getOpenTimes() {
        return openTimes;
    }

    public void setOpenTimes(List<ReserveParamsOpenTimeDto> openTimes) {
        this.openTimes = openTimes;
    }
}
