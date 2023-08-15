package com.java110.user.cmd.login;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.utils.util.TeldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Java110CmdDoc(title = "特来电获取token",
        description = "主要用于 特来电平台使用,<br/>" +
                "请求其他接口时 头信息中需要加 Authorization: Bearer token ，<br/>" +
                "token 是这个接口返回的内容<br/> " +
                "会话保持为2小时，请快要到2小时时，再次登录，保持会话</br>",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/ext/{appId}/query_token",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "login.getTokenForTeld",
        seq = 1
)

@Java110ParamsDoc(
        headers = {
                @Java110HeaderDoc(name = "Content-Type", defaultValue = "application/json", description = "application/json"),
        },
        params = {
                @Java110ParamDoc(name = "OperatorID", length = 30, remark = "调用方的组织机构代码"),
                @Java110ParamDoc(name = "OperatorSecret", length = 30, remark = "服务方分配的唯一识别密钥"),
        })

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "Ret", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "Msg", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "Data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "TokenAvailableTime", type = "int", remark = "有效期"),
                @Java110ParamDoc(parentNodeName = "data", name = "AccessToken", type = "String", remark = "临时票据"),
        }
)

@Java110ExampleDoc(
        reqBody = "{\n" +
                "  \"OperatorID\": \"123456789\",\n" +
                "  \"Data\": \"mYvffpNoFf4E/ZTC1tOw41TC5OlkEobfAYCm5N8hEusaLUaUIqOrXtdbMrSck0DSmfM7mRuOGMoCQzH0nWPGuw==\",\n" +
                "  \"TimeStamp\": \"20180120165755\",\n" +
                "  \"Seq\": \"0001\",\n" +
                "  \"Sig\": \"D2D584A14F3F284445DF85D0E8C0697C\"\n" +
                "}",
        resBody = "{\n" +
                "    \"Ret\": 0,\n" +
                "    \"Msg\": \"\",\n" +
                "    \"Data\": \"uxeKP0ezR5yL8xSg4/ZCDh/N91/u86NXFxd2DrwZVW8zCPYcpl59Twz/yQZ3RaO4rDDrGmkvQignmNEJ+k4PGxdmIC+4fpJ8rU6osSobY+AeA0uueuQ5+eQiWBL6p6v5XMMm91brtK8yfFELYUWQzVcxABnAwK/+dyxtUhqLIxUpkwTEU/4ktN40df9IzzlLO5uvUknPGYu9yL0pp5w9vdRxmA1RiiTDNCysz6klr9bunGV3VJa2qpLcgeZMf/oG\",\n" +
                "    \"Sig\": \"58E52010C7DEE87FE183B0AFA5B2BE30\"\n" +
                "}"
)
@Java110Cmd(serviceCode = "query_token")
public class QueryTokenForTeldCmd extends Cmd {


    public static String OPERATOR_SECRET = "1234567890abcdef"; //

    //
    public static String OPERATOR_ID = "123456789"; //9位组织机构代码
    //	// open平台地址
    public static String URL = "http://hlht.telaidian.com.cn:9501/evcs/v20161110/";

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "Data", "未包含加密报文");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String signKey = MappingCache.getValue("TELD_DOMAIN","SIGN_KEY");
        String aesKey = MappingCache.getValue("TELD_DOMAIN","AES_KEY");
        String aesIv = MappingCache.getValue("TELD_DOMAIN","AES_IV");

        String data = TeldUtil.Decrypt(reqJson.getString("Data"), aesKey, aesIv);

        if (StringUtil.isEmpty(data)) {
            throw new CmdException("未包含报文");
        }

        JSONObject dataObj = JSONObject.parseObject(data);


        UserDto userDto = new UserDto();
        userDto.setTel(dataObj.getString("OperatorID"));
        userDto.setPassword(AuthenticationFactory.md5UserPassword(dataObj.getString("OperatorSecret")));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        Map userMap = new HashMap();
        userMap.put(CommonConstant.LOGIN_USER_ID, userDtos.get(0).getUserId());
        userMap.put(CommonConstant.LOGIN_USER_NAME, userDtos.get(0).getUserName());
        String paramOut = "";
        try {
            String token = AuthenticationFactory.createAndSaveToken(userMap);
            JSONObject param = new JSONObject();
            param.put("OperatorID", dataObj.getString("OperatorID"));
            param.put("SuccStat", 0);
            param.put("AccessToken", token);
            param.put("TokenAvailableTime", 7200);
            param.put("FailReason", 0);
            paramOut = TeldUtil.generateReturnParam(param.toJSONString(), aesKey, aesIv, signKey, dataObj.getString("OperatorID"));
            context.setResponseEntity(new ResponseEntity(paramOut, HttpStatus.OK));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
