package com.java110.dto.parking;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 停车场数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ParkingAreaDto extends PageDto implements Serializable {

    private String typeCd;
    private String num;
    private String paId;
    private String[] paIds;
    private String remark;
    private String communityId;
    private List<ParkingAreaAttrDto> attrs;


    private String createTime;

    private String statusCd = "0";


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

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
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

    public String[] getPaIds() {
        return paIds;
    }

    public void setPaIds(String[] paIds) {
        this.paIds = paIds;
    }

    public List<ParkingAreaAttrDto> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<ParkingAreaAttrDto> attrs) {
        this.attrs = attrs;
    }
}
