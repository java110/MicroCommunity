package com.java110.api.smo.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.payment.IGoodsToNotifySMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PayUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service("goodsToNotifySMOImpl")
public class GoodsToNotifySMOImpl implements IGoodsToNotifySMO {
    private static final Logger logger = LoggerFactory.getLogger(AppAbstractComponentSMO.class);

    private static final String APP_ID = "992020011134400001";


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Override
    public ResponseEntity<String> toNotify(String param, HttpServletRequest request) {
        String resXml = "";
        ResponseEntity responseEntity = null;
        try {

            Map<String, Object> map = PayUtil.getMapFromXML(param);
            logger.info("【小程序支付回调】 回调数据： \n" + map);
            String returnCode = (String) map.get("return_code");
            if ("SUCCESS".equalsIgnoreCase(returnCode)) {
                String returnmsg = (String) map.get("result_code");
                if ("SUCCESS".equals(returnmsg)) {
                    //更新数据
                    int result = confirmPayFee(map);
                    if (result > 0) {
                        //支付成功
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml>";
                    }
                } else {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[报文为空]></return_msg>" + "</xml>";
                    logger.info("支付失败:" + resXml);
                }
            } else {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]></return_msg>" + "</xml>";
                logger.info("【订单支付失败】");
            }
        } catch (Exception e) {
            logger.error("通知失败", e);
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[鉴权失败]></return_msg>" + "</xml>";
        }

        logger.info("【小程序支付回调响应】 响应内容：\n" + resXml);
        return new ResponseEntity<String>(resXml, HttpStatus.OK);
    }

    public int confirmPayFee(Map<String, Object> map) {
        String wId = map.get("wId").toString();
        wId = wId.replace(" ", "+");
        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        ResponseEntity<String> responseEntity = null;
        for (String key : map.keySet()) {
            if ("wId".equals(key)) {
                continue;
            }
            paramMap.put(key, map.get(key).toString());
        }
        String appId = WechatFactory.getAppId(wId);
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setAppId(wechatAuthProperties.getAppId());
        smallWeChatDto.setAppSecret(wechatAuthProperties.getSecret());
        smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
        smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());

        String sign = PayUtil.createSign(paramMap, smallWeChatDto.getPayPassword());

        if (!sign.equals(map.get("sign"))) {
            throw new IllegalArgumentException("鉴权失败");
        }

        String outTradeNo = map.get("out_trade_no").toString();
        String openId = "";
        String paySwitch = MappingCache.getValue(AppAbstractComponentSMO.DOMAIN_WECHAT_PAY, AppAbstractComponentSMO.WECHAT_SERVICE_PAY_SWITCH);
        if (AppAbstractComponentSMO.WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
            openId = map.get("sub_openid").toString();
        } else {
            openId = map.get("openid").toString();
        }

        responseEntity = getUserInfoByOpenId(restTemplate, openId);

        logger.debug("查询用户信息返回报文：" + responseEntity);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("根绝openId 查询用户信息异常" + openId);
        }

        JSONObject userResult = JSONObject.parseObject(responseEntity.getBody());
        JSONObject realUserInfo = userResult.getJSONArray("data").getJSONObject(0);
        String useId = realUserInfo.getString("userId");

        //查询用户ID
        JSONObject paramIn = new JSONObject();
        paramIn.put("orderId", outTradeNo);
        String url = "storeOrder/updateStoreOrder";
        responseEntity = this.callCenterService(restTemplate, useId, paramIn.toJSONString(), url, HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return 0;
        }

        JSONObject data = JSONObject.parseObject(responseEntity.getBody());

        if (ResultVo.CODE_OK != data.getInteger("code")) {
            return 0;
        }
        return 1;
    }

    /**
     * 获取用户信息
     *
     * @param restTemplate
     * @return
     */
    protected ResponseEntity<String> getUserInfoByOpenId(RestTemplate restTemplate, String openId) {
        //Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = this.callCenterService(restTemplate, "-1", "",
                 ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS + "?openId=" + openId + "&page=1&row=1", HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}

        return responseEntity;

    }

    /**
     * 调用中心服务
     *
     * @return
     */
    protected ResponseEntity<String> callCenterService(RestTemplate restTemplate, String userId, String param, String url, HttpMethod httpMethod) {

        ResponseEntity<String> responseEntity = null;
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_APP_ID.toLowerCase(), APP_ID);
        header.add(CommonConstant.HTTP_USER_ID.toLowerCase(), userId);
        header.add(CommonConstant.HTTP_TRANSACTION_ID.toLowerCase(), UUID.randomUUID().toString());
        header.add(CommonConstant.HTTP_REQ_TIME.toLowerCase(), DateUtil.getDefaultFormateTimeString(new Date()));
        header.add(CommonConstant.HTTP_SIGN.toLowerCase(), "");
        header.add("content-type", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<String>(param, header);
        //logger.debug("请求中心服务信息，{}", httpEntity);
        try {
            responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("请求地址为,{} 请求中心服务信息，{},中心服务返回信息，{}", url, httpEntity, responseEntity);
        }
        return responseEntity;
    }


}
