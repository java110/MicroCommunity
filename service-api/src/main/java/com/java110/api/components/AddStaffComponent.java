package com.java110.api.components;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IStaffServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加员工 组件
 * Created by Administrator on 2019/4/2.
 */
@Component("addStaff")
public class AddStaffComponent {

    @Autowired
    IStaffServiceSMO staffServiceSMOImpl;

    /**
     * 保存员工
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> saveStaff(IPageData pd) {

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = staffServiceSMOImpl.saveStaff(pd);
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
