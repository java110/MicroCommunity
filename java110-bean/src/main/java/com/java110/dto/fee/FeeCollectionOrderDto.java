package com.java110.dto.fee;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 催缴单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeCollectionOrderDto extends PageDto implements Serializable {

    //状态 W 等待催缴 ，D 催缴中  F 催缴完成
    public static final String STATE_WAIT = "W";
    public static final String STATE_DOING = "D";
    public static final String STATE_FINISH = "F";
    public static final String STATE_ERROR = "E";

    //催缴方式，001 仅短信方式  002 短信微信方式 003 仅微信方式
    public static final String COLLECTION_WAY_SMS = "001";
    public static final String COLLECTION_WAY_WECHAT_SMS = "002";
    public static final String COLLECTION_WAY_WECHAT = "003";

    private String orderId;
    private String staffName;
    private String state;
    private String communityId;
    private String collectionWay;
    private String staffId;
    private String remark;
    private String collectionName;


    private Date createTime;

    private String statusCd = "0";


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCollectionWay() {
        return collectionWay;
    }

    public void setCollectionWay(String collectionWay) {
        this.collectionWay = collectionWay;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
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
