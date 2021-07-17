package com.java110.dto.fee;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @ClassName BillDto
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/28 17:11
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class BillDto extends PageDto implements Serializable {

    private String billId;
    private String billName;
    private String receivable;
    private String curReceivable;
    private String receipts;
    private String billTime;
    private String curBillTime;
    private String communityId;
    private String configId;
    private String createTime;
    private String remark;
    private String curBill;
    private String curBillName;
    private String feeName;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getReceivable() {
        return receivable;
    }

    public void setReceivable(String receivable) {
        this.receivable = receivable;
    }

    public String getReceipts() {
        return receipts;
    }

    public void setReceipts(String receipts) {
        this.receipts = receipts;
    }

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String billTime) {
        this.billTime = billTime;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCurBill() {
        return curBill;
    }

    public void setCurBill(String curBill) {
        this.curBill = curBill;
    }

    public String getCurReceivable() {
        return curReceivable;
    }

    public void setCurReceivable(String curReceivable) {
        this.curReceivable = curReceivable;
    }

    public String getCurBillTime() {
        return curBillTime;
    }

    public void setCurBillTime(String curBillTime) {
        this.curBillTime = curBillTime;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getCurBillName() {
        return curBillName;
    }

    public void setCurBillName(String curBillName) {
        this.curBillName = curBillName;
    }
}
