package com.java110.app.smo.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.app.properties.WechatAuthProperties;
import com.java110.app.smo.AppAbstractComponentSMO;
import com.java110.app.smo.payment.IToNotifySMO;
import com.java110.app.smo.payment.IToPaySMO;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service("toNotifySMOImpl")
public class ToNotifySMOImpl implements IToNotifySMO {
    private static final Logger logger = LoggerFactory.getLogger(AppAbstractComponentSMO.class);

    private static final String APP_ID = "992020011134400001";


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Override
    public ResponseEntity<String> toNotify(HttpServletRequest request) {
        String resXml = "";
        ResponseEntity responseEntity = null;
        try {
            InputStream inputStream = request.getInputStream();
//获取请求输入流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            inputStream.close();
            String wxReqXml = new String(outputStream.toByteArray(), "utf-8");
            logger.debug("微信回调报文" + wxReqXml);
            Map<String, Object> map = PayUtil.getMapFromXML(wxReqXml);
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

        logger.info("【小程序支付回调响应】 响应内容：\n" + resXml);
        return new ResponseEntity<String>(resXml, HttpStatus.OK);
    }

    public int confirmPayFee(Map<String, Object> map) {
        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        ResponseEntity<String> responseEntity = null;
        for (String key : map.keySet()) {
            paramMap.put(key, map.get(key).toString());
        }
        String sign = PayUtil.createSign(paramMap, wechatAuthProperties.getKey());

        if (!sign.equals(map.get("sign"))) {
            throw new IllegalArgumentException("鉴权失败");
        }

        String outTradeNo = map.get("out_trade_no").toString();
        String openId = map.get("openid").toString();

        responseEntity = getUserInfoByOpenId(restTemplate, openId);

        logger.debug("查询用户信息返回报文：" + responseEntity);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("根绝openId 查询用户信息异常" + openId);
        }

        JSONObject userResult = JSONObject.parseObject(responseEntity.getBody());
        JSONObject realUserInfo = userResult.getJSONArray("users").getJSONObject(0);
        String useId = realUserInfo.getString("userId");

        //查询用户ID
        JSONObject paramIn = new JSONObject();
        paramIn.put("oId", outTradeNo);
        String url = ServiceConstant.SERVICE_API_URL + "/api/fee.payFeeConfirm";
        responseEntity = this.callCenterService(restTemplate, useId, paramIn.toJSONString(), url, HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return 0;
        }
        return 1;
    }

    /**
     * 获取用户信息
     *
     * @param restTemplate
     * @return
     */
    protected ResponseEntity<String> getUserInfoByOpenId(RestTemplate restTemplate, String openId) {
        //Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = this.callCenterService(restTemplate, "-1", "",
                ServiceConstant.SERVICE_API_URL + "/api/user.listUsers?openId=" + openId + "&page=1&row=1", HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}

        return responseEntity;

    }

    /**
     * 调用中心服务
     *
     * @return
     */
    protected ResponseEntity<String> callCenterService(RestTemplate restTemplate, String userId, String param, String url, HttpMethod httpMethod) {

        ResponseEntity<String> responseEntity = null;
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_APP_ID.toLowerCase(), APP_ID);
        header.add(CommonConstant.HTTP_USER_ID.toLowerCase(), userId);
        header.add(CommonConstant.HTTP_TRANSACTION_ID.toLowerCase(), UUID.randomUUID().toString());
        header.add(CommonConstant.HTTP_REQ_TIME.toLowerCase(), DateUtil.getDefaultFormateTimeString(new Date()));
        header.add(CommonConstant.HTTP_SIGN.toLowerCase(), "");
        HttpEntity<String> httpEntity = new HttpEntity<String>(param, header);
        //logger.debug("请求中心服务信息，{}", httpEntity);
        try {
            responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>("请求下游系统异常，" + e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("请求地址为,{} 请求中心服务信息，{},中心服务返回信息，{}", url, httpEntity, responseEntity);
            return responseEntity;
        }

    }

}
