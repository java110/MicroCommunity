package com.java110.utils.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuxw on 2017/7/24.
 */
public class DateUtil {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");

    public static final String LAST_TIME = "2038-01-01 00:00:00";

    private static Map<String, SimpleDateFormat> formats = new HashMap();
    public static final String DATE_FORMATE_STRING_DEFAULT = "yyyyMMddHHmmss";
    public static final String DATE_FORMATE_STRING_A = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMATE_STRING_B = "yyyy-MM-dd";
    public static final String DATE_FORMATE_STRING_C = "MM/dd/yyyy HH:mm:ss a";
    public static final String DATE_FORMATE_STRING_D = "yyyy-MM-dd HH:mm:ss a";
    public static final String DATE_FORMATE_STRING_E = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATE_FORMATE_STRING_F = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String DATE_FORMATE_STRING_G = "yyyy-MM-dd'T'HH:mm:ssz";
    public static final String DATE_FORMATE_STRING_H = "yyyyMMdd";
    public static final String DATE_FORMATE_STRING_I = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMATE_STRING_J = "yyyyMMddHHmmss.SSS";
    public static final String DATE_FORMATE_STRING_K = "yyyyMMddHHmmssSSS";
    public static final String DATE_FORMATE_STRING_L = "MMdd";

    static {
        formats.put("yyyyMMddHHmmss", new SimpleDateFormat("yyyyMMddHHmmss"));
        formats.put("yyyy-MM-dd HH:mm:ss", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        formats.put("yyyy-MM-dd", new SimpleDateFormat("yyyy-MM-dd"));
        formats.put("MM/dd/yyyy HH:mm:ss a", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a"));
        formats.put("yyyy-MM-dd HH:mm:ss a", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a"));
        formats.put("yyyy-MM-dd'T'HH:mm:ss'Z'", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        formats.put("yyyy-MM-dd'T'HH:mm:ssZ", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
        formats.put("yyyy-MM-dd'T'HH:mm:ssz", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz"));
        formats.put("yyyyMMdd", new SimpleDateFormat("yyyyMMdd"));
        formats.put("yyyy-MM-dd HH:mm:ss.SSS", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        formats.put("yyyyMMddHHmmss.SSS", new SimpleDateFormat("yyyyMMddHHmmss.SSS"));
        formats.put("yyyyMMddHHmmssSSS", new SimpleDateFormat("yyyyMMddHHmmssSSS"));
        formats.put("MMdd", new SimpleDateFormat("MMdd"));
    }


    /**
     * 返回 yyyyMMddhhmmss 格式的日期串
     *
     * @return
     */
    public static String getyyyyMMddhhmmssDateString() {
        return dateFormat.format(new Date());
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Date getLastDate() throws ParseException {
        return getDateFromString("2037-12-01", DATE_FORMATE_STRING_B);
    }

    /**
     * 转TimeStamp
     *
     * @param date
     * @return
     */
    public static Timestamp getTimestamp(Date date) {
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

    /**
     * 获取未来时间
     *
     * @param second 秒
     * @return
     */
    public static Date getFutureDate(int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static String getFormatTimeString(Date date, String pattern) {
        SimpleDateFormat sDateFormat = getDateFormat(pattern);

        synchronized (sDateFormat) {
            return sDateFormat.format(date);
        }
    }

    public static String getDefaultFormateTimeString(Date date) {
        return getFormatTimeString(date, "yyyyMMddHHmmss");
    }

    public static SimpleDateFormat getDateFormat(String pattern) {
        SimpleDateFormat sDateFormat = (SimpleDateFormat) formats.get(pattern);
        if (sDateFormat == null) {
            sDateFormat = new SimpleDateFormat(pattern);
            formats.put(pattern, sDateFormat);
        }
        return sDateFormat;
    }

    public static Date getDateFromString(String date, String pattern)
            throws ParseException {
        SimpleDateFormat sDateFormat = getDateFormat(pattern);

        synchronized (sDateFormat) {
            return sDateFormat.parse(date);
        }
    }

    public static Date getDefaultDateFromString(String date)
            throws ParseException {
        return getDateFromString(date, "yyyyMMddHHmmss");
    }

    public static String getNowDefault() {
        return getNow("yyyyMMddHHmmss");
    }

    public static String getNow(String pattern) {
        return getFormatTimeString(new Date(), pattern);
    }

    public static String getLastTime() {
        return LAST_TIME;
    }

    public static String getNowII() {
        return getFormatTimeString(new Date(), "yyyyMMdd");
    }

    public static long dateString2Long(String str, String pattern)
            throws ParseException {
        return getDateFromString(str, pattern).getTime();
    }

    /**
     * 校验字符串是否可以格式化为时间
     *
     * @param str
     * @param pattern
     * @return
     */
    public static boolean judgeDate(String str, String pattern) {
        try {
            dateString2Long(str, pattern);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String longToDateStringDefault(long time) {
        return getFormatTimeString(new Date(time), "yyyyMMddHHmmss");
    }

    public static String longToDateString(long time, String pattern) {
        return getFormatTimeString(new Date(time), pattern);
    }

    public static long date2Long(Date date) {
        return date.getTime();
    }

    public static Date longToDate(long time) {
        return new Date(time);
    }

    public static Date getDateFromStringAdaptTwoPattern(String date)
            throws ParseException {
        try {
            return getDateFromString(date, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
        }
        return getDateFromString(date, "yyyy-MM-dd");
    }

    public static String changeNumDateToDate(String numdate, String inFormat, String outFormat)
            throws ParseException {
        Date date = getDateFromString(numdate, inFormat);
        return getFormatTimeString(date, outFormat);
    }

    public static String getNextMonthFistDay(String nowdate, String inFormat, String outFormat)
            throws ParseException {
        Date date = getDateFromString(nowdate, inFormat);

        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.set(2, cl.get(2) + 1);
        cl.set(5, 1);

        date = cl.getTime();
        return getFormatTimeString(date, outFormat);
    }

    public static boolean isLeapYear(int year) {
        if (year % 400 == 0)
            return true;
        if (year % 4 == 0) {
            return (year % 100 != 0);
        }

        return false;
    }

    public static String getLastDay(String nowdate, String inFormat, String outFormat)
            throws ParseException {
        String returndate = "";

        Date date = getDateFromString(nowdate, inFormat);

        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        switch (cl.get(2)) {
            case 0:
                cl.set(5, 31);
                break;
            case 1:
                int year = cl.get(1);

                if (isLeapYear(year))
                    cl.set(5, 29);
                else {
                    cl.set(5, 28);
                }
                break;
            case 2:
                cl.set(5, 31);
                break;
            case 3:
                cl.set(5, 30);
                break;
            case 4:
                cl.set(5, 31);
                break;
            case 5:
                cl.set(5, 30);
                break;
            case 6:
                cl.set(5, 31);
                break;
            case 7:
                cl.set(5, 31);
                break;
            case 8:
                cl.set(5, 30);
                break;
            case 9:
                cl.set(5, 31);
                break;
            case 10:
                cl.set(5, 30);
                break;
            case 11:
                cl.set(5, 31);
        }

        date = cl.getTime();

        returndate = getFormatTimeString(date, outFormat);

        return returndate;
    }

    public static String getMonthLastDay(String fmt) {
        String returndate = "";
        Date date = null;
        Calendar cl = Calendar.getInstance();
        switch (cl.get(2)) {
            case 0:
                cl.set(5, 31);
                break;
            case 1:
                int year = cl.get(1);

                if (isLeapYear(year))
                    cl.set(5, 29);
                else {
                    cl.set(5, 28);
                }
                break;
            case 2:
                cl.set(5, 31);
                break;
            case 3:
                cl.set(5, 30);
                break;
            case 4:
                cl.set(5, 31);
                break;
            case 5:
                cl.set(5, 30);
                break;
            case 6:
                cl.set(5, 31);
                break;
            case 7:
                cl.set(5, 31);
                break;
            case 8:
                cl.set(5, 30);
                break;
            case 9:
                cl.set(5, 31);
                break;
            case 10:
                cl.set(5, 30);
                break;
            case 11:
                cl.set(5, 31);
        }

        date = cl.getTime();

        returndate = getFormatTimeString(date, fmt);

        return returndate;
    }

    public static Date getNextMonthFirstDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    public static Date getFirstDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static String getNextMonthFirstDay(String fmt) {
        String returndate = "";
        Date date = getNextMonthFirstDate();
        returndate = getFormatTimeString(date, fmt);

        return returndate;
    }

    public static boolean compareDate(String fistDate, String secondDate, String format)
            throws ParseException {
        boolean flag = false;

        Date fist = null;
        Date second = null;

        fist = getDateFromString(fistDate, format);

        second = getDateFromString(secondDate, format);
        if (fist.before(second)) {
            flag = true;
        }

        return flag;
    }

    public static boolean isRightDate(String value, String varValue) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(varValue);
            format.setLenient(false);
            format.parse(value);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    /**
     * 在给定的日期加上或减去指定月份后的日期
     *
     * @param sourceDate 原始时间
     * @param month      要调整的月份，向前为负数，向后为正数
     * @return
     */
    public static Date stepMonth(Date sourceDate, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.MONTH, month);

        return c.getTime();
    }

    public static String dateTimeToDate(String dateTime) {
        String dateStr = "";
        try {
            Date date = getDateFromString(dateTime, DATE_FORMATE_STRING_A);
            dateStr = getFormatTimeString(date, DATE_FORMATE_STRING_B);
        } catch (ParseException e) {
            dateStr = dateTime;
        }

        return dateStr;
    }

    public static int getYear() {
        Date date = getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Date date = getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else if (nowTime.compareTo(beginTime) == 0 || nowTime.compareTo(endTime) == 0) {
            return true;
        } else {
            return false;
        }
    }

    //获取两个日期之间的天数
    public static int daysBetween(Date now, Date returnDate) {
        Calendar cNow = Calendar.getInstance();
        Calendar cReturnDate = Calendar.getInstance();
        cNow.setTime(now);
        cReturnDate.setTime(returnDate);
        setTimeToMidnight(cNow);
        setTimeToMidnight(cReturnDate);
        long todayMs = cNow.getTimeInMillis();
        long returnMs = cReturnDate.getTimeInMillis();
        long intervalMs = todayMs - returnMs;
        return millisecondsToDays(intervalMs);
    }

    //获取两个日期之间的毫秒数
    private static void setTimeToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

    //获取两个日期之间的分钟数
    private static int millisecondsToDays(long intervalMs) {
        return (int) (intervalMs / (1000 * 86400));
    }
}
