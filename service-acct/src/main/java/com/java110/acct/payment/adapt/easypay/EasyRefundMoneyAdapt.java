package com.java110.acct.payment.adapt.easypay;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IRefundMoneyAdapt;
import com.java110.acct.payment.adapt.bbgpay.EncryptDecryptFactory;
import com.java110.acct.payment.adapt.easypay.utils.HttpConnectUtils;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.core.client.OssUploadTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.paymentPool.PaymentPoolDto;
import com.java110.dto.paymentPoolValue.PaymentPoolValueDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.intf.acct.IOnlinePayRefundV1InnerServiceSMO;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.utils.util.PayUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("easyRefundMoney")
public class EasyRefundMoneyAdapt implements IRefundMoneyAdapt {


    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl;

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;


    private static Logger logger = LoggerFactory.getLogger(EasyRefundMoneyAdapt.class);

    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;

    @Autowired
    private OssUploadTemplate ossUploadTemplate;

    @Autowired
    private IOnlinePayRefundV1InnerServiceSMO onlinePayRefundV1InnerServiceSMOImpl;

    @Override
    public ResultVo refund(OnlinePayDto onlinePayDto, PaymentPoolDto paymentPoolDto) throws Exception {

        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolDto.getPpId());
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);

        if (paymentPoolValueDtos == null || paymentPoolValueDtos.isEmpty()) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }

        String ORGID = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGID"); // 客户编号
        String ORGMERCODE = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGMERCODE");
        String ORGTERMNO = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGTERMNO");
        String EASYPAY_PUBLIC_KEY = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "EASYPAY_PUBLIC_KEY");
        String MER_RSA_PRIVATE_KEY = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "MER_RSA_PRIVATE_KEY");

        JSONObject paramIn = new JSONObject();
        paramIn.put("orgId", ORGID);
        paramIn.put("orgMercode", ORGMERCODE);
        paramIn.put("orgTermno", ORGTERMNO);
        paramIn.put("signType", BasePay.SIGN_TYPE_RSA256);
        paramIn.put("orgTrace", GenerateCodeFactory.getGeneratorId("10"));

        JSONObject bizData = new JSONObject();
        bizData.put("oriOrgTrace", onlinePayDto.getOrderId());
        bizData.put("transAmt", PayUtil.moneyToIntegerStr(Double.parseDouble(onlinePayDto.getRefundFee())));

        paramIn.put("bizData", bizData);
        String sign = BasePay.sign(paramIn, MER_RSA_PRIVATE_KEY);
        paramIn.put("sign", sign);

        String requestStr = paramIn.toJSONString();

        String response = null;
        try {
            response = HttpConnectUtils.sendHttpSRequest(BasePay.BASE_URL + "/ledger/mposrefund", requestStr, "JSON", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\n响应报文：" + response);
        BasePay.checkSign(response, EASYPAY_PUBLIC_KEY);

        JSONObject paramOut = JSONObject.parseObject(response);
        if (!"000000".equals(paramOut.getString("sysRetCode"))) {
            return new ResultVo(ResultVo.CODE_ERROR, "退款失败" + paramOut.getString("sysRetMsg"));
        }

        JSONObject resData = paramOut.getJSONObject("bizData");

        if (!"00".equals(resData.getString("tradeRetCode"))) {
            return new ResultVo(ResultVo.CODE_ERROR, "退款失败" + paramOut.getString("tradeRetMsg"));

        }
        return new ResultVo(ResultVo.CODE_OK, "退款完成");


    }


}
