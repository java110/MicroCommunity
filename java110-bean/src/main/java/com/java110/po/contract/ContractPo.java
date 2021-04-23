package com.java110.po.contract;

import com.java110.po.contractFile.ContractFilePo;

import java.io.Serializable;
import java.util.List;

public class ContractPo implements Serializable {

    private String aLink;
    private String aContacts;
    private String amount;
    private String contractType;
    private String statusCd = "0";
    private String storeId;
    private String operator;
    private String signingTime;
    private String bContacts;
    private String partyA;
    private String bLink;
    private String partyB;
    private String contractId;
    private String objId;
    private String contractName;
    private String startTime;
    private String endTime;
    private String state;
    private String stateDesc;
    private String contractCode;
    private String objType;
    private String operatorLink;
    private String contractParentId;
    private String objName;
    private String objPersonName;
    private String objPersonId;
    private String startUserId;
    private List<ContractFilePo> contractFilePo;

    public String getaLink() {
        return aLink;
    }

    public void setaLink(String aLink) {
        this.aLink = aLink;
    }

    public String getaContacts() {
        return aContacts;
    }

    public void setaContacts(String aContacts) {
        this.aContacts = aContacts;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSigningTime() {
        return signingTime;
    }

    public void setSigningTime(String signingTime) {
        this.signingTime = signingTime;
    }

    public String getbContacts() {
        return bContacts;
    }

    public void setbContacts(String bContacts) {
        this.bContacts = bContacts;
    }

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getbLink() {
        return bLink;
    }

    public void setbLink(String bLink) {
        this.bLink = bLink;
    }

    public String getPartyB() {
        return partyB;
    }

    public void setPartyB(String partyB) {
        this.partyB = partyB;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getOperatorLink() {
        return operatorLink;
    }

    public void setOperatorLink(String operatorLink) {
        this.operatorLink = operatorLink;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getContractParentId() {
        return contractParentId;
    }

    public void setContractParentId(String contractParentId) {
        this.contractParentId = contractParentId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getObjPersonName() {
        return objPersonName;
    }

    public void setObjPersonName(String objPersonName) {
        this.objPersonName = objPersonName;
    }

    public String getObjPersonId() {
        return objPersonId;
    }

    public void setObjPersonId(String objPersonId) {
        this.objPersonId = objPersonId;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }
    public List<ContractFilePo> getContractFilePo() {
        return contractFilePo;
    }

    public void setContractFilePo(List<ContractFilePo> contractFilePo) {
        this.contractFilePo = contractFilePo;
    }

}
