package com.java110.dto.product;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 产品数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ProductDto extends ProductActivityDto implements Serializable {

    private String productId;
    private String unitName;
    private String isPostage;
    private String sort;
    private String storeId;
    private String barCode;
    private String postage;
    private String prodName;
    private String state;
    private String stateName;
    private String keyword;
    private String prodDesc;
    private String categoryId;
    private String categoryName;

    private String content;

    private String coverPhoto;

    private String stock;
    private String sales;

    private List<String> carouselFigurePhotos;

    private List<ProductSpecValueDto> productSpecValues;

    private ProductSpecValueDto defaultSpecValue;


    private Date createTime;

    private String statusCd = "0";


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getIsPostage() {
        return isPostage;
    }

    public void setIsPostage(String isPostage) {
        this.isPostage = isPostage;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public List<String> getCarouselFigurePhotos() {
        return carouselFigurePhotos;
    }

    public void setCarouselFigurePhotos(List<String> carouselFigurePhotos) {
        this.carouselFigurePhotos = carouselFigurePhotos;
    }

    public List<ProductSpecValueDto> getProductSpecValues() {
        return productSpecValues;
    }

    public void setProductSpecValues(List<ProductSpecValueDto> productSpecValues) {
        this.productSpecValues = productSpecValues;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public ProductSpecValueDto getDefaultSpecValue() {
        return defaultSpecValue;
    }

    public void setDefaultSpecValue(ProductSpecValueDto defaultSpecValue) {
        this.defaultSpecValue = defaultSpecValue;
    }
}
