package com.java110.log.type;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by wuxw on 2017/4/22.
 */
public class CommonTest
        extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CommonTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CommonTest.class );
    }


    public void testSubString(){
        String actionTypeCds = "'C1','A1','M1',";
        actionTypeCds = actionTypeCds.endsWith(",")?actionTypeCds.substring(0,actionTypeCds.length()-1):actionTypeCds;

        System.out.println(actionTypeCds);
    }


}
