package com.java110.api.components;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IPrivilegeServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加权限组组件
 * <p>
 * add by wuxw
 * 2019-04-08
 */

@Component("addPrivilegeGroup")
public class AddPrivilegeGroupComponent {

    @Autowired
    private IPrivilegeServiceSMO privilegeServiceSMOImpl;


    /**
     * 保存权限组信息
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> savePrivilegeGroupInfo(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = privilegeServiceSMOImpl.savePrivilegeGroup(pd);
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
