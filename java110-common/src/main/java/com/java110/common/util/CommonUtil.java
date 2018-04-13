package com.java110.common.util;

import com.java110.common.log.LoggerEngine;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * 公用工具类
 * Created by wuxw on 2017/3/10.
 */
public class CommonUtil extends LoggerEngine {


    /**
     * 将 30*1000 转为 30000
     * 不能出现小数点等
     * @param val
     * @return
     */
    public static int multiplicativeStringToInteger(String val){
        try {
            if (StringUtils.isEmpty(val)) {
                return 0;
            }
            if (val.contains("*")) {
                String[] vals = val.split("\\*");
                int value = 1;
                for(int vIndex = 0 ; vIndex < vals.length;vIndex++){
                    if(!NumberUtils.isNumber(vals[vIndex])){
                        throw new ClassCastException("配置的数据有问题，必须配置为30*1000格式");
                    }
                    value *= Integer.parseInt(vals[vIndex]);
                }
                return value;
            }
            if(NumberUtils.isNumber(val)){
                return Integer.parseInt(val);
            }
        }catch (Exception e){
            logger.error("---------------[CommonUtil.multiplicativeStringToInteger]----------------类型转换失败",e);
            return 0;
        }
        return 0;
    }

    /**
     * 生成六位验证码
     * @return
     */
    public static String generateVerificationCode(){
        Random random = new Random();

        String result="";
        for(int i=0;i<6;i++){
            result+=random.nextInt(10);
        }

        return result;
    }
}
