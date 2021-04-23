package com.java110.user.bmo.staffAppAuth;

import com.java110.po.staffAppAuth.StaffAppAuthPo;
import org.springframework.http.ResponseEntity;

public interface ISaveStaffAppAuthBMO {


    /**
     * 添加员工微信认证
     * add by wuxw
     *
     * @param staffAppAuthPo
     * @return
     */
    ResponseEntity<String> save(StaffAppAuthPo staffAppAuthPo);


}
