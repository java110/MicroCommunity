package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.Map;

/**
 * 主键生成
 * Created by wuxw on 2018/6/3.
 */
public class CodeDataFlow extends AbstractDataFlowContext {

    /**
     * 前缀
     */
    private String prefix;

    private String hostName;

    private String port;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPort() {
        return port;
    }



    public void setPort(String port) {
        this.port = port;
    }

    public CodeDataFlow(Date startDate, String code) {
        super(startDate, code);
    }

    @Override
    public CodeDataFlow doBuilder(String reqInfo, Map<String, String> headerAll) throws Exception {
        JSONObject reqInfoObj = JSONObject.parseObject(reqInfo);
        this.setReqJson(reqInfoObj);
        this.setReqData(reqInfo);
        this.setTransactionId(reqInfoObj.getString("transactionId"));
        this.setRequestTime(reqInfoObj.getString("requestTime"));

        if (headerAll != null && !headerAll.isEmpty()){
           this.requestCurrentHeaders.putAll(headerAll);
           this.requestHeaders.putAll(headerAll);
        }

        if(headerAll != null && headerAll.containsKey("hostName")) {
            this.setHostName(headerAll.get("hostName"));
        }
        if(headerAll != null && headerAll.containsKey("port")) {
            this.setPort(headerAll.get("port"));
        }
        this.setPrefix(reqInfoObj.getString("prefix"));
        return this;
    }

    @Override
    public IOrders getOrder() {
        return this;
    }
}
