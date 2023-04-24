package com.java110.api.smo.payment.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.rentingPool.RentingConfigDto;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.payment.IRentingToPaySMO;
import com.java110.api.smo.payment.adapt.IPayAdapt;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service("rentingToPaySMOImpl")
public class RentingToPaySMOImpl extends AppAbstractComponentSMO implements IRentingToPaySMO {
    private static final Logger logger = LoggerFactory.getLogger(AppAbstractComponentSMO.class);


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

        Assert.jsonObjectHaveKey(paramIn, "rentingId", "请求报文中未包含房源ID");

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {

        ResponseEntity responseEntity = null;

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setAppId(wechatAuthProperties.getAppId());
        smallWeChatDto.setAppSecret(wechatAuthProperties.getSecret());
        smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
        smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());


        //查询用户ID
        paramIn.put("userId", pd.getUserId());
        //查询
        String url = "/renting/queryRentingPool?rentingId="
                + paramIn.getString("rentingId") + "&row=1&page=1";
        responseEntity = super.callCenterService(restTemplate, pd, "", url, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject body = JSONObject.parseObject(responseEntity.getBody().toString());

        if (ResultVo.CODE_OK != body.getInteger("code")) {
            return responseEntity;
        }

        if (body.getInteger("total") < 1) {
            return responseEntity;
        }

        RentingPoolDto rentingPoolDto = BeanConvertUtil.covertBean(body.getJSONArray("data").get(0), RentingPoolDto.class);

        String appType = OwnerAppUserDto.APP_TYPE_WECHAT;

        Map tmpParamIn = new HashMap();
        tmpParamIn.put("userId", pd.getUserId());
        tmpParamIn.put("appType", appType);

        String orderId = GenerateCodeFactory.getOId();
        double service = Double.parseDouble(rentingPoolDto.getServicePrice());
        double rate = 0.0;
        String feeName = rentingPoolDto.getRoomName() + "出租服务费";
        if (RentingPoolDto.STATE_TO_PAY.equals(rentingPoolDto.getState())) {
            rate = Double.parseDouble(rentingPoolDto.getServiceTenantRate());
            feeName += "(租客)";
        } else if (RentingPoolDto.STATE_OWNER_TO_PAY.equals(rentingPoolDto.getState())) {
            rate = Double.parseDouble(rentingPoolDto.getServiceOwnerRate());
            feeName += "(业主)";
        } else {
            throw new IllegalAccessException("当前状态不是支付状态");
        }
        String rentingFormula = rentingPoolDto.getRentingFormula();
        BigDecimal serviceDec = new BigDecimal(service);
        BigDecimal rateDec = new BigDecimal(rate);
        double money = 0.0;
        if (RentingConfigDto.RENTING_FORMULA_RATE.equals(rentingFormula)) {
            BigDecimal monthMoney = new BigDecimal(rentingPoolDto.getPrice());
            money = serviceDec.multiply(rateDec).multiply(monthMoney).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else {
            money = serviceDec.multiply(rateDec).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }
        String payAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_ADAPT);
        payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAY_ADAPT : payAdapt;
        //支付适配器
        IPayAdapt tPayAdapt = ApplicationContextFactory.getBean(payAdapt, IPayAdapt.class);
        Map result = tPayAdapt.java110Payment(outRestTemplate, feeName, WechatAuthProperties.TRADE_TYPE_NATIVE, orderId, money, "", smallWeChatDto, wechatAuthProperties.getRentingNotifyUrl());
        result.put("money", money);
        responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);
        if (!"0".equals(result.get("code"))) {
            return responseEntity;
        }

        Map orderInfo = BeanConvertUtil.beanCovertMap(rentingPoolDto);
        orderInfo.put("orderId", orderId);
        orderInfo.put("money", money);
        orderInfo.put("feeName", feeName);

        String order = JSONObject.toJSONString(orderInfo);

        CommonCache.setValue(RentingPoolDto.REDIS_PAY_RENTING + orderId, order, CommonCache.PAY_DEFAULT_EXPIRE_TIME);

        return responseEntity;
    }


    private SmallWeChatDto getSmallWechat(IPageData pd, JSONObject paramIn) {

        ResponseEntity responseEntity = null;

        pd = PageData.newInstance().builder(pd.getUserId(), "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "smallWeChat.listSmallWeChats?appId="
                        + paramIn.getString("appId") + "&page=1&row=1", HttpMethod.GET);

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
