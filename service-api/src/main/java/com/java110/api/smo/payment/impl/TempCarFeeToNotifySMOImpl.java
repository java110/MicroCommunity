package com.java110.api.smo.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.payment.IOweFeeToNotifySMO;
import com.java110.api.smo.payment.ITempCarFeeToNotifySMO;
import com.java110.api.smo.payment.adapt.IOweFeeToNotifyAdapt;
import com.java110.api.smo.payment.adapt.ITempCarFeeToNotifyAdapt;
import com.java110.core.context.IPageData;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service("tempCarFeeToNotifySMOImpl")
public class TempCarFeeToNotifySMOImpl extends AppAbstractComponentSMO implements ITempCarFeeToNotifySMO {
    private static final Logger logger = LoggerFactory.getLogger(TempCarFeeToNotifySMOImpl.class);

    /**
     * 支付宝appId
     */
    private static final String APP_ID = "992022082855370008";

    private static final String DEFAULT_OWE_FEE_TO_NOTIFY_ADAPT = "wechatTempCarFeeToNotifyAdapt";// 默认微信通用支付


    @Override
    public ResponseEntity<String> toNotify(String param, HttpServletRequest request) {
        String wId = request.getParameter("wId");
        String payNotifyAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_NOTIFY_ADAPT);
        payNotifyAdapt = StringUtil.isEmpty(payNotifyAdapt) ? DEFAULT_OWE_FEE_TO_NOTIFY_ADAPT : payNotifyAdapt;
        //支付适配器
        ITempCarFeeToNotifyAdapt tPayNotifyAdapt = ApplicationContextFactory.getBean(payNotifyAdapt, ITempCarFeeToNotifyAdapt.class);
        String resXml = tPayNotifyAdapt.confirmPayFee(param,wId);
        logger.info("【小程序支付回调响应】 响应内容：\n" + resXml);
        return new ResponseEntity<String>(resXml, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> aliPayToNotify(String param, HttpServletRequest request) {


        String url = "alipay.notifyPayTempCarFee";
        /**
         *   postParameters.put("carNum", tempCarPayOrderDto.getCarNum());
         *         postParameters.put("extPaId", tempCarPayOrderDto.getPaId());
         *         postParameters.put("orderId", tempCarPayOrderDto.getOrderId());
         *         postParameters.put("amount", tempCarPayOrderDto.getAmount());
         *         postParameters.put("payTime", tempCarPayOrderDto.getPayTime());
         *         postParameters.put("payType", tempCarPayOrderDto.getPayType());
         */
        ResponseEntity<String> responseEntity = super.callCenterService(getHeaders("-1"), param, url, HttpMethod.POST);

        return responseEntity;
    }

    private Map<String, String> getHeaders(String userId) {
        Map<String, String> headers = new HashMap<>();
        headers.put(CommonConstant.HTTP_APP_ID.toLowerCase(), APP_ID);
        headers.put(CommonConstant.HTTP_USER_ID.toLowerCase(), userId);
        headers.put(CommonConstant.HTTP_TRANSACTION_ID.toLowerCase(), UUID.randomUUID().toString());
        headers.put(CommonConstant.HTTP_REQ_TIME.toLowerCase(), DateUtil.getDefaultFormateTimeString(new Date()));
        headers.put(CommonConstant.HTTP_SIGN.toLowerCase(), "");
        return headers;
    }


    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        return null;
    }
}
