package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.Environment;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.user.UserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "user.resetSystemUserPwd")
public class ResetSystemUserPwdCmd extends Cmd {
    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;
    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Environment.isDevEnv();
        super.validateAdmin(context);
        Assert.hasKeyAndValue(reqJson,"staffId","未包含人员ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {


        UserDto userDto = new UserDto();
        userDto.setStatusCd("0");
        userDto.setUserId(reqJson.getString("staffId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUserHasPwd(userDto);

        Assert.listOnlyOne(userDtos, "数据错误查询到多条用户信息或单条");

        JSONObject userInfo = JSONObject.parseObject(JSONObject.toJSONString(userDtos.get(0)));
        String pwd = GenerateCodeFactory.getRandomCode(6);
        //userInfo.putAll(paramObj);
        userInfo.put("password", AuthenticationFactory.passwdMd5(pwd));


        UserPo userPo = BeanConvertUtil.covertBean(userInfo, UserPo.class);

        int flag = userV1InnerServiceSMOImpl.updateUser(userPo);
        if (flag < 1) {
            throw new CmdException("重置失败");
        }

        JSONObject paramOut = new JSONObject();
        paramOut.put("pwd", pwd);
        paramOut.put("code",0);
        paramOut.put("msg","成功");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }



}
