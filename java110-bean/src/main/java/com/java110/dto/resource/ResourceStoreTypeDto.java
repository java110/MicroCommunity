package com.java110.dto.resource;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 物品类型数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ResourceStoreTypeDto extends PageDto implements Serializable {


    private String parentRstId;
    private String parentName;
    private String parentDescription;

    private String rstId;
    private String name;
    private String description;
    private String storeId;
    private Date createTime;
    private String statusCd = "0";
    private String parentId ; //父级id,默认为0

    private List<ResourceStoreTypeDto> subTypes;

    public String getRstId() {
        return rstId;
    }

    public void setRstId(String rstId) {
        this.rstId = rstId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentRstId() {
        return parentRstId;
    }

    public void setParentRstId(String parentRstId) {
        this.parentRstId = parentRstId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentDescription() {
        return parentDescription;
    }

    public void setParentDescription(String parentDescription) {
        this.parentDescription = parentDescription;
    }

    public List<ResourceStoreTypeDto> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(List<ResourceStoreTypeDto> subTypes) {
        this.subTypes = subTypes;
    }
}
