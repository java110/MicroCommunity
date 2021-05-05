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

    /**
     * 2003	现金账户
     * 2004	积分账户
     * 2005	优惠券账户
     * 2006	金币账户
     */
    public static final String ACCT_TYPE_CASH = "2003";
    public static final String ACCT_TYPE_INTEGRAL = "2004";
    public static final String ACCT_TYPE_COUPON = "2005";
    public static final String ACCT_TYPE_GOLD = "2006";


    //对象类型，6006 个人 7007 商户
    public static final String OBJ_TYPE_PERSON = "6006";
    public static final String OBJ_TYPE_STORE = "7007";
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
