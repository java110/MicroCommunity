package com.java110.dto.contractChangePlan;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 合同变更计划数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ContractChangePlanDto extends PageDto implements Serializable {

    private String planType;
private String contractId;
private String planId;
private String remark;
private String changePerson;
private String state;
private String storeId;


    private Date createTime;

    private String statusCd = "0";


    public String getPlanType() {
        return planType;
    }
public void setPlanType(String planType) {
        this.planType = planType;
    }
public String getContractId() {
        return contractId;
    }
public void setContractId(String contractId) {
        this.contractId = contractId;
    }
public String getPlanId() {
        return planId;
    }
public void setPlanId(String planId) {
        this.planId = planId;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getChangePerson() {
        return changePerson;
    }
public void setChangePerson(String changePerson) {
        this.changePerson = changePerson;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
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
}
