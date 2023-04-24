package com.java110.dto.inspection;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 巡检项目数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class InspectionItemDto extends PageDto implements Serializable {

    private String itemId;
private String itemName;
private String remark;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getItemId() {
        return itemId;
    }
public void setItemId(String itemId) {
        this.itemId = itemId;
    }
public String getItemName() {
        return itemName;
    }
public void setItemName(String itemName) {
        this.itemName = itemName;
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
