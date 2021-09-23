package com.java110.api.components.purchaseApply;

import com.java110.core.context.IPageData;
import com.java110.api.smo.purchaseApply.IAddPurchaseApplySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加采购申请组件
 */
@Component("addPurchaseApply")
public class AddPurchaseApplyComponent {

    @Autowired
    private IAddPurchaseApplySMO addPurchaseApplySMOImpl;

    /**
     * 添加采购申请数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addPurchaseApplySMOImpl.savePurchaseApply(pd);
    }

    public IAddPurchaseApplySMO getAddPurchaseApplySMOImpl() {
        return addPurchaseApplySMOImpl;
    }

    public void setAddPurchaseApplySMOImpl(IAddPurchaseApplySMO addPurchaseApplySMOImpl) {
        this.addPurchaseApplySMOImpl = addPurchaseApplySMOImpl;
    }
}
