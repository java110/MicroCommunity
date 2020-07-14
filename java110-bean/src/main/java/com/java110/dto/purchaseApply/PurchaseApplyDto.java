package com.java110.dto.purchaseApply;

import com.java110.dto.PageDto;
import com.java110.vo.api.purchaseApply.PurchaseApplyDetailVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 采购申请数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PurchaseApplyDto extends PageDto implements Serializable {

    public static final String RES_ORDER_TYPE_ENTER = "10000"; //采购入库
    public static final String RES_ORDER_TYPE_OUT = "20000"; //出库

    public static final String STATE_WAIT_DEAL = "1000"; // 等待处理
    public static final String STATE_DEALING = "1001"; // 审核中

    private String resOrderType;
    private String description;
    private String applyOrderId;
    private String[] applyOrderIds;
    private String bId;
    private String state;
    private String storeId;
    private String userName;
    private String userId;
    private String stateName;
    private String endUserName;
    private String endUserTel;
    private List<PurchaseApplyDetailVo> purchaseApplyDetailVo;

    private String createTime;

    private String statusCd = "0";

    private String currentUserId;
    private String processInstanceId;
    private String taskId;
    private String auditCode;
    private String auditMessage;
    private String staffId;
    private String staffName;
    private String staffTel;
    private String startUserId;




    public String getResOrderType() {
        return resOrderType;
    }

    public void setResOrderType(String resOrderType) {
        this.resOrderType = resOrderType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplyOrderId() {
        return applyOrderId;
    }

    public void setApplyOrderId(String applyOrderId) {
        this.applyOrderId = applyOrderId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
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

    public List<PurchaseApplyDetailVo> getPurchaseApplyDetailVo() {
        return purchaseApplyDetailVo;
    }

    public void setPurchaseApplyDetailVo(List<PurchaseApplyDetailVo> purchaseApplyDetailVo) {
        this.purchaseApplyDetailVo = purchaseApplyDetailVo;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }


    public String getEndUserName() {
        return endUserName;
    }

    public void setEndUserName(String endUserName) {
        this.endUserName = endUserName;
    }

    public String getEndUserTel() {
        return endUserTel;
    }

    public void setEndUserTel(String endUserTel) {
        this.endUserTel = endUserTel;
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

    public String[] getApplyOrderIds() {
        return applyOrderIds;
    }

    public void setApplyOrderIds(String[] applyOrderIds) {
        this.applyOrderIds = applyOrderIds;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }
}
