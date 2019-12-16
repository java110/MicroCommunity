package com.java110.vo.api.advert;

import java.io.Serializable;

public class ApiAdvertItemDataVo implements Serializable {

    private String itemTypeCd;
    private String communityId;
    private String advertItemId;
    private String advertId;
    private String url;
    private String seq;


    public String getItemTypeCd() {
        return itemTypeCd;
    }

    public void setItemTypeCd(String itemTypeCd) {
        this.itemTypeCd = itemTypeCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

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
}
