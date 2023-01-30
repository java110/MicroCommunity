package com.java110.api.smo.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.payment.IToPayInGoOutSMO;
import com.java110.core.context.IPageData;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("toPayInGoOutSMOImpl")
public class ToPayInGoOutSMOImpl extends AppAbstractComponentSMO implements IToPayInGoOutSMO {
    private static final Logger logger = LoggerFactory.getLogger(ToPayInGoOutSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;


    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> toPay(IPageData pd) {
        return super.businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        Assert.jsonObjectHaveKey(paramIn, "settingId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含settingId节点");

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {

        ResponseEntity responseEntity = null;


        String ownerUrl = UrlCache.getOwnerUrl()
                + "/#/pages/reportInfoDetail/reportInfoDetail?settingId=" +
                paramIn.getString("settingId") +
                "&communityId=" + paramIn.getString("communityId");
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        smallWeChatDto.setObjId(paramIn.getString("communityId"));
        List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);

        if (smallWeChatDtos != null && smallWeChatDtos.size() > 0) {
            ownerUrl += ("&wAppId=" + smallWeChatDtos.get(0).getAppId());
        }
        Map result = new HashMap();
        result.put("codeUrl", ownerUrl);
        responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);

        return responseEntity;
    }


}
