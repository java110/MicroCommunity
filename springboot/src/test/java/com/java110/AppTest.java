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

       String value = "0.51*R+0.03 * L +0.77";

        value = value.replace("\n", "")
                .replace("\r", "")
                .trim();
        if (value.contains("C")) { //处理小区面积


        } else if (value.contains("F")) { //处理楼栋

        } else if (value.contains("U")) { //处理单元
        } else if (value.contains("RL")) {

        } else if (value.contains("R")) { //处理 房屋面积
            value = value.replace("R", "100");
        } else if (value.contains("X")) {// 处理 房屋系数
            value = value.replace("X", "1");
        }
        if (value.contains("L")) {//处理房屋层数
            System.out.printf("L");
            value = value.replace("L", "1");
        }

        System.out.printf("value="+value);
    }
}
