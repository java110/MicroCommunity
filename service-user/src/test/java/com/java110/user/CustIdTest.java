package com.java110.user;

import com.java110.entity.user.BoCust;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wuxw on 2017/4/11.
 */
public class CustIdTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CustIdTest(String testName )
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
        assertTrue( true );
    }

    public void testCustId(){
        String oldCustId = "-600405001201704120001";

        int targetId = NumberUtils.toInt(oldCustId,0);

        System.out.printf("targetId : " + targetId);

        if(StringUtils.isBlank(oldCustId) || oldCustId.startsWith("-") ){
            System.out.printf(" oldCustId " + oldCustId);
        }


    }
}