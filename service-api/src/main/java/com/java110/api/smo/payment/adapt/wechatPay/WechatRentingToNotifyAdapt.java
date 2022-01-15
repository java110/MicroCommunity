/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.api.smo.payment.adapt.wechatPay;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.payment.adapt.IPayNotifyAdapt;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * 微信通用 支付 通知实现
 * 此实现方式为 直接调用微信下单方式，不经过 第三方支付平台
 *
 * @desc add by 吴学文 15:33
 */

@Component(value = "wechatRentingToNotifyAdapt")
public class WechatRentingToNotifyAdapt extends DefaultAbstractComponentSMO implements IPayNotifyAdapt {

    private static final Logger logger = LoggerFactory.getLogger(WechatRentingToNotifyAdapt.class);

    private static final String APP_ID = "992020011134400001";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    /**
     * 预下单
     *
     * @param param
     * @return
     * @throws Exception
     */
    public String confirmPayFee(String param,String wId) {
        String resXml = "";
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

        return resXml;
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
        //String appId = WechatFactory.getAppId(wId);
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setAppId(wechatAuthProperties.getAppId());
        smallWeChatDto.setAppSecret(wechatAuthProperties.getSecret());
        smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
        smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        String sign = PayUtil.createSign(paramMap, smallWeChatDto.getPayPassword());

        if (!sign.equals(map.get("sign"))) {
            throw new IllegalArgumentException("鉴权失败");
        }

        String orderId = map.get("out_trade_no").toString();
        String order = CommonCache.getAndRemoveValue(RentingPoolDto.REDIS_PAY_RENTING + orderId);

        if (StringUtil.isEmpty(order)) {
            return 1;// 说明已经处理过了 再不处理
        }

        //查询用户ID
        JSONObject paramIn = JSONObject.parseObject(order);
        paramIn.put("oId", orderId);
        String url = "fee.rentingPayFeeConfirm";
        responseEntity = this.callCenterService(getHeaders("-1"), paramIn.toJSONString(), url, HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return 0;
        }
        return 1;
    }

    private Map<String, String> getHeaders(String userId) {
        Map<String, String> headers = new HashMap<>();
        headers.put(CommonConstant.HTTP_APP_ID.toLowerCase(), APP_ID);
        headers.put(CommonConstant.HTTP_USER_ID.toLowerCase(), userId);
        headers.put(CommonConstant.HTTP_TRANSACTION_ID.toLowerCase(), UUID.randomUUID().toString());
        headers.put(CommonConstant.HTTP_REQ_TIME.toLowerCase(), DateUtil.getDefaultFormateTimeString(new Date()));
        headers.put(CommonConstant.HTTP_SIGN.toLowerCase(), "");
        return headers;
    }

//    /**
//     * 调用中心服务
//     *
//     * @return
//     */
//    protected ResponseEntity<String> callCenterService(RestTemplate restTemplate, String userId, String param, String url, HttpMethod httpMethod) {
//
//        ResponseEntity<String> responseEntity = null;
//        HttpHeaders header = new HttpHeaders();
//        header.add(CommonConstant.HTTP_APP_ID.toLowerCase(), APP_ID);
//        header.add(CommonConstant.HTTP_USER_ID.toLowerCase(), userId);
//        header.add(CommonConstant.HTTP_TRANSACTION_ID.toLowerCase(), UUID.randomUUID().toString());
//        header.add(CommonConstant.HTTP_REQ_TIME.toLowerCase(), DateUtil.getDefaultFormateTimeString(new Date()));
//        header.add(CommonConstant.HTTP_SIGN.toLowerCase(), "");
//        HttpEntity<String> httpEntity = new HttpEntity<String>(param, header);
//        //logger.debug("请求中心服务信息，{}", httpEntity);
//        try {
//            responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
//        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
//            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
//        } catch (Exception e) {
//            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        } finally {
//            logger.debug("请求地址为,{} 请求中心服务信息，{},中心服务返回信息，{}", url, httpEntity, responseEntity);
//        }
//        return responseEntity;
//    }

}
