package com.java110.acct.payment.adapt.wechat;

import com.java110.acct.payment.IRefundMoneyAdapt;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.core.client.OssUploadTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.onlinePayRefund.OnlinePayRefundDto;
import com.java110.dto.paymentPool.PaymentPoolDto;
import com.java110.dto.paymentPoolValue.PaymentPoolValueDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.dto.wechat.SmallWeChatDto;
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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.util.*;

@Service("wechatRefundMoney")
public class WechatRefundMoneyAdapt implements IRefundMoneyAdapt {


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

    public static final String wechatReturnUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    private static Logger logger = LoggerFactory.getLogger(WechatRefundMoneyAdapt.class);

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

        String mchId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_MCHID");
        String key = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_KEY");
        String certData = paymentPoolDto.getCertPath();

        System.out.println("------------------------------证书地址：" + certData);

        SortedMap<String, String> parameters = new TreeMap<String, String>();
        String paySwitch = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_PAY_SWITCH);
        parameters.put("appid", onlinePayDto.getAppId());//appid
        parameters.put("mch_id", mchId);//商户号
        if (WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
           // mchId = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID);
            parameters.put("mch_id", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID));//商户号
            parameters.put("sub_mch_id", mchId);//商户号
        }

        // todo 查询退费明细
        OnlinePayRefundDto onlinePayRefundDto = new OnlinePayRefundDto();
        onlinePayRefundDto.setPayId(onlinePayDto.getPayId());
        onlinePayRefundDto.setState(OnlinePayDto.STATE_WT);
        List<OnlinePayRefundDto> onlinePayRefundDtos = onlinePayRefundV1InnerServiceSMOImpl.queryOnlinePayRefunds(onlinePayRefundDto);
        String tranNo = GenerateCodeFactory.getGeneratorId("11");
        if (onlinePayRefundDtos != null && onlinePayRefundDtos.size() > 0) {
            tranNo = onlinePayRefundDtos.get(0).getRefundId();
        }


        parameters.put("nonce_str", PayUtil.makeUUID(32));//随机数
        parameters.put("out_trade_no", onlinePayDto.getOrderId());//商户订单号
        parameters.put("out_refund_no", tranNo);//我们自己设定的退款申请号，约束为UK
        parameters.put("total_fee", PayUtil.moneyToIntegerStr(Double.parseDouble(onlinePayDto.getTotalFee())));//订单金额 单位为分！！！这里稍微注意一下
        parameters.put("refund_fee", PayUtil.moneyToIntegerStr(Double.parseDouble(onlinePayDto.getRefundFee())));//退款金额 单位为分！！！
        parameters.put("sign", PayUtil.createSign(parameters, key));
        String xmlData = PayUtil.mapToXml(parameters);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(getPkcs12(certData));
        try {
            //这里写密码..默认是你的MCHID
            keyStore.load(inputStream, parameters.get("mch_id").toCharArray());
        } finally {
            inputStream.close();
        }
        SSLContext sslcontext = SSLContexts.custom()
                //这里也是写密码的
                .loadKeyMaterial(keyStore, parameters.get("mch_id").toCharArray())
                .build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        String jsonStr = "";
        try {
            HttpPost httpost = new HttpPost(wechatReturnUrl);
            httpost.setEntity(new StringEntity(xmlData, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                //接受到返回信息
                jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        Map<String, String> resMap = PayUtil.xmlStrToMap(jsonStr);
        if ("SUCCESS".equals(resMap.get("return_code")) && "SUCCESS".equals(resMap.get("result_code"))) {
            return new ResultVo(ResultVo.CODE_OK, "退款完成");
        } else {
            return new ResultVo(ResultVo.CODE_ERROR, resMap.get("return_msg"));
        }

    }

    private byte[] getPkcs12(String fileName) {
        List<FileDto> fileDtos = new ArrayList<>();
        byte[] context = null;
        String ftpPath = "hc/";
        String ossSwitch = MappingCache.getValue(MappingConstant.FILE_DOMAIN, OSSUtil.OSS_SWITCH);
        if (StringUtil.isEmpty(ossSwitch) || !OSSUtil.OSS_SWITCH_OSS.equals(ossSwitch)) {
            String ftpServer = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_SERVER);
            int ftpPort = Integer.parseInt(MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_PORT));
            String ftpUserName = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERNAME);
            String ftpUserPassword = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERPASSWORD);
            context = ftpUploadTemplate.downFileByte(ftpPath, fileName, ftpServer,
                    ftpPort, ftpUserName,
                    ftpUserPassword);
        } else {
            context = ossUploadTemplate.downFileByte(ftpPath, fileName, "",
                    0, "",
                    "");
        }
        return context;
    }

}
