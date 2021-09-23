package com.java110.api.components.purchaseApply;


import com.java110.core.context.IPageData;
import com.java110.api.smo.purchaseApply.IListPurchaseApplysSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 采购申请组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("purchaseApplyManage")
public class PurchaseApplyManageComponent {

    @Autowired
    private IListPurchaseApplysSMO listPurchaseApplysSMOImpl;

    /**
     * 查询采购申请列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listPurchaseApplysSMOImpl.listPurchaseApplys(pd);
    }

    public IListPurchaseApplysSMO getListPurchaseApplysSMOImpl() {
        return listPurchaseApplysSMOImpl;
    }

    public void setListPurchaseApplysSMOImpl(IListPurchaseApplysSMO listPurchaseApplysSMOImpl) {
        this.listPurchaseApplysSMOImpl = listPurchaseApplysSMOImpl;
    }
}
