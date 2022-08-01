package com.java110.dto;

import com.java110.dto.floorAttr.FloorAttrDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 小区楼数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FloorDto extends PageDto implements Serializable {


    /**
     * floorId
     */
    private String floorId;

    private String[] floorIds;

    private String communityId;

    /**
     * 编号
     */
    private String floorNum;

    private String floorArea;

    /**
     * 名称
     */
    private String floorName;

    private String remark;

    private String userId;

    private String userName;

    private Date createTime;

    private String statusCd = "0";

    private int seq;

    private List<FloorAttrDto> floorAttrDto;

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String[] getFloorIds() {
        return floorIds;
    }

    public void setFloorIds(String[] floorIds) {
        this.floorIds = floorIds;
    }

    public String getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(String floorArea) {
        this.floorArea = floorArea;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public List<FloorAttrDto> getFloorAttrDto() {
        return floorAttrDto;
    }

    public void setFloorAttrDto(List<FloorAttrDto> floorAttrDto) {
        this.floorAttrDto = floorAttrDto;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
