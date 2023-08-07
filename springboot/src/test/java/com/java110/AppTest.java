package com.java110;

import static org.junit.Assert.assertTrue;

import com.java110.utils.util.DateUtil;
import org.junit.Test;

import java.util.Calendar;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void should()
    {

        String body = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ns2:initTransResponse xmlns:ns2=\"http://ws.conflux.sunshine.com\"><String>&lt;result&gt;&lt;msg&gt;成功&lt;/msg&gt;&lt;code&gt;1&lt;/code&gt;&lt;token&gt;44190044897985894840&lt;/token&gt;&lt;/result&gt;</String></ns2:initTransResponse></soap:Body></soap:Envelope>";

       String token = body.substring(body.indexOf("&lt;token&gt;")+13,body.indexOf("&lt;/token&gt;"));
        System.out.println(token);
    }
}
