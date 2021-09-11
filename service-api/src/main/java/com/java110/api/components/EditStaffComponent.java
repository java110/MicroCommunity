package com.java110.api.components;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IStaffServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑员工信息
 * Created by Administrator on 2019/4/3.
 */
@Component("editStaff")
public class EditStaffComponent {

    @Autowired
    IStaffServiceSMO staffServiceSMOImpl;

    /**
     * 修改员工信息
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> modifyStaff(IPageData pd) {

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = staffServiceSMOImpl.modifyStaff(pd);
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
