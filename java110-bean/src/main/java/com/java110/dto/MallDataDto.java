package com.java110.dto;

import com.alibaba.fastjson.JSONObject;

public class MallDataDto {

    private String mallApiCode;

    private JSONObject data;

    public MallDataDto() {
    }

    public MallDataDto(String mallApiCode, JSONObject data) {
        this.mallApiCode = mallApiCode;
        this.data = data;
    }



    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getMallApiCode() {
        return mallApiCode;
    }

    public void setMallApiCode(String mallApiCode) {
        this.mallApiCode = mallApiCode;
    }
}
