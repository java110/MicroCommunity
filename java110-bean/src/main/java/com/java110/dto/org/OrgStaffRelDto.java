package com.java110.dto.org;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 组织员工关系数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OrgStaffRelDto extends PageDto implements Serializable {

    //600311000001
    //600311000002
    public static final String REL_CD_ADMIN="600311000001";//管理员
    public static final String REL_CD_PUBLIC="600311000002";//管理员

    private String relId;
    private String storeId;
    private String orgId;
    private String orgName;
    private String staffId;
    private String staffName;
    private String[] staffIds;
    private String relCd;
    private String relCdName;
    private String departmentId;
    private String departmentName;
    private String companyId;
    private String companyName;
    private String parentOrgId;
    private String parentOrgName;

    private String orgLevel;


    private Date createTime;

    private String statusCd = "0";


    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getRelCd() {
        return relCd;
    }

    public void setRelCd(String relCd) {
        this.relCd = relCd;
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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String[] getStaffIds() {
        return staffIds;
    }

    public void setStaffIds(String[] staffIds) {
        this.staffIds = staffIds;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getRelCdName() {
        return relCdName;
    }

    public void setRelCdName(String relCdName) {
        this.relCdName = relCdName;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }
}
