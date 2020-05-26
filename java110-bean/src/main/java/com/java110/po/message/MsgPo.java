package com.java110.po.message;

import java.io.Serializable;

/**
 * @ClassName MsgPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/26 22:26
 * @Version 1.0
 * add by wuxw 2020/5/26
 **/
public class MsgPo implements Serializable {

    private String msgId;
    private String msgType;
    private String title;
    private String url;
    private String viewTypeCd;
    private String viewObjId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getViewTypeCd() {
        return viewTypeCd;
    }

    public void setViewTypeCd(String viewTypeCd) {
        this.viewTypeCd = viewTypeCd;
    }

    public String getViewObjId() {
        return viewObjId;
    }

    public void setViewObjId(String viewObjId) {
        this.viewObjId = viewObjId;
    }
}
