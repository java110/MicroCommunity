package com.java110.vo.api.msg;

import java.io.Serializable;
import java.util.Date;

public class ApiMsgDataVo implements Serializable {
    private String msgType;
    private String msgId;
    private String viewObjId;
    private String title;
    private String viewTypeCd;
    private String url;
    private String createTime;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getViewObjId() {
        return viewObjId;
    }

    public void setViewObjId(String viewObjId) {
        this.viewObjId = viewObjId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViewTypeCd() {
        return viewTypeCd;
    }

    public void setViewTypeCd(String viewTypeCd) {
        this.viewTypeCd = viewTypeCd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
