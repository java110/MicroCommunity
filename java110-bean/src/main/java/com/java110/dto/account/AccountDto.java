package com.java110.dto.account;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 账户数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AccountDto extends PageDto implements Serializable {

    private String amount;
private String acctType;
private String objId;
private String acctId;
private String acctName;
private String objType;


    private Date createTime;

    private String statusCd = "0";


    public String getAmount() {
        return amount;
    }
public void setAmount(String amount) {
        this.amount = amount;
    }
public String getAcctType() {
        return acctType;
    }
public void setAcctType(String acctType) {
        this.acctType = acctType;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getAcctId() {
        return acctId;
    }
public void setAcctId(String acctId) {
        this.acctId = acctId;
    }
public String getAcctName() {
        return acctName;
    }
public void setAcctName(String acctName) {
        this.acctName = acctName;
    }
public String getObjType() {
        return objType;
    }
public void setObjType(String objType) {
        this.objType = objType;
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
