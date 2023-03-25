package com.java110.api.smo.payment.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.payment.IToQrPayOweFeeSMO;
import com.java110.api.smo.payment.adapt.IPayAdapt;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceConstant;
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
import java.util.Map;

@Service("toQrPayOweFeeSMOImpl")
public class ToQrPayOweFeeSMOImpl extends AppAbstractComponentSMO implements IToQrPayOweFeeSMO {
    private static final Logger logger = LoggerFactory.getLogger(ToQrPayOweFeeSMOImpl.class);


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

        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(paramIn, "roomId", "请求报文中未包含房屋信息节点");

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
        String url = "/feeApi/listOweFees?page=1&row=50&communityId=" + paramIn.getString("communityId") + "&payObjId=" + paramIn.getString("roomId") + "&payObjType=3333";
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
        money = tmpMoney.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

        String payAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_ADAPT);
        payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAY_ADAPT : payAdapt;
        //支付适配器
        IPayAdapt tPayAdapt = ApplicationContextFactory.getBean(payAdapt, IPayAdapt.class);
        Map result = tPayAdapt.java110Payment(outRestTemplate, "物业二维码收款", WechatAuthProperties.TRADE_TYPE_NATIVE,
                orderId, money, "", smallWeChatDto, wechatAuthProperties.getOweFeeNotifyUrl());
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


    private SmallWeChatDto getSmallWechat(IPageData pd, JSONObject paramIn) {

        ResponseEntity responseEntity = null;

        pd = PageData.newInstance().builder(pd.getUserId(), "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "smallWeChat.listSmallWeChats?communityId="
                        + paramIn.getString("communityId") + "&page=1&row=1&weChatType=1100&communityId="+paramIn.getString("communityId"), HttpMethod.GET);

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
