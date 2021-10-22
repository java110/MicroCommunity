package com.java110.api.components;


import com.java110.core.context.IPageData;
import com.java110.api.smo.IPrivilegeServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("privilege")
public class PrivilegeComponent {


    @Autowired
    private IPrivilegeServiceSMO privilegeServiceSMOImpl;

    /**
     * 查询权限
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> listPrivilege(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = privilegeServiceSMOImpl.loadListPrivilege(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
