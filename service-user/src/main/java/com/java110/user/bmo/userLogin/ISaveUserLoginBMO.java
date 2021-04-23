package com.java110.user.bmo.userLogin;

import com.java110.po.userLogin.UserLoginPo;
import org.springframework.http.ResponseEntity;
public interface ISaveUserLoginBMO {


    /**
     * 添加用户登录
     * add by wuxw
     * @param userLoginPo
     * @return
     */
    ResponseEntity<String> save(UserLoginPo userLoginPo);


}
