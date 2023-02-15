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
package com.java110.api.smo.payment.adapt.plutuspay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.payment.adapt.IPayAdapt;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.PlutusFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * http://open.plutuspay.com/index.html
 *
 * @desc add by 吴学文 15:33
 */

@Component(value = "plutusPayAdapt")
public class PlutusPayAdapt implements IPayAdapt {
    private static final Logger logger = LoggerFactory.getLogger(PlutusPayAdapt.class);

    //微信支付
    public static final String PAY_UNIFIED_ORDER_URL = "https://api.plutuspay.com/open/v2/jsPay";
    public static final String PAY_UNIFIED_ORDER_NATIVE_URL = "https://api.plutuspay.com/open/v2/preCreate";


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

        if (WechatAuthProperties.TRADE_TYPE_JSAPI.equals(tradeType)) {
            resultMap.putAll(JSONObject.toJavaObject(resMap, Map.class));
            resultMap.put("sign", resultMap.get("paySign"));
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
//        } else {
//            resultMap.put("code", resMap.getString("errCode"));
//            resultMap.put("msg", resMap.getString("errMsg"));
//            logger.info("【小程序支付】统一下单失败，失败原因:" + resMap.get("errMsg"));
//        }
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

        paramMap.put("appId", smallWeChatDto.getAppId());
        paramMap.put("openId", openid);
        paramMap.put("sn", smallWeChatDto.getMchId()); // 富友分配给二级商户的商户号
        paramMap.put("outTradeId", orderNum);
        if (OwnerAppUserDto.APP_TYPE_WECHAT_MINA.equals(tradeType)) {
            paramMap.put("isMiniProgram", true);
        }
        paramMap.put("tradeAmount", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("payTypeId", "1003");
        paramMap.put("notifyUrl", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));

        logger.debug("调用支付统一下单接口" + paramMap.toJSONString());
        String privateKey = CommunitySettingFactory.getRemark(smallWeChatDto.getObjId(), "PLUTUS_PRIVATE_KEY");
        String devId = CommunitySettingFactory.getValue(smallWeChatDto.getObjId(), "PLUTUS_DEV_ID");

        String param = PlutusFactory.Encryption(paramMap.toJSONString(), privateKey, smallWeChatDto.getPayPassword(), devId);
        System.out.println(param);
        String str = "";
        if (WechatAuthProperties.TRADE_TYPE_NATIVE.equals(tradeType)) {
            str = PlutusFactory.post(PAY_UNIFIED_ORDER_NATIVE_URL, param);
        } else {
            str = PlutusFactory.post(PAY_UNIFIED_ORDER_URL, param);
        }
        System.out.println(str);

        JSONObject json = JSON.parseObject(str);

        String signature = json.getString("signature");
        String content = json.getString("content");
        String publicKey = CommunitySettingFactory.getRemark(smallWeChatDto.getObjId(), "PLUTUS_PUBLIC_KEY");

        //验签
        Boolean verify = PlutusFactory.verify256(content, Base64.decode(signature), publicKey);
        //验签成功
        if (!verify) {
            throw new IllegalArgumentException("支付失败签名失败");
        }
        //解密
        byte[] bb = PlutusFactory.decrypt(Base64.decode(content), smallWeChatDto.getPayPassword());
        //服务器返回内容
        String paramOut = new String(bb);

        JSONObject paramObj = JSONObject.parseObject(paramOut);
        logger.debug("统一下单返回" + paramOut);

        if (!"00".equals(paramObj.getString("status"))) {
            throw new IllegalArgumentException("支付失败" + paramObj.getString("error"));
        }

        if (WechatAuthProperties.TRADE_TYPE_NATIVE.equals(tradeType)) {
            paramObj.put("code", 0);
            return paramObj;
        } else {
            return paramObj.getJSONObject("payInfo");
        }
    }

    public static void main(String[] args) {
        String str = "{\n" +
                "  \"code\": 0,\n" +
                "  \"msg\": null,\n" +
                "  \"content\": \"UUVA4Zh6r0ZzZT1xik2VphAIACe0sNY0awk6do5uiWuOVgqq+4zVAEHTySOjhw5DEcMPIstWJIU7mJrUnm/3GCXq8T49YUFTH+JDJ11jwVUsiuu9HjS3I3bucYMM4xqrvnovqq0VEbP5mRJQY7pECtTa2KikB0wb7bB2hwrsOMJv/Ml2KRPXN59Xt0t42xbP0wxclkgg3iCrPEX79sfer12+wwCKFo028CqN4l/xFN4TelbCiEO35n5Aq+c3k+TzlE6FWJGJDfhu62WGpIOMugUwbNgbuVnmGb92Q5K6/OnTX4oAuOoE0C06jvEOjjiiOMpB2CsEwPAkhnWppu/yIAjZpjVbBEenjcE5Y6UiqCRhlzFVcPEgjIxR6/iuSxRn4KNKcSzeXxzhG5zXtdfHRDsMhbVLNZS6aTvTYLnC2oK9LexU4pomOyu9ZISmApiE2XmgIuzNx8FRrpcuEY5Uh29Y/yNoZghuAaKeqrFevVdzEdr9b+mQOk/CA6vAXxYi4UG1WGsQrAW72puUSWSo3gk9YhC5NYscKR2P3VYcVGsTpTsKxNw84A6tP9o3ubNdAMGFw9Gjjsw+iZlgTkvD6RmWRkgJ1nZcPn8uAw5t6ShMl2+PAZLfHAQbJjYd3y/lznff8tMv8ee50SJgDD07pHqaZXSaikJ1rTGKzB/NEDJFctmE2X18pvynu4Q9pwDhDMOOgKg6TCRqcaEDW+kqpg2+jO7/KnGabmAtO/ryW2p7VDbQpa3dEP05L/KliVsIYX4pQq6f8IunmxsBIs2cIHREI4HqH52OXSms3aHyKxgrEkABF79+teeCqE/YDhwg07/DcmHVkiIrPuo2qj2x+yS4/hMCxLsQhtoH3e1BK8U=\",\n" +
                " \"signature\": \"alnaEdVgctJkB0O2Qch1CtLUZS5qwr/pwSDFVoLqg6COCz3NAoKNhkPT7p1Ls4uaHq/j7cbG/XrHt0GQQ8UM+2z+DexheHZDG/qILpP4uI3Gkj8ER9z+yR2eGePoLh97WKCf09F/VqRL6Df9OJvPGw/R4rvbtTK85hXv7IxlPlx496/V4nokJumT3Ixb6j932vz1G4d3Jmeq8euvbpS5js6Ikq2p6XEFgAJUUODdmzp5ESch53vMzcV4P7Xsph/qtYLBls68B2b0xtyXusd0RfFTN7u5Ht9tsMki6SJbUEye+uxN2jdb/OjMDGpvpZFO0oozK5NnDNQFyOpafZICGA==\" }";
        JSONObject json = JSON.parseObject(str);

        String signature = json.getString("signature");
        String content = json.getString("content");
        //解密
        byte[] bb = PlutusFactory.decrypt(Base64.decode(content), "5475734f75736368496d565678494a6b");
        //服务器返回内容
        String paramOut = new String(bb);
        System.out.println(paramOut);
    }


}
