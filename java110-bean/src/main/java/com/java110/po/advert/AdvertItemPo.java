package com.java110.po.advert;

import java.io.Serializable;

/**
 * @ClassName AdvertItemPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/26 21:32
 * @Version 1.0
 * add by wuxw 2020/5/26
 **/
public class AdvertItemPo implements Serializable {

    private String advertItemId;

    private String advertId;
    private String itemTypeCd;
    private String url;
    private String communityId;
    private String seq;
    private String statusCd="0";


    public String getAdvertItemId() {
        return advertItemId;
    }

    public void setAdvertItemId(String advertItemId) {
        this.advertItemId = advertItemId;
    }

    public String getAdvertId() {
        return advertId;
    }

    public void setAdvertId(String advertId) {
        this.advertId = advertId;
    }

    public String getItemTypeCd() {
        return itemTypeCd;
    }

    public void setItemTypeCd(String itemTypeCd) {
        this.itemTypeCd = itemTypeCd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
