package com.java110.dto.allocationStorehouse;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 仓库调拨数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AllocationStorehouseDto extends PageDto implements Serializable {
    public static final String STATE_APPLY = "1200";//状态，1200 调拨申请 1201 调拨审核 1202 调拨完成 1203 调拨失败 1204 调拨已审核 1205 已退还
    public static final String STATE_AUDIT = "1201";//状态，1200 调拨申请 1201 调拨审核 1202 调拨完成 1203 调拨失败 1204 调拨已审核 1205 已退还
    public static final String STATE_SUCCESS = "1202";//状态，1200 调拨申请 1201 调拨审核 1202 调拨完成 1203 调拨失败 1204 调拨已审核 1205 已退还
    public static final String STATE_FAIL = "1203";//状态，1200 调拨申请 1201 调拨审核 1202 调拨完成 1203 调拨失败 1204 调拨已审核 1205 已退还
    public static final String STATE_REVIEWED = "1204";//状态，1200 调拨申请 1201 调拨审核 1202 调拨完成 1203 调拨失败 1204 调拨已审核 1205 已退还
    public static final String STATE_RETURN = "1205";//状态，1200 调拨申请 1201 调拨审核 1202 调拨完成 1203 调拨失败 1204 调拨已审核 1205 已退还
    private String asId;
    private String[] asIds;
    private String storeId;
    private String bId;
    private String resId;
    private String shIdz;
    private String resName;
    private String startUserId;
    private String shIda;
    private String startUserName;
    private String state;
    private String stock;
    private String remark;
    private String stateName;
    private String shaName;
    private String shzName;
    private String resCode;
    private Date createTime;
    private String applyId;
    private String price;
    private String originalStock;
    private String rstName;
    private String parentRstName;
    private String specName;
    private String rstId;
    private String parentRstId;
    private String rssId;

    private String statusCd = "0";

    private String currentUserId;
    private String processInstanceId;
    private String taskId;
    private String auditCode;
    private String auditMessage;
    private String staffId;
    private String staffName;
    private String staffTel;
    private String applyType;
    private String applyTypeName;
    private String unitCodeName;
    private String miniUnitCodeName;

    private String isFixed;
    private String isFixedName;
    private String timesId;

    public String getAsId() {
        return asId;
    }

    public void setAsId(String asId) {
        this.asId = asId;
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

    public String getShIdz() {
        return shIdz;
    }

    public void setShIdz(String shIdz) {
        this.shIdz = shIdz;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getShIda() {
        return shIda;
    }

    public void setShIda(String shIda) {
        this.shIda = shIda;
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getShaName() {
        return shaName;
    }

    public void setShaName(String shaName) {
        this.shaName = shaName;
    }

    public String getShzName() {
        return shzName;
    }

    public void setShzName(String shzName) {
        this.shzName = shzName;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAuditCode() {
        return auditCode;
    }

    public void setAuditCode(String auditCode) {
        this.auditCode = auditCode;
    }

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffTel() {
        return staffTel;
    }

    public void setStaffTel(String staffTel) {
        this.staffTel = staffTel;
    }

    public String[] getAsIds() {
        return asIds;
    }

    public void setAsIds(String[] asIds) {
        this.asIds = asIds;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOriginalStock() {
        return originalStock;
    }

    public void setOriginalStock(String originalStock) {
        this.originalStock = originalStock;
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


    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplyTypeName() {
        return applyTypeName;
    }

    public void setApplyTypeName(String applyTypeName) {
        this.applyTypeName = applyTypeName;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
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

    public String getTimesId() {
        return timesId;
    }

    public void setTimesId(String timesId) {
        this.timesId = timesId;
    }
}
