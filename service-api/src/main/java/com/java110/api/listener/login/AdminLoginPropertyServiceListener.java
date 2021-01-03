/*
 * Copyright 2017-2021 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.api.listener.login;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.store.StoreUserDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.userLogin.UserLoginDto;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserLoginInnerServiceSMO;
import com.java110.po.userLogin.UserLoginPo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户注册 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("adminLoginPropertyServiceListener")
public class AdminLoginPropertyServiceListener extends AbstractServiceApiDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(AdminLoginPropertyServiceListener.class);


    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_ADMIN_LOGIN_PROPERTY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public int getOrder() {
        return 0;
    }

    private void validate(JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "username", "未包含需要登录的用户名");
        Assert.hasKeyAndValue(reqJson, "userId", "未包含需要登录的用户ID");
        Assert.hasKeyAndValue(reqJson, "curPasswd", "未包含当前用户的密码");
        Assert.hasKeyAndValue(reqJson, "curUserName", "未包含当前用户的用户名");
    }


    /**
     * @param event
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        //获取数据上下文对象
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        ResponseEntity<String> responseEntity = null;
        JSONObject paramIn = JSONObject.parseObject(dataFlowContext.getReqData());
        validate(paramIn);

        UserDto userDto = new UserDto();
        userDto.setUserName(paramIn.getString("curUserName"));
        userDto.setPassword(paramIn.getString("curPasswd"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        if (userDtos == null || userDtos.size() < 1) {
            responseEntity = new ResponseEntity<String>("用户或密码错误", HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return;
        }

        //校验当前账户商户是不是 管理员商户
        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setStoreTypeCd("800900000001");
        storeUserDto.setUserId(userDtos.get(0).getUserId());
        List<StoreUserDto> storeUserDtos = storeInnerServiceSMOImpl.getStoreUserInfo(storeUserDto);

        Assert.listOnlyOne(storeUserDtos, "当前用户不是运营团队 不能免登录");


        // 校验 需要登录的物业账号是否存在
        userDto = new UserDto();
        userDto.setUserId(paramIn.getString("userId"));
        userDto.setUserName(paramIn.getString("username"));
        userDtos = userInnerServiceSMOImpl.getUsers(userDto);
        Assert.listOnlyOne(userDtos, "需要登录的物业账号不存在");

        //校验当前账户商户是不是 管理员商户
        storeUserDto = new StoreUserDto();
        storeUserDto.setStoreTypeCd("800900000003"); //物业账号
        storeUserDto.setUserId(userDtos.get(0).getUserId());
        storeUserDtos = storeInnerServiceSMOImpl.getStoreUserInfo(storeUserDto);

        Assert.listOnlyOne(storeUserDtos, "需要免密登录的账号不是物业账号");

        userDto = userDtos.get(0);
        JSONObject userInfo = JSONObject.parseObject(JSONObject.toJSONString(userDto));
        try {
            Map userMap = new HashMap();
            userMap.put(CommonConstant.LOGIN_USER_ID, userDto.getUserId());
            userMap.put(CommonConstant.LOGIN_USER_NAME, userDto.getUserName());
            String token = AuthenticationFactory.createAndSaveToken(userMap);
            userInfo.remove("password");
            userInfo.put("token", token);
            //记录登录日志
            UserLoginPo userLoginPo = new UserLoginPo();
            userLoginPo.setLoginId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_loginId));
            userLoginPo.setLoginTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            userLoginPo.setPassword(userDto.getPassword());
            userLoginPo.setSource(UserLoginDto.SOURCE_WEB);
            userLoginPo.setToken(token);
            userLoginPo.setUserId(userInfo.getString("userId"));
            userLoginPo.setUserName(userInfo.getString("userName"));
            userLoginInnerServiceSMOImpl.saveUserLogin(userLoginPo);
            responseEntity = new ResponseEntity<String>(userInfo.toJSONString(), HttpStatus.OK);
            dataFlowContext.setResponseEntity(responseEntity);
        } catch (Exception e) {
            logger.error("登录异常：", e);
            throw new SMOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "系统内部错误，请联系管理员");
        }
    }

}
