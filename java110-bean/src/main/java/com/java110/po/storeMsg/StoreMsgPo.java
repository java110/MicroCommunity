package com.java110.po.storeMsg;

import java.io.Serializable;
import java.util.Date;

public class StoreMsgPo implements Serializable {

    private String msgType;
private String viewId;
private String msgId;
private String shareId;
private String statusCd = "0";
private String title;
private String msgFlag;
private String content;
public String getMsgType() {
        return msgType;
    }
public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
public String getViewId() {
        return viewId;
    }
public void setViewId(String viewId) {
        this.viewId = viewId;
    }
public String getMsgId() {
        return msgId;
    }
public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
public String getShareId() {
        return shareId;
    }
public void setShareId(String shareId) {
        this.shareId = shareId;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getTitle() {
        return title;
    }
public void setTitle(String title) {
        this.title = title;
    }
public String getMsgFlag() {
        return msgFlag;
    }
public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }
public String getContent() {
        return content;
    }
public void setContent(String content) {
        this.content = content;
    }



}
