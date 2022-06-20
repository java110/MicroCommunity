package com.java110.api.smo.payment.impl;

import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.payment.IToNotifySMO;
import com.java110.api.smo.payment.adapt.IPayNotifyAdapt;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("toNotifySMOImpl")
public class ToNotifySMOImpl implements IToNotifySMO {

    private static final Logger logger = LoggerFactory.getLogger(AppAbstractComponentSMO.class);

    private static final String DEFAULT_PAY_NOTIFY_ADAPT = "wechatPayNotifyAdapt";// 默认微信通用支付

    @Override
    public ResponseEntity<String> toNotify(String param, HttpServletRequest request) {

        String wId = request.getParameter("wId");

        String payNotifyAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_NOTIFY_ADAPT);
        payNotifyAdapt = StringUtil.isEmpty(payNotifyAdapt) ? DEFAULT_PAY_NOTIFY_ADAPT : payNotifyAdapt;
        //支付适配器IPayNotifyAdapt
        logger.debug("适配器：" + payNotifyAdapt);
        IPayNotifyAdapt tPayNotifyAdapt = ApplicationContextFactory.getBean(payNotifyAdapt, IPayNotifyAdapt.class);
        String resXml = tPayNotifyAdapt.confirmPayFee(param, wId);
        logger.info("【小程序支付回调响应】 响应内容：\n" + resXml);
        return new ResponseEntity<String>(resXml, HttpStatus.OK);
    }
}
