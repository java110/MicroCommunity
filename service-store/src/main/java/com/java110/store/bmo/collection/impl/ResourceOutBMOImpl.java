package com.java110.store.bmo.collection.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.userStorehouse.UserStorehouseDto;
import com.java110.intf.common.IPurchaseApplyUserInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.userStorehouse.UserStorehousePo;
import com.java110.store.bmo.collection.IResourceOutBMO;
import com.java110.utils.util.Assert;
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

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Override
    @Java110Transactional
    public ResponseEntity<String> out(PurchaseApplyPo purchaseApplyPo) {
        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        purchaseApplyDto.setApplyOrderId(purchaseApplyPo.getApplyOrderId());
        List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplys(purchaseApplyDto);
        Assert.listOnlyOne(purchaseApplyDtos, "出库单不存在");
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = purchaseApplyPo.getPurchaseApplyDetailPos();
        for (PurchaseApplyDetailPo purchaseApplyDetailPo : purchaseApplyDetailPos) {
            purchaseApplyDetailInnerServiceSMOImpl.updatePurchaseApplyDetail(purchaseApplyDetailPo);
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(purchaseApplyDetailPo.getResId());
            resourceStorePo.setStock("-" + purchaseApplyDetailPo.getPurchaseQuantity());
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);
            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setResId(purchaseApplyDetailPo.getResId());
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            if (resourceStoreDtos == null || resourceStoreDtos.size() < 1) {
                continue;
            }
            //入库到个人仓库中
            UserStorehousePo userStorehousePo = new UserStorehousePo();
            userStorehousePo.setUsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_usId));
            userStorehousePo.setResId(resourceStoreDtos.get(0).getResId());
            userStorehousePo.setResName(resourceStoreDtos.get(0).getResName());
            userStorehousePo.setStoreId(resourceStoreDtos.get(0).getStoreId());
            userStorehousePo.setUserId(purchaseApplyDtos.get(0).getUserId());
            //查询物品 是否已经存在
            UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
            userStorehouseDto.setResId(resourceStoreDtos.get(0).getResId());
            userStorehouseDto.setUserId(purchaseApplyDtos.get(0).getUserId());
            userStorehouseDto.setStoreId(resourceStoreDtos.get(0).getStoreId());
            List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
            if (userStorehouseDtos == null || userStorehouseDtos.size() < 1) {
                userStorehousePo.setStock(purchaseApplyDetailPo.getPurchaseQuantity());
                userStorehouseInnerServiceSMOImpl.saveUserStorehouses(userStorehousePo);
            } else {
                int total = Integer.parseInt(purchaseApplyDetailPo.getPurchaseQuantity()) + Integer.parseInt(userStorehouseDtos.get(0).getStock());
                userStorehousePo.setStock(total + "");
                userStorehousePo.setUsId(userStorehouseDtos.get(0).getUsId());
                userStorehouseInnerServiceSMOImpl.updateUserStorehouses(userStorehousePo);
            }
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "出库成功");
    }
}
