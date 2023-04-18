package com.java110.dto.market;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 营销黑名单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MarketBlacklistDto extends PageDto implements Serializable {

    private String personName;
    private String filter;
    private String[] filters;
    private String remark;
    private String blId;


    private Date createTime;

    private String statusCd = "0";


    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBlId() {
        return blId;
    }

    public void setBlId(String blId) {
        this.blId = blId;
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

    public String[] getFilters() {
        return filters;
    }

    public void setFilters(String[] filters) {
        this.filters = filters;
    }
}
