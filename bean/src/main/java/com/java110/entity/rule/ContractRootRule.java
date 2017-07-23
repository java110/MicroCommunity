package com.java110.entity.rule;

import com.alibaba.fastjson.JSONObject;

/**
 * 请求报文封装
 * Created by wuxw on 2017/7/23.
 */
public class ContractRootRule {


    private TcpContRule tcpCont;


    private JSONObject svcCont;


    public TcpContRule getTcpCont() {
        return tcpCont;
    }

    public void setTcpCont(TcpContRule tcpCont) {
        this.tcpCont = tcpCont;
    }

    public JSONObject getSvcCont() {
        return svcCont;
    }

    public void setSvcCont(JSONObject svcCont) {
        this.svcCont = svcCont;
    }
}
