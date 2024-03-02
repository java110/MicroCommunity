package com.java110.user.cmd.wechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.intf.user.IUserAttrV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.List;

/**
 * 获取小程序的openId
 */
@Java110Cmd(serviceCode = "wechat.getWechatMiniOpenId")
public class GetWechatMiniOpenIdCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(GetWechatMiniOpenIdCmd.class);

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Autowired
    private IUserAttrV1InnerServiceSMO userAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "code", "未包含code");
        Assert.hasKeyAndValue(reqJson, "appId", "未包含小程序ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String appId = "";
        String appSecret = "";

        if ("MALL".equals(reqJson.getString("appId"))) {
            appId = MappingCache.getValue(MappingConstant.MALL_WECHAT_DOMAIN, "wechatAppId");
            appSecret = MappingCache.getValue(MappingConstant.MALL_WECHAT_DOMAIN, "wechatAppSecret");
        } else {
            SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(reqJson.getString("appId"));
            String communityId = reqJson.getString("communityId");
            if (!StringUtil.isEmpty(communityId)) {
                smallWeChatDto.setObjId(communityId);
            }
            List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);

            //todo 读取全局
            if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
                appId = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appId");
                appSecret = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appSecret");
            } else {
                appId = smallWeChatDtos.get(0).getAppId();
                appSecret = smallWeChatDtos.get(0).getAppSecret();
            }
        }


        ResponseEntity<String> responseEntity;
        String code = reqJson.getString("code");
        String urlString = "https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={secret}&js_code={code}&grant_type={grantType}";
        String response = outRestTemplate.getForObject(
                urlString, String.class,
                appId,
                appSecret,
                code,
                "authorization_code");

        logger.debug("微信返回报文：" + response);

        //Assert.jsonObjectHaveKey(response, "errcode", "返回报文中未包含 错误编码，接口出错");
        JSONObject responseObj = JSONObject.parseObject(response);

        if (responseObj.containsKey("errcode") && !"0".equals(responseObj.getString("errcode"))) {
            throw new IllegalArgumentException("微信验证失败，可能是code失效" + responseObj);
        }

        String openId = responseObj.getString("openid");

        context.setResponseEntity(ResultVo.createResponseEntity(openId));
    }
}
