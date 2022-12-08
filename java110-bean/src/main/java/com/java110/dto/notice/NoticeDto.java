package com.java110.dto.notice;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 通知数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class NoticeDto extends PageDto implements Serializable {

    public static final String STATE_WAIT = "1000";// 待通知
    public static final String STATE_DOING = "2000";//通知中
    public static final String STATE_FINISH = "3000";//通知完成

    public static final String OBJ_TYPE_COMMUNITY = "001";//小区
    public static final String OBJ_TYPE_FLOOR = "002";//楼栋
    public static final String OBJ_TYPE_UNIT = "003";//单元
    public static final String OBJ_TYPE_ROOM = "004";//房屋
    public static final String OBJ_TYPE_ALL = "005";//全部发送

    public static final String NOTICE_TYPE_OWNER_WECHAT = "1003";//发送给业主微信

    private String noticeTypeCd;
    private String[] noticeTypeCds;
    private String context;
    private String startTime;
    private String endTime;
    private String communityId;
    private String title;
    private String userId;
    private String noticeId;
    private String objType;
    private String objId;
    private String objName;
    private String state;
    private String stateName;

    private Date createTime;

    private String noticeTypeCdName;

    private String statusCd = "0";

    public String getNoticeTypeCd() {
        return noticeTypeCd;
    }

    public void setNoticeTypeCd(String noticeTypeCd) {
        this.noticeTypeCd = noticeTypeCd;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNoticeTypeCdName() {
        return noticeTypeCdName;
    }

    public void setNoticeTypeCdName(String noticeTypeCdName) {
        this.noticeTypeCdName = noticeTypeCdName;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
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

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String[] getNoticeTypeCds() {
        return noticeTypeCds;
    }

    public void setNoticeTypeCds(String[] noticeTypeCds) {
        this.noticeTypeCds = noticeTypeCds;
    }
}
