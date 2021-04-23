package com.java110.entity;

import java.util.Date;

/**
 * 默认 实体，对公用属性 封装
 * Created by wuxw on 2017/5/20.
 */
public class DefaultEntity {


    private String versionId;

    private Date start_dt;

    private Date create_dt;

    private Date end_dt;

    private Date version_dt;

    private String status_cd;


    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Date getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(Date start_dt) {
        this.start_dt = start_dt;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }

    public Date getEnd_dt() {
        return end_dt;
    }

    public void setEnd_dt(Date end_dt) {
        this.end_dt = end_dt;
    }

    public String getStatus_cd() {
        return status_cd;
    }

    public void setStatus_cd(String status_cd) {
        this.status_cd = status_cd;
    }

    public Date getVersion_dt() {
        return version_dt;
    }

    public void setVersion_dt(Date version_dt) {
        this.version_dt = version_dt;
    }
}
