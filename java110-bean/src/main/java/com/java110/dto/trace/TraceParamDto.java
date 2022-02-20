package com.java110.dto.trace;

import java.io.Serializable;

public class TraceParamDto implements Serializable {

    private String  reqHeader;
    private String  resHeader;
    private String  reqParam;
    private String  resParam;

    public String getReqHeader() {
        return reqHeader;
    }

    public void setReqHeader(String reqHeader) {
        this.reqHeader = reqHeader;
    }

    public String getResHeader() {
        return resHeader;
    }

    public void setResHeader(String resHeader) {
        this.resHeader = resHeader;
    }

    public String getReqParam() {
        return reqParam;
    }

    public void setReqParam(String reqParam) {
        this.reqParam = reqParam;
    }

    public String getResParam() {
        return resParam;
    }

    public void setResParam(String resParam) {
        this.resParam = resParam;
    }
}
