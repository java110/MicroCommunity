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

        Calendar createTimeCal = Calendar.getInstance();
        for (int i = 0; i< 100;i++) {
            createTimeCal.add(Calendar.SECOND, 1);
            System.out.println(DateUtil.getFormatTimeStringA(createTimeCal.getTime()));
        }
    }
}
