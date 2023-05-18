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
        Date startTime = DateUtil.getDateFromStringB("2022-06-30");
        Date endTime = DateUtil.getDateFromStringB("2024-01-01");
        double month = dayCompare(startTime,endTime);
        System.out.println(month);
    }

    /**
     * 计算 两个时间点月份
     *
     * @param fromDate 开始时间
     * @param toDate   结束时间
     * @return
     */
    public double dayCompare(Date fromDate, Date toDate) {
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
