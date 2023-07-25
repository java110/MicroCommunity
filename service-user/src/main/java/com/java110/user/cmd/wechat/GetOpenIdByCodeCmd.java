package com.java110.user.cmd.wechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;

/**
 * 根据小程序code 获取openId
 */

@Java110Cmd(serviceCode = "wechat.getOpenIdByCode")
public class GetOpenIdByCodeCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(GetOpenIdByCodeCmd.class);
    @Autowired
    private RestTemplate outRestTemplate;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "code", "未包含code");
        Assert.hasKeyAndValue(reqJson, "appId", "未包含小程序ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        if("MALL".equals(reqJson.getString("appId"))){
            MappingCache.getValue()
        }


        ResponseEntity<String> responseEntity;
        String code = reqJson.getString("code");
        String urlString = "https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={secret}&js_code={code}&grant_type={grantType}";
        String response = outRestTemplate.getForObject(
                urlString, String.class,
                smallWeChatDto.getAppId(),
                smallWeChatDto.getAppSecret(),
                code,
                wechatAuthProperties.getGrantType());

        logger.debug("wechatAuthProperties:" + JSONObject.toJSONString(wechatAuthProperties));

        logger.debug("微信返回报文：" + response);

        //Assert.jsonObjectHaveKey(response, "errcode", "返回报文中未包含 错误编码，接口出错");
        JSONObject responseObj = JSONObject.parseObject(response);

        if (responseObj.containsKey("errcode") && !"0".equals(responseObj.getString("errcode"))) {
            throw new IllegalArgumentException("微信验证失败，可能是code失效" + responseObj);
        }

        String openId = responseObj.getString("openid");
        String sessionKey = responseObj.getString("session_key");

    }
}
