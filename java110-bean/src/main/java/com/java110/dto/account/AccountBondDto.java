package com.java110.dto.account;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 保证金数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AccountBondDto extends PageDto implements Serializable {

    private String amount;
private String bondId;
private String bondMonth;
private String bondType;
private String objId;
private String remark;
private String bondName;


    private Date createTime;

    private String statusCd = "0";


    public String getAmount() {
        return amount;
    }
public void setAmount(String amount) {
        this.amount = amount;
    }
public String getBondId() {
        return bondId;
    }
public void setBondId(String bondId) {
        this.bondId = bondId;
    }
public String getBondMonth() {
        return bondMonth;
    }
public void setBondMonth(String bondMonth) {
        this.bondMonth = bondMonth;
    }
public String getBondType() {
        return bondType;
    }
public void setBondType(String bondType) {
        this.bondType = bondType;
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
public String getBondName() {
        return bondName;
    }
public void setBondName(String bondName) {
        this.bondName = bondName;
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
