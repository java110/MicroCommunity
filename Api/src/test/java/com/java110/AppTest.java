package com.java110;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

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
    public void jsonRemoveTest(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("wuxw","123213");
        jsonObject.put("wangym","344444");

        System.out.printf(jsonObject.toJSONString());

        jsonObject.remove("wuxw");

        System.out.printf(jsonObject.toJSONString());


    }
}
