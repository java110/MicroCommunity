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
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Object convert(Object value, Class target, Object context) {

        return getValue(value, target);
    }

    public static Object getValue(Object value, Class target) {

        if (value == null) {
            return value;
        }
        //1.0 String 转 Date
        if (value instanceof String && target == Date.class) {
            String date = (String) value;
            Date newDate = null;
            if (date.contains(":")) {
                //sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            try {
                newDate = sdf.parse(date);
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

        if (value instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) value;
            return bd.toPlainString();
        }
        if (target == String.class) {
            return String.valueOf(value);
        }

        if (target == int.class || target == Integer.class) {
            return Integer.parseInt(String.valueOf(value));
        }

        if (target == long.class || target == Long.class) {
            return Long.parseLong(String.valueOf(value));
        }

        if (target == double.class || target == Double.class) {
            return Double.parseDouble(String.valueOf(value));
        }

        return value;
    }
}
