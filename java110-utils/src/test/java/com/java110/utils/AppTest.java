package com.java110.utils;

import com.java110.utils.util.DateUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Calendar;

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

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        Calendar today = Calendar.getInstance();
        today.setTime(DateUtil.getDateFromStringB("2022-12-26"));
        int week = today.get(Calendar.WEEK_OF_MONTH);
        int curDay = today.get(Calendar.DAY_OF_WEEK);

        //一周第一天是否为星期天
        boolean isFirstSunday = (today.getFirstDayOfWeek() == Calendar.SUNDAY);
        //获取周几
        //若一周第一天为星期天，则-1
        if (isFirstSunday) {
            curDay = curDay - 1;
            if (curDay == 0) {
                curDay = 7;
            }
        }

    }
}
