package com.java110.dto.contract;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 合同类型规格数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ContractTypeSpecDto extends PageDto implements Serializable {

    private String specType;
private String specName;
private String specHoldplace;
private String specValueType;
private String specCd;
private String storeId;
private String specShow;
private String contractTypeId;
private String required;
private String listShow;


    private Date createTime;

    private String statusCd = "0";


    public String getSpecType() {
        return specType;
    }
public void setSpecType(String specType) {
        this.specType = specType;
    }
public String getSpecName() {
        return specName;
    }
public void setSpecName(String specName) {
        this.specName = specName;
    }
public String getSpecHoldplace() {
        return specHoldplace;
    }
public void setSpecHoldplace(String specHoldplace) {
        this.specHoldplace = specHoldplace;
    }
public String getSpecValueType() {
        return specValueType;
    }
public void setSpecValueType(String specValueType) {
        this.specValueType = specValueType;
    }
public String getSpecCd() {
        return specCd;
    }
public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getSpecShow() {
        return specShow;
    }
public void setSpecShow(String specShow) {
        this.specShow = specShow;
    }
public String getContractTypeId() {
        return contractTypeId;
    }
public void setContractTypeId(String contractTypeId) {
        this.contractTypeId = contractTypeId;
    }
public String getRequired() {
        return required;
    }
public void setRequired(String required) {
        this.required = required;
    }
public String getListShow() {
        return listShow;
    }
public void setListShow(String listShow) {
        this.listShow = listShow;
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
