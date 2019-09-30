package com.java110.utils.util;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.FeeDetailDto;
import com.java110.dto.FeeDto;
import com.java110.vo.api.ApiFeeVo;
import junit.framework.TestCase;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BeanConvertUtilTest extends TestCase {

    public void testCovertBean() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberTypeCd("123");
        communityMemberDto.setStatusCd("1");
        communityMemberDto.setMemberId("123123");

        Map info = new HashMap();

        //Map _info  = BeanConvertUtil.beanCovertMap(communityMemberDto);
        Map _info  = BeanUtils.describe(communityMemberDto);

        System.out.println(JSONObject.toJSONString(_info));
    }

    public void testCoverBeanStringToDate(){
        Map reqJson = new HashMap();
        reqJson.put("startTime","2019-06-02 00:00:00");
        //reqJson.put("endTime","2019-06-03");

        FeeDetailDto feeDetailDto = BeanConvertUtil.covertBean(reqJson, FeeDetailDto.class);

        System.out.printf("feeDetailDto:"+ JSONObject.toJSONString(feeDetailDto));
    }

    public void testCoverBeanDateToString(){
        FeeDto feeDto = new FeeDto();
        feeDto.setStartTime(new Date());
        feeDto.setAmount("1.00");

        ApiFeeVo apiFeeVo = BeanConvertUtil.covertBean(feeDto, ApiFeeVo.class);

        System.out.printf("apiFeeVo:"+ JSONObject.toJSONString(apiFeeVo));

    }
}