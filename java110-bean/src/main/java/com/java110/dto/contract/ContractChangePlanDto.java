package com.java110.dto.contract;

import com.java110.dto.PageDto;
import com.java110.dto.contract.ContractDto;

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
public class ContractChangePlanDto extends ContractDto implements Serializable {

    public static final String STATE_W = "11";//11 待审核 22 审核通过 33 审核失败
    public static final String STATE_S = "22";//11 待审核 22 审核通过 33 审核失败
    public static final String STATE_F = "33";//11 待审核 22 审核通过 33 审核失败

    public static final String PLAN_TYPE_CHANGE_ROOM = "3003";

    public static final String PLAN_TYPE_CHANGE_RENT_DATE = "2002";

    private String planType;
    private String planTypeName;
    private String contractId;
    private String planId;
    private String[] planIds;
    private String remark;
    private String changePerson;
    private String changePersonName;
    private String state;
    private String storeId;
    private String nextUserId;


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

    public String getPlanTypeName() {
        return planTypeName;
    }

    public void setPlanTypeName(String planTypeName) {
        this.planTypeName = planTypeName;
    }

    public String getChangePersonName() {
        return changePersonName;
    }

    public void setChangePersonName(String changePersonName) {
        this.changePersonName = changePersonName;
    }

    public String[] getPlanIds() {
        return planIds;
    }

    public void setPlanIds(String[] planIds) {
        this.planIds = planIds;
    }

    @Override
    public String getNextUserId() {
        return nextUserId;
    }

    @Override
    public void setNextUserId(String nextUserId) {
        this.nextUserId = nextUserId;
    }
}
