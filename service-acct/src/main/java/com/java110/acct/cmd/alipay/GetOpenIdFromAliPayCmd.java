package com.java110.acct.cmd.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;

import java.text.ParseException;


@Java110Cmd(serviceCode = "alipay.getOpenIdFromAliPay")
public class GetOpenIdFromAliPayCmd extends Cmd {
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "authCode", "未包含authCode");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String communityId = reqJson.getString("communityId");
        String openId = "";
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                CommunitySettingFactory.getValue(communityId, "APP_ID"),
                CommunitySettingFactory.getRemark(communityId, "APP_PRIVATE_KEY"),
                "json",
                "UTF-8",
                CommunitySettingFactory.getRemark(communityId, "ALIPAY_PUBLIC_KEY"),
                "RSA2");
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(reqJson.getString("authCode"));
        request.setGrantType("authorization_code");
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
            AlipayUserInfoShareRequest requestUserInfo = new AlipayUserInfoShareRequest();
            AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(requestUserInfo, oauthTokenResponse.getAccessToken());
            openId = userinfoShareResponse.getUserId();
        } catch (Exception e) {
            //处理异常
            e.printStackTrace();
            throw new CmdException("获取用户信息失败"+e);
        }

        context.setResponseEntity(ResultVo.createResponseEntity(openId));

    }
}
