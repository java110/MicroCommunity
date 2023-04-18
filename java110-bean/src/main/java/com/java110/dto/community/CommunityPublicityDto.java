package com.java110.dto.community;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 小区公示数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CommunityPublicityDto extends PageDto implements Serializable {

    private String createUserId;
    private String pubId;
    private String context;
    private String collectCount;
    private String pubType;

    private String pubTypeName;
    private String likeCount;
    private String createUserName;
    private String title;
    private String communityId;
    private String readCount;
    private String headerImg;


    private Date createTime;

    private String statusCd = "0";


    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getPubId() {
        return pubId;
    }

    public void setPubId(String pubId) {
        this.pubId = pubId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(String collectCount) {
        this.collectCount = collectCount;
    }

    public String getPubType() {
        return pubType;
    }

    public void setPubType(String pubType) {
        this.pubType = pubType;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
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

    public String getPubTypeName() {
        return pubTypeName;
    }

    public void setPubTypeName(String pubTypeName) {
        this.pubTypeName = pubTypeName;
    }
}
