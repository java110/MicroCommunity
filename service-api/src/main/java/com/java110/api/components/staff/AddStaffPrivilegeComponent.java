package com.java110.api.components.staff;


import com.java110.core.context.IPageData;
import com.java110.api.smo.IStaffServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 员工添加权限
 */
@Component("addStaffPrivilege")
public class AddStaffPrivilegeComponent {


    @Autowired
    private IStaffServiceSMO staffServiceSMOImpl;

    /**
     * 查询员工还没有添加的权限组
     */
    public ResponseEntity<String> listNoAddPrivilegeGroup(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = staffServiceSMOImpl.listNoAddPrivilegeGroup(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        return responseEntity;
    }

    /**
     * 查询员工还没有添加的权限
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> listNoAddPrivilege(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = staffServiceSMOImpl.listNoAddPrivilege(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        return responseEntity;
    }

    /**
     * 添加权限和权限组
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> addStaffPrivilegeOrPrivilegeGroup(IPageData pd) {

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = staffServiceSMOImpl.addStaffPrivilegeOrPrivilegeGroup(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        return responseEntity;
    }

    public IStaffServiceSMO getStaffServiceSMOImpl() {
        return staffServiceSMOImpl;
    }

    public void setStaffServiceSMOImpl(IStaffServiceSMO staffServiceSMOImpl) {
        this.staffServiceSMOImpl = staffServiceSMOImpl;
    }
}
