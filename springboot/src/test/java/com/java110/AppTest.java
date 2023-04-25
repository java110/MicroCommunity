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

       JSONObject param = new JSONObject();
       param.put("tel","18909715555");
       param.put("passwd","admin");
        try {
            String accessToken = Base64Convert.byteToBase64(param.toJSONString().getBytes("UTF-8"));
            System.out.printf("accessToken="+accessToken);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }
}
