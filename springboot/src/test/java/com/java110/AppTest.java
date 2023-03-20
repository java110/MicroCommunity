package com.java110;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.DateUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        double usedHours = Math.ceil((DateUtil.getCurrentDate().getTime() - DateUtil.getDateFromStringA("2023-03-13 23:32:18").getTime()) / (60 * 60 * 1000.00));
        System.out.printf(usedHours+"");
    }
}
