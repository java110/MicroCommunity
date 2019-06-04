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
public class ApiParkingSpaceDataVo extends Vo {
    /**
     * floorId
     */
    private String area;
    private String typeCd;
    private String num;
    private String psId;
    private String state;
    private String communityId;
    private String userId;

    private String remark;

    private String userName;

    private String createTime;

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
}
