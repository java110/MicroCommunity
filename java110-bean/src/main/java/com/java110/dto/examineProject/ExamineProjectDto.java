package com.java110.dto.examineProject;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 考核项目数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ExamineProjectDto extends PageDto implements Serializable {

    private String postCd;
private String post;
private String name;
private String weight;
private String state;
private String communityId;
private String projectId;


    private Date createTime;

    private String statusCd = "0";


    public String getPostCd() {
        return postCd;
    }
public void setPostCd(String postCd) {
        this.postCd = postCd;
    }
public String getPost() {
        return post;
    }
public void setPost(String post) {
        this.post = post;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getWeight() {
        return weight;
    }
public void setWeight(String weight) {
        this.weight = weight;
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
}
