package com.java110.user.bmo.userLogin.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.user.UserDto;
import com.java110.dto.userLogin.UserLoginDto;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserLoginInnerServiceSMO;
import com.java110.user.bmo.userLogin.IGetUserLoginBMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getUserLoginBMOImpl")
public class GetUserLoginBMOImpl implements IGetUserLoginBMO {

    public static final String PREFIX_CODE = "java110_";

    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;


    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

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

    /**
     * 生成HCCODE
     *
     * @param userDto
     * @return
     */
    @Override
    public ResponseEntity<String> generatorHcCode(UserDto userDto) {

        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        if(userDtos == null || userDtos.size()< 1){
            throw new IllegalArgumentException("用户不存在");
        }

        String hcCode = PREFIX_CODE + GenerateCodeFactory.getUUID();
        CommonCache.setValue(hcCode, JSONObject.toJSONString(userDtos.get(0)), CommonCache.defaultExpireTime);
        JSONObject paramOut = new JSONObject();
        paramOut.put("hcCode", hcCode);
        return ResultVo.createResponseEntity(paramOut);
    }

}
