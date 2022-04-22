package com.java110.code;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.java110.utils.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Set;

/**
 * @ClassName translate
 * @Description TODO
 * @Author wuxw
 * @Date 2022/4/21 10:37
 * @Version 1.0
 * add by wuxw 2022/4/21
 **/
public class translate {

    public static void main(String[] args) throws Exception {
        String enJsonStr = getJsonStr("C:\\Users\\Administrator\\Documents\\project\\hc\\MicroCommunity\\java110-generator\\src\\main\\java\\com\\java110\\code\\enJson.json");
        String cnJsonStr = getJsonStr("C:\\Users\\Administrator\\Documents\\project\\hc\\MicroCommunity\\java110-generator\\src\\main\\java\\com\\java110\\code\\cnJson.json");


        JSONObject enJson = JSONObject.parseObject(enJsonStr, Feature.OrderedField);
        JSONObject cnJson = JSONObject.parseObject(cnJsonStr, Feature.OrderedField);

        for (String key : cnJson.keySet()) {
            Object keyValue = cnJson.get(key);
            if (keyValue instanceof JSONObject) {
                JSONObject keyObj = cnJson.getJSONObject(key);
                int keyIndex = 0;
                for(String subKeyObj :keyObj.keySet()){
                   String value = getObjValue(enJson,key,keyIndex);
                   keyObj.put(subKeyObj,value);
                    keyIndex++;
                }
            }
        }

        System.out.println(cnJson.toJSONString());
    }

    public static String getObjValue(JSONObject enJson, String objKey, int keyIndex) {
        JSONObject jsonObject = null;
        if(StringUtil.isEmpty(objKey)){
            jsonObject = enJson;
        }else{
            jsonObject = enJson.getJSONObject(objKey);
        }
        if(jsonObject == null){
            return "";
        }
        int index = 0;
        for (String key : jsonObject.keySet()) {
            if (index == keyIndex) {
                return jsonObject.getString(key);
            }
            index++;
        }

        return "";

    }

    public static String getJsonStr(String jsonStr) throws Exception {
        File file = new File(jsonStr);
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        String context = "";
        while ((str = in.readLine()) != null) {
            context += (str + "\n");
            //doDealHtmlNode(str,fileName);
        }
        return context;
    }


}
