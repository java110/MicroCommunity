package com.java110.vo.api.resourceStore;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiResourceStoreVo extends MorePageVo implements Serializable {
    List<ApiResourceStoreDataVo> resourceStores;
    private String subTotal;
    private String totalPrice;


    public List<ApiResourceStoreDataVo> getResourceStores() {
        return resourceStores;
    }

    public void setResourceStores(List<ApiResourceStoreDataVo> resourceStores) {
        this.resourceStores = resourceStores;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
