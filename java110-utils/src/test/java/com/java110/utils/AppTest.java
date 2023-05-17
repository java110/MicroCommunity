package com.java110.utils;

import com.java110.utils.util.DateUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Calendar;
import java.util.List;

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
        String startTime = "2023-03-02 04:00:00";
        String endTime = "2024-01-01 00:00:00";


        List<String> months = DateUtil.getMonthBetweenDate(startTime,endTime);

        for(String month : months) {
            System.out.println(month);
        }


    }
}
