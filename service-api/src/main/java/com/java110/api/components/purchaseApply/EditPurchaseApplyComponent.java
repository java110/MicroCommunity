package com.java110.api.components.purchaseApply;

import com.java110.core.context.IPageData;
import com.java110.api.smo.purchaseApply.IEditPurchaseApplySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editPurchaseApply")
public class EditPurchaseApplyComponent {

    @Autowired
    private IEditPurchaseApplySMO editPurchaseApplySMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editPurchaseApplySMOImpl.updatePurchaseApply(pd);
    }

    public IEditPurchaseApplySMO getEditPurchaseApplySMOImpl() {
        return editPurchaseApplySMOImpl;
    }

    public void setEditPurchaseApplySMOImpl(IEditPurchaseApplySMO editPurchaseApplySMOImpl) {
        this.editPurchaseApplySMOImpl = editPurchaseApplySMOImpl;
    }
}
