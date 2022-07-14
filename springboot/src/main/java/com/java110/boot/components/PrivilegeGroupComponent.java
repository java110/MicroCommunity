package com.java110.boot.components;

import com.java110.boot.smo.IPrivilegeServiceSMO;
import com.java110.core.context.IPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 权限组
 */

@Component("privilegeGroup")
public class PrivilegeGroupComponent {

    @Autowired
    private IPrivilegeServiceSMO privilegeServiceSMOImpl;

    /**
     * 查询权限组
     *
     * @param pd 页面
     * @return
     */
    public ResponseEntity<String> listPrivilegeGroup(IPageData pd) {

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = privilegeServiceSMOImpl.listPrivilegeGroup(pd);
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
