package com.java110.api.smo.payment.impl;

import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.payment.IRentingToNotifySMO;
import com.java110.api.smo.payment.adapt.IRentingToNotifyAdapt;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Service("rentingToNotifySMOImpl")
public class RentingToNotifySMOImpl implements IRentingToNotifySMO {
    private static final Logger logger = LoggerFactory.getLogger(AppAbstractComponentSMO.class);

    private static final String APP_ID = "992020011134400001";


    private static final String DEFAULT_RENTING_TO_NOTIFY_ADAPT = "wechatRentingToNotifyAdapt";// 默认微信通用支付


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Override
    public ResponseEntity<String> toNotify(String param, HttpServletRequest request) {
        String wId = request.getParameter("wId");
        String payNotifyAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_NOTIFY_ADAPT);
        payNotifyAdapt = StringUtil.isEmpty(payNotifyAdapt) ? DEFAULT_RENTING_TO_NOTIFY_ADAPT : payNotifyAdapt;
        //支付适配器
        IRentingToNotifyAdapt tPayNotifyAdapt = ApplicationContextFactory.getBean(payNotifyAdapt, IRentingToNotifyAdapt.class);
        String resXml = tPayNotifyAdapt.confirmPayFee(param,wId);
        logger.info("【小程序支付回调响应】 响应内容：\n" + resXml);
        return new ResponseEntity<String>(resXml, HttpStatus.OK);
    }


}
