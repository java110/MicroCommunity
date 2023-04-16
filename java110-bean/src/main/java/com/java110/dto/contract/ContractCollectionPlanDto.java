package com.java110.dto.contract;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 合同收款计划数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ContractCollectionPlanDto extends PageDto implements Serializable {

    private String contractId;
private String feeName;
private String planName;
private String planId;
private String remark;
private String storeId;
private String feeId;


    private Date createTime;

    private String statusCd = "0";


    public String getContractId() {
        return contractId;
    }
public void setContractId(String contractId) {
        this.contractId = contractId;
    }
public String getFeeName() {
        return feeName;
    }
public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
public String getPlanName() {
        return planName;
    }
public void setPlanName(String planName) {
        this.planName = planName;
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
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getFeeId() {
        return feeId;
    }
public void setFeeId(String feeId) {
        this.feeId = feeId;
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
