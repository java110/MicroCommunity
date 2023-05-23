package com.java110.job.adapt.returnMoney.wechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.core.client.OssUploadTemplate;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.onlinePay.OnlinePayDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.entity.order.Business;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.onlinePay.OnlinePayPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.OSSUtil;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
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
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.util.*;

/**
 * 退费审核通过后 通知 微信支付平台退款处理
 *
 * @author fqz
 * @Date 2021-08-19 10:12
 */
@Component(value = "returnPayFeeMoneyAdapt")
public class ReturnPayFeeMoneyAdapt extends DatabusAdaptImpl {

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";

    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";

    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";

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

    private static Logger logger = LoggerFactory.getLogger(ReturnPayFeeMoneyAdapt.class);

    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;

    @Autowired
    private OssUploadTemplate ossUploadTemplate;

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        OnlinePayPo oaWorkflowDataPo = BeanConvertUtil.covertBean(data, OnlinePayPo.class);
        try {
            doPayFeeMoney(oaWorkflowDataPo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知退款
     *
     * @param oaWorkflowDataPo
     */
    public void doPayFeeMoney(OnlinePayPo oaWorkflowDataPo) throws Exception {
        //查询小区信息
        OnlinePayDto onlinePayDto = new OnlinePayDto();
        onlinePayDto.setPayId(oaWorkflowDataPo.getPayId());
        onlinePayDto.setState(OnlinePayDto.STATE_WT);
        List<OnlinePayDto> onlinePayDtos = onlinePayV1InnerServiceSMOImpl.queryOnlinePays(onlinePayDto);
        if (onlinePayDtos == null || onlinePayDtos.size() < 1) {
            return;
        }
        String payPassword = "";
        String certData = "";
        String mchPassword = "";
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setMchId(onlinePayDtos.get(0).getMchId());
        smallWeChatDto.setAppId(onlinePayDtos.get(0).getAppId());
        List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            payPassword = MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "key");
            certData = MappingCache.getRemark(MappingConstant.WECHAT_STORE_DOMAIN, "cert");
            mchPassword = MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "mchId");
        } else {
            payPassword = smallWeChatDtos.get(0).getPayPassword();
            certData = smallWeChatDtos.get(0).getCertPath();
            mchPassword = smallWeChatDtos.get(0).getMchId();
            if(StringUtil.isEmpty(certData)){
                certData = MappingCache.getRemark(MappingConstant.WECHAT_STORE_DOMAIN, "cert");
            }
        }


        SortedMap<String, String> parameters = new TreeMap<String, String>();
        String paySwitch = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_PAY_SWITCH);
        parameters.put("appid", onlinePayDtos.get(0).getAppId());//appid
        parameters.put("mch_id", onlinePayDtos.get(0).getMchId());//商户号
        if (WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
            mchPassword = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID);
            parameters.put("mch_id", mchPassword);//商户号
            parameters.put("sub_mch_id", onlinePayDtos.get(0).getMchId());//商户号
        }
        parameters.put("nonce_str", PayUtil.makeUUID(32));//随机数
        parameters.put("out_trade_no", onlinePayDtos.get(0).getOrderId());//商户订单号
        parameters.put("out_refund_no", onlinePayDtos.get(0).getPayId());//我们自己设定的退款申请号，约束为UK
        parameters.put("total_fee", PayUtil.moneyToIntegerStr(Double.parseDouble(onlinePayDtos.get(0).getTotalFee())));//订单金额 单位为分！！！这里稍微注意一下
        parameters.put("refund_fee", PayUtil.moneyToIntegerStr(Double.parseDouble(onlinePayDtos.get(0).getRefundFee())));//退款金额 单位为分！！！
        parameters.put("sign", PayUtil.createSign(parameters, payPassword));
        String xmlData = PayUtil.mapToXml(parameters);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(getPkcs12(certData));
        try {
            //这里写密码..默认是你的MCHID
            keyStore.load(inputStream, mchPassword.toCharArray());
        } finally {
            inputStream.close();
        }
        SSLContext sslcontext = SSLContexts.custom()
                //这里也是写密码的
                .loadKeyMaterial(keyStore, mchPassword.toCharArray())
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
            doUpdateOnlinePay(onlinePayDtos.get(0).getOrderId(), OnlinePayDto.STATE_CT, "退款完成");
        } else {
            doUpdateOnlinePay(onlinePayDtos.get(0).getOrderId(), OnlinePayDto.STATE_FT, resMap.get("return_msg"));
        }
    }

    private void doUpdateOnlinePay(String orderId, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);
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
