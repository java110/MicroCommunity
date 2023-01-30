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
package com.java110.api.smo.payment.adapt.chinaums;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.ChinaUmsFactory;
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
 * 本接口为商户的订单H5页面向银商网络支付前置系统发起的支付跳转
 * 商户需遵循商户订单号生成规范，即以银商分配的4位来源编号作为账单号的前4位，且在商户系统中此账单号保证唯一。总长
 * 度需大于6位，小于28位。银商的推荐规则为（无特殊情况下，建议遵守此规则）：
 * {来源编号(4位)}{时间(yyyyMMddmmHHssSSS)(17位)}{7位随机数}
 *
 * @desc add by 吴学文 15:33
 */

@Component(value = "chinaUmsPayAdapt")
public class ChinaUmsPayAdapt implements IPayAdapt {
    private static final Logger logger = LoggerFactory.getLogger(ChinaUmsPayAdapt.class);

    //微信支付
    public static final String PAY_UNIFIED_ORDER_URL = "https://api-mop.chinaums.com/v1/netpay/wx/unified-order";


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

        if ("SUCCESS".equals(resMap.getString("errCode"))) {
            if (WechatAuthProperties.TRADE_TYPE_JSAPI.equals(tradeType)) {
                resultMap.putAll(JSONObject.toJavaObject(JSONObject.parseObject(resMap.getString("jsPayRequest")), Map.class));
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
            resultMap.put("code", resMap.getString("errCode"));
            resultMap.put("msg", resMap.getString("errMsg"));
            logger.info("【小程序支付】统一下单失败，失败原因:" + resMap.get("errMsg"));
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

        JSONObject paramMap = new JSONObject();
        paramMap.put("requestTimestamp", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        paramMap.put("mid", smallWeChatDto.getMchId()); // 富友分配给二级商户的商户号
        paramMap.put("tid", "CV5EW7IM"); //终端号
        paramMap.put("instMid", "YUEDANDEFAULT");
        paramMap.put("merOrderId", "11WP"+orderNum);
        paramMap.put("totalAmount", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("notifyUrl", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));
        paramMap.put("tradeType", tradeType);
        paramMap.put("subopenId", openid);
        paramMap.put("subAppId", smallWeChatDto.getAppId());

        logger.debug("调用支付统一下单接口" + paramMap.toJSONString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", ChinaUmsFactory.getAccessToken(smallWeChatDto));
        HttpEntity httpEntity = new HttpEntity(paramMap.toJSONString(), headers);
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(
                wechatAuthProperties.getWxPayUnifiedOrder(), HttpMethod.POST, httpEntity, String.class);

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
