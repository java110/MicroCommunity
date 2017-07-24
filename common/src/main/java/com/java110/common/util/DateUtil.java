package com.java110.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wuxw on 2017/7/24.
 */
public class DateUtil {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");


    /**
     * 返回 yyyyMMddhhmmss 格式的日期串
     * @return
     */
    public static String getyyyyMMddhhmmssDateString(){
        return dateFormat.format(new Date());
    }
}
