package com.java110.po.user;

import java.io.Serializable;

/**
 * @ClassName UserTagPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 22:44
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class UserTagPo implements Serializable {

    private String id;
    private String tagId;
    private String userId;
    private String tagCd;

    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTagCd() {
        return tagCd;
    }

    public void setTagCd(String tagCd) {
        this.tagCd = tagCd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
