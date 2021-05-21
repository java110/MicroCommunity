package com.java110.dto.shopVipAccount;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 会员账户数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ShopVipAccountDto extends PageDto implements Serializable {


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

    private String amount;
    private String vipAcctId;
    private String vipId;
    private String acctType;
    private String acctTypeName;
    private String shopId;
    private String acctName;
    private String storeId;


    private Date createTime;

    private String statusCd = "0";


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVipAcctId() {
        return vipAcctId;
    }

    public void setVipAcctId(String vipAcctId) {
        this.vipAcctId = vipAcctId;
    }

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

}
