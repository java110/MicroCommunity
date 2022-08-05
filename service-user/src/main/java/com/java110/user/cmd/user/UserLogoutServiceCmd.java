package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Java110Cmd(serviceCode = "user.service.logout")
public class UserLogoutServiceCmd  extends Cmd {
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson,"token","请求报文中未包含token 节点请检查");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ResponseEntity responseEntity= null;
        try {
            //删除 token 信息
            AuthenticationFactory.deleteToken(reqJson.getString("token"));
            responseEntity = new ResponseEntity<String>("退出登录成功", HttpStatus.OK);
        } catch (Exception e) {
            //Invalid signature/claims
            responseEntity = new ResponseEntity<String>("退出登录失败，请联系管理员", HttpStatus.UNAUTHORIZED);
        }
        context.setResponseEntity(responseEntity);
    }
}
