package com.java110.entity.center;

import java.io.Serializable;

/**
 * Created by wuxw on 2018/5/19.
 */
public class DataFlowLog implements Serializable {

    private String request;

    private String response;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
