package com.java110.api.components.staff;


import com.java110.core.context.IPageData;
import com.java110.api.smo.IStaffServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 搜索员工
 */
@Component("searchStaff")
public class SearchStaffComponent {

    @Autowired
    IStaffServiceSMO staffServiceSMOImpl;

    public ResponseEntity<String> listStaff(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = staffServiceSMOImpl.loadData(pd);
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
