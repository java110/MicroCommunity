package com.java110.po.store;

import java.io.Serializable;

/**
 * @ClassName StorePhotoPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 22:07
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class StorePhotoPo implements Serializable {

    private String storePhotoId;
    private String storeId;
    private String storePhotoTypeCd;
    private String photo;

    public String getStorePhotoId() {
        return storePhotoId;
    }

    public void setStorePhotoId(String storePhotoId) {
        this.storePhotoId = storePhotoId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStorePhotoTypeCd() {
        return storePhotoTypeCd;
    }

    public void setStorePhotoTypeCd(String storePhotoTypeCd) {
        this.storePhotoTypeCd = storePhotoTypeCd;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
