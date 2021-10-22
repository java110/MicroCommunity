package com.java110.api.components.staff;


import com.java110.core.context.IPageData;
import com.java110.api.smo.staff.IResetStaffPwdServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 搜索员工
 */
@Component("resetStaffPwd")
public class ResetStaffPwdComponent {

    @Autowired
    IResetStaffPwdServiceSMO resetStaffPwdServiceSMOImpl;

    public ResponseEntity<String> reset(IPageData pd) {
        return resetStaffPwdServiceSMOImpl.reset(pd);
    }

    public IResetStaffPwdServiceSMO getResetStaffPwdServiceSMOImpl() {
        return resetStaffPwdServiceSMOImpl;
    }

    public void setResetStaffPwdServiceSMOImpl(IResetStaffPwdServiceSMO resetStaffPwdServiceSMOImpl) {
        this.resetStaffPwdServiceSMOImpl = resetStaffPwdServiceSMOImpl;
    }
}
