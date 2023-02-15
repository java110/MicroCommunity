package com.java110.core.factory;

import com.java110.dto.smsConfig.SmsConfigDto;
import com.java110.utils.cache.MappingCache;
import com.java110.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * @ClassName SendSmsFactory
 * @Description 验证码短信发送接口
 * @Author wuxw
 * @Date 2020/2/10 10:14
 * @Version 1.0
 * add by wuxw 2020/2/10
 **/
public class SendSmsFactory {

    private static final String SMS_DOMAIN = "SMS_DOMAIN";
    private static final String SMS_COMPANY = "SMS_COMPANY";
    public static final String SMS_COMPANY_ALI = "ALI";
    public static final String SMS_COMPANY_TENCENT = "TENCENT";
    public static final String SMS_COMPANY_YIDONG = "YIDONG"; // 移动 短信
    public static final String VALIDATE_CODE = "_validateTel";

    /**
     * 短信开关
     */
    public static final String SMS_SEND_SWITCH = "SMS_SEND_SWITCH";

    public static void sendSms(String tel, String code) {

        String smsCompany = MappingCache.getValue(SMS_DOMAIN, SMS_COMPANY);

        if (!StringUtils.isEmpty(smsCompany) && SMS_COMPANY_ALI.equals(smsCompany.trim())) {
            AliSendMessageFactory.sendMessage(tel, code);
        }else if(SMS_COMPANY_YIDONG.equals(smsCompany.trim())){
            YidongSendMessageFactory.sendMessage(tel,code);
        } else {
            TencentSendMessageFactory.sendMessage(tel, code);
        }
    }

    public static ResultVo sendOweFeeSms(String tel, SmsConfigDto smsConfigDto, Object param){

        ResultVo resultVo = null;
        if (SMS_COMPANY_ALI.equals(smsConfigDto.getSmsType().trim())) {
            resultVo = AliSendMessageFactory.sendOweFeeSms(tel, param,smsConfigDto);
        } else if(SMS_COMPANY_YIDONG.equals(smsConfigDto.getSmsType().trim())){
            resultVo = YidongSendMessageFactory.sendOweFeeSms(tel, param,smsConfigDto);
        } else {
            resultVo = TencentSendMessageFactory.sendOweFeeSms(tel, param,smsConfigDto);
        }

        return resultVo;
    }



    /**
     * 生成验证码
     *
     * @param limit 位数
     * @return
     */
    public static String generateMessageCode(int limit) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < limit; i++) {
            result += (random.nextInt(9) + 1);
        }
        return result;
    }
}
