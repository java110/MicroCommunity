package com.java110.vo.api.auditUser;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiAuditUserVo extends MorePageVo implements Serializable {
    List<ApiAuditUserDataVo> auditUsers;


    public List<ApiAuditUserDataVo> getAuditUsers() {
        return auditUsers;
    }

    public void setAuditUsers(List<ApiAuditUserDataVo> auditUsers) {
        this.auditUsers = auditUsers;
    }
}
