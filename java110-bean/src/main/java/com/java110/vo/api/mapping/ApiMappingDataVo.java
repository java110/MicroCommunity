package com.java110.vo.api.mapping;

import java.io.Serializable;
import java.util.Date;

public class ApiMappingDataVo implements Serializable {

    private String id;
private String domain;
private String name;
private String key;
private String value;
private String remark;
public String getId() {
        return id;
    }
public void setId(String id) {
        this.id = id;
    }
public String getDomain() {
        return domain;
    }
public void setDomain(String domain) {
        this.domain = domain;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getKey() {
        return key;
    }
public void setKey(String key) {
        this.key = key;
    }
public String getValue() {
        return value;
    }
public void setValue(String value) {
        this.value = value;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }



}
