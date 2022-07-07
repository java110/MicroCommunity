package com.java110.utils.util;

import org.springframework.cglib.core.Converter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName Java110Converter
 * @Description TODO
 * @Author wuxw
 * @Date 2020/1/28 17:16
 * @Version 1.0
 * add by wuxw 2020/1/28
 **/
public class Java110Converter implements Converter {


    @Override
    public Object convert(Object value, Class target, Object context) {

        return getValue(value, target);
    }

    public static Object getValue(Object value, Class target) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (value == null) {
            return value;
        }
        //1.0 String 转 Date
        if (value instanceof String && target == Date.class) {
            String date = (String) value;
            Date newDate = null;
            SimpleDateFormat strdf = null;
            if (date.contains(":")) {
                strdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            } else {
                strdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            try {
                newDate = strdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return newDate;
        }

        //2.0 Date 转 Date
        if (value instanceof Date && target == Date.class) {
            return value;
        }

        // 3.0 Date 转 String
        if (value instanceof Date && target == String.class) {
            Date date = (Date) value;
            String newDate = null;
            try {
                newDate = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return newDate;
        }
        // 3.0 Timestamp 转 String
        if (value instanceof Timestamp && target == String.class) {
            Date date = new Date(((Timestamp) value).getTime());
            String newDate = null;
            try {
                newDate = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return newDate;
        }

        if (value instanceof BigDecimal && target == String.class) {
            BigDecimal bd = (BigDecimal) value;
            return bd.toPlainString();
        }
        if (target == String.class) {
            return String.valueOf(value);
        }

        if (target == int.class || target == Integer.class) {
            if(StringUtil.isNullOrNone(value)){
                return 0;
            }
            return Integer.parseInt(String.valueOf(value));
        }

        if (target == long.class || target == Long.class) {
            if(StringUtil.isNullOrNone(value)){
                return 0;
            }
            return Long.parseLong(String.valueOf(value));
        }

        if (target == double.class || target == Double.class) {
            if(StringUtil.isNullOrNone(value)){
                return 0;
            }
            return Double.parseDouble(String.valueOf(value));
        }

        if (target == String[].class) {
            return String.valueOf(value).split(",");
        }

        //1.0 String 转 Date
        if (value instanceof String && target == boolean.class) {
            String bl = (String) value;
            if ("true".equals(bl)) {
                return true;
            }
            return false;
        }

        return value;
    }
}
