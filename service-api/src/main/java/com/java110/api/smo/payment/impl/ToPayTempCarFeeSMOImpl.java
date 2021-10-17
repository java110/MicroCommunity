package com.java110.api.smo.payment.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.payment.IToPayTempCarFeeSMO;
import com.java110.api.smo.payment.adapt.IPayAdapt;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.app.AppDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service("toPayTempCarFeeSMOImpl")
public class ToPayTempCarFeeSMOImpl extends AppAbstractComponentSMO implements IToPayTempCarFeeSMO {
    private static final Logger logger = LoggerFactory.getLogger(ToPayTempCarFeeSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;


    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Override
    public ResponseEntity<String> toPay(IPageData pd) {
        return super.businessProcess(pd);
    }


    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        Assert.jsonObjectHaveKey(paramIn, "carNum", "请求报文中未包含房屋信息节点");
        Assert.jsonObjectHaveKey(paramIn, "appId", "请求报文中未包含appId节点");
        Assert.jsonObjectHaveKey(paramIn, "openId", "请求报文中未包含openId节点");
        Assert.jsonObjectHaveKey(paramIn, "paId", "请求报文中未包含paId节点");
        Assert.jsonObjectHaveKey(paramIn, "inoutId", "请求报文中未包含inoutId节点");

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {

        ResponseEntity responseEntity = null;

        SmallWeChatDto smallWeChatDto = getSmallWechat(pd, paramIn);

        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        }


        //查询用户ID
        paramIn.put("userId", pd.getUserId());
        String url = "tempCarFee.getTempCarFeeOrder?paId=" + paramIn.getString("paId") + "&carNum=" + paramIn.getString("carNum");
        responseEntity = super.callCenterService(restTemplate, pd, "", url, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        JSONObject orderInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        if (orderInfo.getIntValue("code") != 0) {
            throw new IllegalArgumentException("缴费失败");
        }
        JSONObject fee = orderInfo.getJSONObject("data");

        double money = fee.getDouble("payCharge");
        String orderId = orderInfo.getString("oId");
        String openId = paramIn.getString("openId");
        String payAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_ADAPT);
        payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAY_ADAPT : payAdapt;
        //支付适配器
        IPayAdapt tPayAdapt = ApplicationContextFactory.getBean(payAdapt, IPayAdapt.class);
        Map result = tPayAdapt.java110Payment(outRestTemplate, paramIn.getString("feeName"), paramIn.getString("tradeType"),
                orderId, money, openId, smallWeChatDto, wechatAuthProperties.getTempCarFeeNotifyUrl());
        responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);
        if (!"0".equals(result.get("code"))) {
            return responseEntity;
        }
        JSONObject saveFees = new JSONObject();
        saveFees.put("orderId", paramIn.getString("inoutId"));
        saveFees.put("carNum", paramIn.getString("carNum"));
        saveFees.put("amount", money);
        saveFees.put("paId", paramIn.getString("paId"));
        saveFees.put("payTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        saveFees.put("payType", "2");
        CommonCache.setValue(FeeDto.REDIS_PAY_TEMP_CAR_FEE + orderId, saveFees.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        return responseEntity;
    }


    private SmallWeChatDto getSmallWechat(IPageData pd, JSONObject paramIn) {

        ResponseEntity responseEntity = null;

        pd = PageData.newInstance().builder(pd.getUserId(), "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "smallWeChat.listSmallWeChats?appId="
                        + paramIn.getString("appId") + "&page=1&row=1&communityId=" + paramIn.getString("communityId"), HttpMethod.GET);

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
