package com.java110.vo.api.purchaseApply;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPurchaseApplyVo extends MorePageVo implements Serializable {
    List<ApiPurchaseApplyDataVo> purchaseApplys;


    public List<ApiPurchaseApplyDataVo> getPurchaseApplys() {
        return purchaseApplys;
    }

    public void setPurchaseApplys(List<ApiPurchaseApplyDataVo> purchaseApplys) {
        this.purchaseApplys = purchaseApplys;
    }
}
