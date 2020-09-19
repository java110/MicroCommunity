package com.java110.user.bmo.userLogin;
import com.java110.dto.userLogin.UserLoginDto;
import org.springframework.http.ResponseEntity;
public interface IGetUserLoginBMO {


    /**
     * 查询用户登录
     * add by wuxw
     * @param  userLoginDto
     * @return
     */
    ResponseEntity<String> get(UserLoginDto userLoginDto);


}
