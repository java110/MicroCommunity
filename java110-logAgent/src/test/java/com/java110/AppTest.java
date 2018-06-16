package com.java110;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.core.context.DataFlow;
import com.java110.core.context.TransactionLog;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testDataFlow(){
        /*TransactionLog transactionLog = new DataFlow(new Date(),"0000");
        transactionLog.reBuilder("234","234324","F",0);

        System.out.println(transactionLog.toString());*/

        JSONObject order = JSONObject.parseObject("{\"orders\":{\"response1\":{\"code\":\"1999\"}}}");
        System.out.println(JSONPath.eval(order,"$.orders.response.code"));
    }
}
