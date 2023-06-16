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

     Calendar calendar = Calendar.getInstance();
     calendar.set(Calendar.YEAR,2023);
     calendar.set(Calendar.MONTH,11);
        System.out.printf(DateUtil.getFormatTimeString(calendar.getTime(),DateUtil.DATE_FORMATE_STRING_A));
    }
}
