package com.java110.utils.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;

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

    public static String jsonToUrlParam(JSONObject json)  {
        StringBuilder params = new StringBuilder();
        json.keySet().forEach(key -> {
            String value = json.getString(key);
            try {
                String encodedKey = URLEncoder.encode(key, "UTF-8");
                String encodedValue = URLEncoder.encode(value, "UTF-8");
                params.append(encodedKey).append("=").append(encodedValue).append("&");
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        // 删除最后一个'&'
        if (params.length() > 0) {
            params.deleteCharAt(params.length() - 1);
        }
        return params.toString();
    }
}
