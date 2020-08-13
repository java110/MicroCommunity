package com.java110.utils.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义 断言
 * Created by wuxw on 2017/4/22.
 */
public class Assert extends org.springframework.util.Assert {


    public static void hasValue(Object value,String msg){
        Assert.notNull(value, msg);
        Assert.hasLength(value.toString(), msg);

    }

    /**
     * 判断 jsonObject 是否为空
     *
     * @param jsonObject
     * @param key
     * @param message
     */
    public static void isNotNull(Map jsonObject, String key, String message) {
        Assert.notEmpty(jsonObject, message);

        if (!jsonObject.containsKey(key)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断 jsonObject 是否为空
     *
     * @param jsonObject
     * @param key
     * @param message
     */
    public static void jsonObjectHaveKey(JSONObject jsonObject, String key, String message) {
        isNotNull(jsonObject, key, message);
    }


    /**
     * 判断 jsonObject 是否为空
     *
     * @param jsonStr
     * @param key
     * @param message
     */
    public static void jsonObjectHaveKey(String jsonStr, String key, String message) {
        Assert.hasLength(jsonStr, "不是有效的json为空," + message);
        if (isJsonObject(jsonStr)) {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            isNotNull(jsonObject, key, message);
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断 jsonObject 是否为空
     *
     * @param info
     * @param key
     * @param message
     */
    public static void hasKey(Map info, String key, String message) {
        isNotNull(info, key, message);
    }

    /**
     * 判断 jsonObject 是否为空
     *
     * @param info
     * @param key
     * @param message
     */
    public static void hasKeyAndValue(Map info, String key, String message) {
        isNotNull(info, key, message);
        hasLength(info.get(key) == null ? "" : info.get(key).toString(), message);
    }

    /**
     * 判断 jsonObject 是否为空
     *
     * @param info
     * @param key
     * @param message
     */
    public static void hasKeyAndValue(Object info, String key, String message) {
        hasKeyAndValue(BeanConvertUtil.beanCovertMap(info), key, message);
    }


    /**
     * 判断json是否为空
     *
     * @param jsonArray
     * @param message
     */
    public static void listIsNull(List jsonArray, String message) {

        if (jsonArray != null && jsonArray.size() > 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断json是否为空
     *
     * @param jsonArray
     * @param message
     */
    public static void listNotNull(List jsonArray, String message) {

        Assert.notNull(jsonArray, message);

        if (jsonArray.size() < 1) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * 数组只有一条数据
     *
     * @param jsonArray
     * @param message
     */
    public static void listOnlyOne(List jsonArray, String message) {

        Assert.notNull(jsonArray, message);

        if (jsonArray.size() != 1) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断list 是否为空
     *
     * @param targetList
     * @param message
     */
    public static void isNotNull(List<?> targetList, String message) {

        Assert.notNull(targetList, message);

        if (targetList.size() < 1) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断是否只有一条记录数据
     *
     * @param targetList
     * @param message
     */
    public static void isOne(List<?> targetList, String message) {
        Assert.notNull(targetList, message);

        if (targetList.size() != 1) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验map 中是否有值
     *
     * @param targetMap
     * @param message
     */
    public static void hasSize(Map<?, ?> targetMap, String message) {
        Assert.isNull(targetMap, message);

        if (targetMap.size() < 1) {
            throw new IllegalArgumentException(message);
        }

    }

    /**
     * 判断 jsonObject 是否为空
     *
     * @param strValue
     * @param message
     */
    public static void isJsonObject(String strValue, String message) {
        if (!isJsonObject(strValue)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 校验是否为JSON
     *
     * @param msg
     * @return
     */
    public static Boolean isJsonObject(String msg) {
        try {
            JSONObject.parseObject(msg);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Boolean isPageJsonObject(String msg) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            if (!jsonObject.containsKey("meta") || !jsonObject.containsKey("param")) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 校验是否为整数
     *
     * @param text
     * @param msg
     */
    public static void isInteger(String text, String msg) {
        if (!StringUtils.isNumeric(text)) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void isDate(String text, String msg) {
        try {
            DateUtil.getDefaultDateFromString(text);
        } catch (Exception e) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void isDate(String text, String format, String msg) {
        try {
            DateUtil.getDateFromString(text, format);
        } catch (Exception e) {
            throw new IllegalArgumentException(msg);
        }
    }


    /**
     * 判断字符串是否是金额
     *
     * @param str 金额字符串
     * @param msg 异常时信息
     */
    public static void isMoney(String str, String msg) {
        Pattern pattern = java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(str);
        if (!match.matches()) {
            throw new IllegalArgumentException(msg);

        }
    }

    /**
     * 检验是否在 infos 中存在 flowComponent 对应组件的key
     *
     * @param infos
     * @param flowComponent
     * @param key
     * @param message
     */
    public static void hasKeyByFlowData(JSONArray infos, String flowComponent, String key, String message) {

        for (int infoIndex = 0; infoIndex < infos.size(); infoIndex++) {
            JSONObject _info = infos.getJSONObject(infoIndex);
            if (_info.containsKey(flowComponent) && _info.getString("flowComponent").equals(flowComponent)) {
                hasKeyAndValue(_info, key, message);
                break;
            }
        }

    }

    @SuppressWarnings("rawtypes")
    public static boolean objIsEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            if (o.toString().trim().equals("")) {
                return true;
            }
            if (o.equals("null") || o.equals("NULL")) {
                return true;
            }
        } else if (o instanceof List) {
            if (((List) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Map) {
            if (((Map) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Set) {
            if (((Set) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Object[]) {
            if (((Object[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof int[]) {
            if (((int[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof long[]) {
            if (((long[]) o).length == 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检验是否在 infos 中存在 flowComponent 对应组件的key
     *
     * @param key
     * @param message
     */
    public static void isEmail(JSONObject info, String key, String message) {
        hasKeyAndValue(info, key, message);
        if (!ValidatorUtil.isEmail(info.getString(key))) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void judgeAttrValue(JSONObject paramObj){
        if (!paramObj.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramObj.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }
        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            if (!"Y".equals(attr.getString("required"))) {
                continue;
            }
            Assert.hasKeyAndValue(attr, "value", attr.getString("specName") + "不能为空");

            //整数
            if ("2002".equals(attr.getString("specValueType"))) {
                Assert.isInteger(attr.getString("value"), attr.getString("specName") + "不是整数");
            }

            //整数
            if ("3003".equals(attr.getString("specValueType"))) {
                Assert.isMoney(attr.getString("value"), attr.getString("specName") + "不是金额类型 如 3.00");
            }

            // 日期4004
            if ("4004".equals(attr.getString("specValueType"))) {
                Assert.isDate(attr.getString("value"), DateUtil.DATE_FORMATE_STRING_B, attr.getString("specName") + "不是日期格式 YYYY-MM-DD");
            }

            // 日期5005
            if ("5005".equals(attr.getString("specValueType"))) {
                Assert.isDate(attr.getString("value"), DateUtil.DATE_FORMATE_STRING_A, attr.getString("specName") + "不是日期格式 YYYY-MM-DD hh:mm:ss");
            }
        }
    }


}
