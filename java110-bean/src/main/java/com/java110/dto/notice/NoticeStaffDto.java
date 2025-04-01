package com.java110.dto.notice;

import java.io.Serializable;

public class NoticeStaffDto implements Serializable {

    private String communityId;
    private String staffId;
    private String title;

    private String notifyUserName;

    public NoticeStaffDto() {
    }

    public NoticeStaffDto(String communityId, String staffId, String title, String notifyUserName) {
        this.communityId = communityId;
        this.staffId = staffId;
        this.title = title;
        this.notifyUserName = notifyUserName;
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
}
