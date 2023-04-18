package com.java110.job.adapt.market.executor;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.OutRestTemplate;
import com.java110.dto.market.MarketSmsValueDto;
import com.java110.dto.market.MarketTextDto;
import com.java110.intf.common.IMarketSmsValueV1InnerServiceSMO;
import com.java110.job.adapt.market.DefaultSendExecutor;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * https://console.shlianlu.com/#/document/api_4_2
 * <p>
 * 联麓信息
 */
@Service("sendExecutorSMS")
public class SendExecutorSMS extends DefaultSendExecutor {

    //短信地址
    private static final String SMS_URL = "https://api.shlianlu.com/sms/trade/normal/send";

    private static final String APP_ID = "AppId";
    private static final String APP_KEY = "AppKey";
    private static final String MCH_ID = "MchId";
    private static final String SIGN_NAME = "SignName";

    public static String Version = "Version";
    public static String FIELD_SIGN = "Signature";
    public static String ContextParamSet = "ContextParamSet";
    public static String TemplateParamSet = "TemplateParamSet";
    public static String SessionContextSet = "SessionContextSet";
    public static String PhoneNumberSet = "PhoneNumberSet";
    public static String SessionContext = "SessionContext";

    @Autowired
    private IMarketSmsValueV1InnerServiceSMO marketSmsValueV1InnerServiceSMOImpl;

    @Autowired
    private OutRestTemplate outRestTemplate;

    @Override
    public ResultVo doSend(MarketTextDto marketTextDto, String tel, String communityId,String openId) {

        MarketSmsValueDto marketSmsValueDto = new MarketSmsValueDto();
        marketSmsValueDto.setSmsId(marketTextDto.getSmsId());
        List<MarketSmsValueDto> marketSmsValueDtos = marketSmsValueV1InnerServiceSMOImpl.queryMarketSmsValues(marketSmsValueDto);

        if (marketSmsValueDtos == null || marketSmsValueDtos.size() < 1) {
            throw new IllegalArgumentException("未包含 sms 配置信息");
        }

        JSONObject param = new JSONObject();
        param.put("Type", "1");
        param.put("PhoneNumberSet", new String[]{tel});
        param.put("AppId", getMarketValue(marketSmsValueDtos, APP_ID));
        //param.put("ExtendCode","");
        param.put("Version", "1.1.0");
        param.put("MchId", getMarketValue(marketSmsValueDtos, MCH_ID));
        param.put("SessionContext", marketTextDto.getTextContent());
        param.put("SignType", "MD5");
        param.put("TimeStamp", DateUtil.getCurrentDate().getTime());
        param.put("SignName", getMarketValue(marketSmsValueDtos, SIGN_NAME));

        param.put("Signature", generateSignature(param, getMarketValue(marketSmsValueDtos, APP_KEY)));

        HttpEntity httpEntity = new HttpEntity(param.toJSONString(), getHeaders());
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(SMS_URL, HttpMethod.POST, httpEntity, String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return new ResultVo(ResultVo.CODE_ERROR, responseEntity.getBody());
        }

        JSONObject result = JSONObject.parseObject(responseEntity.getBody());

        if ("00".equals(result.getString("status"))) {
            return new ResultVo(ResultVo.CODE_OK, "成功", "1001");
        }
        return new ResultVo(ResultVo.CODE_ERROR, result.getString("message"), "1001");


    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json;charset=utf-8");
        //httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        return httpHeaders;
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key  API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, Object> data, String key) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(FIELD_SIGN) || k.equals(PhoneNumberSet) || k.equals(TemplateParamSet) || k.equals(SessionContext) || k.equals(SessionContextSet) || k.equals(ContextParamSet)) {
                continue;
            }
            if (data.get(k) != null && data.get(k).toString().trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).toString().trim()).append("&");
        }
        sb.append("key=").append(key);
        System.out.println(sb);
        String sign = MD5(sb.toString()).toUpperCase();
        System.out.println(sign);
        return sign;

    }

    public static String MD5(String data) {
        try {
            java.security.MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
