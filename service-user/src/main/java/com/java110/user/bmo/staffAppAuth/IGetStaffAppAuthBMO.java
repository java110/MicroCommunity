package com.java110.user.bmo.staffAppAuth;

import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import org.springframework.http.ResponseEntity;

public interface IGetStaffAppAuthBMO {


    /**
     * 查询员工微信认证
     * add by wuxw
     *
     * @param staffAppAuthDto
     * @return
     */
    ResponseEntity<String> get(StaffAppAuthDto staffAppAuthDto);


}
