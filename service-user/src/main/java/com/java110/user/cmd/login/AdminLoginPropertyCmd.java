package com.java110.user.cmd.login;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.cache.Java110RedisConfig;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.store.StoreUserDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.user.UserLoginDto;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserLoginInnerServiceSMO;
import com.java110.po.user.UserLoginPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.*;
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
                @Java110ParamDoc(parentNodeName = "data", name = "userId", type = "String", remark = "用户ID"),
                @Java110ParamDoc(parentNodeName = "data", name = "token", type = "String", remark = "临时票据"),
        }
)

@Java110ExampleDoc(
        reqBody = "{'username':'admin','userId':'123','curPassWd':'admin','curUserName':'18909711443'}",
        resBody = "{'code':0,'msg':'成功','data':{'userId':'123123','token':'123213'}}"
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
        reqJson.put("curPasswd", AuthenticationFactory.passwdMd5(reqJson.getString("curPasswd")));
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = CmdContextUtils.getUserId(context);
        ResponseEntity<String> responseEntity = null;
        UserDto userDto = new UserDto();
        userDto.setUserName(reqJson.getString("curUserName"));
        userDto.setPassword(reqJson.getString("curPasswd"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        if (ListUtil.isNull(userDtos)) {
            throw new CmdException("用户或密码错误");

        }

        // 校验 需要登录的物业账号是否存在
        userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        userDto.setUserName(reqJson.getString("username"));
        userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "物业账号不存在");

        //校验当前账户商户是不是 管理员商户

        userDto = userDtos.get(0);
        JSONArray data = new JSONArray();
        JSONObject userInfo = null;
        try {
            Map userMap = new HashMap();
            userMap.put(CommonConstant.LOGIN_USER_ID, userDto.getUserId());
            userMap.put(CommonConstant.LOGIN_USER_NAME, userDto.getName());
            String token = AuthenticationFactory.createAndSaveToken(userMap);
            userInfo = BeanConvertUtil.beanCovertJson(userDto);
            userInfo.remove("userPwd");
            userInfo.put("token", token);
            data.add(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SMOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "系统内部错误，请联系管理员");
        }


        //记录登录日志
        UserLoginPo userLoginPo = new UserLoginPo();
        userLoginPo.setLoginId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_loginId));
        userLoginPo.setLoginTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        userLoginPo.setPassword("******");
        userLoginPo.setSource(UserLoginDto.SOURCE_WEB);
        userLoginPo.setToken(userInfo.getString("token"));
        userLoginPo.setUserId(userInfo.getString("userId"));
        userLoginPo.setUserName(userInfo.getString("userName"));
        userLoginInnerServiceSMOImpl.saveUserLogin(userLoginPo);

        responseEntity = ResultVo.createResponseEntity(data);
        context.setResponseEntity(responseEntity);
    }


    /**
     * 清理用户缓存
     *
     * @param userId
     */
    private void clearUserCache(String userId) {
        //员工商户缓存 getStoreInfo
        String storeId = "";

        String storeInfo = CommonCache.getValue("getStoreInfo" + Java110RedisConfig.GET_STORE_INFO_EXPIRE_TIME_KEY + "::" + userId);
        if (!StringUtil.isEmpty(storeInfo)) {
            CommonCache.removeValue("getStoreInfo" + Java110RedisConfig.GET_STORE_INFO_EXPIRE_TIME_KEY + "::" + userId);
            JSONObject storeObj = JSONObject.parseObject(storeInfo);
            storeId = storeObj.getJSONObject("msg").getString("storeId");
            CommonCache.removeValue("getStoreEnterCommunitys" + Java110RedisConfig.GET_STORE_ENTER_COMMUNITYS_EXPIRE_TIME_KEY + "::" + storeId);
        }
        //员工权限
        CommonCache.removeValue("getUserPrivileges" + Java110RedisConfig.DEFAULT_EXPIRE_TIME_KEY + "::" + userId);
    }
}
