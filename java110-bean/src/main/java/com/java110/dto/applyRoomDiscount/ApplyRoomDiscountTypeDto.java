package com.java110.dto.applyRoomDiscount;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 优惠申请类型数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ApplyRoomDiscountTypeDto extends PageDto implements Serializable {

    private String applyType;
private String typeDesc;
private String typeName;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getApplyType() {
        return applyType;
    }
public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
public String getTypeDesc() {
        return typeDesc;
    }
public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
public String getTypeName() {
        return typeName;
    }
public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
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
