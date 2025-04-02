package com.java110.dto.notice;

import java.io.Serializable;

public class NoticeStaffDto implements Serializable {

    private String communityId;
    private String staffId;
    private String title;

    private String notifyUserName;

    private String url;

    public NoticeStaffDto() {
    }

    /**
     * 通知实体
     * "流程名称", "发起时间", "发起人"
     *
     * @param communityId    小区
     * @param staffId        通知人
     * @param title          流程名称
     * @param notifyUserName 发起人
     */
    public NoticeStaffDto(String communityId, String staffId, String title, String notifyUserName) {
        this.communityId = communityId;
        this.staffId = staffId;
        this.title = title;
        this.notifyUserName = notifyUserName;
    }

    /**
     * 通知实体
     * "流程名称", "发起时间", "发起人"
     *
     * @param communityId    小区
     * @param staffId        通知人
     * @param title          流程名称
     * @param notifyUserName 发起人
     * @param url            打开地址
     */
    public NoticeStaffDto(String communityId, String staffId, String title, String notifyUserName, String url) {
        this.communityId = communityId;
        this.staffId = staffId;
        this.title = title;
        this.notifyUserName = notifyUserName;
        this.url = url;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotifyUserName() {
        return notifyUserName;
    }

    public void setNotifyUserName(String notifyUserName) {
        this.notifyUserName = notifyUserName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
