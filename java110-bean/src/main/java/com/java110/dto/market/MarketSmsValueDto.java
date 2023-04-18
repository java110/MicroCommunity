package com.java110.dto.market;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 营销信息值数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MarketSmsValueDto extends PageDto implements Serializable {

    private String valueId;
    private String smsId;
    private String[] smsIds;
    private String smsKey;
    private String smsValue;

    private String name;


    private Date createTime;

    private String statusCd = "0";


    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getSmsKey() {
        return smsKey;
    }

    public void setSmsKey(String smsKey) {
        this.smsKey = smsKey;
    }

    public String getSmsValue() {
        return smsValue;
    }

    public void setSmsValue(String smsValue) {
        this.smsValue = smsValue;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String[] getSmsIds() {
        return smsIds;
    }

    public void setSmsIds(String[] smsIds) {
        this.smsIds = smsIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
