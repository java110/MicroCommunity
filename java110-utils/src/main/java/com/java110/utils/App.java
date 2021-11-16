package com.java110.utils;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.DateUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        Calendar curDateCal = Calendar.getInstance();
        curDateCal.set(Calendar.DAY_OF_MONTH, 1);
        curDateCal.set(Calendar.HOUR_OF_DAY, 0);
        curDateCal.set(Calendar.MINUTE, 0);
        curDateCal.set(Calendar.SECOND, 0);
        Date curDate = curDateCal.getTime();
        System.out.println(DateUtil.getFormatTimeString(curDate,DateUtil.DATE_FORMATE_STRING_A));
    }
}
