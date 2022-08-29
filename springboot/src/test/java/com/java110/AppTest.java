package com.java110;

import static org.junit.Assert.assertTrue;

import com.java110.utils.util.DateUtil;
import org.junit.Test;

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

        Date startTime = DateUtil.getDateFromStringA("2022-08-29 11:47:20");
        Date endTime = DateUtil.getDateFromStringA("2022-08-29 11:48:21");



       double min = (endTime.getTime() - startTime.getTime()) / (60 * 1000* 1.00);

        System.out.println(new Double(Math.ceil(min)).longValue());
    }
}
