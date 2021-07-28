package com.java110.store.bmo.purchase.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.store.bmo.purchase.IResourceEnterBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("resourceEnterBMOImpl")
public class ResourceEnterBMOImpl implements IResourceEnterBMO {

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Override
    @Java110Transactional
    public ResponseEntity<String> enter(PurchaseApplyPo purchaseApplyPo) {
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = purchaseApplyPo.getPurchaseApplyDetailPos();
        for (PurchaseApplyDetailPo purchaseApplyDetailPo : purchaseApplyDetailPos) {
            purchaseApplyDetailInnerServiceSMOImpl.updatePurchaseApplyDetail(purchaseApplyDetailPo);
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(purchaseApplyDetailPo.getResId());
            resourceStorePo.setPurchasePrice(purchaseApplyDetailPo.getPrice());
            resourceStorePo.setStock(purchaseApplyDetailPo.getPurchaseQuantity());
            resourceStorePo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_ENTER);
            //查询物品资源信息
            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setResId(purchaseApplyDetailPo.getResId());
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            Assert.listOnlyOne(resourceStoreDtos, "查询物品资源信息错误！");
            if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniUnitStock())) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            //获取最小计量单位数量
            BigDecimal miniUnitStock = new BigDecimal(resourceStoreDtos.get(0).getMiniUnitStock());
            if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniStock())) {
                throw new IllegalArgumentException("最小计量总数不能为空！");
            }
            //获取采购前物品最小计量总数
            BigDecimal miniStock = new BigDecimal(resourceStoreDtos.get(0).getMiniStock());
            //计算采购的物品最小计量总数
            BigDecimal purchaseQuantity = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity());
            BigDecimal purchaseMiniStock = purchaseQuantity.multiply(miniUnitStock);
            //计算采购后物品最小计量总数
            BigDecimal nowMiniStock = miniStock.add(purchaseMiniStock);
            resourceStorePo.setMiniStock(String.valueOf(nowMiniStock));
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);
        }
        //获取订单号
        String applyOrderId = purchaseApplyPo.getApplyOrderId();
        PurchaseApplyPo purchaseApply = new PurchaseApplyPo();
        purchaseApply.setApplyOrderId(applyOrderId);
        purchaseApply.setState(PurchaseApplyDto.STATE_AUDITED);
        purchaseApply.setStatusCd("0");
        purchaseApplyInnerServiceSMOImpl.updatePurchaseApply(purchaseApply);
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "采购申请成功");
    }
}
