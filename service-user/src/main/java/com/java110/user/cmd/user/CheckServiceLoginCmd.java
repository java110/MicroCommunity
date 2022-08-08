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

import javax.naming.AuthenticationException;
import java.text.ParseException;
import java.util.Map;

@Java110Cmd(serviceCode = "check.service.login")
public class CheckServiceLoginCmd extends Cmd {
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.jsonObjectHaveKey(reqJson,"token","请求报文中未包含token 节点请检查");
        ResponseEntity responseEntity= null;
        try {
            Map<String, String> claims = AuthenticationFactory.verifyToken(reqJson.getString("token"));
            if(claims == null || claims.isEmpty()){
                throw new AuthenticationException("认证失败，从token中解析到信息为空");
            }
            JSONObject resultInfo = new JSONObject();
            resultInfo.put("userId",claims.get("userId"));
            responseEntity = new ResponseEntity<String>(resultInfo.toJSONString(), HttpStatus.OK);
        } catch (Exception e) {
            //Invalid signature/claims
            responseEntity = new ResponseEntity<String>("认证失败，不是有效的token", HttpStatus.UNAUTHORIZED);
        }
        context.setResponseEntity(responseEntity);
    }
}
