package com.java110.dto.staffCommunity;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 员工小区数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class StaffCommunityDto extends PageDto implements Serializable {

    private String staffName;
private String communityName;
private String storeName;
private String communityId;
private String storeId;
private String scId;
private String staffId;


    private Date createTime;

    private String statusCd = "0";


    public String getStaffName() {
        return staffName;
    }
public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
public String getCommunityName() {
        return communityName;
    }
public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
public String getStoreName() {
        return storeName;
    }
public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getScId() {
        return scId;
    }
public void setScId(String scId) {
        this.scId = scId;
    }
public String getStaffId() {
        return staffId;
    }
public void setStaffId(String staffId) {
        this.staffId = staffId;
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
