package com.java110.user.cmd.login;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.store.StoreUserDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.userLogin.UserLoginDto;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserLoginInnerServiceSMO;
import com.java110.po.userLogin.UserLoginPo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Java110CmdDoc(title = "运营团队admin登录到物业账号",
        description = "在admin账号下可以登录到管理的物业系统账号下",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/login.adminLoginProperty",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "login.adminLoginProperty",
        seq = 2
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "username", length = 30, remark = "需要登录的用户"),
        @Java110ParamDoc(name = "userId", length = 30, remark = "需要登录的用户ID"),
        @Java110ParamDoc(name = "curPasswd", length = 30, remark = "当前用户密码"),
        @Java110ParamDoc(name = "curUserName", length = 64, remark = "当前用户"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "userId", type = "String", remark = "用户ID"),
                @Java110ParamDoc(parentNodeName = "data",name = "token", type = "String", remark = "临时票据"),
        }
)

@Java110ExampleDoc(
        reqBody="{'username':'admin','userId':'123','curPassWd':'admin','curUserName':'18909711443'}",
        resBody="{'code':0,'msg':'成功','data':{'userId':'123123','token':'123213'}}"
)

@Java110Cmd(serviceCode = "login.adminLoginProperty")
public class AdminLoginPropertyCmd extends Cmd {

    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "username", "未包含需要登录的用户名");
        Assert.hasKeyAndValue(reqJson, "userId", "未包含需要登录的用户ID");
        Assert.hasKeyAndValue(reqJson, "curPasswd", "未包含当前用户的密码");
        Assert.hasKeyAndValue(reqJson, "curUserName", "未包含当前用户的用户名");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ResponseEntity<String> responseEntity = null;
        UserDto userDto = new UserDto();
        userDto.setUserName(reqJson.getString("curUserName"));
        userDto.setPassword(reqJson.getString("curPasswd"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        if (userDtos == null || userDtos.size() < 1) {
            context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_UNAUTHORIZED, "用户或密码错误"));
            return;
        }

        //校验当前账户商户是不是 管理员商户
        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setStoreTypeCd("800900000001");
        storeUserDto.setUserId(userDtos.get(0).getUserId());
        List<StoreUserDto> storeUserDtos = storeInnerServiceSMOImpl.getStoreUserInfo(storeUserDto);

        if (storeUserDtos == null || storeUserDtos.size() < 1) {
            context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_UNAUTHORIZED, "当前用户不是运营团队 不能免登录"));
            return;
        }

        // 校验 需要登录的物业账号是否存在
        userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        userDto.setUserName(reqJson.getString("username"));
        userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "物业账号不存在");

        //校验当前账户商户是不是 管理员商户
        storeUserDto = new StoreUserDto();
        storeUserDto.setStoreTypeCd("800900000003"); //物业账号
        storeUserDto.setUserId(userDtos.get(0).getUserId());
        storeUserDtos = storeInnerServiceSMOImpl.getStoreUserInfo(storeUserDto);

        if (storeUserDtos == null || storeUserDtos.size() < 1) {
            context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_UNAUTHORIZED, "需要免密登录的账号不是物业账号"));
            return;
        }

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
            context.setResponseEntity(responseEntity);
        } catch (Exception e) {
            throw new SMOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "系统内部错误，请联系管理员");
        }
    }
}
