package com.java110.api.components.privilege;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IPrivilegeServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 权限组 添加权限
 */
@Component("addPrivilege")
public class AddPrivilegeComponent {

    @Autowired
    private IPrivilegeServiceSMO privilegeServiceSMOImpl;


    /**
     * 查询未被绑定权限
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> listNoAddPrivilege(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = privilegeServiceSMOImpl.listNoAddPrivilege(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        return responseEntity;
    }

    public ResponseEntity<String> addPrivilegeToPrivilegeGroup(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = privilegeServiceSMOImpl.addPrivilegeToPrivilegeGroup(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        return responseEntity;
    }

    public IPrivilegeServiceSMO getPrivilegeServiceSMOImpl() {
        return privilegeServiceSMOImpl;
    }

    public void setPrivilegeServiceSMOImpl(IPrivilegeServiceSMO privilegeServiceSMOImpl) {
        this.privilegeServiceSMOImpl = privilegeServiceSMOImpl;
    }
}

