package com.java110.api.components.purchaseApply;

import com.java110.core.context.IPageData;
import com.java110.api.smo.purchaseApply.IDeletePurchaseApplySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加采购申请组件
 */
@Component("deletePurchaseApply")
public class DeletePurchaseApplyComponent {

    @Autowired
    private IDeletePurchaseApplySMO deletePurchaseApplySMOImpl;

    /**
     * 添加采购申请数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deletePurchaseApplySMOImpl.deletePurchaseApply(pd);
    }

    public IDeletePurchaseApplySMO getDeletePurchaseApplySMOImpl() {
        return deletePurchaseApplySMOImpl;
    }

    public void setDeletePurchaseApplySMOImpl(IDeletePurchaseApplySMO deletePurchaseApplySMOImpl) {
        this.deletePurchaseApplySMOImpl = deletePurchaseApplySMOImpl;
    }
}
