package com.java110.dto.businesstype;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description cbusinesstype数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CbusinesstypeDto extends PageDto implements Serializable {

    private String businessTypeCd;
private String name;
private String description;
private String id;
private String userId;


    private Date createTime;

    private String statusCd = "0";


    public String getBusinessTypeCd() {
        return businessTypeCd;
    }
public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getDescription() {
        return description;
    }
public void setDescription(String description) {
        this.description = description;
    }
public String getId() {
        return id;
    }
public void setId(String id) {
        this.id = id;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
