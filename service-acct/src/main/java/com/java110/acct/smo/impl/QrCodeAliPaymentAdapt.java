package com.java110.acct.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * 阿里支付
 */
@Service
public class QrCodeAliPaymentAdapt implements IQrCodePaymentSMO {

    private static Logger logger = LoggerFactory.getLogger(QrCodeAliPaymentAdapt.class);

    /**
     * APP_ID 应用id
     */
    public final static String APP_ID = "2016093000630181";

    /**
     * 应用私钥
     */
    public final static String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCTO5ERumMOQlwy2+2hrSUgqFd+PsQ5Au+sK7+xNrB6fmR2oSWSn+X8EEjQBWq21RWV1ekABWSsG2BtOWlnWxT77tfyXLOzejR3vujFhwLPshzA9lfR2R5fH/F3l0MGmWvb3GGYqp2q2SR7jXUtOU5dEEGHUYRd9zoy9CPEM2Iu1xWA5ks51C5XH2AJV62ZDCc82sAJt7xfrDLCZbwKGp0a4b6ZgA1OoZDn7Jzp160fFA81EbkfrKK+qOoyMV9dbHSotHHQfD3+49o1coz3vpPY9r/kD6FhZ3ZdU1SfYccaEpyMAbdfR/RAtHlRiA9JUgXXFiExavKUEwdEGizwug45AgMBAAECggEAftF2GXEUVXnvdJdfTj2Xl1OpWUKzPfA7hW+BhCF5TWmFG3GerXcxYDaeoR+pVaahGxjPw4bhuiUyn6IuGiqoHoESXN5gox2GCAbW9R1f3IqsncESz9xWftVC5iHSR+LqtsxS/G8ps7mp5QppffS4fQy9hNNAUrfa13zXOe6QiYq/BHiBzJKT4Bp4P5iQMM4/J/Y4fPOHTHEo4iZX+a2WC2ZBMbAXZWfMmIKenqZVDuf7GO8/TpHMpkI9pEePU7njSqRX2+zixcVJ/M8EgSj0JCFZUUCKDz78Nuu2UvdTrLfFTYTIrr4nhVdsduDpJ0LKSJEcvaxku1AhgRgWBgfj6QKBgQDzugV2tnWG495gQyipqPXIYd2JVIdWfAgUfNdDD8bd/FZ/grATQo4R4zDdgXS5a2c0AFzJU7MMkWo4DTKpjU6aZIdY8PdgnI3SefKJzw3ndBGPou5RbVFVkrehahU4u1LVKldmo/pvlMCqpp00UBkO2Tzl4v6lLQsRKXvJCDJvjwKBgQCapZpIW+nk8QjkArsqY2j9NQeRz3JRQ6UJkW4sf+4JgbtXgXgu/arpCTevCi50zqv8TE4nmJj8YtE0H7qbBgOSgJV2i+0/eRCsV8oNu9WzQVM8qyGtoQ4fKxhMNN2CcWGh5gXk2wRG6qFz69MxEJ+RAK5ev4w1bDu89SS4d5dBtwKBgDqEZEFvZ2JenYqxNTce6PwWezE4yVG7b9kzbB+ezxmHN2FgCFRne2LSEG5uYY8POXUjzSNNZETVORKCILLSyZeKIXD6UfqxO9/YBaKOwwDaPkklTmDghVGta0dnB/daCnZBt0BeiId3yJZwcHgZ7xpAkD2FtawE145kTmMjA7EDAoGAEqpGMtGVK2LENfD2cilJdLY/0aN+IrzNYJhC+e/+5PrZh7hCrzlCtZm9NM0/yi1fqLX1AOZ0IJI0udvSg/930ujeIU7GNdEA6Cw4YOv44QfTahEloXU85Roodpoy2hmhNQ80Suj8XSrYjcs61EMzXyb75MetvozvsODNq84TXYUCgYB11cFAnLXztA2wKjRSsPP8Hm1vDLmq4CoM2qyqeDSdVCeRqQJ4VSwtsFsGRPXfrrx4CbTKDRiV04hJTVYtnpX67bo34GIXxW1RtLYeCk1lWBI7u3D398qMGcpbo3FzwJ+v8pD3uzTTnk88Xx2qUeghRoLvQd/KP0dcfpZLllBOFA==";
    /**
     * 编码
     */
    public final static String CHARSET = "UTF-8";

