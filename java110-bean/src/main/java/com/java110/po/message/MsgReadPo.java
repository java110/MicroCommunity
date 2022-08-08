package com.java110.po.message;

import java.io.Serializable;

/**
 * @ClassName ReadMsgPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 10:24
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class MsgReadPo implements Serializable {

    private String msgReadId;
    private String msgId;
    private String userId;
    private String userName;
    private String statusCd="0";

    public String getMsgReadId() {
        return msgReadId;
    }

    public void setMsgReadId(String msgReadId) {
        this.msgReadId = msgReadId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
