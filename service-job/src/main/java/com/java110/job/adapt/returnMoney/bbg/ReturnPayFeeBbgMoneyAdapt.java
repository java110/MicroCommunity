package com.java110.job.adapt.returnMoney.bbg;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.core.client.OssUploadTemplate;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.onlinePay.OnlinePayDto;
import com.java110.entity.order.Business;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.returnMoney.bbg.lib.AesEncrypt;
import com.java110.job.adapt.returnMoney.bbg.lib.CAUtil;
import com.java110.job.adapt.returnMoney.bbg.lib.HttpRequestUtil;
import com.java110.job.adapt.returnMoney.bbg.lib.JsonUtil;
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
@Component(value = "returnPayFeeBbgMoneyAdapt")
public class ReturnPayFeeBbgMoneyAdapt extends DatabusAdaptImpl {

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";

    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";

    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";

    private static String VERSION = "1.0";

    private static String SIGN_TYPE = "RSA2";// 加密算法：SM4、RSA2

    private static String refundUrl = "https://mbank.bankofbbg.com/www/corepaycer/Refund";// 退款地址

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

    private static Logger logger = LoggerFactory.getLogger(ReturnPayFeeBbgMoneyAdapt.class);

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
     * @param onlinePayPo
     */
    public void doPayFeeMoney(OnlinePayPo onlinePayPo) throws Exception {
        //查询小区信息
        OnlinePayDto onlinePayDto = new OnlinePayDto();
        onlinePayDto.setPayId(onlinePayPo.getPayId());
        onlinePayDto.setState(OnlinePayDto.STATE_WT);
        List<OnlinePayDto> onlinePayDtos = onlinePayV1InnerServiceSMOImpl.queryOnlinePays(onlinePayDto);
        if (onlinePayDtos == null || onlinePayDtos.size() < 1) {
            return;
        }

        String communityId = "";

        String mchtNo_SM4 = CommunitySettingFactory.getValue(communityId, "mchtNo_SM4");

        Map<String, Object> params = new HashMap<>();
        params.put("version", VERSION);// 版本号 1.0
        params.put("mcht_no", mchtNo_SM4);// 收款商户编号
        params.put("tran_no", onlinePayDto.getOrderId());// 商户流水
        params.put("org_txn_no", onlinePayDto.getTransactionId());// 原平台流水
        params.put("device_ip", "172.0.0.1");// 设备发起交易IP
        params.put("amt", onlinePayDto.getRefundFee());// 交易金额
        params.put("ware_name", onlinePayDto.getPayName());// 摘要备注

        // 对准备加签参数排序
        String decryParams = EncryptDecryptFactory.execute(communityId, refundUrl, params);

        JSONObject paramOut = JSONObject.parseObject(decryParams);
        if (!"0000".equals(paramOut.getString("return_code"))
                || !"SUCCESS".equals(paramOut.getString("status"))
                || !"SUCCESS".equals(paramOut.getString("deal_status"))) {
            doUpdateOnlinePay(onlinePayDtos.get(0).getOrderId(), OnlinePayDto.STATE_FT, "退款失败" + paramOut.getString("return_message"));
            return;
        }
        doUpdateOnlinePay(onlinePayDtos.get(0).getOrderId(), OnlinePayDto.STATE_CT, "退款完成");

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
