package com.java110.dto.account;

import com.java110.dto.account.AccountBankDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 账户提现数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AccountWithdrawalApplyDto extends AccountBankDto implements Serializable {

    private String applyId;
private String applyUserName;
private String amount;
private String context;
private String acctId;
private String state;
private String applyUserTel;
private String applyUserId;
private String stateName;
private String bankId;
private String objId;

    private Date createTime;

    private String statusCd = "0";

    public AccountWithdrawalApplyDto() {
    }


    public String getApplyId() {
        return applyId;
    }
public void setApplyId(String applyId) {
        this.applyId = applyId;
    }
public String getApplyUserName() {
        return applyUserName;
    }
public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }
public String getAmount() {
        return amount;
    }
public void setAmount(String amount) {
        this.amount = amount;
    }
public String getContext() {
        return context;
    }
public void setContext(String context) {
        this.context = context;
    }
public String getAcctId() {
        return acctId;
    }
public void setAcctId(String acctId) {
        this.acctId = acctId;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getApplyUserTel() {
        return applyUserTel;
    }
public void setApplyUserTel(String applyUserTel) {
        this.applyUserTel = applyUserTel;
    }
public String getApplyUserId() {
        return applyUserId;
    }
public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }
}
