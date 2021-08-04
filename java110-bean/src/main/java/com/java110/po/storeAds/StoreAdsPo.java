package com.java110.po.storeAds;

import java.io.Serializable;
import java.util.Date;

public class StoreAdsPo implements Serializable {

    private String adName;
private String adType;
private String shareId;
private String startTime;
private String pageUrl;
private String statusCd = "0";
private String state;
private String endTime;
private String advertType;
private String adsId;
private String seq;
private String url;
public String getAdName() {
        return adName;
    }
public void setAdName(String adName) {
        this.adName = adName;
    }
public String getAdType() {
        return adType;
    }
public void setAdType(String adType) {
        this.adType = adType;
    }
public String getShareId() {
        return shareId;
    }
public void setShareId(String shareId) {
        this.shareId = shareId;
    }
public String getStartTime() {
        return startTime;
    }
public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
public String getPageUrl() {
        return pageUrl;
    }
public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getEndTime() {
        return endTime;
    }
public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
public String getAdvertType() {
        return advertType;
    }
public void setAdvertType(String advertType) {
        this.advertType = advertType;
    }
public String getAdsId() {
        return adsId;
    }
public void setAdsId(String adsId) {
        this.adsId = adsId;
    }
public String getSeq() {
        return seq;
    }
public void setSeq(String seq) {
        this.seq = seq;
    }
public String getUrl() {
        return url;
    }
public void setUrl(String url) {
        this.url = url;
    }



}
