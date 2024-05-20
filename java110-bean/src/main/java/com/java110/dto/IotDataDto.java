package com.java110.dto;

import com.alibaba.fastjson.JSONObject;

public class IotDataDto {

    private String iotApiCode;

    private JSONObject data;

    public IotDataDto() {
    }

    public IotDataDto(String iotApiCode, JSONObject data) {
        this.iotApiCode = iotApiCode;
        this.data = data;
    }

    public String getIotApiCode() {
        return iotApiCode;
    }

    public void setIotApiCode(String iotApiCode) {
        this.iotApiCode = iotApiCode;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
