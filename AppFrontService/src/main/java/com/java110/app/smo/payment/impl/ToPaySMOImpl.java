package com.java110.app.smo.payment.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.app.properties.WechatAuthProperties;
import com.java110.app.smo.AppAbstractComponentSMO;
import com.java110.app.smo.payment.IToPaySMO;
import com.java110.core.context.IPageData;
import com.java110.dto.order.WxOrderDto;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.PayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ToPaySMOImpl extends AppAbstractComponentSMO implements IToPaySMO {
    private static final Logger logger = LoggerFactory.getLogger(AppAbstractComponentSMO.class);


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Override
    public ResponseEntity<String> toPay(IPageData pd) {
        return super.businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(paramIn, "cycles", "请求报文中未包含cycles节点");
        Assert.jsonObjectHaveKey(paramIn, "receivedAmount", "请求报文中未包含receivedAmount节点");
        Assert.jsonObjectHaveKey(paramIn, "feeId", "请求报文中未包含feeId节点");

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {

        ResponseEntity responseEntity = null;
        //查询用户ID
        paramIn.put("userId", pd.getUserId());
        String url = ServiceConstant.SERVICE_API_URL + "/api/fee.payFeePre";
        responseEntity = super.callCenterService(restTemplate, pd, paramIn.toJSONString(), url, HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        JSONObject orderInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        String orderId = orderInfo.getString("oId");
        double money = Double.parseDouble(orderInfo.getString("receivableAmount"));
        Map tmpParamIn = new HashMap();
        tmpParamIn.put("userId", pd.getUserId());
        responseEntity = super.getUserAndAttr(pd, restTemplate, tmpParamIn);
        logger.debug("查询用户信息返回报文：" + responseEntity);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("未查询用户信息异常" + tmpParamIn);
        }

        JSONObject userResult = JSONObject.parseObject(responseEntity.getBody().toString());
        int total = userResult.getIntValue("total");
        if(total < 1){
            //未查询到用户信息
            throw new IllegalArgumentException("未查询微信用户");
        }

        JSONObject realUserInfo = userResult.getJSONArray("users").getJSONObject(0);

        String openId = realUserInfo.getString("openId");

        //微信下单PayUtil
        Map result = java110Payment(orderId, money, openId);
        responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);

        return responseEntity;
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
    private Map<String, String> java110Payment(String orderNum, double money, String openId) throws Exception {
        logger.info("【小程序支付】 统一下单开始, 订单编号=" + orderNum);
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
//生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元

        double payAmount = PayUtil.getPayAmountByEnv("DEV", money);
//添加或更新支付记录(参数跟进自己业务需求添加)

        Map<String, String> resMap = this.java110UnifieldOrder(orderNum, wechatAuthProperties.TRADE_TYPE_JSAPI, payAmount, openId);
        if ("SUCCESS".equals(resMap.get("return_code")) && "SUCCESS".equals(resMap.get("result_code"))) {
            resultMap.put("appId", wechatAuthProperties.getAppId());
            resultMap.put("timeStamp", PayUtil.getCurrentTimeStamp());
            resultMap.put("nonceStr", PayUtil.makeUUID(32));
            resultMap.put("package", "prepay_id=" + resMap.get("prepay_id"));
            resultMap.put("signType", "MD5");
            resultMap.put("sign", PayUtil.createSign(resultMap, wechatAuthProperties.getKey()));
            resultMap.put("code", "0");
            resultMap.put("msg", "下单成功");
            logger.info("【小程序支付】统一下单成功，返回参数:" + resultMap);
        } else {
            resultMap.put("code", resMap.get("return_code"));
            resultMap.put("msg", resMap.get("return_msg"));
            logger.info("【小程序支付】统一下单失败，失败原因:" + resMap.get("return_msg"));
        }
        return resultMap;
    }

    /**
     * 小程序支付统一下单
     */
    private Map<String, String> java110UnifieldOrder(String orderNum, String tradeType, double payAmount, String openid) throws Exception {
//封装参数
        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("appid", wechatAuthProperties.getAppId());
        paramMap.put("mch_id", wechatAuthProperties.getMchId());
        paramMap.put("nonce_str", PayUtil.makeUUID(32));
        paramMap.put("body", "");
        paramMap.put("out_trade_no", orderNum);
        paramMap.put("total_fee", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("spbill_create_ip", PayUtil.getLocalIp());
        paramMap.put("notify_url", wechatAuthProperties.getWxNotifyUrl());
        paramMap.put("trade_type", tradeType);
        paramMap.put("openid", openid);
        paramMap.put("sign", PayUtil.createSign(paramMap, wechatAuthProperties.getKey()));
//转换为xml
        String xmlData = PayUtil.mapToXml(paramMap);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                wechatAuthProperties.getWxPayUnifiedOrder(), xmlData, String.class);
//请求微信后台，获取预支付ID
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("支付失败" + responseEntity.getBody());
        }
        return PayUtil.xmlStrToMap(responseEntity.getBody());
    }
}
