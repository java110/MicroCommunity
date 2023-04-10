package com.java110;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.RoomAttrDto;
import com.java110.dto.community.CommunityDto;
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

        BigDecimal moneyDec = new BigDecimal(1)
                .multiply(new BigDecimal(1))
                .multiply(new BigDecimal(Double.parseDouble("0.01")))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.printf(moneyDec.doubleValue()+"");

    }
}
