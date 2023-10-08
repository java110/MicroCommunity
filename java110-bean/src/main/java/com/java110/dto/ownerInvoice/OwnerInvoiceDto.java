package com.java110.dto.ownerInvoice;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 业主发票数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OwnerInvoiceDto extends PageDto implements Serializable {

    private String oiId;
private String invoiceName;
private String ownerName;
private String invoiceType;
private String invoiceAddress;
private String remark;
private String ownerId;
private String communityId;
private String invoiceNum;


    private Date createTime;

    private String statusCd = "0";


    public String getOiId() {
        return oiId;
    }
public void setOiId(String oiId) {
        this.oiId = oiId;
    }
public String getInvoiceName() {
        return invoiceName;
    }
public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }
public String getOwnerName() {
        return ownerName;
    }
public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
public String getInvoiceType() {
        return invoiceType;
    }
public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }
public String getInvoiceAddress() {
        return invoiceAddress;
    }
public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getOwnerId() {
        return ownerId;
    }
public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getInvoiceNum() {
        return invoiceNum;
    }
public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
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
