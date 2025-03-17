package com.java110.user.cmd.login;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.app.AppDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.user.UserLoginDto;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserLoginInnerServiceSMO;
import com.java110.po.user.UserLoginPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户登录 功能
 * 请求地址为/app/login.pcUserLogin
 */

@Java110CmdDoc(title = "员工登录",
        description = "登录功能 主要用于 员工 或者管理员登录使用,<br/>" +
                "请求其他接口时 头信息中需要加 Authorization: Bearer token ，<br/>" +
                "token 是这个接口返回的内容<br/> " +
                "会话保持为2小时，请快要到2小时时，再次登录，保持会话</br>",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/login.pcUserLogin",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "login.pcUserLogin",
        seq = 1
)

@Java110ParamsDoc(
        headers = {
                @Java110HeaderDoc(name = "APP-ID", defaultValue = "通过dev账户分配应用", description = "应用APP-ID"),
                @Java110HeaderDoc(name = "TRANSACTION-ID", defaultValue = "uuid", description = "交易流水号"),
                @Java110HeaderDoc(name = "REQ-TIME", defaultValue = "20220917120915", description = "请求时间 YYYYMMDDhhmmss"),
                @Java110HeaderDoc(name = "JAVA110-LANG", defaultValue = "zh-cn", description = "语言中文"),
                @Java110HeaderDoc(name = "USER-ID", defaultValue = "-1", description = "调用用户ID 一般写-1"),
        },
        params = {
                @Java110ParamDoc(name = "username", length = 30, remark = "用户名，物业系统分配"),
                @Java110ParamDoc(name = "passwd", length = 30, remark = "密码，物业系统分配"),
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
        reqBody = "{'username':'wuxw','passwd':'admin'}",
        resBody = "{'code':0,'msg':'成功','data':{'userId':'123123','token':'123213'}}"
)
@Java110Cmd(serviceCode = "login.pcUserLogin")
public class PcUserLoginCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(PcUserLoginCmd.class);
    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "username", "用户登录，未包含username节点，请检查");
        Assert.hasKeyAndValue(reqJson, "passwd", "用户登录，未包含passwd节点，请检查");

        AuthenticationFactory.checkLoginErrorCount(reqJson.getString("username"));

        String appId = CmdContextUtils.getAppId(cmdDataFlowContext);
        if(AppDto.PROPERTY_APP.equals(appId)){
            reqJson.put("passwd", AuthenticationFactory.passwdMd5(reqJson.getString("passwd")));
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ResponseEntity responseEntity = null;
        //根据AppId 查询 是否有登录的服务，查询登录地址调用
        UserDto userDto = new UserDto();
        userDto.setName(reqJson.getString("username"));
        userDto.setPassword(reqJson.getString("passwd"));
        userDto.setLevelCds(new String[]{UserDto.LEVEL_CD_ADMIN, UserDto.LEVEL_CD_STAFF});
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            userDto.setName("");
            userDto.setTel(reqJson.getString("username"));
            userDtos = userInnerServiceSMOImpl.getUsers(userDto);
        }
        if (ListUtil.isNull(userDtos)) {
            responseEntity = new ResponseEntity<String>("用户或密码错误", HttpStatus.UNAUTHORIZED);
            AuthenticationFactory.userLoginError(reqJson.getString("username"));
            cmdDataFlowContext.setResponseEntity(responseEntity);
            return;
        }

        //检查商户状态
        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(userDtos.get(0).getUserId());
        List<StoreUserDto> storeUserDtos = storeInnerServiceSMOImpl.getStoreUserInfo(storeUserDto);

        if (!ListUtil.isNull(storeUserDtos)) {
            String state = storeUserDtos.get(0).getState();
            if ("48002".equals(state)) {
                responseEntity = new ResponseEntity<String>("当前商户限制登录，请联系管理员", HttpStatus.UNAUTHORIZED);
                cmdDataFlowContext.setResponseEntity(responseEntity);
                return;
            }
        }
        UserDto allUserDto = new UserDto();
        allUserDto.setTel(userDtos.get(0).getTel());
        userDtos = userInnerServiceSMOImpl.getStaffs(userDto);
        if (userDtos.isEmpty()) {
            throw new CmdException("用户不存在");
        }
        JSONArray data = new JSONArray();
        JSONObject userInfo = null;
        for(UserDto aUserDto: userDtos) {
            try {
                Map userMap = new HashMap();
                userMap.put(CommonConstant.LOGIN_USER_ID, aUserDto.getUserId());
                userMap.put(CommonConstant.LOGIN_USER_NAME, aUserDto.getUserName());
                String token = AuthenticationFactory.createAndSaveToken(userMap);
                userInfo= BeanConvertUtil.beanCovertJson(aUserDto);
                userInfo.remove("userPwd");
                userInfo.put("token", token);
                data.add(userInfo);
            } catch (Exception e) {
                logger.error("登录异常：", e);
                throw new SMOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "系统内部错误，请联系管理员");
            }
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
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

}
