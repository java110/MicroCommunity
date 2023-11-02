package com.java110.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.paymentPool.PaymentPoolDto;
import com.java110.dto.paymentPoolConfig.PaymentPoolConfigDto;
import com.java110.intf.acct.IPaymentPoolConfigV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 统一支付接口
 */
@Java110Cmd(serviceCode = "payment.cashier")
public class CashierCmd extends Cmd {


    private static final Logger logger = LoggerFactory.getLogger(CashierCmd.class);

    protected static final String DEFAULT_PAYMENT_ADAPT = "wechatPaymentFactory";// 默认微信通用支付

    @Autowired
    private IPaymentPoolConfigV1InnerServiceSMO paymentPoolConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    /**
     * 校验
     *
     * @param event   事件对象
     * @param context 请求报文数据
     * @param reqJson
     * @throws CmdException
     */

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "business", "未包含业务");
        Assert.hasKeyAndValue(reqJson, "cashierUserId", "未包含收银人员");
        Assert.hasKeyAndValue(reqJson, "openId", "未包含openId");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");

        context.getReqHeaders().put("user-id", reqJson.getString("cashierUserId"));
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        logger.debug(">>>>>>>>>>>>>>>>支付参数报文,{}", reqJson.toJSONString());
        String appId = context.getReqHeaders().get("app-id");
        String userId = reqJson.getString("cashierUserId");


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
        if ("DEV".equals(env) || "TEST".equals(env)) {
            paymentBusiness.notifyPayment(paymentOrderDto, reqJson);
            JSONObject param = new JSONObject();
            param.put("code", "100");
            param.put("msg", "演示环境不触发支付");
            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
            return;
        }

        // 3.0 如果支付金额为0 直接调用 支付完通知接口
        if (paymentOrderDto.getMoney() <= 0) {
            paymentBusiness.notifyPayment(paymentOrderDto, reqJson);
            JSONObject param = new JSONObject();
            param.put("code", "100");
            param.put("msg", "扣费为0回调成功");
            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
            return;
        }

        // todo 3.0 寻找当前支付适配器
        String payAdapt = computeAdapt(reqJson.getString("business"), reqJson);
        //MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAYMENT_ADAPT);
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
        CommonCache.setValue("unifiedPayment_" + paymentOrderDto.getOrderId(), reqJson.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
    }

    /**
     * 计算适配器
     *
     * @param business
     * @param reqJson
     * @return
     */
    private String computeAdapt(String business, JSONObject reqJson) {

        String communityId = reqJson.getString("communityId");
        //todo 如果是单个费用缴费
        PaymentPoolDto paymentPoolDto = ifPayFeeBusiness(business, reqJson);
        if (paymentPoolDto != null) {
            reqJson.put("paymentPoolId", paymentPoolDto.getPpId());
            return paymentPoolDto.getBeanJsapi();
        }

        //todo 如果是临时车
        paymentPoolDto = ifTempCarFeeBusiness(business, communityId);
        if (paymentPoolDto != null) {
            reqJson.put("paymentPoolId", paymentPoolDto.getPpId());
            return paymentPoolDto.getBeanJsapi();
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
        return paymentPoolDtos.get(0).getBeanJsapi();
    }

    /**
     * 临时车场景处理
     *
     * @param business
     * @param communityId
     * @return
     */
    private PaymentPoolDto ifTempCarFeeBusiness(String business, String communityId) {
        if (!"tempCarFee".equals(business)) {
            return null;
        }
        //todo 按小区查询 支付信息
        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setCommunityId(communityId);
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_TEMP_CAT);
        paymentPoolDto.setState("Y");
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            return null;
        }

        return paymentPoolDtos.get(0);
    }

    private PaymentPoolDto ifPayFeeBusiness(String business, JSONObject reqJson) {
        String feeId = "";
        if (!"payFee".equals(business) || !reqJson.containsKey("feeId")) {
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
