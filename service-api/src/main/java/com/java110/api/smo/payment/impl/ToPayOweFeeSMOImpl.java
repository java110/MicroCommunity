package com.java110.api.smo.payment.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.payment.IToPayOweFeeSMO;
import com.java110.api.smo.payment.adapt.IPayAdapt;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.app.AppDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("toPayOweFeeSMOImpl")
public class ToPayOweFeeSMOImpl extends AppAbstractComponentSMO implements IToPayOweFeeSMO {
    private static final Logger logger = LoggerFactory.getLogger(ToPayOweFeeSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;


    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Override
    public ResponseEntity<String> toPay(IPageData pd) {
        return super.businessProcess(pd);
    }


    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(paramIn, "appId", "请求报文中未包含appId节点");

        if (!paramIn.containsKey("ownerId") && !paramIn.containsKey("roomId")) {
            throw new IllegalArgumentException("未包含房屋或者业主");
        }
        if (StringUtil.isEmpty(paramIn.getString("ownerId")) && StringUtil.isEmpty(paramIn.getString("roomId"))) {
            throw new IllegalArgumentException("未包含房屋或者业主");
        }

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

        String payObjType = "3333";
        if (paramIn.containsKey("payObjType")) {
            payObjType = paramIn.getString("payObjType");
        }

        String ownerId = paramIn.getString("ownerId");
        String roomId = paramIn.getString("roomId");

        //查询用户ID
        paramIn.put("userId", pd.getUserId());
        String url = "/feeApi/listOweFees?page=1&row=50&communityId=" + paramIn.getString("communityId") + "&payObjId=" + roomId + "&payObjType=" + payObjType;
        if(!StringUtil.isEmpty(ownerId)){
            url = "/feeApi/listOweFees?page=1&row=50&communityId=" + paramIn.getString("communityId") + "&ownerId=" + ownerId;
        }
        responseEntity = super.callCenterService(restTemplate, pd, "", url, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        JSONObject orderInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONArray fees = orderInfo.getJSONArray("data");

        if (fees == null || fees.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未包含欠费费用");
        }
        String orderId = GenerateCodeFactory.getOId();
        double money = 0.0;
        BigDecimal tmpMoney = new BigDecimal(money);
        BigDecimal feePrice = null;
        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            feePrice = new BigDecimal(fees.getJSONObject(feeIndex).getDouble("feeTotalPrice"));
            tmpMoney = tmpMoney.add(feePrice);
        }

        String feeName = getFeeName(fees.getJSONObject(0));
        money = tmpMoney.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        String appType = OwnerAppUserDto.APP_TYPE_WECHAT_MINA;
        if (AppDto.WECHAT_OWNER_APP_ID.equals(pd.getAppId())) {
            appType = OwnerAppUserDto.APP_TYPE_WECHAT;
        } else if (AppDto.WECHAT_MINA_OWNER_APP_ID.equals(pd.getAppId())) {
            appType = OwnerAppUserDto.APP_TYPE_WECHAT_MINA;
        } else {
            appType = OwnerAppUserDto.APP_TYPE_APP;
        }
        Map tmpParamIn = new HashMap();
        tmpParamIn.put("userId", pd.getUserId());
        tmpParamIn.put("appType", appType);
        responseEntity = super.getOwnerAppUser(pd, restTemplate, tmpParamIn);
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

        JSONObject realUserInfo = userResult.getJSONArray("data").getJSONObject(0);

        String openId = realUserInfo.getString("openId");
        String payAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_ADAPT);
        payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAY_ADAPT : payAdapt;
        //支付适配器
        IPayAdapt tPayAdapt = ApplicationContextFactory.getBean(payAdapt, IPayAdapt.class);
        Map result = tPayAdapt.java110Payment(outRestTemplate, feeName, paramIn.getString("tradeType"),
                orderId, money, openId, smallWeChatDto, wechatAuthProperties.getOweFeeNotifyUrl());
        responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);
        if (!"0".equals(result.get("code"))) {
            return responseEntity;
        }
        JSONObject saveFees = new JSONObject();
        saveFees.put("orderId", orderId);
        saveFees.put("money", money);
        saveFees.put("roomId", paramIn.getString("roomId"));
        saveFees.put("communityId", paramIn.getString("communityId"));
        saveFees.put("fees", fees);
        CommonCache.setValue(FeeDto.REDIS_PAY_OWE_FEE + orderId, saveFees.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        return responseEntity;
    }

    private String getFeeName(JSONObject feeDto) {
        //查询小区名称
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(feeDto.getString("communityId"));
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存在");

        JSONArray feeAttrDtos = feeDto.getJSONArray("feeAttrDtos");
        if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
            return communityDtos.get(0).getName() + "-" + feeDto.getString("feeName");
        }

        for (int attrIndex = 0; attrIndex < feeAttrDtos.size(); attrIndex++) {
            if (FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME.equals(feeAttrDtos.getJSONObject(attrIndex).getString("specCd"))) {
                return communityDtos.get(0).getName() + "-" + feeAttrDtos.getJSONObject(attrIndex).getString("value") + "-" + feeDto.getString("feeName");
            }
        }

        return communityDtos.get(0).getName() + "-" + feeDto.getString("feeName");
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
