package com.java110.dto.contractType;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 合同类型数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ContractTypeDto extends PageDto implements Serializable {

    private String audit;
private String typeName;
private String remark;
private String storeId;
private String contractTypeId;


    private Date createTime;

    private String statusCd = "0";


    public String getAudit() {
        return audit;
    }
public void setAudit(String audit) {
        this.audit = audit;
    }
public String getTypeName() {
        return typeName;
    }
public void setTypeName(String typeName) {
        this.typeName = typeName;
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
public String getContractTypeId() {
        return contractTypeId;
    }
public void setContractTypeId(String contractTypeId) {
        this.contractTypeId = contractTypeId;
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
