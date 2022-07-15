package com.java110.po.machine;

import java.io.Serializable;

/**
 * @ClassName MachineRecordPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/27 13:32
 * @Version 1.0
 * add by wuxw 2020/5/27
 **/
public class MachineRecordPo implements Serializable {
    private String machineRecordId;
    private String machineId;
    private String machineCode;
    private String name;
    private String openTypeCd;
    private String communityId;
    private String tel;
    private String idCard;
    private String recordTypeCd;
    private String fileId;
    private String fileTime;

    private String applyOrderId;
    private String purchaseUserId;
    private String resOrderType;
    private String noticeState;
    private String auditMessage;
    private String similar;
    private String statusCd;

    public String getMachineRecordId() {
        return machineRecordId;
    }

    public void setMachineRecordId(String machineRecordId) {
        this.machineRecordId = machineRecordId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenTypeCd() {
        return openTypeCd;
    }

    public void setOpenTypeCd(String openTypeCd) {
        this.openTypeCd = openTypeCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRecordTypeCd() {
        return recordTypeCd;
    }

    public void setRecordTypeCd(String recordTypeCd) {
        this.recordTypeCd = recordTypeCd;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public String getApplyOrderId() {
        return applyOrderId;
    }

    public void setApplyOrderId(String applyOrderId) {
        this.applyOrderId = applyOrderId;
    }

    public String getPurchaseUserId() {
        return purchaseUserId;
    }

    public void setPurchaseUserId(String purchaseUserId) {
        this.purchaseUserId = purchaseUserId;
    }

    public String getResOrderType() {
        return resOrderType;
    }

    public void setResOrderType(String resOrderType) {
        this.resOrderType = resOrderType;
    }

    public String getNoticeState() {
        return noticeState;
    }

    public void setNoticeState(String noticeState) {
        this.noticeState = noticeState;
    }

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }

    public String getSimilar() {
        return similar;
    }

    public void setSimilar(String similar) {
        this.similar = similar;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
