package com.java110.dto.account;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 账户交易数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AccountDetailDto extends PageDto implements Serializable {


    //类型，1001 转入 2002 转出
    public static String DETAIL_TYPE_IN = "1001";
    public static String DETAIL_TYPE_IN_CANCEL = "3003";
    public static String DETAIL_TYPE_OUT = "2002";
    public static String ORDER_TYPE_USER = "6006";
    public static String ORDER_TYPE_STORE = "7007";

    private String detailType;
    private String amount;
    private String orderId;
    private String objId;
    private String detailId;
    private String acctId;
    private String acctName;
    private String relAcctId;
    private String remark;
    private String objType;
    private String acctType;
    private String bId;

    private Date createTime;

    private String statusCd = "0";


    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getRelAcctId() {
        return relAcctId;
    }

    public void setRelAcctId(String relAcctId) {
        this.relAcctId = relAcctId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }
}
