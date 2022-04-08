package com.java110.po.parking;

import java.io.Serializable;

/**
 * @ClassName ParkingAreaPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 14:39
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class ParkingAreaPo implements Serializable {

    private String paId;
    private String communityId;
    private String num;
    private String typeCd;
    private String remark;
    private String createTime;

    private String statusCd = "0";

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
