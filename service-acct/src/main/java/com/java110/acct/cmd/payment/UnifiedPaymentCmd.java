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


@Java110CmdDoc(title = "统一支付接口",
        description = "系统中的统一支付接口",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/payment.unifiedPayment",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "payment.unifiedPayment"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "business",length = 64, remark = "支付场景，比如场地预约 为 venueReservation"),
        @Java110ParamDoc(name = "payAdapt",length = 64, remark = "支付适配器，非必填"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "tradeType", length = 30, remark = "支付类型 NATIVE JSAPI APP"),
        @Java110ParamDoc(name = "...", length = 30, remark = "其他参数根据相应接口协议传"),

})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 100 成功不需要唤起支付窗口，直接支付成功，可能从账户等做了扣款，其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "....", type = "String", length = 250, remark = "相应支付厂商要求字段，具体参考 不同厂商协议"),

        }
)

@Java110ExampleDoc(
        reqBody="{\"business\":\"venueReservation\",\"communityId\":\"123123\",\"...\":\"...\"}",
        resBody="{\"code\":0,\"msg\":\"成功\",\"...\":\"...\"}"
)

/**
 * 统一支付接口
 */
@Java110Cmd(serviceCode = "payment.unifiedPayment")
public class UnifiedPaymentCmd extends Cmd{


    private static final Logger logger = LoggerFactory.getLogger(UnifiedPaymentCmd.class);

    protected static final String DEFAULT_PAYMENT_ADAPT = "wechatPaymentFactory";// 默认微信通用支付

    /**
     * 校验
     * @param event              事件对象
     * @param context 请求报文数据
     * @param reqJson
     * @throws CmdException
     */

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson,"business","未包含业务");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        logger.debug(">>>>>>>>>>>>>>>>支付参数报文,{}",reqJson.toJSONString());
        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");


        //1.0 查询当前支付的业务

        IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(reqJson.getString("business"), IPaymentBusiness.class);

        if(paymentBusiness == null){
            throw new CmdException("当前支付业务不支持");
        }

        //2.0 相应业务 下单 返回 单号 ，金额，
        PaymentOrderDto paymentOrderDto =  paymentBusiness.unified(context,reqJson);
        paymentOrderDto.setAppId(appId);
        paymentOrderDto.setUserId(userId);


        logger.debug(">>>>>>>>>>>>>>>>支付业务下单返回,{}",JSONObject.toJSONString(paymentOrderDto));

        String env = MappingCache.getValue(MappingConstant.ENV_DOMAIN,"HC_ENV");

        // 这里 演示环境不向微信下单
        if ("DEV".equals(env) || "TEST".equals(env)) {
            paymentBusiness.notifyPayment(paymentOrderDto,reqJson);
            JSONObject param = new JSONObject();
            param.put("code", "100");
            param.put("msg", "演示环境不触发支付");
            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
            return ;
        }

        // 3.0 如果支付金额为0 直接调用 支付完通知接口
        if (paymentOrderDto.getMoney() <= 0) {
            paymentBusiness.notifyPayment(paymentOrderDto,reqJson);
            JSONObject param = new JSONObject();
            param.put("code", "100");
            param.put("msg", "扣费为0回调成功");
            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
            return ;
        }

        // 3.0 寻找当前支付适配器
        String payAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAYMENT_ADAPT);
        payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAYMENT_ADAPT : payAdapt;

        if(reqJson.containsKey("payAdapt") && !StringUtil.isEmpty(reqJson.getString("payAdapt"))){
            payAdapt = reqJson.getString("payAdapt");
        }

        IPaymentFactoryAdapt tPayAdapt = ApplicationContextFactory.getBean(payAdapt, IPaymentFactoryAdapt.class);

        // 4.0 相应支付厂家下单
        Map result = null;
        try {
            result = tPayAdapt.java110Payment(paymentOrderDto,reqJson, context);
        } catch (Exception e) {
            logger.error("支付异常",e);
            throw new CmdException(e.getLocalizedMessage());
        }
        ResponseEntity<String> responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);

        logger.debug("调用支付厂家返回,{}",responseEntity);
        context.setResponseEntity(responseEntity);


        // redis 中 保存 请求参数
        CommonCache.setValue("unifiedPayment_"+paymentOrderDto.getOrderId(),reqJson.toJSONString(),CommonCache.PAY_DEFAULT_EXPIRE_TIME);
    }
}
