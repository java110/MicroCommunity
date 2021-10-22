package com.java110.utils.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class UrlParamToJsonUtil {

    public static JSONObject getJson(String paramStr) {

        if (StringUtil.isEmpty(paramStr) || paramStr.indexOf("?") < 0) {
            return new JSONObject();
        }
        paramStr = paramStr.substring(paramStr.indexOf("?") + 1);
        String[] params = paramStr.split("&");
        JSONObject obj = new JSONObject();
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
            if (param.length >= 2) {
                String key = param[0];
                String value = param[1];
                for (int j = 2; j < param.length; j++) {
                    value += "=" + param[j];
                }
                try {
                    obj.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
