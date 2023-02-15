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
package com.java110.api.smo.payment.adapt.fuiouPay;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.payment.adapt.IPayAdapt;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 富友 支付
 * 此实现方式为  通过 富友支付 去下单不直接去掉微信
 * <p>
 * 商户调用此接口则用户可使用支付宝或微信进行支付。
 * 本接口支持： 微信公众号、 微信小程序、 微信 APP， 支付宝服务窗等
 * <p>
 * <p>
 * 说明：信息通过 http 或 https 形式 post 请求递交给前置系统，编码必须为 UTF-8
 * Json 格式参数名：如下表
 * 参数值：如下表
 * 测试地址： https://aipaytest.fuioupay.com/aggregatePay/wxPreCreate
 * 生产地址： https://aipay.fuioupay.com/aggregatePay/wxPreCreate
 * 生产地址 2： https://aipay-xs.fuioupay.com/aggregatePay/wxPreCreate
 * <p>
 * 该接口常应用于聚合二维码（静态二维码、统一收款码、台卡等不同叫法），用户扫二维码进入微信公众号/支付宝服务窗
 * /QQJS 页面，页面调此接口生成订单，接受订单参数后调起官方支付接口支付。详见公众号/服务窗对接流程
 * 步骤 1：用户通过支付宝(服务窗)、微信(公众号)进入到商户 H5 页面，或者是通过扫描台卡进入。
 * 步骤 2：用户选择商品、输入支付金额等进行下单支付
 * 步骤 3：商户将订单信息发送给富友，返回支付信息(用于调起支付宝、微信的参数)。
 * 步骤 4：商户拿到支付信息后调起微信或者支付宝进行支付
 * 步骤 5：支付结果以回调(2.5)的方式通知到商户
 *
 * @desc add by 吴学文 15:33
 */

@Component(value = "fuiouPayAdapt")
public class FuiouPayAdapt implements IPayAdapt {
    private static final Logger logger = LoggerFactory.getLogger(FuiouPayAdapt.class);

    //微信支付
    public static final String PAY_UNIFIED_ORDER_URL = "https://aipay.fuioupay.com/aggregatePay/wxPreCreate";


    private static final String VERSION = "1.0";

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

        JSONObject resMap = null;

        if (StringUtil.isEmpty(notifyUrl)) {
            resMap = this.java110UnifieldOrder(outRestTemplate, feeName, orderNum, tradeType, payAmount, openId, smallWeChatDto);
        } else {
            resMap = this.java110UnifieldOrder(outRestTemplate, feeName, orderNum, tradeType, payAmount, openId, smallWeChatDto, notifyUrl);
        }

        if ("000000".equals(resMap.getString("result_code"))) {
            if (WechatAuthProperties.TRADE_TYPE_JSAPI.equals(tradeType)) {
                resultMap.putAll(JSONObject.toJavaObject(JSONObject.parseObject(resMap.getString("reserved_pay_info")), Map.class));
                resultMap.put("sign",resultMap.get("paySign"));
            } else if (WechatAuthProperties.TRADE_TYPE_APP.equals(tradeType)) {
                resultMap.put("appId", smallWeChatDto.getAppId());
                resultMap.put("timeStamp", PayUtil.getCurrentTimeStamp());
                resultMap.put("nonceStr", PayUtil.makeUUID(32));
                resultMap.put("partnerid", smallWeChatDto.getMchId());
                resultMap.put("prepayid", resMap.getString("session_id"));
                //resultMap.put("signType", "MD5");
                resultMap.put("sign", PayUtil.createSign(resultMap, smallWeChatDto.getPayPassword()));
            } else if (WechatAuthProperties.TRADE_TYPE_NATIVE.equals(tradeType)) {
                resultMap.put("prepayId", resMap.getString("session_id"));
                resultMap.put("codeUrl", resMap.getString("qr_code"));
            }
            resultMap.put("code", "0");
            resultMap.put("msg", "下单成功");
            logger.info("【小程序支付】统一下单成功，返回参数:" + resultMap);
        } else {
            resultMap.put("code", resMap.getString("result_code"));
            resultMap.put("msg", resMap.getString("result_msg"));
            logger.info("【小程序支付】统一下单失败，失败原因:" + resMap.get("return_msg"));
        }
        return resultMap;
    }

    /**
     * 小程序支付统一下单
     */
    private JSONObject java110UnifieldOrder(RestTemplate outRestTemplate, String feeName, String orderNum,
                                            String tradeType, double payAmount, String openid,
                                            SmallWeChatDto smallWeChatDto) throws Exception {
        return java110UnifieldOrder(outRestTemplate, feeName, orderNum, tradeType, payAmount, openid, smallWeChatDto, wechatAuthProperties.getWxNotifyUrl());
    }

    /**
     * 小程序支付统一下单
     */
    private JSONObject java110UnifieldOrder(RestTemplate outRestTemplate, String feeName, String orderNum,
                                            String tradeType, double payAmount, String openid,
                                            SmallWeChatDto smallWeChatDto, String notifyUrl) throws Exception {

        String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);
        String orderPre = CommunitySettingFactory.getValue(smallWeChatDto.getObjId(), "FUIOU_ORDER_PRE");

        JSONObject paramMap = new JSONObject();
        paramMap.put("version", VERSION);
        paramMap.put("mchnt_cd", smallWeChatDto.getMchId()); // 富友分配给二级商户的商户号
        paramMap.put("random_str", PayUtil.makeUUID(32));
        paramMap.put("order_amt", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("mchnt_order_no", orderPre + orderNum);
        paramMap.put("txn_begin_ts", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_DEFAULT));
        paramMap.put("goods_des", systemName + feeName);
        paramMap.put("term_id", "abcdefgh");
        paramMap.put("term_ip", PayUtil.getLocalIp());
        paramMap.put("notify_url", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));
        paramMap.put("trade_type", tradeType);
        paramMap.put("sub_openid", openid);
        paramMap.put("sub_appid", smallWeChatDto.getAppId());

        paramMap.put("sign", createSign(paramMap, smallWeChatDto.getPayPassword()));

        logger.debug("调用支付统一下单接口" + paramMap.toJSONString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity httpEntity = new HttpEntity(paramMap.toJSONString(), headers);
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(
                PAY_UNIFIED_ORDER_URL, HttpMethod.POST, httpEntity, String.class);

        logger.debug("统一下单返回" + responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("支付失败" + responseEntity.getBody());
        }
        return JSONObject.parseObject(responseEntity.getBody());
    }

    /**
     * 富友 生成sign 方法
     *
     * @param paramMap
     * @param payPassword
     * @return
     */
    private String createSign(JSONObject paramMap, String payPassword) {
        String str = paramMap.getString("mchnt_cd") + "|"
                + paramMap.getString("trade_type") + "|"
                + paramMap.getString("order_amt") + "|"
                + paramMap.getString("mchnt_order_no") + "|"
                + paramMap.getString("txn_begin_ts") + "|"
                + paramMap.getString("goods_des") + "|"
                + paramMap.getString("term_id") + "|"
                + paramMap.getString("term_ip") + "|"
                + paramMap.getString("notify_url") + "|"
                + paramMap.getString("random_str") + "|"
                + paramMap.getString("version") + "|"
                + payPassword;
        return PayUtil.md5(str);
    }
}
