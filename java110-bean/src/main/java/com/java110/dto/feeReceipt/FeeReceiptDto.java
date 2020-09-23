package com.java110.dto.feeReceipt;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 收据数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeReceiptDto extends PageDto implements Serializable {

    private String amount;
private String objId;
private String remark;
private String objName;
private String communityId;
private String receiptId;
private String objType;


    private Date createTime;

    private String statusCd = "0";


    public String getAmount() {
        return amount;
    }
public void setAmount(String amount) {
        this.amount = amount;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getObjName() {
        return objName;
    }
public void setObjName(String objName) {
        this.objName = objName;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getReceiptId() {
        return receiptId;
    }
public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
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
