package com.java110.dto.data;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class ExportDataDto implements Serializable{

    private String businessAdapt;

    private JSONObject reqJson;

    private String fileName;

    private String downloadId;


    public String getBusinessAdapt() {
        return businessAdapt;
    }

    public void setBusinessAdapt(String businessAdapt) {
        this.businessAdapt = businessAdapt;
    }

    public JSONObject getReqJson() {
        return reqJson;
    }

    public void setReqJson(JSONObject reqJson) {
        this.reqJson = reqJson;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }
}
