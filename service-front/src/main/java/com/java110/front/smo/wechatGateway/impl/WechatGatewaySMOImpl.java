package com.java110.front.smo.wechatGateway.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.factory.WechatFactory;
import com.java110.front.properties.WechatAuthProperties;
import com.java110.front.smo.AppAbstractComponentSMO;
import com.java110.front.smo.wechatGateway.IWechatGatewaySMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.Assert;
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

/**
 * wx登录
 */
@Service("ownerAppLoginSMOImpl")
public class WechatGatewaySMOImpl extends AppAbstractComponentSMO implements IWechatGatewaySMO {

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
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "fromUserName", "请求报文中未包含fromUserName");
        Assert.hasKeyAndValue(paramIn, "toUserName", "请求报文中未包含toUserName");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {

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
            responseStr = WechatFactory.formatText(toUserName, fromUserName, WechatConstant.NO_BIND_OWNER_RESPONSE_MESSAGE);
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

        ResponseEntity responseEntity = this.callCenterService(restTemplate, pd, "", ServiceConstant.SERVICE_API_URL + "/api/owner.listAppUserBindingOwners?openId=" + openId, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return true;
        }

        JSONObject ownerInfo = JSONObject.parseObject(responseEntity.getBody().toString());

        if (ownerInfo.getInteger("total") != 1) {
            return false;
        }
        return true;
    }

}
