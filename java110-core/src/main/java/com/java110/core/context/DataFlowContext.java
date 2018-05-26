package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.entity.center.DataFlowLinksCost;
import com.java110.entity.center.DataFlowLog;

import java.util.List;
import java.util.Map;

/**
 * 数据流上下文
 * Created by wuxw on 2018/5/18.
 */
public interface DataFlowContext {

    /**
     * 请求报文
     * @return
     */
    public String getReqData();

    public JSONObject getReqJson();
    /**
     * 返回报文
     * @return
     */
    public JSONObject getResJson();


    /**
     * 添加各个环节的耗时
     * @param dataFlowLinksCost
     */
    public void addLinksCostDates(DataFlowLinksCost dataFlowLinksCost);

    /**
     * 添加日志信息
     * @param dataFlowLog
     */
    public void addLogDatas(DataFlowLog dataFlowLog);

    public List<DataFlowLinksCost> getLinksCostDates();

    public List<Business> getBusinesses();

    public Map<String, String> getHeaders();


    public Orders getOrder();


    /**
     * 获取当前Business
     * @return
     */
    public Business getCurrentBusiness();

    public void setResJson(JSONObject resJson);

    /**
     * 获取参数
     * @return
     */
    public Map<String, Object> getParamOut();

    /**
     * 添加参数
     * @param key
     * @param value
     */
    public void addParamOut(String key,Object value);


    public String getbId();


}
