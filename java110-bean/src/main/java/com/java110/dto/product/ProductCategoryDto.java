package com.java110.dto.product;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 产品目录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ProductCategoryDto extends PageDto implements Serializable {

    private String categoryLevel;
private String parentCategoryId;
private String storeId;
private String categoryName;
private String categoryId;
private String seq;
private String isShow;


    private Date createTime;

    private String statusCd = "0";


    public String getCategoryLevel() {
        return categoryLevel;
    }
public void setCategoryLevel(String categoryLevel) {
        this.categoryLevel = categoryLevel;
    }
public String getParentCategoryId() {
        return parentCategoryId;
    }
public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getCategoryName() {
        return categoryName;
    }
public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
public String getCategoryId() {
        return categoryId;
    }
public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
public String getSeq() {
        return seq;
    }
public void setSeq(String seq) {
        this.seq = seq;
    }
public String getIsShow() {
        return isShow;
    }
public void setIsShow(String isShow) {
        this.isShow = isShow;
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
}
