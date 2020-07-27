package com.java110.front.smo.wechatGateway.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.front.AbstractFrontServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.front.properties.WechatAuthProperties;
import com.java110.front.smo.wechatGateway.IWechatGatewaySMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.ServiceCodeSmallWechatAttrConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import java.util.List;

/**
 * wx登录
 */
@Service("wechatGatewaySMOImpl")
public class WechatGatewaySMOImpl extends AbstractFrontServiceSMO implements IWechatGatewaySMO {

    private final static Logger logger = LoggerFactory.getLogger(WechatGatewaySMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Override
    public ResponseEntity<String>
    gateway(IPageData pd) throws Exception {

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

            String noBindOwnerResponseMessage = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.NO_BIND_OWNER);

            if (StringUtil.isEmpty(noBindOwnerResponseMessage)) {

                noBindOwnerResponseMessage = WechatConstant.NO_BIND_OWNER_RESPONSE_MESSAGE;
            }

            responseStr = WechatFactory.formatText(toUserName, fromUserName, noBindOwnerResponseMessage);
            return new ResponseEntity<String>(responseStr, HttpStatus.OK);
        }

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
                ServiceConstant.SERVICE_API_URL + "/api/smallWeChat.listSmallWeChats?appId="
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
        if (StringUtils.isEmpty(keyword)) {
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

}
