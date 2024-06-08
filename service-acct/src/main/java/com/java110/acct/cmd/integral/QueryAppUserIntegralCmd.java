package com.java110.acct.cmd.integral;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.utils.exception.CmdException;

import java.text.ParseException;


/**
 * 查询用户积分
 * <p>
 * 积分在商城系统管理，所以这里需要调用商城系统接口查询
 */
@Java110Cmd(serviceCode = "integral.queryAppUserIntegral")
public class QueryAppUserIntegralCmd extends Cmd {
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }
}
