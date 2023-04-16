package com.java110.dto.examineProject;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 员工考核项目数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ExamineStaffProjectDto extends PageDto implements Serializable {

    private String esId;
    private String[] esIds;
    private String espId;
    private String communityId;
    private String projectId;
    private String[] projectIds;

    private String projectName;

    private String weight;

    private Date createTime;

    private String statusCd = "0";


    public String getEsId() {
        return esId;
    }

    public void setEsId(String esId) {
        this.esId = esId;
    }

    public String getEspId() {
        return espId;
    }

    public void setEspId(String espId) {
        this.espId = espId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String[] getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(String[] projectIds) {
        this.projectIds = projectIds;
    }

    public String[] getEsIds() {
        return esIds;
    }

    public void setEsIds(String[] esIds) {
        this.esIds = esIds;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
