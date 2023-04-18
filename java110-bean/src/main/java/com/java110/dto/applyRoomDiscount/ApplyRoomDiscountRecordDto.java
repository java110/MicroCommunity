package com.java110.dto.applyRoomDiscount;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 验房记录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ApplyRoomDiscountRecordDto extends PageDto implements Serializable {

    private String ardrId;
    private String ardId;
    private String createUserId;
    private String createUserName;
    private String remark;
    private String communityId;
    private List<String> photos;
    private String videoName;
    private String isTrue;
    private String isTrueName;
    private String roomId;
    private String roomName;
    private String state;
    private String stateName;

    //文件真实名称
    private String fileRealName;

    //文件存储名称
    private String fileSaveName;

    private String url;

    //文件类型
    private String relTypeCd;

    private Date createTime;

    private String statusCd = "0";


    public String getArdrId() {
        return ardrId;
    }

    public void setArdrId(String ardrId) {
        this.ardrId = ardrId;
    }

    public String getArdId() {
        return ardId;
    }

    public void setArdId(String ardId) {
        this.ardId = ardId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
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

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(String isTrue) {
        this.isTrue = isTrue;
    }

    public String getIsTrueName() {
        return isTrueName;
    }

    public void setIsTrueName(String isTrueName) {
        this.isTrueName = isTrueName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getFileRealName() {
        return fileRealName;
    }

    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }

    public String getFileSaveName() {
        return fileSaveName;
    }

    public void setFileSaveName(String fileSaveName) {
        this.fileSaveName = fileSaveName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRelTypeCd() {
        return relTypeCd;
    }

    public void setRelTypeCd(String relTypeCd) {
        this.relTypeCd = relTypeCd;
    }
}
