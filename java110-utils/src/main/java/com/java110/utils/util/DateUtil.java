package com.java110.utils.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public static final String DATE_FORMATE_STRING_M = "yyyyMM";
    public static final String DATE_FORMATE_STRING_N = "HHmmss";
    public static final String DATE_FORMATE_STRING_O = "yyyyMMddHHmm";
    public static final String DATE_FORMATE_STRING_Q = "yyyy-MM";



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
        formats.put("yyyyMM", new SimpleDateFormat("yyyyMM"));
        formats.put("HHmmss", new SimpleDateFormat("HHmmss"));
        formats.put("yyyyMMddHHmm", new SimpleDateFormat("yyyyMMddHHmm"));
        formats.put("yyyy-MM", new SimpleDateFormat("yyyy-MM"));

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

    public static String getFormatTimeStringA(Date date) {
        SimpleDateFormat sDateFormat = getDateFormat(DateUtil.DATE_FORMATE_STRING_A);

        synchronized (sDateFormat) {
            return sDateFormat.format(date);
        }
    }

    public static String getFormatTimeStringB(Date date) {
        SimpleDateFormat sDateFormat = getDateFormat(DateUtil.DATE_FORMATE_STRING_B);

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

    public static Date getDateFromStringB(String date) {
        SimpleDateFormat sDateFormat = getDateFormat(DateUtil.DATE_FORMATE_STRING_B);
        try {
            synchronized (sDateFormat) {
                return sDateFormat.parse(date);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date getDateFromStringA(String date) {
        SimpleDateFormat sDateFormat = getDateFormat(DateUtil.DATE_FORMATE_STRING_A);
        try {
            synchronized (sDateFormat) {
                return sDateFormat.parse(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
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
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    public static Date getFirstDate() {
        Calendar curDateCal = Calendar.getInstance();
        curDateCal.set(Calendar.DAY_OF_MONTH, 1);
        curDateCal.set(Calendar.HOUR_OF_DAY, 0);
        curDateCal.set(Calendar.MINUTE, 0);
        curDateCal.set(Calendar.SECOND, 0);
        Date curDate = curDateCal.getTime();
        return curDate;
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

    public static void main(String[] args) throws ParseException {

//        SimpleDateFormat sf = new SimpleDateFormat(DateUtil.DATE_FORMATE_STRING_A);
//        Calendar c = Calendar.getInstance();
//        c.setTime(DateUtil.getDateFromString("2021-12-03",DateUtil.DATE_FORMATE_STRING_A));
//        c.add(Calendar.DAY_OF_MONTH, 125);
//        System.out.println("增加一天后日期:"+sf.format(c.getTime()));
        System.out.println("2021-12-07".compareTo(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B)));
    }

    public static String getAddDayString(Date date, String pattern, int days) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return sf.format(c.getTime());
    }

    public static String getAddDayStringB(Date date, int days) {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMATE_STRING_B);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return sf.format(c.getTime());
    }

    public static String getAddDayStringA(Date date, int days) {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMATE_STRING_A);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return sf.format(c.getTime());
    }


    public static String getAddHoursStringA(Date date, int hours) {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMATE_STRING_A);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hours);
        return sf.format(c.getTime());
    }


    public static String getAddMonthStringA(Date date, int month) {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMATE_STRING_A);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);
        return sf.format(c.getTime());
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

    /**
     * 在给定的日期加上或减去指定天数后的日期
     *
     * @param sourceDate 原始时间
     * @param day        要调整的月份，向前为负数，向后为正数
     * @return
     */
    public static Date stepDay(Date sourceDate, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.DATE, day);
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

    /**
     * 　　 *字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) {
        long between_days = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static double dayCompare(Date fromDate, Date toDate) {
        double resMonth = 0.0;
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);
        //比较月份差 可能有整数 也会负数
        int result = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        //比较年差
        int month = (to.get(Calendar.YEAR) - from.get(Calendar.YEAR)) * 12;

        //真实 相差月份
        result = result + month;

        //开始时间  2021-06-01  2021-08-05   result = 2    2021-08-01
        Calendar newFrom = Calendar.getInstance();
        newFrom.setTime(fromDate);
        newFrom.add(Calendar.MONTH, result);
        //如果加月份后 大于了当前时间 默认加 月份 -1 情况 12-19  21-01-10
        //这个是神的逻辑一定好好理解
        if (newFrom.getTime().getTime() > toDate.getTime()) {
            newFrom.setTime(fromDate);
            result = result - 1;
            newFrom.add(Calendar.MONTH, result);
        }

        // t1 2021-08-01   t2 2021-08-05
        long t1 = newFrom.getTime().getTime();
        long t2 = to.getTime().getTime();
        //相差毫秒
        double days = (t2 - t1) * 1.00 / (24 * 60 * 60 * 1000);
        BigDecimal tmpDays = new BigDecimal(days); //相差天数
        BigDecimal monthDay = null;
        Calendar newFromMaxDay = Calendar.getInstance();
        newFromMaxDay.set(newFrom.get(Calendar.YEAR), newFrom.get(Calendar.MONTH), 1, 0, 0, 0);
        newFromMaxDay.add(Calendar.MONTH, 1); //下个月1号
        //在当前月中 这块有问题
        if (toDate.getTime() < newFromMaxDay.getTime().getTime()) {
            monthDay = new BigDecimal(newFrom.getActualMaximum(Calendar.DAY_OF_MONTH));
            return tmpDays.divide(monthDay, 4, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(result)).doubleValue();
        }
        // 上月天数
        days = (newFromMaxDay.getTimeInMillis() - t1) * 1.00 / (24 * 60 * 60 * 1000);
        tmpDays = new BigDecimal(days);
        monthDay = new BigDecimal(newFrom.getActualMaximum(Calendar.DAY_OF_MONTH));
        BigDecimal preRresMonth = tmpDays.divide(monthDay, 4, BigDecimal.ROUND_HALF_UP);

        //下月天数
        days = (t2 - newFromMaxDay.getTimeInMillis()) * 1.00 / (24 * 60 * 60 * 1000);
        tmpDays = new BigDecimal(days);
        monthDay = new BigDecimal(newFromMaxDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        resMonth = tmpDays.divide(monthDay, 4, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(result)).add(preRresMonth).doubleValue();
        return resMonth;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysUp(Date date1, Date date2) {
        double days = ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24 * 1.00));
        return new Double(Math.ceil(days)).intValue();
    }

    /**
     * 获取两个日期之间的所有月份 (年月)
     *
     * @param startTime
     * @param endTime
     * @return：list
     */
    public static List<String> getMonthBetweenDate(Date startTime, Date endTime) {
        return getMonthBetweenDate(DateUtil.getFormatTimeStringA(startTime), DateUtil.getFormatTimeStringA(endTime));
    }

    /**
     * 获取两个日期之间的所有月份 (年月)
     *
     * @param startTime
     * @param endTime
     * @return：list
     */
    public static List<String> getMonthBetweenDate(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE_STRING_Q);
        // 声明保存日期集合
        List<String> list = new ArrayList<>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {

                // 把日期添加到集合
                list.add(sdf.format(startDate));

                // 设置日期
                calendar.setTime(startDate);

                //把月数增加 1
                calendar.add(Calendar.MONTH, 1);

                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 除去 小时 分 秒
     * @param time
     * @return
     */
    public static Date timeToDate(Date time){
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(time);
        setTimeToMidnight(calendar);
        return calendar.getTime();
    }

}
