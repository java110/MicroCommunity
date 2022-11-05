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
    public void shouldAnswerWithTrue()
    {

        double amount = Double.parseDouble("0.6");
        BigDecimal amountBig = new BigDecimal(amount);
        amount = amountBig.subtract(new BigDecimal(Double.parseDouble("0.6"))).doubleValue();
        System.out.println(amount);
    }
}
