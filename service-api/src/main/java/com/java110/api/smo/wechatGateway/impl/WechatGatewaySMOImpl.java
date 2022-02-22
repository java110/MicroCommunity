package com.java110.api.smo.wechatGateway.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.wechatGateway.IWechatGatewaySMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.wechatSubscribe.WechatSubscribeDto;
import com.java110.intf.user.IWechatSubscribeV1InnerServiceSMO;
import com.java110.po.wechatSubscribe.WechatSubscribePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.ServiceCodeSmallWechatAttrConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * wx登录
 */
@Service("wechatGatewaySMOImpl")
public class WechatGatewaySMOImpl extends DefaultAbstractComponentSMO implements IWechatGatewaySMO {

    private final static Logger logger = LoggerFactory.getLogger(WechatGatewaySMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Autowired
    private IWechatSubscribeV1InnerServiceSMO wechatSubscribeV1InnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> gateway(IPageData pd, String wId) throws Exception {

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "fromUserName", "请求报文中未包含fromUserName");
        Assert.hasKeyAndValue(paramIn, "toUserName", "请求报文中未包含toUserName");

        logger.debug("doLogin入参：" + paramIn.toJSONString());
        String responseStr = "";

        String fromUserName = paramIn.getString("fromUserName");
        String toUserName = paramIn.getString("toUserName");
        String keyword = paramIn.getString("keyword");
        String msgType = paramIn.getString("msgType");
        String event = paramIn.getString("event");
        String eventKey = paramIn.getString("eventKey");
        //判断用户是否绑定业主
        boolean bindFlag = judgeBindOwner(pd, fromUserName);
        if (!bindFlag) {

            String noBindOwnerResponseMessage = MappingCache.getRemark(WechatConstant.WECHAT_DOMAIN, WechatConstant.NO_BIND_OWNER);

            if (StringUtil.isEmpty(noBindOwnerResponseMessage)) {

                noBindOwnerResponseMessage = WechatConstant.NO_BIND_OWNER_RESPONSE_MESSAGE;
            }
            if (!noBindOwnerResponseMessage.contains("wAppId=")) {
                if (noBindOwnerResponseMessage.indexOf("W_APP_ID") > 0) {
                    noBindOwnerResponseMessage = noBindOwnerResponseMessage.replace("W_APP_ID", "wAppId=" + WechatFactory.getAppId(wId));
                } else {
                    if (noBindOwnerResponseMessage.indexOf("?") > -1) {
                        noBindOwnerResponseMessage += ("&wAppId=" + WechatFactory.getAppId(wId));
                    } else {
//                        noBindOwnerResponseMessage += ("?wAppId=" + WechatFactory.getAppId(wId));
                    }
                }
            }

            responseStr = WechatFactory.formatText(toUserName, fromUserName, noBindOwnerResponseMessage);
            return new ResponseEntity<String>(responseStr, HttpStatus.OK);
        }
        //保存到关注表
        saveWechatSubscribe(WechatFactory.getAppId(wId), fromUserName);

        if (WechatConstant.MSG_TYPE_TEXT.equals(msgType)) {
            responseStr = textResponseHandler(fromUserName, toUserName, keyword);
        } else if (WechatConstant.MSG_TYPE_EVENT.equals(msgType)) {
            responseStr = eventResponseHandler(fromUserName, toUserName, keyword, event, eventKey);
        } else {
            responseStr = eventResponseHandler(fromUserName, toUserName, keyword, event, eventKey);
        }
        return new ResponseEntity<>(responseStr, HttpStatus.OK);
    }


