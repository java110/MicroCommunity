package com.java110;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
        JSONObject jsonObject = JSONObject.parseObject("{\n" +
                "\t\"name\": \"吴学文\",\n" +
                "\t\"age\": \"29\",\n" +
                "\t\"sex\": 1,\n" +
                "\t\"typeCd\": \"10004\",\n" +
                "\t\"idCard\": \"632126199109162011\",\n" +
                "\t\"startTime\": \"2020-01-02 20:40:10\",\n" +
                "\t\"endTime\": \"2020-02-02 20:39:58\",\n" +
                "\t\"tel\": \"18999999999\",\n" +
                "\t\"photos\": [\"data:image/jpeg;base64,/\"]\n" +
                "\t \"msgCode\": \"1234567\",\n" +
                "\t\"communityId\": \"7020181217000001\",\n" +
                "\t\"machineIds\": [\"892019121802160005\", \"892019112915930070\"]\n" +
                "}");


        JSONArray machineIds = jsonObject.getJSONArray("machineIds");

        jsonObject.remove("wuxw");

        System.out.printf(jsonObject.toJSONString());


    }
}
