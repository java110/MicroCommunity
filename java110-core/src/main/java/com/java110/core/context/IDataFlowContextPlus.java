package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.order.Orders;

import java.util.Map;

/**
 * 总数据流封装接口
 */
public interface IDataFlowContextPlus {




    /**
     * 获取dataflowId
     * @return
     */
    public String getDataFlowId();

    /**
     * 获取请求头
     * @return
     */
    public Map<String, String> getReqHeaders();

    /**
     * 请求json报文
     * @return
     */
    public JSONObject getReqJson();


    /**
     * 设置 返回头信息
     * @param resHeaders 返回头信息
     */
    public void setResHeaders(Map<String, String> resHeaders);


    /**
     * 设置返回json报文
     * @param resJson
     */
    public void setResJson(JSONObject resJson);


    /**
     * 查询 返回头信息
     * @return
     */
    public Map<String, String> getResHeaders();

    /**
     * 查询返回JSON报文
     * @return
     */
    public JSONObject getResJson();
    
}
