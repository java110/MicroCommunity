package com.java110.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.Map;

/**
 * native qrcode 支付
 * add by wuxw 2023-06-25
 */
@Java110Cmd(serviceCode = "payment.nativeQrcodePayment")
public class NativeQrcodePaymentCmd extends Cmd {


    private static final Logger logger = LoggerFactory.getLogger(NativeQrcodePaymentCmd.class);

    protected static final String DEFAULT_PAYMENT_ADAPT = "wechatNativeQrcodePaymentFactory";// 默认微信通用支付


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "business", "未包含业务");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        logger.debug(">>>>>>>>>>>>>>>>支付参数报文,{}", reqJson.toJSONString());
        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");


        //1.0 查询当前支付的业务

        IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(reqJson.getString("business"), IPaymentBusiness.class);

        if (paymentBusiness == null) {
            throw new CmdException("当前支付业务不支持");
        }

        //2.0 相应业务 下单 返回 单号 ，金额，
        PaymentOrderDto paymentOrderDto = paymentBusiness.unified(context, reqJson);
        paymentOrderDto.setAppId(appId);
        paymentOrderDto.setUserId(userId);


        logger.debug(">>>>>>>>>>>>>>>>支付业务下单返回,{}", JSONObject.toJSONString(paymentOrderDto));

        String env = MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV");

        // 这里 演示环境不向微信下单
//        if ("DEV".equals(env) || "TEST".equals(env)) {
//            paymentBusiness.notifyPayment(paymentOrderDto, reqJson);
//            JSONObject param = new JSONObject();
//            param.put("code", "100");
//            param.put("msg", "演示环境不触发支付");
//            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
//            return;
//        }
//
//        // 3.0 如果支付金额为0 直接调用 支付完通知接口
//        if (paymentOrderDto.getMoney() <= 0) {
//            paymentBusiness.notifyPayment(paymentOrderDto, reqJson);
//            JSONObject param = new JSONObject();
//            param.put("code", "100");
//            param.put("msg", "扣费为0回调成功");
//            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
//            return;
//        }

        // 3.0 寻找当前支付适配器
        String payAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.NATIVE_QRCODE_PAYMENT_ADAPT);
        payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAYMENT_ADAPT : payAdapt;

        if (reqJson.containsKey("payAdapt") && !StringUtil.isEmpty(reqJson.getString("payAdapt"))) {
            payAdapt = reqJson.getString("payAdapt");
        }

        IPaymentFactoryAdapt tPayAdapt = ApplicationContextFactory.getBean(payAdapt, IPaymentFactoryAdapt.class);

        // 4.0 相应支付厂家下单
        Map result = null;
        try {
            result = tPayAdapt.java110Payment(paymentOrderDto, reqJson, context);
        } catch (Exception e) {
            logger.error("支付异常", e);
            throw new CmdException(e.getLocalizedMessage());
        }
        ResponseEntity<String> responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);

        logger.debug("调用支付厂家返回,{}", responseEntity);
        context.setResponseEntity(responseEntity);


        // redis 中 保存 请求参数
        CommonCache.setValue("nativeQrcodePayment_" + paymentOrderDto.getOrderId(), reqJson.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
    }
}
