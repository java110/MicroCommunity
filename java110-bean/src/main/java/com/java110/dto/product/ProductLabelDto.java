package com.java110.dto.product;

import com.java110.dto.product.ProductDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 产品标签数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ProductLabelDto extends ProductDto implements Serializable {

    private String labelId;
    private String productId;
    private String storeId;
    private String labelCd;
    private String hasProduct;


    private Date createTime;

    private String statusCd = "0";


    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getLabelCd() {
        return labelCd;
    }

    public void setLabelCd(String labelCd) {
        this.labelCd = labelCd;
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

    public String getHasProduct() {
        return hasProduct;
    }

    public void setHasProduct(String hasProduct) {
        this.hasProduct = hasProduct;
    }
}
