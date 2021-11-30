package com.java110.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.acct.smo.impl.QrCodeWechatPaymentAdapt;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 扫码付
 */
@Java110Cmd(serviceCode = "payment.qrCodePayment")
public class QrCodePaymentCmd extends AbstractServiceCmdListener {
    private static Logger logger = LoggerFactory.getLogger(QrCodePaymentCmd.class);


    private IQrCodePaymentSMO qrCodePaymentSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "authCode", "未包含授权码");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String orderId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId);
        double receivedAmount = Double.parseDouble(reqJson.getString("receivedAmount"));
        String authCode = reqJson.getString("authCode");
        if (StringUtil.isEmpty(authCode) || authCode.length() < 2) {
            throw new IllegalArgumentException("授权码错误");
        }

        int pre = Integer.parseInt(authCode.substring(0, 2));
        if (pre > 24 && pre < 31) { // 支付宝
            qrCodePaymentSMOImpl = ApplicationContextFactory.getBean("qrCodeAliPaymentAdapt", IQrCodePaymentSMO.class);
        }else{
            qrCodePaymentSMOImpl = ApplicationContextFactory.getBean("qrCodeWechatPaymentAdapt", IQrCodePaymentSMO.class);
        }

        ResultVo resultVo = null;
        try {
            resultVo = qrCodePaymentSMOImpl.pay(reqJson.getString("communityId"), orderId, receivedAmount, authCode, "");
        } catch (Exception e) {
            cmdDataFlowContext.setResponseEntity(ResultVo.error(e.getLocalizedMessage()));
            return;
        }
        logger.debug("适配器返回结果:"+resultVo.toString());
        if (ResultVo.CODE_OK != resultVo.getCode()) {
            cmdDataFlowContext.setResponseEntity(ResultVo.error(resultVo.getMsg(),reqJson));
            return;
        }
        String appId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.APP_ID);
        String userId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.USER_ID);
        String paramOut = CallApiServiceFactory.postForApi(appId, reqJson.toJSONString(), "fee.payFee", String.class, userId);
        cmdDataFlowContext.setResponseEntity(new ResponseEntity(paramOut, HttpStatus.OK));
    }

}
