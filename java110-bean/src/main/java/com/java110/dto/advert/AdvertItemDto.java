package com.java110.dto.advert;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 广告项信息数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AdvertItemDto extends PageDto implements Serializable {

    private String itemTypeCd;
    private String[] itemTypeCds;
    private String communityId;
    private String advertItemId;
    private String advertId;
    private String url;
    private String seq;


    private Date createTime;

    private String statusCd = "0";


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

    public String[] getItemTypeCds() {
        return itemTypeCds;
    }

    public void setItemTypeCds(String[] itemTypeCds) {
        this.itemTypeCds = itemTypeCds;
    }
}
