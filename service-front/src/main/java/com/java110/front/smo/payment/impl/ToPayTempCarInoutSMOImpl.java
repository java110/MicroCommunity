package com.java110.front.smo.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.front.smo.AppAbstractComponentSMO;
import com.java110.front.smo.payment.IToPayTempCarInoutSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service("toPayTempCarInoutSMOImpl")
public class ToPayTempCarInoutSMOImpl extends AppAbstractComponentSMO implements IToPayTempCarInoutSMO {
    private static final Logger logger = LoggerFactory.getLogger(ToPayTempCarInoutSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;



    @Override
    public ResponseEntity<String> toPay(IPageData pd) {
        return super.businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(paramIn, "receivedAmount", "请求报文中未包含receivedAmount节点");
        Assert.jsonObjectHaveKey(paramIn, "feeId", "请求报文中未包含feeId节点");
        Assert.jsonObjectHaveKey(paramIn, "feeName", "请求报文中未包含feeName节点");

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {

        ResponseEntity responseEntity = null;
        //查询用户ID
        paramIn.put("userId", pd.getUserId());
        String url = ServiceConstant.SERVICE_API_URL + "/api/fee.payFeePreTempCarInout";
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
        if (total < 1) {
            //未查询到用户信息
            throw new IllegalArgumentException("未查询微信用户");
        }

        JSONObject realUserInfo = userResult.getJSONArray("users").getJSONObject(0);

        String openId = realUserInfo.getString("openId");

        //微信下单PayUtil
        Map result = super.java110Payment(restTemplate,paramIn.getString("feeName"),paramIn.getString("tradeType"), orderId, money, openId);
        responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);

        return responseEntity;
    }



}
