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
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.paymentPool.PaymentPoolDto;
import com.java110.dto.paymentPoolConfig.PaymentPoolConfigDto;
import com.java110.intf.acct.IPaymentPoolConfigV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
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


    @Autowired
    private IPaymentPoolConfigV1InnerServiceSMO paymentPoolConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;


    private IQrCodePaymentSMO qrCodePaymentSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "authCode", "未包含授权码");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "未包含支付金额");
        Assert.hasKeyAndValue(reqJson, "subServiceCode", "未包含支付接口");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String orderId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId);
        double receivedAmount = Double.parseDouble(reqJson.getString("receivedAmount"));
        String authCode = reqJson.getString("authCode");
        if (StringUtil.isEmpty(authCode) || authCode.length() < 2) {
            throw new IllegalArgumentException("授权码错误");
        }

        String payQrAdapt = computeAdapt(reqJson.getString("subServiceCode"),reqJson);//MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_QR_ADAPT);

        if (StringUtil.isEmpty(payQrAdapt)) {
            int pre = Integer.parseInt(authCode.substring(0, 2));
            if (pre > 24 && pre < 31) { // 支付宝
                qrCodePaymentSMOImpl = ApplicationContextFactory.getBean("qrCodeAliPaymentAdapt", IQrCodePaymentSMO.class);
                reqJson.put("primeRate", FeeDetailDto.PRIME_REATE_ALI_QRCODE);
            } else {
                qrCodePaymentSMOImpl = ApplicationContextFactory.getBean("qrCodeWechatPaymentAdapt", IQrCodePaymentSMO.class);
                reqJson.put("primeRate", FeeDetailDto.PRIME_REATE_WECHAT_QRCODE);
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

        //todo 缓存订单ID
        CommonCache.setValue("qrCode_order"+orderId,orderId,CommonCache.TOKEN_EXPIRE_TIME);

        ResultVo resultVo = null;
        try {
            resultVo = qrCodePaymentSMOImpl.pay(reqJson.getString("communityId"), orderId,
                    receivedAmount, authCode, feeName,reqJson.getString("paymentPoolId"));
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
        reqJson.put("payOrderId",orderId);

        orderId = CommonCache.getAndRemoveValue("qrCode_order"+orderId);

        if(StringUtil.isEmpty(orderId)){
            throw new CmdException("订单已经处理过");
        }

        JSONObject paramOut = CallApiServiceFactory.postForApi(appId, reqJson, reqJson.getString("subServiceCode"), JSONObject.class, userId);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }


    /**
     * 计算适配器
     *
     * @param reqJson
     * @return
     */
    private String computeAdapt(String business, JSONObject reqJson) {

        String communityId = reqJson.getString("communityId");
        //todo 如果是单个费用缴费
        PaymentPoolDto paymentPoolDto = ifPayFeeBusiness(business, reqJson);
        if (paymentPoolDto != null) {
            reqJson.put("paymentPoolId", paymentPoolDto.getPpId());
            return paymentPoolDto.getBeanQrcode();
        }

        //todo 按小区查询 支付信息
        paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setCommunityId(communityId);
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_COMMUNITY);
        paymentPoolDto.setState("Y");
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            throw new IllegalArgumentException("小区未配置支付信息");
        }

        reqJson.put("paymentPoolId", paymentPoolDtos.get(0).getPpId());
        return paymentPoolDtos.get(0).getBeanQrcode();
    }

    private PaymentPoolDto ifPayFeeBusiness(String business, JSONObject reqJson) {
        String feeId = "";
        if (!"fee.payFee".equals(business) || !reqJson.containsKey("feeId")) {
            return null;
        }

        feeId = reqJson.getString("feeId");
        if (StringUtil.isNumber(feeId)) {
            return null;
        }

        PayFeeDto feeDto = new PayFeeDto();
        feeDto.setFeeId(feeId);
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(feeDto);

        if (feeDtos == null || feeDtos.isEmpty()) {
            return null;
        }

        PaymentPoolConfigDto paymentPoolConfigDto = new PaymentPoolConfigDto();
        paymentPoolConfigDto.setConfigId(feeDtos.get(0).getConfigId());
        paymentPoolConfigDto.setCommunityId(feeDtos.get(0).getCommunityId());
        List<PaymentPoolConfigDto> paymentPoolConfigDtos = paymentPoolConfigV1InnerServiceSMOImpl.queryPaymentPoolConfigs(paymentPoolConfigDto);
        if (paymentPoolConfigDtos == null || paymentPoolConfigDtos.isEmpty()) {
            return null;
        }

        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setPpId(paymentPoolConfigDtos.get(0).getPpId());
        paymentPoolDto.setCommunityId(paymentPoolConfigDtos.get(0).getCommunityId());
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_FEE_CONFIG);
        paymentPoolDto.setState("Y");
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            return null;
        }

        return paymentPoolDtos.get(0);
    }
}
