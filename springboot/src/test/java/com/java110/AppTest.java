package com.java110;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.RoomAttrDto;
import com.java110.dto.community.CommunityDto;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.DateUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
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

     Calendar calendar = Calendar.getInstance();
     calendar.set(Calendar.YEAR,2023);
     calendar.set(Calendar.MONTH,11);
        System.out.printf(DateUtil.getFormatTimeString(calendar.getTime(),DateUtil.DATE_FORMATE_STRING_A));
    }
}
