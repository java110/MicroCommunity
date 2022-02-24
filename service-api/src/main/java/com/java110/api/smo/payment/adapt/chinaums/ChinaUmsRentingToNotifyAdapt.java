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
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.payment.adapt.IPayNotifyAdapt;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 富友 支付 通知实现
 * 说明：信息通过 http 或 https 形式 post 请求递交给前置系统，编码必须为 UTF-8
 * Json 格式参数名：如下表
 * 参数值：如下表
 * 测试地址：商户提供
 * 生产地址：待定
 * <p>
 * 如图中第 6 步中异步回调，下单(主扫)交易的结果是以异步的形式进行回调的。富友在接受到支付宝等支付通道的回调结果以
 * 后再回调商户。商户接收回调成功处理成功后返回字符串”1” , 后富友停止回调给商户。最多回调 5 次，每次间隔 30S。
 * （重要~重要~重要：不保证通知最终一定能成功，在订单状态不明或者没有收到微信，支付结果通知的情况下，
 * 建议商户主动调用【2.3 订单查询】确认订单状态）
 * 只有主扫、公众号/服务窗支付会通过此接口发异步通知，条码支付没有异步通知。
 *
 * @desc add by 吴学文 15:33
 */

@Component(value = "chinaUmsRentingToNotifyAdapt")
public class ChinaUmsRentingToNotifyAdapt extends DefaultAbstractComponentSMO implements IPayNotifyAdapt {

    private static final Logger logger = LoggerFactory.getLogger(ChinaUmsRentingToNotifyAdapt.class);

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
        JSONObject resJson = new JSONObject();
        resJson.put("result_code", "010002");
        resJson.put("result_msg", "失败");
        try {
            JSONObject map = JSONObject.parseObject(param);
            logger.info("【富友支付回调】 回调数据： \n" + map);
            String resultCode = map.getString("result_code");
            if ("000000".equals(resultCode)) {
                //更新数据
                int result = confirmPayFee(map);
                if (result > 0) {
                    //支付成功
                    resJson.put("result_code", "000000");
                    resJson.put("result_msg", "成功");
                }
            }
        } catch (Exception e) {
            logger.error("通知失败", e);
            resJson.put("result_msg", "鉴权失败");
        }

        return resJson.toJSONString();
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


    public int confirmPayFee(JSONObject map) {

        //String appId = WechatFactory.getAppId(wId);
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setAppId(wechatAuthProperties.getAppId());
        smallWeChatDto.setAppSecret(wechatAuthProperties.getSecret());
        smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
        smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        String sign = createSign(map, smallWeChatDto.getPayPassword());

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
        ResponseEntity responseEntity = this.callCenterService(getHeaders("-1"), paramIn.toJSONString(), url, HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return 0;
        }
        return 1;
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
                + paramMap.getString("mchnt_order_no") + "|"
                + paramMap.getString("settle_order_amt") + "|"
                + paramMap.getString("order_amt") + "|"
                + paramMap.getString("txn_fin_ts") + "|"
                + paramMap.getString("reserved_fy_settle_dt") + "|"
                + paramMap.getString("random_str") + "|"
                + payPassword;
        return PayUtil.md5(str);
    }

}
