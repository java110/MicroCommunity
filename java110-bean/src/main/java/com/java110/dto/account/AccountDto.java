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
    private String acctTypeName;
    private String objId;
    private String [] objIds;
    private String acctId;
    private String [] acctIds;
    private String acctName;
    private String objType;
    private String partId;
    private String hasMoney; // 1 标识有
    private String link;
    private String idCard;


    private Date createTime;

    private String statusCd = "0";

    //积分账户最大使用积分
    private String maximumNumber;

    //积分账户抵扣比例
    private String deductionProportion;

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

    public String getAcctTypeName() {
        return acctTypeName;
    }

    public void setAcctTypeName(String acctTypeName) {
        this.acctTypeName = acctTypeName;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getHasMoney() {
        return hasMoney;
    }

    public void setHasMoney(String hasMoney) {
        this.hasMoney = hasMoney;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String[] getObjIds() {
        return objIds;
    }

    public void setObjIds(String[] objIds) {
        this.objIds = objIds;
    }

    public String[] getAcctIds() {
        return acctIds;
    }

    public void setAcctIds(String[] acctIds) {
        this.acctIds = acctIds;
    }

    public String getMaximumNumber() {
        return maximumNumber;
    }

    public void setMaximumNumber(String maximumNumber) {
        this.maximumNumber = maximumNumber;
    }

    public String getDeductionProportion() {
        return deductionProportion;
    }

    public void setDeductionProportion(String deductionProportion) {
        this.deductionProportion = deductionProportion;
    }
}
