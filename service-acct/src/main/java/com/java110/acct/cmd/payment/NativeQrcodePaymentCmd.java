package com.java110.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.paymentPool.PaymentPoolDto;
import com.java110.dto.paymentPoolConfig.PaymentPoolConfigDto;
import com.java110.intf.acct.IPaymentPoolConfigV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * native qrcode 支付
 * add by wuxw 2023-06-25
 */
@Java110Cmd(serviceCode = "payment.nativeQrcodePayment")
public class NativeQrcodePaymentCmd extends Cmd {


    private static final Logger logger = LoggerFactory.getLogger(NativeQrcodePaymentCmd.class);

    protected static final String DEFAULT_PAYMENT_ADAPT = "wechatNativeQrcodePaymentFactory";// 默认微信通用支付

    @Autowired
    private IPaymentPoolConfigV1InnerServiceSMO paymentPoolConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "business", "未包含业务");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        logger.debug(">>>>>>>>>>>>>>>>支付参数报文,{}", reqJson.toJSONString());
        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");
        reqJson.put("createUserId", userId);
        reqJson.put("storeId", CmdContextUtils.getStoreId(context));

        //1.0 查询当前支付的业务

        IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(reqJson.getString("business"), IPaymentBusiness.class);

        if (paymentBusiness == null) {
            throw new CmdException("当前支付业务不支持");
        }

        //2.0 相应业务 下单 返回 单号 ，金额，
        PaymentOrderDto paymentOrderDto = paymentBusiness.unified(context, reqJson);
        paymentOrderDto.setAppId(appId);
        paymentOrderDto.setUserId(userId);
        reqJson.put("money", paymentOrderDto.getMoney());

        String token = GenerateCodeFactory.getUUID();

        // redis 中 保存 请求参数
        CommonCache.setValue("nativeQrcodePayment_" + token, reqJson.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        JSONObject result = new JSONObject();
        result.put("codeUrl", UrlCache.getOwnerUrl() + "/#/pages/fee/qrCodeCashier?qrToken=" + token);

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(result);

        logger.debug("调用支付厂家返回,{}", responseEntity);
        context.setResponseEntity(responseEntity);

    }

}
