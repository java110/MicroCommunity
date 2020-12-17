package com.java110.utils.util;

import com.java110.utils.log.LoggerEngine;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 公用工具类
 * Created by wuxw on 2017/3/10.
 */
public class CommonUtil extends LoggerEngine {


    /**
     * 将 30*1000 转为 30000
     * 不能出现小数点等
     *
     * @param val
     * @return
     */
    public static int multiplicativeStringToInteger(String val) {
        try {
            if (StringUtils.isEmpty(val)) {
                return 0;
            }
            if (val.contains("*")) {
                String[] vals = val.split("\\*");
                int value = 1;
                for (int vIndex = 0; vIndex < vals.length; vIndex++) {
                    if (!NumberUtils.isNumber(vals[vIndex])) {
                        throw new ClassCastException("配置的数据有问题，必须配置为30*1000格式");
                    }
                    value *= Integer.parseInt(vals[vIndex]);
                }
                return value;
            }
            if (NumberUtils.isNumber(val)) {
                return Integer.parseInt(val);
            }
        } catch (Exception e) {
            logger.error("---------------[CommonUtil.multiplicativeStringToInteger]----------------类型转换失败", e);
            return 0;
        }
        return 0;
    }

    /**
     * 生成六位验证码
     *
     * @return
     */
    public static String generateVerificationCode() {
        Random random = new Random();

        String result = "";
        for (int i = 0; i < 6; i++) {
            result += (random.nextInt(9) + 1);;
        }

        return result;
    }

    // 手机号码前三后四脱敏
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    //身份证前三后四脱敏
    public static String idEncrypt(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }


    //效验
    public static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 根据身份证号获取年龄
     * @param certId
     * @return
     */
    public static String getAgeByCertId(String certId) {
        String birthday = "";
        if (certId.length() == 18) {
            birthday = certId.substring(6, 10) + "/"
                    + certId.substring(10, 12) + "/"
                    + certId.substring(12, 14);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date();
        Date birth = new Date();
        try {
            birth = sdf.parse(birthday);
        } catch (ParseException e) {
        }
        long intervalMilli = now.getTime() - birth.getTime();
        int age = (int) (intervalMilli/(24 * 60 * 60 * 1000))/365;
        System.out.println(age);
        return age +"";
    }
}
