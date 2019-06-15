package com.java110.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 停车位数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ParkingSpaceDto extends PageDto implements Serializable {

    private String area;
private String typeCd;
private String num;
private String psId;
private String remark;
private String state;
private String communityId;
private String userId;

private String[] psIds;

private String[] states;



    private Date createTime;

    private String statusCd = "0";


    public String getArea() {
        return area;
    }
public void setArea(String area) {
        this.area = area;
    }
public String getTypeCd() {
        return typeCd;
    }
public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }
public String getNum() {
        return num;
    }
public void setNum(String num) {
        this.num = num;
    }
public String getPsId() {
        return psId;
    }
public void setPsId(String psId) {
        this.psId = psId;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
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
public String getUserId() {
        return userId;
    }
public void setUserId(String userId) {
        this.userId = userId;
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

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }


    public String[] getPsIds() {
        return psIds;
    }

    public void setPsIds(String[] psIds) {
        this.psIds = psIds;
    }
}
