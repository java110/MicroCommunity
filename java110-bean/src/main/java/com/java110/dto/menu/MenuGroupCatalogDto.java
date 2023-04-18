package com.java110.dto.menu;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 菜单目录组数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MenuGroupCatalogDto extends PageDto implements Serializable {

    private String storeType;
    private String gId;
    private String gcId;
    private String caId;
    private String mgName;

    public String getGcId() {
        return gcId;
    }

    public void setGcId(String gcId) {
        this.gcId = gcId;
    }

    private Date createTime;

    private String statusCd = "0";


    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getCaId() {
        return caId;
    }

    public void setCaId(String caId) {
        this.caId = caId;
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

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getMgName() {
        return mgName;
    }

    public void setMgName(String mgName) {
        this.mgName = mgName;
    }
}
