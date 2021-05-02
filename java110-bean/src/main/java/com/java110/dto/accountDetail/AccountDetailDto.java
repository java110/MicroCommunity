package com.java110.dto.accountDetail;

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

    private String detailType;
private String amount;
private String orderId;
private String objId;
private String detailId;
private String acctId;
private String relAcctId;
private String remark;
private String objType;


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
}
