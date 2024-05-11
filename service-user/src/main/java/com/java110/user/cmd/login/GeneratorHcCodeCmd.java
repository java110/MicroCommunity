package com.java110.user.cmd.login;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.user.UserDto;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "login.generatorHcCode")
public class GeneratorHcCodeCmd extends Cmd {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = CmdContextUtils.getUserId(context);
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        if (ListUtil.isNull(userDtos)) {
            throw new IllegalArgumentException("用户不存在");
        }

        //todo 调用商城接口 获取hcCode
        String hcCode = mallInnerServiceSMOImpl.generatorMallCode(userDtos.get(0));

//        String hcCode = PREFIX_CODE + GenerateCodeFactory.getUUID();
//        CommonCache.setValue(hcCode, JSONObject.toJSONString(userDtos.get(0)), CommonCache.defaultExpireTime);
        JSONObject paramOut = new JSONObject();
        paramOut.put("hcCode", hcCode);
        context.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }
}
