package com.java110.web.components.staff;


import com.java110.core.context.IPageData;
import com.java110.web.smo.IChangeStaffPwdServiceSMO;
import com.java110.web.smo.IStaffServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 搜索员工
 */
@Component("changeStaffPwd")
public class ChangeStaffPwdComponent {

    @Autowired
    IChangeStaffPwdServiceSMO changeStaffPwdServiceSMOImpl;

    public ResponseEntity<String> change(IPageData pd) {
        return changeStaffPwdServiceSMOImpl.change(pd);
    }

    public IChangeStaffPwdServiceSMO getChangeStaffPwdServiceSMOImpl() {
        return changeStaffPwdServiceSMOImpl;
    }

    public void setChangeStaffPwdServiceSMOImpl(IChangeStaffPwdServiceSMO changeStaffPwdServiceSMOImpl) {
        this.changeStaffPwdServiceSMOImpl = changeStaffPwdServiceSMOImpl;
    }
}
