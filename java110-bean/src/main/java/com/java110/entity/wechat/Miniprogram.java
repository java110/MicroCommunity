package com.java110.entity.wechat;

import java.io.Serializable;

public class Miniprogram implements Serializable {

    private String appid;

    private String pagepath;


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }
}
