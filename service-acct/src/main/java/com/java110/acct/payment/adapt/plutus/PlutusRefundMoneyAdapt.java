package com.java110.acct.payment.adapt.plutus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IRefundMoneyAdapt;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.core.client.OssUploadTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.PlutusFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.onlinePayRefund.OnlinePayRefundDto;
import com.java110.dto.paymentPool.PaymentPoolDto;
import com.java110.dto.paymentPoolValue.PaymentPoolValueDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.intf.acct.IOnlinePayRefundV1InnerServiceSMO;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.OSSUtil;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.util.*;

@Service("plutusRefundMoney")
public class PlutusRefundMoneyAdapt implements IRefundMoneyAdapt {


    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";

    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";

    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";

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

    public static final String wechatReturnUrl = "https://api.plutuspay.com/open/v2/refund";

    private static Logger logger = LoggerFactory.getLogger(PlutusRefundMoneyAdapt.class);

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


        String mchId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_MCHID");
        String key = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_KEY");
        String privateKey = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_PRIVATE_KEY");
        String devId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_DEV_ID");
        String publicKey = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_PUBLIC_KEY");

        JSONObject parameters = new JSONObject();
        parameters.put("sn", mchId);//商户号
        parameters.put("outTradeId", onlinePayDto.getOrderId());//商户号
        parameters.put("outRefundId", onlinePayDto.getPayId());//我们自己设定的退款申请号，约束为UK
        parameters.put("refundAmount", PayUtil.moneyToIntegerStr(Double.parseDouble(onlinePayDto.getTotalFee())));//订单金额 单位为分！！！这里稍微注意一下


        String param = PlutusFactory.Encryption(parameters.toJSONString(), privateKey, key, devId);
        System.out.println(param);

        String str = PlutusFactory.post(wechatReturnUrl, param);
        System.out.println(str);

        JSONObject json = JSON.parseObject(str);

        String signature = json.getString("signature");
        String content = json.getString("content");


        //验签
        Boolean verify = PlutusFactory.verify256(content, org.bouncycastle.util.encoders.Base64.decode(signature), publicKey);
        //验签成功
        if (!verify) {
            throw new IllegalArgumentException("支付失败签名失败");
        }
        //解密
        byte[] bb = PlutusFactory.decrypt(Base64.decode(content), key);
        //服务器返回内容
        String paramOut = new String(bb);
        System.out.println(paramOut);

        JSONObject paramObj = JSONObject.parseObject(paramOut);

        if (paramObj.getIntValue("status") != 2) {
            return new ResultVo(ResultVo.CODE_ERROR, paramObj.getString("remark"));
        } else {
            return new ResultVo(ResultVo.CODE_OK, "退款完成");
        }

    }


}
