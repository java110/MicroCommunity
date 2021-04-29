package com.java110.dto.allocationStorehouseApply;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 调拨申请数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AllocationStorehouseApplyDto extends PageDto implements Serializable {

    private String applyId;
private String startUserId;
private String startUserName;
private String applyCount;
private String remark;
private String state;
private String storeId;


    private Date createTime;

    private String statusCd = "0";


    public String getApplyId() {
        return applyId;
    }
public void setApplyId(String applyId) {
        this.applyId = applyId;
    }
public String getStartUserId() {
        return startUserId;
    }
public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }
public String getStartUserName() {
        return startUserName;
    }
public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }
public String getApplyCount() {
        return applyCount;
    }
public void setApplyCount(String applyCount) {
        this.applyCount = applyCount;
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
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
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
