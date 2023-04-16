package com.java110.dto.data;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 数据权限数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class DataPrivilegeDto extends PageDto implements Serializable {

    private String code;
private String dpId;
private String name;
private String remark;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getCode() {
        return code;
    }
public void setCode(String code) {
        this.code = code;
    }
public String getDpId() {
        return dpId;
    }
public void setDpId(String dpId) {
        this.dpId = dpId;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
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
}