    /**
     * 支付宝公钥
     */
    public final static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjeZA25b4mJZpWWXHR0joM8eOSm1DuBDKKgq5ZYDkxKUPhLuXfK588EcFaSGlW9yzpvNkxBbq3QEzqx5K3zYaVHjzh0y+8vlfCcFINMpcYgy3q8NsQHgipO994TqIUwukac8GzezVAT7avtwBeWMNGJDnpQAJNkOD6se2sMu2IXFa7GkfRxhJCMNi/e/d16j0D/YfT2F1c1hRzH1Ey0I3Rkx1WxZTPYH+lCSVaIkxVtolYIpoktE70ZVcD1ERy5eipWCT4sdl/UMifgTnj+PyL+R6lzMUVPKQ+xk5G5PoJUiRSAOkqcaB+KTaerL0h0VzOXKqQ2O/3ppdrTn4X+mAgQIDAQAB";
    /**
     * (沙箱)网关
     */
    public final static String GETEWAY_URL = "https://openapi.alipay.com/gateway.do";

    /**
     * 格式化
     */
    public final static String FORMAT = "json";
    /**
     * 格式化
     */
    public final static String APP_AUTH_TOKEN = "APP_AUTH_TOKEN";

    /**
     * 签名类型
     */
    public final static String SIGN_TYPE = "RSA2";

    @Override
    public ResultVo pay(String communityId, String orderNum, double money, String authCode, String feeName) throws Exception {
        String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);

        AlipayClient alipayClient = new DefaultAlipayClient(GETEWAY_URL,
                CommunitySettingFactory.getValue(communityId, "APP_ID"),
                CommunitySettingFactory.getRemark(communityId, "APP_PRIVATE_KEY"),
                "json", "UTF-8", CommunitySettingFactory.getRemark(communityId, "ALIPAY_PUBLIC_KEY"), "RSA2");
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        if (!StringUtil.isEmpty(CommunitySettingFactory.getValue(communityId, APP_AUTH_TOKEN))) {
            request.putOtherTextParam("app_auth_token", CommunitySettingFactory.getValue(communityId, APP_AUTH_TOKEN));
        }
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderNum);
        bizContent.put("total_amount", money);
        bizContent.put("subject", systemName + feeName);
        bizContent.put("scene", "bar_code");
        bizContent.put("auth_code", authCode);
        request.setBizContent(bizContent.toString());
        AlipayTradePayResponse response = alipayClient.execute(request);
        logger.debug("支付宝返回:" + JSONObject.toJSONString(response));
        if ("10000".equals(response.getCode()) && response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

        if ("10000".equals(response.getCode()) && response.isSuccess()) {
            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else {
            return new ResultVo(ResultVo.CODE_ERROR, StringUtil.isEmpty(response.getSubMsg())?"等待用户支付":response.getSubMsg());
        }
    }

    @Override
    public ResultVo checkPayFinish(String communityId, String orderNum) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                CommunitySettingFactory.getValue(communityId, "APP_ID"),
                CommunitySettingFactory.getRemark(communityId, "APP_PRIVATE_KEY"),
                "json", "UTF-8",
                CommunitySettingFactory.getRemark(communityId, "ALIPAY_PUBLIC_KEY"), "RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        if (!StringUtil.isEmpty(CommunitySettingFactory.getValue(communityId, APP_AUTH_TOKEN))) {
            request.putOtherTextParam("app_auth_token", CommunitySettingFactory.getValue(communityId, APP_AUTH_TOKEN));
        }
        request.setBizContent("{" +
                "  \"out_trade_no\":\"" + orderNum + "\"," +
                "  \"trade_no\":\"\"," +
                "  \"query_options\":[" +
                "    \"trade_settle_info\"" +
                "  ]" +
                "}");
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            return new ResultVo(ResultVo.CODE_ERROR, "查询失败" + e.getErrMsg());
        }
        logger.debug("支付宝返回:" + JSONObject.toJSONString(response));
        if (!"10000".equals(response.getCode())) {
            return new ResultVo(ResultVo.CODE_ERROR, response.getMsg());
        }
        String tradeStatus = response.getTradeStatus();
        if ("WAIT_BUYER_PAY".equals(tradeStatus)) {
            return new ResultVo(ResultVo.CODE_WAIT_PAY, "等待支付完成");
        } else if ("TRADE_SUCCESS".equals(tradeStatus)) {
            return new ResultVo(ResultVo.CODE_OK, "支付成功");
        } else {
            return new ResultVo(ResultVo.CODE_ERROR, "未知错误" + tradeStatus);
        }
    }
}
