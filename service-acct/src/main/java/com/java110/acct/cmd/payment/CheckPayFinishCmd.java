package com.java110.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.smo.IQrCodePaymentSMO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 扫码付
 */
@Java110Cmd(serviceCode = "payment.checkPayFinish")
public class CheckPayFinishCmd extends AbstractServiceCmdListener {


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
        } else {
            qrCodePaymentSMOImpl = ApplicationContextFactory.getBean("qrCodeWechatPaymentAdapt", IQrCodePaymentSMO.class);
        }

        ResultVo resultVo = null;
        try {
            resultVo = qrCodePaymentSMOImpl.checkPayFinish(reqJson.getString("communityId"), orderId);
        } catch (Exception e) {
            cmdDataFlowContext.setResponseEntity(ResultVo.error(e.getLocalizedMessage()));
            return;
        }
        if (ResultVo.CODE_OK != resultVo.getCode()) {
            cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(resultVo));
            return;
        }
        String appId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.APP_ID);
        String userId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.USER_ID);
        String paramOut = CallApiServiceFactory.postForApi(appId, reqJson.toJSONString(), "fee.payFee", String.class, userId);
        cmdDataFlowContext.setResponseEntity(new ResponseEntity(paramOut, HttpStatus.OK));
    }

}
