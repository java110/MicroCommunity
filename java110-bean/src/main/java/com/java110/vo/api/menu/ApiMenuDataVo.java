package com.java110.vo.api.menu;

import java.io.Serializable;
import java.util.Date;

public class ApiMenuDataVo implements Serializable {

    private String mId;
private String name;
private String url;
private String seq;
private String isShow;
private String description;
public String getMId() {
        return mId;
    }
public void setMId(String mId) {
        this.mId = mId;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getUrl() {
        return url;
    }
public void setUrl(String url) {
        this.url = url;
    }
public String getSeq() {
        return seq;
    }
public void setSeq(String seq) {
        this.seq = seq;
    }
public String getIsShow() {
        return isShow;
    }
public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
public String getDescription() {
        return description;
    }
public void setDescription(String description) {
        this.description = description;
    }



}
