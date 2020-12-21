package com.java110.front.smo.payment.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.front.properties.WechatAuthProperties;
import com.java110.front.smo.AppAbstractComponentSMO;
import com.java110.front.smo.payment.IToNotifySMO;
import com.java110.front.smo.payment.adapt.IPayNotifyAdapt;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.BeanConvertUtil;
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
import java.util.*;

@Service("toNotifySMOImpl")
public class ToNotifySMOImpl implements IToNotifySMO {
    private static final Logger logger = LoggerFactory.getLogger(AppAbstractComponentSMO.class);



    private static final String DEFAULT_PAY_NOTIFY_ADAPT = "wechatPayNotifyAdapt";// 默认微信通用支付


    @Override
    public ResponseEntity<String> toNotify(String param, HttpServletRequest request) {

        String payNotifyAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_NOTIFY_ADAPT);
        payNotifyAdapt = StringUtil.isEmpty(payNotifyAdapt) ? DEFAULT_PAY_NOTIFY_ADAPT : payNotifyAdapt;
        //支付适配器
        IPayNotifyAdapt tPayNotifyAdapt = ApplicationContextFactory.getBean(payNotifyAdapt, IPayNotifyAdapt.class);
        String resXml = tPayNotifyAdapt.confirmPayFee(param);
        logger.info("【小程序支付回调响应】 响应内容：\n" + resXml);
        return new ResponseEntity<String>(resXml, HttpStatus.OK);
    }
}
