package com.java110.acct.payment.adapt.wechat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.integral.IGiftIntegral;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.core.client.OssUploadTemplate;
import com.java110.core.client.RestTemplate;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.integral.GiftIntegralDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class WechatIntegralShareAcct {

    private static final Logger logger = LoggerFactory.getLogger(WechatIntegralShareAcct.class);


    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";


    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";

    // 添加分账接收方API
    private static final String ADD_RECEIVER_URL = "https://api.mch.weixin.qq.com/pay/profitsharingaddreceiver";
    private static final String FINISH_RECEIVER_URL = "https://api.mch.weixin.qq.com/secapi/pay/profitsharingfinish";

    // 删除分账接收方api
    private static final String DELETE_RECEIVER_URL = "https://api.mch.weixin.qq.com/pay/profitsharingremovereceiver";

    // 请求分账API
    private static final String PROFIT_SHARING_URL = "https://api.mch.weixin.qq.com/secapi/pay/profitsharing";

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;

    @Autowired
    private OssUploadTemplate ossUploadTemplate;

    @Autowired
    private IGiftIntegral giftIntegralImpl;

    public JSONArray addReceivers(GiftIntegralDto giftIntegralDto) throws Exception {
        JSONArray params = new JSONArray();
        JSONObject param = new JSONObject();
        param.put("type", "MERCHANT_ID");
        param.put("account", giftIntegralDto.getPlatformMchId());
        param.put("amount", PayUtil.moneyToIntegerStr(giftIntegralDto.getMoney()));
        param.put("description", "积分费用");

        params.add(param);

        JSONObject paramIn = new JSONObject();
        paramIn.put("type", "MERCHANT_ID");
        paramIn.put("account", giftIntegralDto.getPlatformMchId());
        paramIn.put("name", giftIntegralDto.getPlatformMchName());
        paramIn.put("relation_type", "PARTNER");

        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("appid", giftIntegralDto.getAppId());
        paramMap.put("mch_id", giftIntegralDto.getMchId());
        paramMap.put("nonce_str", PayUtil.makeUUID(32));

        String paySwitch = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_PAY_SWITCH);
        if (WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
            paramMap.put("appid", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_APP_ID));  //服务商appid，是服务商注册时公众号的id
            paramMap.put("mch_id", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID));  //服务商商户号
            //paramMap.put("sub_appid", smallWeChatDto.getAppId());//起调小程序appid
            paramMap.put("sub_mch_id", giftIntegralDto.getMchId());//起调小程序的商户号

        }
        paramMap.put("receiver", paramIn.toJSONString());
        paramMap.put("sign", PayUtil.createSignSha256(paramMap, giftIntegralDto.getMchKey()));
        //转换为xml
        String xmlData = PayUtil.mapToXml(paramMap);

        logger.debug("调用添加接收方接口" + xmlData);
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(
                ADD_RECEIVER_URL, xmlData, String.class);
        logger.debug("添加接收方接口返回" + responseEntity);
        System.out.println("调用添加接收方接口" + xmlData + ";添加接收方接口返回" + responseEntity);

        return params;
    }


    /**
     * 申请分账
     *
     * @return
     * @throws Exception
     */
    public JSONObject applyProfileShare(PaymentOrderDto paymentOrderDto, GiftIntegralDto giftIntegralDto, JSONArray params) throws Exception {
        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("appid", giftIntegralDto.getAppId());
        paramMap.put("mch_id", giftIntegralDto.getMchId());
        paramMap.put("nonce_str", PayUtil.makeUUID(32));
        paramMap.put("transaction_id", paymentOrderDto.getTransactionId());
        paramMap.put("out_order_no", "FZ" + paymentOrderDto.getOrderId());

        String paySwitch = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_PAY_SWITCH);
        if (WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
            paramMap.put("appid", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_APP_ID));  //服务商appid，是服务商注册时公众号的id
            paramMap.put("mch_id", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID));  //服务商商户号
            //paramMap.put("sub_appid", smallWeChatDto.getAppId());//起调小程序appid
            paramMap.put("sub_mch_id", giftIntegralDto.getMchId());//起调小程序的商户号
        }
        paramMap.put("receivers", params.toJSONString());
        paramMap.put("sign", PayUtil.createSignSha256(paramMap, giftIntegralDto.getMchKey()));
        //转换为xml
        String xmlData = PayUtil.mapToXml(paramMap);

        JSONObject paramOut = request(PROFIT_SHARING_URL, xmlData, giftIntegralDto, paramMap.get("mch_id"));
        logger.debug("请求分账 信息{}，返回分账信息{}", xmlData, paramOut.toJSONString());
        return paramOut;

    }


    public void deleteReceivers(GiftIntegralDto giftIntegralDto) throws Exception {

        JSONArray deleteParams = new JSONArray();
        JSONObject param = null;
        Double amount = 0.0;

        JSONObject paramIn = new JSONObject();
        paramIn.put("type", "MERCHANT_ID");
        paramIn.put("account", giftIntegralDto.getPlatformMchId());
        deleteParams.add(paramIn);

        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("appid", giftIntegralDto.getAppId());
        paramMap.put("mch_id", giftIntegralDto.getMchId());
        paramMap.put("nonce_str", PayUtil.makeUUID(32));

        String paySwitch = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_PAY_SWITCH);
        if (WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
            paramMap.put("appid", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_APP_ID));  //服务商appid，是服务商注册时公众号的id
            paramMap.put("mch_id", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID));  //服务商商户号
            // paramMap.put("sub_appid", smallWeChatDto.getAppId());//起调小程序appid
            paramMap.put("sub_mch_id", giftIntegralDto.getMchId());//起调小程序的商户号
        }
        paramMap.put("receiver", deleteParams.toJSONString());
        paramMap.put("sign", PayUtil.createSignSha256(paramMap, giftIntegralDto.getMchKey()));
        //转换为xml
        String xmlData = PayUtil.mapToXml(paramMap);

        logger.debug("调用添加接收方接口" + xmlData);
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(
                DELETE_RECEIVER_URL, xmlData, String.class);
        logger.debug("添加接收方接口返回" + responseEntity);

    }

    private JSONObject request(String url, String reqData, GiftIntegralDto giftIntegralDto, String key) {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(getPkcs12(giftIntegralDto.getCertPath()));
        JSONObject paramOut = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            //这里写密码..默认是你的MCHID
            keyStore.load(inputStream, key.toCharArray());

            SSLContext sslcontext = SSLContexts.custom()
                    //这里也是写密码的
                    .loadKeyMaterial(keyStore, key.toCharArray())
                    .build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            String jsonStr = "";

            HttpPost httpost = new HttpPost(url);
            httpost.setEntity(new StringEntity(reqData, "UTF-8"));
            httpost.setHeader("Accept", "text/xml, text/plain");
            httpost.setHeader("Content-Type", "text/xml; charset=utf-8");
            httpost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            response = httpclient.execute(httpost);

            HttpEntity entity = response.getEntity();
            //接受到返回信息
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            EntityUtils.consume(entity);
            System.out.println("调用添加接收方接口" + reqData + ";添加接收方接口返回" + jsonStr);
            paramOut = JSONObject.parseObject(JSONObject.toJSONString(PayUtil.xmlStrToMap(jsonStr)));
        } catch (Exception e) {
            logger.error("请求微信异常", e);
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return paramOut;

    }

    private byte[] getPkcs12(String fileName) {
        List<FileDto> fileDtos = new ArrayList<>();

        byte[] context = null;
        String ftpPath = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_PATH);

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

    public void sendIntegral(GiftIntegralDto giftIntegralDto) {

        giftIntegralImpl.gift(giftIntegralDto);
    }
}