    @Override
    public SmallWeChatDto getSmallWechat(IPageData pd, SmallWeChatDto smallWeChatDto) {

        //List<SmallWeChatDto> smallWeChatDtos = super.getForApis(pd, smallWeChatDto, ServiceCodeSmallWeChatConstant.LIST_SMALL_WE_CHATS, SmallWeChatDto.class);
        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                "smallWeChat.listSmallWeChats?appId="
                        + smallWeChatDto.getAppId() + "&page=1&row=1", HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        JSONObject smallWechatObj = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONArray smallWeChats = smallWechatObj.getJSONArray("smallWeChats");


        if (smallWeChats == null || smallWeChats.size() < 1) {
            return null;
        }
        smallWeChatDto = BeanConvertUtil.covertBean(smallWeChats.get(0), SmallWeChatDto.class);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(smallWeChatDto.getObjId());
        smallWechatAttrDto.setWechatId(smallWeChatDto.getWeChatId());
        List<SmallWechatAttrDto> smallWechatAttrs = super.getForApis(pd, smallWechatAttrDto, ServiceCodeSmallWechatAttrConstant.LIST_SMALLWECHATATTRS, SmallWechatAttrDto.class);

        smallWeChatDto.setSmallWechatAttrs(smallWechatAttrs);
        return smallWeChatDto;
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 文字处理
     *
     * @param fromUserName
     * @param toUserName
     * @param keyword
     * @return
     */
    private String textResponseHandler(String fromUserName, String toUserName,
                                       String keyword) {
        if (StringUtil.isEmpty(keyword)) {
            return WechatFactory
                    .formatText(toUserName, fromUserName, "未包含任何信息");
        } else {
            String responseStr = keyWordHandler(fromUserName, toUserName,
                    keyword);
            return WechatFactory
                    .formatText(toUserName, fromUserName, responseStr);
        }
    }

    /**
     * 事件处理
     *
     * @param fromUserName
     * @param toUserName
     * @param keyword
     * @return
     */
    private String keyWordHandler(String fromUserName, String toUserName,
                                  String keyword) {
        // TODO Auto-generated method stub
        String domain = WechatConstant.TOKEN;
        //String url = domain + "/IMSS/indexPage.do";
        String remark = MappingCache.getRemark(WechatConstant.WECHAT_DOMAIN, WechatConstant.WELCOME);

        if (StringUtil.isEmpty(remark)) {
            remark = WechatConstant.DEFAULT_WELCOME;
        }

        return remark;
    }

    /**
     * 事件处理
     *
     * @param fromUserName
     * @param toUserName
     * @param event
     * @param eventKey
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"unchecked"})
    public String eventResponseHandler(String fromUserName, String toUserName, String keyWords, String event,
                                       String eventKey) throws Exception {
        String resultStr = "";
        //
        if (event.equals("subscribe")) {
            resultStr = MappingCache.getRemark(WechatConstant.WECHAT_DOMAIN, WechatConstant.WELCOME);
        } else if (event.equals("unsubscribe")) {

        } else if (event.equalsIgnoreCase("CLICK")) {
            resultStr = textResponseHandler(fromUserName, toUserName,
                    eventKey);
        } else {

        }
        if (StringUtil.isEmpty(resultStr)) {
            resultStr = WechatConstant.DEFAULT_WELCOME;
        }
        return WechatFactory.formatText(toUserName, fromUserName, resultStr);
    }

    /**
     * 判断是否绑定业主
     *
     * @param openId
     * @return
     */
    private boolean judgeBindOwner(IPageData pd, String openId) {
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setOpenId(openId);
        List<OwnerAppUserDto> ownerAppUserDtos = super.getForApis(pd, ownerAppUserDto, ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS, OwnerAppUserDto.class);

        if (ownerAppUserDtos == null || ownerAppUserDtos.size() != 1) {
            return false;
        }
        return true;
    }


    /**
     * 校验是否在关注表里
     *
     * @param appId
     * @param fromUserName
     */
    private void saveWechatSubscribe(String appId, String fromUserName) {
        WechatSubscribeDto wechatSubscribeDto = new WechatSubscribeDto();
        wechatSubscribeDto.setOpenId(fromUserName);
        wechatSubscribeDto.setOpenId(appId);
        List<WechatSubscribeDto> wechatSubscribeDtos = wechatSubscribeV1InnerServiceSMOImpl.queryWechatSubscribes(wechatSubscribeDto);
        if (wechatSubscribeDtos != null && wechatSubscribeDtos.size() > 0) {
            return;
        }
        WechatSubscribePo wechatSubscribePo = new WechatSubscribePo();
        wechatSubscribePo.setAppId(appId);
        wechatSubscribePo.setOpenId(fromUserName);
        wechatSubscribePo.setSubId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
        wechatSubscribePo.setOpenType(WechatSubscribeDto.OPEN_TYPE_WECHAT);
        wechatSubscribeV1InnerServiceSMOImpl.saveWechatSubscribe(wechatSubscribePo);
    }

}
