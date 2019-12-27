package com.java110.vo.api.auditAppUserBindingOwner;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiAuditAppUserBindingOwnerVo extends MorePageVo implements Serializable {
    List<ApiAuditAppUserBindingOwnerDataVo> auditAppUserBindingOwners;


    public List<ApiAuditAppUserBindingOwnerDataVo> getAuditAppUserBindingOwners() {
        return auditAppUserBindingOwners;
    }

    public void setAuditAppUserBindingOwners(List<ApiAuditAppUserBindingOwnerDataVo> auditAppUserBindingOwners) {
        this.auditAppUserBindingOwners = auditAppUserBindingOwners;
    }
}
