package com.java110.po.accountWithdrawalApply;

import java.io.Serializable;
import java.util.Date;

public class AccountWithdrawalApplyPo implements Serializable {
    public static final String STATE_PASS="587"; //审核不通过
    public static final String STATE_ERROR_PAYER="687"; //付款失败

    private String applyId;
private String applyUserName;
private String amount;
private String context;
private String acctId;
private String statusCd = "0";
private String state = "486";
private String applyUserTel;
private String applyUserId;
private String bankId;
private String objId;
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
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
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
