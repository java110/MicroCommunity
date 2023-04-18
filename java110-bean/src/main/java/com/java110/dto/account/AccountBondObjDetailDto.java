package com.java110.dto.account;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 保证金明细数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AccountBondObjDetailDto extends PageDto implements Serializable {

    private String bobjId;
private String objId;
private String objName;
private String bondType;
private String detailId;
private String receivableAmount;
private String remark;
private String startTime;
private String receivedAmount;
private String state;
private String endTime;


    private Date createTime;

    private String statusCd = "0";


    public String getBobjId() {
        return bobjId;
    }
public void setBobjId(String bobjId) {
        this.bobjId = bobjId;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getBondType() {
        return bondType;
    }
public void setBondType(String bondType) {
        this.bondType = bondType;
    }
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
public String getReceivableAmount() {
        return receivableAmount;
    }
public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getStartTime() {
        return startTime;
    }
public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
public String getReceivedAmount() {
        return receivedAmount;
    }
public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getEndTime() {
        return endTime;
    }
public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }
}
