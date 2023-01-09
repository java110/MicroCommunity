package com.java110.dto.owner;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 绑定业主数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OwnerAppUserDto extends PageDto implements Serializable {

    public static final String APP_TYPE_APP = "APP";//app绑定业主
    public static final String APP_TYPE_WECHAT_MINA = "WECHAT_MINA";//小程序绑定业主
    public static final String APP_TYPE_WECHAT = "WECHAT";//公众号绑定业主
    public static final String STATE_AUDITING = "10000";// 审核中
    public static final String STATE_AUDIT_SUCCESS = "12000";//审核成功
    public static final String STATE_AUDIT_ERROR = "13000";//审核失败

    private String idCard;
    private String openId;
    private String unionId;
    private String link;
    private String remark;
    private String appUserName;
    private String communityName;
    private String state;
    private String stateName;
    private String appUserId;
    private String communityId;
    private String appTypeCd;
    private String memberId;
    private String ownerId;
    private String userId;
    private String appType;
    private String[] userIds;
    private String oldAppUserId;
    private String sCommunityTel;
    private String communityQrCode;
    private String defaultCommunityId;


    private String[] states;

    private String areaCode;
    private String areaName;
    private String parentAreaCode;
    private String parentAreaName;


    private Date createTime;

    private String statusCd = "0";

    private String nickName;
    private String headImgUrl;
    private String ownerTypeCd;


    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public String getAppUserName() {
        return appUserName;
    }

    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getAppTypeCd() {
        return appTypeCd;
    }

    public void setAppTypeCd(String appTypeCd) {
        this.appTypeCd = appTypeCd;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getParentAreaCode() {
        return parentAreaCode;
    }

    public void setParentAreaCode(String parentAreaCode) {
        this.parentAreaCode = parentAreaCode;
    }

    public String getParentAreaName() {
        return parentAreaName;
    }

    public void setParentAreaName(String parentAreaName) {
        this.parentAreaName = parentAreaName;
    }

    public String getOldAppUserId() {
        return oldAppUserId;
    }

    public void setOldAppUserId(String oldAppUserId) {
        this.oldAppUserId = oldAppUserId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getsCommunityTel() {
        return sCommunityTel;
    }

    public void setsCommunityTel(String sCommunityTel) {
        this.sCommunityTel = sCommunityTel;
    }

    public String getOwnerTypeCd() {
        return ownerTypeCd;
    }

    public void setOwnerTypeCd(String ownerTypeCd) {
        this.ownerTypeCd = ownerTypeCd;
    }

    public String getDefaultCommunityId() {
        return defaultCommunityId;
    }

    public void setDefaultCommunityId(String defaultCommunityId) {
        this.defaultCommunityId = defaultCommunityId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getCommunityQrCode() {
        return communityQrCode;
    }

    public void setCommunityQrCode(String communityQrCode) {
        this.communityQrCode = communityQrCode;
    }
}
