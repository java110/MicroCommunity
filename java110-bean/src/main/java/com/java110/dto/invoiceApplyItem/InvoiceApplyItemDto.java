package com.java110.dto.invoiceApplyItem;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 发票申请项数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class InvoiceApplyItemDto extends PageDto implements Serializable {

    private String itemId;
private String applyId;
private String itemType;
private String itemName;
private String itemAmount;
private String itemObjId;
private String remark;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getItemId() {
        return itemId;
    }
public void setItemId(String itemId) {
        this.itemId = itemId;
    }
public String getApplyId() {
        return applyId;
    }
public void setApplyId(String applyId) {
        this.applyId = applyId;
    }
public String getItemType() {
        return itemType;
    }
public void setItemType(String itemType) {
        this.itemType = itemType;
    }
public String getItemName() {
        return itemName;
    }
public void setItemName(String itemName) {
        this.itemName = itemName;
    }
public String getItemAmount() {
        return itemAmount;
    }
public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }
public String getItemObjId() {
        return itemObjId;
    }
public void setItemObjId(String itemObjId) {
        this.itemObjId = itemObjId;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
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
