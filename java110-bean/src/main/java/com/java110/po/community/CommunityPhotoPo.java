package com.java110.po.community;

import java.io.Serializable;

/**
 * @ClassName CommunityPhoto
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 21:38
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class CommunityPhotoPo implements Serializable {

    private String communityPhotoId;

    private String communityId;
    private String communityPhotoTypeCd;
    private String photo;

    public String getCommunityPhotoId() {
        return communityPhotoId;
    }

    public void setCommunityPhotoId(String communityPhotoId) {
        this.communityPhotoId = communityPhotoId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityPhotoTypeCd() {
        return communityPhotoTypeCd;
    }

    public void setCommunityPhotoTypeCd(String communityPhotoTypeCd) {
        this.communityPhotoTypeCd = communityPhotoTypeCd;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
