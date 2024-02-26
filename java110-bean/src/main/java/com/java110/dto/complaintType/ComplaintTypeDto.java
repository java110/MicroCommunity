package com.java110.dto.complaintType;

import com.java110.dto.PageDto;
import com.java110.dto.complaintTypeUser.ComplaintTypeUserDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 投诉类型数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ComplaintTypeDto extends PageDto implements Serializable {

    public static final String NOTIFY_WAY_SMS = "SMS"; //短信
    public static final String NOTIFY_WAY_WECHAT = "WECHAT"; //微信

    private String typeCd;
    private String typeName;
    private String notifyWay;
    private String remark;
    private String communityId;
    private String appraiseReply;

    private List<ComplaintTypeUserDto> staffs;


    private Date createTime;

    private String statusCd = "0";


    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getNotifyWay() {
        return notifyWay;
    }

    public void setNotifyWay(String notifyWay) {
        this.notifyWay = notifyWay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getAppraiseReply() {
        return appraiseReply;
    }

    public void setAppraiseReply(String appraiseReply) {
        this.appraiseReply = appraiseReply;
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

    public List<ComplaintTypeUserDto> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<ComplaintTypeUserDto> staffs) {
        this.staffs = staffs;
    }
}
