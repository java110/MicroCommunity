package com.java110.entity.rule;

/**
 * 头部信息封装
 * "RuleType":"RULE0001", -- 用来区分走那个规则组
 "ServiceCode": "SVC0001", -- 用来判断走那些规则
 "TransactionID": "1001000101201603081234567890", -- 交易流水
 "ReqTime": "20130817200202123",--请求时间
 * Created by wuxw on 2017/7/23.
 */
public class TcpContRule {

    private String ruleType;

    private String serviceCode;

    private String transactionId;

    private String reqTime;

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }
}
