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
package com.java110.boot.smo.payment.adapt.plutuspay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.boot.properties.WechatAuthProperties;
import com.java110.boot.smo.DefaultAbstractComponentSMO;
import com.java110.boot.smo.payment.adapt.ITempCarFeeToNotifyAdapt;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.PlutusFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.TempCarPayOrderDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PayUtil;
import org.bouncycastle.util.encoders.Base64;
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

@Component(value = "plutusTempCarFeeToNotifyAdapt")
public class PlutusTempCarFeeToNotifyAdapt extends DefaultAbstractComponentSMO implements ITempCarFeeToNotifyAdapt {

    private static final Logger logger = LoggerFactory.getLogger(PlutusTempCarFeeToNotifyAdapt.class);

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

        JSONObject json = JSON.parseObject(param);

        String signature = json.getString("signature");
        String content = json.getString("content");

        String appId = WechatFactory.getAppId(wId);
        SmallWeChatDto smallWeChatDto = getSmallWechat(appId);
        if (smallWeChatDto == null) {
            throw new IllegalArgumentException("未配置公众号或者小程序信息");
        }
        String publicKey = CommunitySettingFactory.getRemark(smallWeChatDto.getObjId(),"PLUTUS_PUBLIC_KEY");
        //验签
        Boolean verify = PlutusFactory.verify256(content, org.bouncycastle.util.encoders.Base64.decode(signature),publicKey);
        //验签成功
        if (!verify) {
            throw new IllegalArgumentException("支付失败签名失败");
        }
        //解密
        byte[] bb = PlutusFactory.decrypt(Base64.decode(content), smallWeChatDto.getPayPassword());
        //服务器返回内容
        String paramOut = new String(bb);
        try {
            JSONObject map = JSONObject.parseObject(paramOut);
            logger.info("【银联支付回调】 回调数据： \n" + map);
            //更新数据
            int result = confirmPayFee(map, wId);
            if (result > 0) {
                //支付成功
                return "SUCCESS";
            }
        } catch (Exception e) {
            logger.error("通知失败", e);
            return "ERROR";
        }
        return "ERROR";
    }


    public int confirmPayFee(JSONObject map, String wId) {
        wId = wId.replace(" ", "+");

        ResponseEntity<String> responseEntity = null;

        String appId = WechatFactory.getAppId(wId);
        SmallWeChatDto smallWeChatDto = getSmallWechat(appId);

        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        }

        //String sign = PayUtil.createChinaUmsSign(paramMap, smallWeChatDto.getPayPassword());
        //JSONObject billPayment = JSONObject.parseObject(map.getString("billPayment"));
        String outTradeNo = map.getString("outTransId");

        //查询用户ID
        JSONObject paramIn = new JSONObject();
        paramIn.put("oId", outTradeNo);
        paramIn.put("payType", TempCarPayOrderDto.PAY_TYPE_WECHAT);
        String url = "tempCarFee.notifyTempCarFeeOrder";
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
