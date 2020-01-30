package com.java110.utils.util;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BeanConvertUtilTest extends TestCase {
    /**
     * bean 转为bean
     *
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public void testCovertBean() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        PersonDto personDto = new PersonDto();
        personDto.setId(1);
        personDto.setName("wuxw");
        personDto.setCreateTime(new Date());
        personDto.setPage("1");
        personDto.setAge(1);
        PersonVo personVo = new PersonVo();
        personVo = BeanConvertUtil.covertBean(personDto, personVo);

        System.out.println("dto 转 vo" + JSONObject.toJSONString(personVo));
    }

    /**
     * bean 转为bean
     *
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public void testCovertBeanAgent() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        PersonVo personVo = new PersonVo();
        personVo.setId("2");
        personVo.setName("wuxw");
        personVo.setCreateTime("2020-01-28 12:12:12");
        PersonDto personDto = new PersonDto();
        personDto = BeanConvertUtil.covertBean(personVo, personDto);

        System.out.println("dto 转 vo" + JSONObject.toJSONString(personDto));
    }

    /**
     * bean 转为bean
     *
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public void testBeanCovertMap() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        PersonDto personVo = new PersonDto();
        personVo.setName("wuxw");
        personVo.setCreateTime(new Date());

        Map map = BeanConvertUtil.beanCovertMap(personVo);

        System.out.println("bean 转 map" + JSONObject.toJSONString(map));
    }

    /**
     * bean 转为bean
     *
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public void testMapCovertBean() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Map info = new HashMap();
        info.put("name", "wuxw");
        info.put("createTime", new Date());
        info.put("page",1);
        PersonDto personDto = null;
         personDto = BeanConvertUtil.covertBean(info,PersonDto.class);

        System.out.println("map 转 bean" + JSONObject.toJSONString(personDto));
    }
}