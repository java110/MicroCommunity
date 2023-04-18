package com.java110.dto.owner;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 业主委员会数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OwnerCommitteeDto extends PageDto implements Serializable {

    private String address;
private String appointTime;
private String idCard;
private String postDesc;
private String sex;
private String link;
private String remark;
private String curTime;
private String post;
private String name;
private String position;
private String state;
private String ocId;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getAddress() {
        return address;
    }
public void setAddress(String address) {
        this.address = address;
    }
public String getAppointTime() {
        return appointTime;
    }
public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }
public String getIdCard() {
        return idCard;
    }
public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
public String getPostDesc() {
        return postDesc;
    }
public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }
public String getSex() {
        return sex;
    }
public void setSex(String sex) {
        this.sex = sex;
    }
public String getLink() {
        return link;
    }
public void setLink(String link) {
        this.link = link;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getCurTime() {
        return curTime;
    }
public void setCurTime(String curTime) {
        this.curTime = curTime;
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
public String getPosition() {
        return position;
    }
public void setPosition(String position) {
        this.position = position;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getOcId() {
        return ocId;
    }
public void setOcId(String ocId) {
        this.ocId = ocId;
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
