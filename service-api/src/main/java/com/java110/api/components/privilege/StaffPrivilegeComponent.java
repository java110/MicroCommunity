package com.java110.api.components.privilege;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IPrivilegeServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("staffPrivilege")
public class StaffPrivilegeComponent {

    @Autowired
    private IPrivilegeServiceSMO privilegeServiceSMOImpl;

    /**
     * @param pd
     * @return
     */
    public ResponseEntity<String> listStaffPrivileges(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = privilegeServiceSMOImpl.listStaffPrivileges(pd);
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
