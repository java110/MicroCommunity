package com.java110.dto.parking;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 车位属性数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ParkingSpaceAttrDto extends PageDto implements Serializable {

    private String attrId;
private String psId;
private String specCd;
private String communityId;
private String value;


    private Date createTime;

    private String statusCd = "0";


    public String getAttrId() {
        return attrId;
    }
public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
public String getPsId() {
        return psId;
    }
public void setPsId(String psId) {
        this.psId = psId;
    }
public String getSpecCd() {
        return specCd;
    }
public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getValue() {
        return value;
    }
public void setValue(String value) {
        this.value = value;
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
