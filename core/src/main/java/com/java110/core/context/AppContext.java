package com.java110.core.context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用上下文对象
 * Created by wuxw on 2017/4/22.
 */
public class AppContext {


    /**
     * olId 主键前缀
     */
    public final static String PREFIX_OLID = "OLID_";

    /**
     * custId 主键前缀
     */
    public final static String PREFIX_CUSTID = "CUSTID_";

    /**
     * 订单动作
     */
    private String bo_action_type;


    /**
     * 为了满足 一个单子上有 多个 olId,custId 不同的处理，
     * key 为 olId,custId 默认 前缀加 请求时的 值 如 OLID_-1
     * value 通过生单后生成的 值 如 12345678
     *
     * 后期取值时通过订单原始请求报文中 olId,custId 对应的值 从map 中获取
     */
    private Map<String,String> keyIdMap = null;

    /**
     * 返回报文
     */
    private JSONObject rspJson;


    private Object reqObj;




    /**
     * 存放订单data节点
     */
    private  Map<String,JSONArray> datas = null;


    public static AppContext newInstance(){
        AppContext context = new AppContext();
        context.keyIdMap = new HashMap<String,String>();
        context.datas = new HashMap<String, JSONArray>();
        return context;
    }


    /**
     * 添加 数据
     * @param data
     */
    public void addData(String actionTypeCd , JSONArray data){
        synchronized (datas){
            datas.put(actionTypeCd,data);
        }
    }

    public void coverData(Map<String,JSONArray> datas){
        this.datas = datas;
    }

    /**
     * 获取 所有数据
     * @return
     */
    public Map<String,JSONArray> getAllDatas(){
        return datas;
    }

    /**
     * 根据 动作类型获取 数据
     * @param actionTypeCd
     * @return
     */
    public JSONArray getDatasByActionTypeCd(String actionTypeCd){
        return datas.get(actionTypeCd);
    }

    /**
     *
     * 根据 原始的olId的值获取新生成的值
     * @param originalKeyIdValue 原始的olId 值 如请求订单中的olId 为 -1 这里就写-1
     * @param defalutValue 如果获取不到对应的值则返回默认值
     * @return
     */
    public String getKeyId(String originalKeyIdValue,String defalutValue){
        if(keyIdMap.containsKey(originalKeyIdValue)){
            return keyIdMap.get(originalKeyIdValue);
        }

        return defalutValue;
    }

    /**
     * 设置新生成的主键值
     * @param prefix
     * @param originalKeyIdValue
     * @param newValue
     */
    public void setKeyId(String prefix,String originalKeyIdValue,String newValue){
        keyIdMap.put(prefix+originalKeyIdValue,newValue);
    }


    public JSONObject getRspJson() {
        return rspJson;
    }

    public void setRspJson(JSONObject rspJson) {
        this.rspJson = rspJson;
    }

    public Object getReqObj() {
        return reqObj;
    }

    public void setReqObj(Object reqObj) {
        this.reqObj = reqObj;
    }


    public String getBo_action_type() {
        return bo_action_type;
    }

    public void setBo_action_type(String bo_action_type) {
        this.bo_action_type = bo_action_type;
    }
}
