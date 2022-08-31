package com.java110.dto.privilegeUser;

import com.java110.dto.PageDto;
import com.java110.dto.roleCommunity.RoleCommunityDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 用户权限数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PrivilegeUserDto extends PageDto implements Serializable {

    public static final String PRIVILEGE_FLAG_GROUP = "1";
    private String privilegeFlag;
    private String puId;
    private String pId;
    private String storeId;
    private String userId;
    private String userName;
    private String roleName;

    private Date createTime;
    private String[] orgIds;

    private String statusCd = "0";
    private List<RoleCommunityDto> roleCommunityDtoList;

    public String getPrivilegeFlag() {
        return privilegeFlag;
    }

    public void setPrivilegeFlag(String privilegeFlag) {
        this.privilegeFlag = privilegeFlag;
    }

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<RoleCommunityDto> getRoleCommunityDtoList() {
        return roleCommunityDtoList;
    }

    public void setRoleCommunityDtoList(List<RoleCommunityDto> roleCommunityDtoList) {
        this.roleCommunityDtoList = roleCommunityDtoList;
    }

    public String[] getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(String[] orgIds) {
        this.orgIds = orgIds;
    }
}
