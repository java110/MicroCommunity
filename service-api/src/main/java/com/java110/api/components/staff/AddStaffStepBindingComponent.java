package com.java110.api.components.staff;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IStaffServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加添加员工组件
 */
@Component("addStaffStepBinding")
public class AddStaffStepBindingComponent {

    @Autowired
    private IStaffServiceSMO staffServiceSMOImpl;

    /**
     * 添加添加员工数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> binding(IPageData pd) {
        return staffServiceSMOImpl.saveStaff(pd);
    }

    public IStaffServiceSMO getStaffServiceSMOImpl() {
        return staffServiceSMOImpl;
    }

    public void setStaffServiceSMOImpl(IStaffServiceSMO staffServiceSMOImpl) {
        this.staffServiceSMOImpl = staffServiceSMOImpl;
    }
}
