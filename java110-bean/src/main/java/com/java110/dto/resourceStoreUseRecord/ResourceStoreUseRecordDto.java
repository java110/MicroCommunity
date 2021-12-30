package com.java110.dto.resourceStoreUseRecord;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 物品使用记录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ResourceStoreUseRecordDto extends PageDto implements Serializable {
    private String repairId;
    private String unitPrice;
    private String createUserId;
    private String quantity;
    private String rsurId;
    private String createUserName;
    private String remark;
    private String storeId;
    private String resId;
    private String communityId;
    private String resName;
    private Date createTime;
    private String rstName;
    private String parentRstName;
    private String specName;
    private String rstId;
    private String parentRstId;
    private String rssId;
    private String resourceStoreName;
    private String startTime;
    private String endTime;
    private String unitCodeName;
    private String miniUnitCodeName;
    //1001 报废回收   2002 工单损耗   3003 公用损耗
    private String state;
    private String stateName;

    private String statusCd = "0";

    private String isFixed;
    private String isFixedName;

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRsurId() {
        return rsurId;
    }

    public void setRsurId(String rsurId) {
        this.rsurId = rsurId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
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

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getRstName() {
        return rstName;
    }

    public void setRstName(String rstName) {
        this.rstName = rstName;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getRstId() {
        return rstId;
    }

    public void setRstId(String rstId) {
        this.rstId = rstId;
    }

    public String getRssId() {
        return rssId;
    }

    public void setRssId(String rssId) {
        this.rssId = rssId;
    }

    public String getResourceStoreName() {
        return resourceStoreName;
    }

    public void setResourceStoreName(String resourceStoreName) {
        this.resourceStoreName = resourceStoreName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUnitCodeName() {
        return unitCodeName;
    }

    public void setUnitCodeName(String unitCodeName) {
        this.unitCodeName = unitCodeName;
    }

    public String getMiniUnitCodeName() {
        return miniUnitCodeName;
    }

    public void setMiniUnitCodeName(String miniUnitCodeName) {
        this.miniUnitCodeName = miniUnitCodeName;
    }

    public String getParentRstName() {
        return parentRstName;
    }

    public void setParentRstName(String parentRstName) {
        this.parentRstName = parentRstName;
    }

    public String getParentRstId() {
        return parentRstId;
    }

    public void setParentRstId(String parentRstId) {
        this.parentRstId = parentRstId;
    }

    public String getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(String isFixed) {
        this.isFixed = isFixed;
    }

    public String getIsFixedName() {
        return isFixedName;
    }

    public void setIsFixedName(String isFixedName) {
        this.isFixedName = isFixedName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
