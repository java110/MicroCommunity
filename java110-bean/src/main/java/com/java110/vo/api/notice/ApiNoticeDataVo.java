package com.java110.vo.api.notice;

import java.io.Serializable;
import java.util.Date;

public class ApiNoticeDataVo implements Serializable {

    private String noticeId;
private String title;
private String noticeTypeCd;
private String context;
private String startTime;
public String getNoticeId() {
        return noticeId;
    }
public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }
public String getTitle() {
        return title;
    }
public void setTitle(String title) {
        this.title = title;
    }
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



}
