package com.java110.user.cmd.wechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 获取小区微信AppId
 */
@Java110Cmd(serviceCode = "wechat.getCommunityWechatAppId")
public class GetCommunityWechatAppIdCmd extends Cmd {

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区ID");
        Assert.hasKeyAndValue(reqJson,"objType","未包含类型");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(reqJson.getString("communityId"));
        smallWeChatDto.setWechatType(reqJson.getString("objType"));
        smallWeChatDto.setWeChatType(reqJson.getString("objType"));
        List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);
        String appId = "";
        //todo 读取全局
        if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
            appId = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appId");
        } else {
            appId = smallWeChatDtos.get(0).getAppId();
        }

        context.setResponseEntity(ResultVo.createResponseEntity(appId));
    }
}
