package com.java110.acct.cmd.integral;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.MallDataDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;


/**
 * 查询用户积分
 * <p>
 * 积分在商城系统管理，所以这里需要调用商城系统接口查询
 */
@Java110Cmd(serviceCode = "integral.queryAppUserIntegral")
public class QueryAppUserIntegralCmd extends Cmd {

    @Autowired
    IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

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
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        reqJson.put("link", userDtos.get(0).getTel());
        ResultVo resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto("queryAppUserIntegralBmoImpl", reqJson));
        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));

    }
}
