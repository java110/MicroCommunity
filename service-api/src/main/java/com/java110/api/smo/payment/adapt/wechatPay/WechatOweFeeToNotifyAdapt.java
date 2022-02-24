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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.payment.adapt.IOweFeeToNotifyAdapt;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * 微信通用 支付 通知实现
 * 此实现方式为 直接调用微信下单方式，不经过 第三方支付平台
 *
 * @desc add by 吴学文 15:33
 */

@Component(value = "wechatOweFeeToNotifyAdapt")
public class WechatOweFeeToNotifyAdapt extends DefaultAbstractComponentSMO implements IOweFeeToNotifyAdapt {

    private static final Logger logger = LoggerFactory.getLogger(WechatOweFeeToNotifyAdapt.class);

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
    public String confirmPayFee(String param, String wId) {
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
        String appId;
        //兼容 港币交易时 或者微信有时不会掉参数的问题
        if (map.containsKey("wId")) {
            String wId = map.get("wId").toString();
            wId = wId.replace(" ", "+");
            appId = WechatFactory.getAppId(wId);
        } else {
            appId = map.get("appid").toString();
        }

        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        ResponseEntity<String> responseEntity = null;
        for (String key : map.keySet()) {
            if ("wId".equals(key)) {
                continue;
            }
            paramMap.put(key, map.get(key).toString());
        }

        SmallWeChatDto smallWeChatDto = getSmallWechat(appId);

        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        }
        String sign = PayUtil.createSign(paramMap, smallWeChatDto.getPayPassword());

        if (!sign.equals(map.get("sign"))) {
            throw new IllegalArgumentException("鉴权失败");
        }

        String orderId = map.get("out_trade_no").toString();
        String order = CommonCache.getAndRemoveValue(FeeDto.REDIS_PAY_OWE_FEE + orderId);

        if (StringUtil.isEmpty(order)) {
            return 1;// 说明已经处理过了 再不处理
        }

        //查询用户ID
        JSONObject paramIn = JSONObject.parseObject(order);
        paramIn.put("oId", orderId);
        freshFees(paramIn);
        String url = "fee.payOweFee";
        responseEntity = this.callCenterService(getHeaders("-1"), paramIn.toJSONString(), url, HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return 0;
        }
        return 1;
    }

    private void freshFees(JSONObject paramIn) {
        if (!paramIn.containsKey("fees")) {
            return;
        }

        JSONArray fees = paramIn.getJSONArray("fees");
        JSONObject fee = null;
        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            fee = fees.getJSONObject(feeIndex);
            if (fee.containsKey("deadlineTime")) {
                fee.put("startTime", fee.getString("endTime"));
                fee.put("endTime", fee.getString("deadlineTime"));
                fee.put("receivedAmount", fee.getString("feePrice"));
                fee.put("state", "");
            }
        }
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

    private SmallWeChatDto getSmallWechat(String appId) {

        ResponseEntity responseEntity = null;

        responseEntity = this.callCenterService(getHeaders("-1"), "",
                "smallWeChat.listSmallWeChats?appId="
                        + appId + "&page=1&row=1", HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        JSONObject smallWechatObj = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONArray smallWeChats = smallWechatObj.getJSONArray("smallWeChats");

        if (smallWeChats == null || smallWeChats.size() < 1) {
            return null;
        }

        return BeanConvertUtil.covertBean(smallWeChats.get(0), SmallWeChatDto.class);
    }


}
