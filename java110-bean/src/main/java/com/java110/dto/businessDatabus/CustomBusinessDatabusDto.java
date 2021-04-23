package com.java110.dto.businessDatabus;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @ClassName 自定义databus
 * @Description 业务数据同步数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CustomBusinessDatabusDto extends PageDto implements Serializable {

    private String businessTypeCd;

    private JSONObject data;

    public CustomBusinessDatabusDto() {
    }

    public CustomBusinessDatabusDto(String businessTypeCd, JSONObject data) {
        this.businessTypeCd = businessTypeCd;
        this.data = data;
    }

    public static CustomBusinessDatabusDto getInstance(String businessTypeCd, JSONObject data) {
        return new CustomBusinessDatabusDto(businessTypeCd, data);
    }


    public String getBusinessTypeCd() {
        return businessTypeCd;
    }

    public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
