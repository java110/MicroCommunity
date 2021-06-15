package com.java110.front.smo.payment.impl;

import com.java110.front.smo.payment.IOweFeeToNotifySMO;
import com.java110.front.smo.payment.adapt.IOweFeeToNotifyAdapt;
import com.java110.front.smo.payment.adapt.IRentingToNotifyAdapt;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("oweFeeToNotifySMOImpl")
public class OweFeeToNotifySMOImpl implements IOweFeeToNotifySMO {
    private static final Logger logger = LoggerFactory.getLogger(OweFeeToNotifySMOImpl.class);

    private static final String APP_ID = "992020011134400001";

    private static final String DEFAULT_OWE_FEE_TO_NOTIFY_ADAPT = "wechatOweFeeToNotifyAdapt";// 默认微信通用支付


    @Override
    public ResponseEntity<String> toNotify(String param, HttpServletRequest request) {
        String wId = request.getParameter("wId");
        String payNotifyAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_NOTIFY_ADAPT);
        payNotifyAdapt = StringUtil.isEmpty(payNotifyAdapt) ? DEFAULT_OWE_FEE_TO_NOTIFY_ADAPT : payNotifyAdapt;
        //支付适配器
        IOweFeeToNotifyAdapt tPayNotifyAdapt = ApplicationContextFactory.getBean(payNotifyAdapt, IOweFeeToNotifyAdapt.class);
        String resXml = tPayNotifyAdapt.confirmPayFee(param,wId);
        logger.info("【小程序支付回调响应】 响应内容：\n" + resXml);
        return new ResponseEntity<String>(resXml, HttpStatus.OK);
    }


}
