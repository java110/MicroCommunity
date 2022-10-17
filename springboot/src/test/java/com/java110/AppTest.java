package com.java110;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.DateUtil;
import org.junit.Test;

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

       List<String> abc = new ArrayList<>();
        abc.add("123123");
        abc.add("123123");
        abc.add("123123");
        abc.add("123123");
        JSONObject data = new JSONObject();
        data.put("pccIds",abc);
        System.out.println(data.toJSONString());
    }
}
