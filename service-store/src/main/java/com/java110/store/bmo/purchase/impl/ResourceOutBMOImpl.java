package com.java110.store.bmo.purchase.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IPurchaseApplyUserInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.store.bmo.purchase.IResourceOutBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("resourceOutBMOImpl")
public class ResourceOutBMOImpl implements IResourceOutBMO {

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;


    @Autowired
    private IPurchaseApplyUserInnerServiceSMO purchaseApplyUserInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;


    @Override
    @Java110Transactional
    public ResponseEntity<String> out(PurchaseApplyPo purchaseApplyPo) {

        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = purchaseApplyPo.getPurchaseApplyDetailPos();
        for (PurchaseApplyDetailPo purchaseApplyDetailPo : purchaseApplyDetailPos) {
            purchaseApplyDetailInnerServiceSMOImpl.updatePurchaseApplyDetail(purchaseApplyDetailPo);
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(purchaseApplyDetailPo.getResId());
            resourceStorePo.setStock("-" + purchaseApplyDetailPo.getPurchaseQuantity());
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "出库成功");
    }
}
