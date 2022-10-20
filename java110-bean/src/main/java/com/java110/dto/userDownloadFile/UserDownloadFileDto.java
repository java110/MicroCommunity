package com.java110.dto.userDownloadFile;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 文件下载数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class UserDownloadFileDto extends PageDto implements Serializable {

    //等待 生成文件
    public static final String STATE_WAIT = "1001";

    // 生成文件中
    public static final String STATE_DOING = "2002";

    // 文件生成完成
    public static final String STATE_FINISH = "3003";

    //生成失败
    public static final String STATE_FAIL = "4004";

    private String name;
    private String downloadId;
    private String downloadUserId;
    private String state;
    private String stateName;
    private String tempUrl;
    private String communityId;
    private String downloadUserName;
    private String fileType;
    private String fileTypeName;


    private Date createTime;

    private String statusCd = "0";

    private String remark;

    private String downloadUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }

    public String getDownloadUserId() {
        return downloadUserId;
    }

    public void setDownloadUserId(String downloadUserId) {
        this.downloadUserId = downloadUserId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTempUrl() {
        return tempUrl;
    }

    public void setTempUrl(String tempUrl) {
        this.tempUrl = tempUrl;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getDownloadUserName() {
        return downloadUserName;
    }

    public void setDownloadUserName(String downloadUserName) {
        this.downloadUserName = downloadUserName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
