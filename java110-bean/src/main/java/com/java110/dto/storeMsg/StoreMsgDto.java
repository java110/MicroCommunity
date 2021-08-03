package com.java110.dto.storeMsg;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 商户消息数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class StoreMsgDto extends PageDto implements Serializable {

    private String msgType;
private String viewId;
private String msgId;
private String shareId;
private String title;
private String msgFlag;
private String content;


    private Date createTime;

    private String statusCd = "0";


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
}
