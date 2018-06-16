package com.java110;

import static org.junit.Assert.assertTrue;

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
        TransactionLog transactionLog = new DataFlow(new Date(),"0000");
        transactionLog.reBuilder("234","234324","F",0);

        System.out.println(transactionLog.toString());
    }
}
