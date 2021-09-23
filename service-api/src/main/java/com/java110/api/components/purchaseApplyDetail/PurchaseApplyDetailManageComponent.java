package com.java110.api.components.purchaseApplyDetail;

import com.java110.core.context.IPageData;
import com.java110.api.smo.purchaseApplyDetail.IListPurchaseApplyDetailsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 出入库明细组件管理类
 * <p>
 * add by fqz
 * <p>
 * 2021-04-22
 */
@Component("purchaseApplyDetailManage")
public class PurchaseApplyDetailManageComponent {

    @Autowired
    private IListPurchaseApplyDetailsSMO listPurchaseApplyDetailsSMOImpl;

    /**
     * 查询采购申请列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listPurchaseApplyDetailsSMOImpl.listPurchaseApplyDetails(pd);
    }

    public IListPurchaseApplyDetailsSMO getListPurchaseApplyDetailsSMOImpl() {
        return listPurchaseApplyDetailsSMOImpl;
    }

    public void setListPurchaseApplyDetailsSMOImpl(IListPurchaseApplyDetailsSMO listPurchaseApplyDetailsSMOImpl) {
        this.listPurchaseApplyDetailsSMOImpl = listPurchaseApplyDetailsSMOImpl;
    }
}
