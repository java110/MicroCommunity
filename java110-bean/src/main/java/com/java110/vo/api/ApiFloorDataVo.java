package com.java110.vo.api;

import com.java110.vo.Vo;

/**
 * @ClassName ApiFloorDataVo
 * @Description TODO
 * @Author wuxw
 * @Date 2019/4/24 11:18
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ApiFloorDataVo  extends Vo {
    /**
     * floorId
     */
    private String floorId;

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

    private String userName;

    private String createTime;
    private int seq;


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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(String floorArea) {
        this.floorArea = floorArea;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
