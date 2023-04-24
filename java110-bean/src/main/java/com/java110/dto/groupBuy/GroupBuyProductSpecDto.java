package com.java110.dto.groupBuy;

import com.java110.dto.product.ProductSpecValueDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 拼团产品规格数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class GroupBuyProductSpecDto extends ProductSpecValueDto implements Serializable {

    private String valueId;
    private String specId;
    private String groupSales;
    private String productId;
    private String[] productIds;
    private String groupPrice;
    private String defaultShow;
    private String storeId;
    private String groupStock;


    private Date createTime;

    private String statusCd = "0";


    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getGroupSales() {
        return groupSales;
    }

    public void setGroupSales(String groupSales) {
        this.groupSales = groupSales;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(String groupPrice) {
        this.groupPrice = groupPrice;
    }

    public String getDefaultShow() {
        return defaultShow;
    }

    public void setDefaultShow(String defaultShow) {
        this.defaultShow = defaultShow;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getGroupStock() {
        return groupStock;
    }

    public void setGroupStock(String groupStock) {
        this.groupStock = groupStock;
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

    public String[] getProductIds() {
        return productIds;
    }

    public void setProductIds(String[] productIds) {
        this.productIds = productIds;
    }

    @Override
    public String getValueId() {
        return valueId;
    }

    @Override
    public void setValueId(String valueId) {
        this.valueId = valueId;
    }
}
