package com.java110;

import static org.junit.Assert.assertTrue;

import com.java110.utils.util.DateUtil;
import org.junit.Test;

import java.util.Calendar;
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

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        System.out.println(DateUtil.getFormatTimeString(calendar.getTime(),DateUtil.DATE_FORMATE_STRING_B));
        System.out.println(DateUtil.getAddDayStringB(new Date(),1));
    }
}
