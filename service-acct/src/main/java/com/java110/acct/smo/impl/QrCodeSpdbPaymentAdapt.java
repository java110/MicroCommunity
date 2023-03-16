package com.java110.acct.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.adapt.spdb.SPDBApiClient;
import com.java110.acct.payment.adapt.spdb.SPDBApiResponse;
import com.java110.acct.payment.adapt.spdb.SPDBSecurity;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PayUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 工商银行支付
 */
@Service
public class QrCodeSpdbPaymentAdapt implements IQrCodePaymentSMO {
    private static Logger logger = LoggerFactory.getLogger(QrCodeSpdbPaymentAdapt.class);

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";

    public static final String wxPayUnifiedOrder = "https://api.spdb.com.cn/spdb/prd/api/acquiring/appPay/initiation";

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Override
    public ResultVo pay(String communityId, String orderNum, double money, String authCode, String feeName) throws Exception {
        logger.info("【浦发银行支付】 统一下单开始, 订单编号=" + orderNum);
        String notifyUrl = UrlCache.getOwnerUrl() + "/app/payment/notify/wechat/992020011134400001";

        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        // double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN,"HC_ENV"), money);
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue("HC_ENV"), money);
        //添加或更新支付记录(参数跟进自己业务需求添加)

        CommonCache.setValue("spdb_qrcode_payment_" + orderNum, payAmount + "", CommonCache.PAY_DEFAULT_EXPIRE_TIME);

        String clientId = CommunitySettingFactory.getValue(communityId, "SPDB_CLIENT_ID");
        String secret = CommunitySettingFactory.getValue(communityId, "SPDB_SECRET");
        String privateKey = CommunitySettingFactory.getRemark(communityId, "SPDB_PRIVATE_KEY");
        String apiPublicKey = CommunitySettingFactory.getRemark(communityId, "SPDB_PUBLIC_KEY");
        String terminalNo = CommunitySettingFactory.getValue(communityId, "SPDB_TERMINAL_NO");
        String spdbMrchNo = CommunitySettingFactory.getValue(communityId, "SPDB_MRCH_NO");

        SPDBSecurity spdbSecurity = new SPDBSecurity(clientId, secret, privateKey, apiPublicKey);
        SPDBApiClient spdbApiClient = new SPDBApiClient(spdbSecurity);

        if (feeName.length() > 127) {
            feeName = feeName.substring(0, 126);
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("ordTtl", feeName);
        paramIn.put("terminalNo", terminalNo);
        // paramIn.put("busnPckt", terminalNo);
        paramIn.put("spdbMrchNo", spdbMrchNo);
        paramIn.put("createTimep", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_DEFAULT));
        paramIn.put("mrchlInfmAdr", notifyUrl);
        paramIn.put("cmdtyDsc", feeName);
        paramIn.put("terminaIP", PayUtil.getLocalIp());
        paramIn.put("channelNo", "00");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 1);
        paramIn.put("trnsFnshStrtTm", DateUtil.getFormatTimeString(c.getTime(), DateUtil.DATE_FORMATE_STRING_DEFAULT));
        paramIn.put("frmrMrchOrdrNo", orderNum);
        paramIn.put("tranAmt", payAmount);
        paramIn.put("prblmNo", orderNum);
        int pre = Integer.parseInt(authCode.substring(0, 2));
        if (pre > 24 && pre < 31) { // 支付宝
            paramIn.put("tranType", "OF");
        } else {
            paramIn.put("tranType", "OC");
        }
        paramIn.put("authrCd", authCode);

        SPDBApiResponse response = spdbApiClient.post(wxPayUnifiedOrder, paramIn.toJSONString());

        if (response.getHttpStatus() != 0000) {
            throw new IllegalArgumentException(response.getResBody());
        }
        logger.debug("统一下单返回" + response.getResBody());

        JSONObject paramOut = JSONObject.parseObject(response.getResBody());

        if (!"0000".equals(paramOut.getString("statusCode"))) {
            throw new IllegalArgumentException(paramOut.getString("statusMsg"));
        }


        if (!"00".equals(paramOut.getString("ordrSt"))) {
            return new ResultVo(ResultVo.CODE_ERROR, "未知异常");
        }

        return new ResultVo(ResultVo.CODE_OK, "成功");

    }

    public ResultVo checkPayFinish(String communityId, String orderNum) {
        SmallWeChatDto shopSmallWeChatDto = null;
        Map<String, String> result = null;

        String clientId = CommunitySettingFactory.getValue(communityId, "SPDB_CLIENT_ID");
        String secret = CommunitySettingFactory.getValue(communityId, "SPDB_SECRET");
        String privateKey = CommunitySettingFactory.getRemark(communityId, "SPDB_PRIVATE_KEY");
        String apiPublicKey = CommunitySettingFactory.getRemark(communityId, "SPDB_PUBLIC_KEY");
        String terminalNo = CommunitySettingFactory.getValue(communityId, "SPDB_TERMINAL_NO");
        String spdbMrchNo = CommunitySettingFactory.getValue(communityId, "SPDB_MRCH_NO");
        try {
            SPDBSecurity spdbSecurity = new SPDBSecurity(clientId, secret, privateKey, apiPublicKey);
            SPDBApiClient spdbApiClient = new SPDBApiClient(spdbSecurity);


            JSONObject paramIn = new JSONObject();
            paramIn.put("mrchOrdrNo", orderNum);
            paramIn.put("spdbMrchNo", spdbMrchNo);
            paramIn.put("tranDate", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H));

            String payAmount = CommonCache.getValue("spdb_qrcode_payment_" + orderNum);
            paramIn.put("tranAmt", payAmount);

            SPDBApiResponse response = spdbApiClient.post(wxPayUnifiedOrder, paramIn.toJSONString());

            if (response.getHttpStatus() != 0000) {
                throw new IllegalArgumentException(response.getResBody());
            }
            logger.debug("统一下单返回" + response.getResBody());

            JSONObject paramOut = JSONObject.parseObject(response.getResBody());

            if (!"0000".equals(paramOut.getString("statusCode"))) {
                throw new IllegalArgumentException(paramOut.getString("statusMsg"));
            }


            if ("00".equals(paramOut.getString("ordrSt"))) {
                return new ResultVo(ResultVo.CODE_OK, "成功");
            } else if ("09".equals(paramOut.getString("ordrSt"))) {
                return new ResultVo(ResultVo.CODE_WAIT_PAY, "等待支付完成");
            } else {
                return new ResultVo(ResultVo.CODE_ERROR, "支付已经被取消，银行 状态码：" + paramOut.getString("ordrSt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo(ResultVo.CODE_ERROR, "未知异常" + e.getMessage());
        }


    }
}
