package com.java110.acct.payment.adapt.bbgpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IRefundMoneyAdapt;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.core.client.OssUploadTemplate;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.PlutusFactory;
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
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("bbgRefundMoney")
public class BbgRefundMoneyAdapt implements IRefundMoneyAdapt {


    private static String VERSION = "1.0";

    private static String SIGN_TYPE = "RSA2";// 加密算法：SM4、RSA2

    private static String refundUrl = "https://mbank.bankofbbg.com/www/corepaycer/Refund";// 退款地址

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


    private static Logger logger = LoggerFactory.getLogger(BbgRefundMoneyAdapt.class);

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


        String mchtNo_SM4 = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "mchtNo_SM4");
        String tranNo = GenerateCodeFactory.getGeneratorId("11");


        Map<String, Object> params = new HashMap<>();
        params.put("version", VERSION);// 版本号 1.0
        params.put("mcht_no", mchtNo_SM4);// 收款商户编号
        params.put("tran_no", tranNo);// 商户流水
        params.put("org_tran_no", onlinePayDto.getOrderId());// 原平台流水
        params.put("device_ip", "172.0.0.1");// 设备发起交易IP
        params.put("amt", onlinePayDto.getRefundFee());// 交易金额
        params.put("ware_name", onlinePayDto.getPayName());// 摘要备注

        // 对准备加签参数排序
        String decryParams = EncryptDecryptFactory.execute(paymentPoolValueDtos, refundUrl, params);

        JSONObject paramOut = JSONObject.parseObject(decryParams);
        if ( !"SUCCESS".equals(paramOut.getString("status")) || !"SUCCESS".equals(paramOut.getString("deal_status"))) {
            return new ResultVo(ResultVo.CODE_ERROR, "退款失败" + paramOut.getString("return_message"));
        }
        if ( !"0000".equals(paramOut.getString("return_code")) && !"0001".equals(paramOut.getString("return_code"))) {
            return new ResultVo(ResultVo.CODE_ERROR, "退款失败" + paramOut.getString("return_message"));
        }
        if("0001".equals(paramOut.getString("return_code"))){
            return new ResultVo(ResultVo.CODE_OK, paramOut.getString("return_message"));

        }
        return new ResultVo(ResultVo.CODE_OK,  "退款完成");


    }


}
