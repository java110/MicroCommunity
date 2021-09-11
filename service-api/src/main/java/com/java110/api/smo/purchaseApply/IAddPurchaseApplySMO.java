package com.java110.api.smo.purchaseApply;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加采购申请接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAddPurchaseApplySMO {

    /**
     * 添加采购申请
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> savePurchaseApply(IPageData pd);
}
