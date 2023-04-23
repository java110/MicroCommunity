package com.java110.dto.reserve;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 预约目录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReserveCatalogDto extends PageDto implements Serializable {

    private String catalogId;
private String name;
private String sort;
private String state;
private String communityId;
private String type;


    private Date createTime;

    private String statusCd = "0";


    public String getCatalogId() {
        return catalogId;
    }
public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getSort() {
        return sort;
    }
public void setSort(String sort) {
        this.sort = sort;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getType() {
        return type;
    }
public void setType(String type) {
        this.type = type;
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
