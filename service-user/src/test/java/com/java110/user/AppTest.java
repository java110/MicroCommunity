package com.java110.user;

import com.java110.doc.annotation.Java110HeaderDoc;
import com.java110.doc.annotation.Java110ParamsDoc;
import com.java110.user.cmd.login.PcUserLoginCmd;
import com.java110.utils.util.DateUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }


    public void testSplit(){
        Class clazz = PcUserLoginCmd.class;


        Java110ParamsDoc java110ParamsDoc = AnnotationUtils.findAnnotation(clazz,Java110ParamsDoc.class);

        Java110HeaderDoc[] java110HeaderDocs = java110ParamsDoc.headers();

        System.out.println(123);

    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        Date startTime = DateUtil.getDateFromStringA("2023-12-02 22:00:00");
        Date endTime = DateUtil.getDateFromStringB("2024-03-01");
        double month = dayCompare(startTime,endTime);
        System.out.println(month * 129.13 * 2.4);
    }

    public static double dayCompare(Date fromDate, Date toDate) {


        //todo 需要计算三端时间 相加即可
        Date fromDateFirstDate = fromDate; // 第一个1日

        Date toDateFirstDate = toDate; // 最后一个1日

        boolean firstDay = true;

        //todo 1.0 计算 fromDateFirstDate
        Calendar fromDateCal = Calendar.getInstance();
        fromDateCal.setTime(fromDate);
        fromDateCal.set(Calendar.DAY_OF_MONTH, 1);
        fromDateCal.set(Calendar.HOUR_OF_DAY,0);
        fromDateCal.set(Calendar.MINUTE,0);
        if (fromDate.getTime() > fromDateCal.getTime().getTime()) {
            fromDateCal.add(Calendar.MONTH, 1);
            firstDay = false;
            fromDateFirstDate = fromDateCal.getTime();
        }

        //todo 2.0 计算 toDateFirstDate
        Calendar toDateCal = Calendar.getInstance();
        toDateCal.setTime(toDate);
        toDateCal.set(Calendar.DAY_OF_MONTH, 1);
        toDateCal.set(Calendar.HOUR_OF_DAY,0);
        toDateCal.set(Calendar.MINUTE,0);

        if (toDate.getTime() > toDateCal.getTime().getTime()) {
            toDateFirstDate = toDateCal.getTime();
        }

        // todo 3.0 计算整数月  fromDateFirstDate --->  toDateFirstDate
        Calendar from = Calendar.getInstance();
        from.setTime(fromDateFirstDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDateFirstDate);
        //比较月份差 可能有整数 也会负数
        int result = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        //比较年差
        int month = (to.get(Calendar.YEAR) - from.get(Calendar.YEAR)) * 12;
        //真实 相差月份
        result = result + month;

        //todo 3.1  如果 fromDate 和toDate 是同一天 则直接返回整月，不再计算 4.0 和5.0
        if (DateUtil.sameMonthDay(fromDate, toDate)) {
            return firstDay ? result : result + 1;
        }

        // todo 4.0 计算 fromDate ---> fromDateFirstDate 的月份
        double days = (fromDateFirstDate.getTime() - fromDate.getTime()) * 1.00 / (24 * 60 * 60 * 1000);
        BigDecimal tmpDays = new BigDecimal(days); //相差天数
        BigDecimal monthDay = new BigDecimal(DateUtil.getMonthDay(fromDate));
        BigDecimal resMonth = tmpDays.divide(monthDay, 4, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(result));

        // todo 5.0 计算  toDateFirstDate ----> toDate 月份
        days = (toDate.getTime() - toDateFirstDate.getTime()) * 1.00 / (24 * 60 * 60 * 1000);
        tmpDays = new BigDecimal(days); //相差天数
        monthDay = new BigDecimal(DateUtil.getMonthDay(toDate));
        resMonth = tmpDays.divide(monthDay, 4, BigDecimal.ROUND_HALF_UP).add(resMonth);

        return resMonth.doubleValue();

    }
    /**
     * 计算 两个时间点月份
     *
     * @param fromDate 开始时间
     * @param toDate   结束时间
     * @return
     */
    public double dayCompare1(Date fromDate, Date toDate) {
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
}
