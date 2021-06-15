package com.java110.dto.msg;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 消息数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MsgDto extends PageDto implements Serializable {

    private String msgType;
    private String msgId;
    private String viewObjId;
    private String title;
    private String viewTypeCd;
    private String url;
    private String userId;

    private String[] viewObjIds;


    private Date createTime;

    private String statusCd = "0";


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

    public String[] getViewObjIds() {
        return viewObjIds;
    }

    public void setViewObjIds(String[] viewObjIds) {
        this.viewObjIds = viewObjIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
