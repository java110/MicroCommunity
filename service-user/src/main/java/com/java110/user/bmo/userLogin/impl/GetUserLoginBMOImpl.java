package com.java110.user.bmo.userLogin.impl;

import com.java110.dto.userLogin.UserLoginDto;
import com.java110.intf.IUserLoginInnerServiceSMO;
import com.java110.user.bmo.userLogin.IGetUserLoginBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getUserLoginBMOImpl")
public class GetUserLoginBMOImpl implements IGetUserLoginBMO {

    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;

    /**
     * @param userLoginDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(UserLoginDto userLoginDto) {


        int count = userLoginInnerServiceSMOImpl.queryUserLoginsCount(userLoginDto);

        List<UserLoginDto> userLoginDtos = null;
        if (count > 0) {
            userLoginDtos = userLoginInnerServiceSMOImpl.queryUserLogins(userLoginDto);
        } else {
            userLoginDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) userLoginDto.getRow()), count, userLoginDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
