package com.java110.utils;

import com.java110.utils.util.DateUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.math.BigDecimal;
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
        BigDecimal curFeePrice = new BigDecimal(346.815+"");
        curFeePrice = curFeePrice.multiply(new BigDecimal(1));

        curFeePrice = curFeePrice.setScale(2,BigDecimal.ROUND_HALF_UP);

        System.out.printf(curFeePrice.doubleValue()+"");

    }
}
