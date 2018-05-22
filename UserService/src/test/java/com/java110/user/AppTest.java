package com.java110.user;

import com.java110.common.util.WebServiceAxisClient;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
        String url = "http://135.192.70.67:9084/serviceAgent/http/FactorageManager_ForAgent?AppKey=2017082401";
        String function = "queryOrderInfo";

        String xml  = "<QueryOrderInfoRequest>\n" +
                "\t<accNbr>18009706604</accNbr>\n" +
                "\t<accNbrType>1</accNbrType> \n" +
                "\t<areaCode>0971</areaCode>\n" +
                "\t<channelId></channelId>\n" +
                "\t<staffCode></staffCode> \t\n" +
                "</QueryOrderInfoRequest>";

        try {
            Object retObj = WebServiceAxisClient.callWebService(url, function, new Object[]{xml});


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
