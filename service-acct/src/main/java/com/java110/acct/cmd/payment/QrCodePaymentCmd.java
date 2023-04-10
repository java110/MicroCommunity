package com.java110.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 扫码付
 */
@Java110Cmd(serviceCode = "payment.qrCodePayment")
public class QrCodePaymentCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(QrCodePaymentCmd.class);

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;


    private IQrCodePaymentSMO qrCodePaymentSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "authCode", "未包含授权码");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "未包含支付金额");
        Assert.hasKeyAndValue(reqJson, "subServiceCode", "未包含支付接口");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String orderId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId);
        double receivedAmount = Double.parseDouble(reqJson.getString("receivedAmount"));
        String authCode = reqJson.getString("authCode");
        if (StringUtil.isEmpty(authCode) || authCode.length() < 2) {
            throw new IllegalArgumentException("授权码错误");
        }

        String payQrAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_QR_ADAPT);

        if (StringUtil.isEmpty(payQrAdapt)) {
            int pre = Integer.parseInt(authCode.substring(0, 2));
            if (pre > 24 && pre < 31) { // 支付宝
                qrCodePaymentSMOImpl = ApplicationContextFactory.getBean("qrCodeAliPaymentAdapt", IQrCodePaymentSMO.class);
                reqJson.put("primeRate", FeeDetailDto.PRIME_REATE_WECHAT_QRCODE);
            } else {
                qrCodePaymentSMOImpl = ApplicationContextFactory.getBean("qrCodeWechatPaymentAdapt", IQrCodePaymentSMO.class);
                reqJson.put("primeRate", FeeDetailDto.PRIME_REATE_ALI_QRCODE);
            }
        } else {
            qrCodePaymentSMOImpl = ApplicationContextFactory.getBean(payQrAdapt, IQrCodePaymentSMO.class);
        }

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(reqJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存在");

        String feeName = communityDtos.get(0).getName();
        if (!StringUtil.isEmpty(reqJson.getString("payerObjName"))) {
            feeName += ("-" + reqJson.getString("payerObjName"));
        }

        if (!StringUtil.isEmpty(reqJson.getString("feeName"))) {
            feeName += ("-" + reqJson.getString("feeName"));
        }

        if (feeName.length() > 120) {
            feeName = feeName.substring(0, 120);
        }

        ResultVo resultVo = null;
        try {
            resultVo = qrCodePaymentSMOImpl.pay(reqJson.getString("communityId"), orderId, receivedAmount, authCode, feeName);
        } catch (Exception e) {
            logger.error("异常了", e);
            cmdDataFlowContext.setResponseEntity(ResultVo.error(e.getLocalizedMessage()));
            return;
        }
        logger.debug("适配器返回结果:" + resultVo.toString());
        if (ResultVo.CODE_OK != resultVo.getCode()) {
            reqJson.put("orderId", orderId);
            cmdDataFlowContext.setResponseEntity(ResultVo.error(resultVo.getMsg(), reqJson));
            return;
        }
        String appId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.APP_ID);
        String userId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.USER_ID);
        //JSONObject paramOut = CallApiServiceFactory.postForApi(appId, reqJson, "fee.payFee", JSONObject.class, userId);
        JSONObject paramOut = CallApiServiceFactory.postForApi(appId, reqJson, reqJson.getString("subServiceCode"), JSONObject.class, userId);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }

}
