package com.java110.common.util;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.CommunityMemberDto;
import junit.framework.TestCase;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
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
}