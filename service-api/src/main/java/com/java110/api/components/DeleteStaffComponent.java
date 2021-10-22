package com.java110.api.components;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IStaffServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 删除员工信息
 * Created by Administrator on 2019/4/4.
 */
@Component("deleteStaff")
public class DeleteStaffComponent {

    @Autowired
    IStaffServiceSMO staffServiceSMOImpl;

    public ResponseEntity<String> delete(IPageData pd) {

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = staffServiceSMOImpl.delete(pd);
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
