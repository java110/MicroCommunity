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

import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.payment.adapt.IPayAdapt;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信通用 支付
 * 此实现方式为 直接调用微信下单方式，不经过 第三方支付平台
 *
 * @desc add by 吴学文 15:33
 */

@Component(value = "wechatPayAdapt")
public class WechatPayAdapt implements IPayAdapt {
    private static final Logger logger = LoggerFactory.getLogger(WechatPayAdapt.class);

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";


    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    /**
     * 预下单
     *
     * @param orderNum
     * @param money
     * @param openId
     * @return
     * @throws Exception
     */
    public Map<String, String> java110Payment(RestTemplate outRestTemplate,
                                              String feeName, String tradeType,
                                              String orderNum, double money,
                                              String openId, SmallWeChatDto smallWeChatDto) throws Exception {
        return java110Payment(outRestTemplate, feeName, tradeType, orderNum, money, openId, smallWeChatDto, "");
    }

    /**
     * 预下单
     *
     * @param orderNum
     * @param money
     * @param openId
     * @return
     * @throws Exception
     */
    public Map<String, String> java110Payment(RestTemplate outRestTemplate,
                                              String feeName, String tradeType,
                                              String orderNum, double money,
                                              String openId, SmallWeChatDto smallWeChatDto, String notifyUrl) throws Exception {
        logger.info("【小程序支付】 统一下单开始, 订单编号=" + orderNum);
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN,"HC_ENV"), money);
        //添加或更新支付记录(参数跟进自己业务需求添加)

        Map<String, String> resMap = null;

        if (StringUtil.isEmpty(notifyUrl)) {
            resMap = this.java110UnifieldOrder(outRestTemplate, feeName, orderNum, tradeType, payAmount, openId, smallWeChatDto);
        } else {
            resMap = this.java110UnifieldOrder(outRestTemplate, feeName, orderNum, tradeType, payAmount, openId, smallWeChatDto, notifyUrl);
        }

        if ("SUCCESS".equals(resMap.get("return_code")) && "SUCCESS".equals(resMap.get("result_code"))) {
            if (WechatAuthProperties.TRADE_TYPE_JSAPI.equals(tradeType)) {

                resultMap.put("appId", smallWeChatDto.getAppId());
                resultMap.put("timeStamp", PayUtil.getCurrentTimeStamp());
                resultMap.put("nonceStr", PayUtil.makeUUID(32));
                resultMap.put("package", "prepay_id=" + resMap.get("prepay_id"));
                resultMap.put("signType", "MD5");
                resultMap.put("sign", PayUtil.createSign(resultMap, smallWeChatDto.getPayPassword()));
            } else if (WechatAuthProperties.TRADE_TYPE_APP.equals(tradeType)) {
                resultMap.put("appId", smallWeChatDto.getAppId());
                resultMap.put("timeStamp", PayUtil.getCurrentTimeStamp());
                resultMap.put("nonceStr", PayUtil.makeUUID(32));
                resultMap.put("partnerid", smallWeChatDto.getMchId());
                resultMap.put("prepayid", resMap.get("prepay_id"));
                //resultMap.put("signType", "MD5");
                resultMap.put("sign", PayUtil.createSign(resultMap, smallWeChatDto.getPayPassword()));
            } else if (WechatAuthProperties.TRADE_TYPE_NATIVE.equals(tradeType)) {
                resultMap.put("prepayId", resMap.get("prepay_id"));
                resultMap.put("codeUrl", resMap.get("code_url"));
            }
            resultMap.put("code", "0");
            resultMap.put("msg", "下单成功");
            logger.info("【小程序支付】统一下单成功，返回参数:" + resultMap + "===notifyUrl===" + notifyUrl);
        } else {
            resultMap.put("code", resMap.get("return_code"));
            resultMap.put("msg", resMap.get("return_msg"));
            logger.info("【小程序支付】统一下单失败，失败原因:" + resMap.get("return_msg") + "===code===" + resMap.get("return_code") + "===notifyUrl===" + notifyUrl);
        }
        return resultMap;
    }

    /**
     * 小程序支付统一下单
     */
    private Map<String, String> java110UnifieldOrder(RestTemplate outRestTemplate, String feeName, String orderNum,
                                                     String tradeType, double payAmount, String openid,
                                                     SmallWeChatDto smallWeChatDto) throws Exception {
        return java110UnifieldOrder(outRestTemplate, feeName, orderNum, tradeType, payAmount, openid, smallWeChatDto, wechatAuthProperties.getWxNotifyUrl());
    }

    /**
     * 小程序支付统一下单
     */
    private Map<String, String> java110UnifieldOrder(RestTemplate outRestTemplate, String feeName, String orderNum,
                                                     String tradeType, double payAmount, String openid,
                                                     SmallWeChatDto smallWeChatDto, String notifyUrl) throws Exception {

        //String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);

        if (feeName.length() > 127) {
            feeName = feeName.substring(0, 126);
        }

        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("appid", smallWeChatDto.getAppId());
        paramMap.put("mch_id", smallWeChatDto.getMchId());
        paramMap.put("nonce_str", PayUtil.makeUUID(32));
        paramMap.put("body", feeName);
        paramMap.put("out_trade_no", orderNum);
        paramMap.put("total_fee", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("spbill_create_ip", PayUtil.getLocalIp());
        paramMap.put("notify_url", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));
        paramMap.put("trade_type", tradeType);
        paramMap.put("openid", openid);

        String paySwitch = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_PAY_SWITCH);
        if (WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
            paramMap.put("appid", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_APP_ID));  //服务商appid，是服务商注册时公众号的id
            paramMap.put("mch_id", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID));  //服务商商户号
            paramMap.put("sub_appid", smallWeChatDto.getAppId());//起调小程序appid
            paramMap.put("sub_mch_id", smallWeChatDto.getMchId());//起调小程序的商户号
            paramMap.put("sub_openid", openid);
            paramMap.remove("openid");
        }
        paramMap.put("sign", PayUtil.createSign(paramMap, smallWeChatDto.getPayPassword()));
//转换为xml
        String xmlData = PayUtil.mapToXml(paramMap);

        logger.debug("调用支付统一下单接口" + xmlData);

        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(
                wechatAuthProperties.getWxPayUnifiedOrder(), xmlData, String.class);

        logger.debug("统一下单返回" + responseEntity);
//请求微信后台，获取预支付ID
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("支付失败" + responseEntity.getBody());
        }
        return PayUtil.xmlStrToMap(responseEntity.getBody());
    }
}
