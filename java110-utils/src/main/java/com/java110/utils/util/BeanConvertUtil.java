package com.java110.utils.util;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName BeanConvertUtil
 * @Description bean 转化工具类
 * @Author wuxw
 * @Date 2019/4/24 12:53
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public final class BeanConvertUtil {

    private BeanConvertUtil() {
    }

    static {
        ConvertUtils.register(new Converter() { //注册一个日期转换器

            public Object convert(Class type, Object value) {
                Date date1 = null;
                if (value instanceof String) {
                    String date = (String) value;
                    SimpleDateFormat sdf = null;
                    if (date.contains(":")) {
                        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    } else {
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    try {
                        date1 = sdf.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return date1;
                }
                return value;
            }
        }, Date.class);


        ConvertUtils.register(new Java110StringConvert(), String.class);
    }


    /**
     * 对象A转为对象B
     * 这个也支持map转bean
     *
     * @param orgBean 原始对象
     * @param dstBean 目标对象类
     * @param <T1>    原始对象
     * @param <T2>    目标对象
     * @return 目标对象
     */
    public static <T1, T2> T2 covertBean(T1 orgBean, T2 dstBean) {

        try {
            //BeanUtils.copyProperties(dstBean, orgBean);
            if (orgBean instanceof Map) {
                BeanMap beanMap = BeanMap.create(dstBean);
                //beanMap.putAll((Map)orgBean);
                objectFieldsPutMap(dstBean, beanMap, (Map) orgBean);
                return dstBean;
            }
            final BeanCopier beanCopier = BeanCopier.create(orgBean.getClass(), dstBean.getClass(), true);
            beanCopier.copy(orgBean, dstBean, new Java110Converter());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("bean转换bean失败", e);
        }
        return dstBean;
    }

    private static void objectFieldsPutMap(Object dstBean, BeanMap beanMap, Map orgMap) {
        //Field[] fields = dstBean.getClass().getDeclaredFields();
        Field[] fields = FieldUtils.getAllFields(dstBean.getClass());
        for (Field field : fields) {
            if (!orgMap.containsKey(field.getName())) {
                continue;
            }
            Class dstClass = field.getType();
            //System.out.println("字段类型" + dstClass);

            Object value = orgMap.get(field.getName());
            //String 转date
            Object tmpValue = Java110Converter.getValue(value, dstClass);
            beanMap.put(field.getName(), tmpValue);
        }
    }

    /**
     * 对象A转为对象B (类)
     * 这个也支持map转bean
     *
     * @param orgBean 原始对象
     * @param t       目标对象类
     * @param <T1>    原始对象
     * @param <T2>    目标对象
     * @return 目标对象
     */
    public static <T1, T2> T2 covertBean(T1 orgBean, Class<T2> t) {

        T2 returnModel = null;
        try {
            returnModel = t.newInstance();
            covertBean(orgBean, returnModel);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("bean转换bean失败", e);
        }
        return returnModel;
    }


    /**
     * 对象A集合转为对象B集合
     *
     * @param orgBeans 原始对象列表
     * @param t        目标对象类
     * @param <T1>     原始对象
     * @param <T2>     目标对象
     * @return 目标对象
     */
    public static <T1, T2> List<T2> covertBeanList(List<T1> orgBeans, Class<T2> t) {
        List<T2> newBeanList = new ArrayList<T2>();
        for (T1 orgbean : orgBeans) {
            T2 newBean = covertBean(orgbean, t);
            newBeanList.add(newBean);
        }
        return newBeanList;
    }

    /**
     * bean转换为map对象
     *
     * @param orgBean 原始bean
     * @return map对象
     */
    public static Map beanCovertMap(Object orgBean) {

        Map<String, Object> map = new HashMap();
        try {
            BeanMap beanMap = BeanMap.create(orgBean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        } catch (Exception e) {
            throw new RuntimeException("bean转换Map失败", e);
        }
        return map;
    }


    /**
     * bean集合转换为map对象集合
     *
     * @param orgBeans 原始bean 列表
     * @return map对象 列表
     */
    public static List<Map<String, Object>> beanCovertMapList(List<Object> orgBeans) {
        List<Map<String, Object>> newMaps = new ArrayList<Map<String, Object>>();
        Map<String, Object> newMap = null;
        for (Object orgbean : orgBeans) {
            newMap = beanCovertMap(orgbean);
            newMaps.add(newMap);
        }
        return newMaps;
    }
}
