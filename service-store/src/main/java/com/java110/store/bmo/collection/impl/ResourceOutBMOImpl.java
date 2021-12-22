package com.java110.store.bmo.collection.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.userStorehouse.UserStorehouseDto;
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
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("resourceOutBMOImpl")
public class ResourceOutBMOImpl implements IResourceOutBMO {

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;


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
            //查询物品资源信息
            ResourceStoreDto resourceStore = new ResourceStoreDto();
            resourceStore.setResId(purchaseApplyDetailPo.getResId());
            List<ResourceStoreDto> resourceStores = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStore);
            Assert.listOnlyOne(resourceStores, "查询物品资源信息错误！");
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(purchaseApplyDetailPo.getResId());
            resourceStorePo.setStock("-" + purchaseApplyDetailPo.getPurchaseQuantity());
            resourceStorePo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_OUT);
            //获取原物品最小计量总数
            if (StringUtil.isEmpty(resourceStores.get(0).getMiniStock())) {
                throw new IllegalArgumentException("最小计量总数不能为空！");
            }
            BigDecimal miniStock1 = new BigDecimal(resourceStores.get(0).getMiniStock());
            BigDecimal purchaseQuantity = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity());
            if (StringUtil.isEmpty(resourceStores.get(0).getMiniUnitStock())) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            //获取物品最小计量单位数量
            BigDecimal miniUnitStock1 = new BigDecimal(resourceStores.get(0).getMiniUnitStock());
            //计算领用物品的最小计量总数
            BigDecimal applyQuantity = purchaseQuantity.multiply(miniUnitStock1);
            //计算物品领用后剩余的最小计量总数
            BigDecimal newMiniStock = miniStock1.subtract(applyQuantity);
            if (newMiniStock.compareTo(BigDecimal.ZERO) == -1) {
                throw new IllegalArgumentException("物品库存已经不足，请确认物品库存！");
            }
            resourceStorePo.setMiniStock(String.valueOf(newMiniStock));
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);
            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setResId(purchaseApplyDetailPo.getResId());
            //查询物品资源
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            if (resourceStoreDtos == null || resourceStoreDtos.size() < 1) {
                continue;
            }
            //获取物品单位
            String unitCode = resourceStoreDtos.get(0).getUnitCode();
            //获取物品最小计量单位
            String miniUnitCode = resourceStoreDtos.get(0).getMiniUnitCode();
            //获取物品最小计量单位数量
            String miniUnitStock = resourceStoreDtos.get(0).getMiniUnitStock();
            //入库到个人仓库中
            UserStorehousePo userStorehousePo = new UserStorehousePo();
            userStorehousePo.setUsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_usId));
            userStorehousePo.setResId(resourceStoreDtos.get(0).getResId());
            userStorehousePo.setResCode(resourceStoreDtos.get(0).getResCode());
            userStorehousePo.setResName(resourceStoreDtos.get(0).getResName());
            userStorehousePo.setStoreId(resourceStoreDtos.get(0).getStoreId());
            userStorehousePo.setUserId(purchaseApplyDtos.get(0).getUserId());
            //查询物品 是否已经存在
            UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
            userStorehouseDto.setResCode(resourceStoreDtos.get(0).getResCode());
            userStorehouseDto.setUserId(purchaseApplyDtos.get(0).getUserId());
            userStorehouseDto.setStoreId(resourceStoreDtos.get(0).getStoreId());
            List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
            if (userStorehouseDtos == null || userStorehouseDtos.size() < 1) {
                userStorehousePo.setStock(purchaseApplyDetailPo.getPurchaseQuantity());
                if (!StringUtil.isEmpty(unitCode) && !StringUtil.isEmpty(miniUnitCode) && !StringUtil.isEmpty(miniUnitStock) && !unitCode.equals(miniUnitCode)) {
                    //获取领取数量
                    BigDecimal purchaseQuantity2 = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity());
                    BigDecimal miniUnitStock2 = new BigDecimal(miniUnitStock);
                    //计算个人物品最小计量总数
                    BigDecimal quantity = purchaseQuantity2.multiply(miniUnitStock2);
                    userStorehousePo.setMiniStock(String.valueOf(quantity));
                } else {
                    userStorehousePo.setMiniStock(purchaseApplyDetailPo.getPurchaseQuantity());
                }
                userStorehouseInnerServiceSMOImpl.saveUserStorehouses(userStorehousePo);
            } else {
                //获取个人物品领用后的库存
                BigDecimal purchaseQuantity3 = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity());
                BigDecimal stock3 = new BigDecimal(userStorehouseDtos.get(0).getStock());
                BigDecimal total = purchaseQuantity3.add(stock3);
                userStorehousePo.setStock(total.toString());
                userStorehousePo.setUsId(userStorehouseDtos.get(0).getUsId());
                if (!StringUtil.isEmpty(unitCode) && !StringUtil.isEmpty(miniUnitCode) && !StringUtil.isEmpty(miniUnitStock) && !unitCode.equals(miniUnitCode)) {
                    //获取本次领取数量
                    BigDecimal miniUnitStock3 = new BigDecimal(miniUnitStock);
                    //计算本次领取的个人物品最小计量总数
                    BigDecimal quantity = purchaseQuantity3.multiply(miniUnitStock3);
                    BigDecimal miniStock = new BigDecimal(0);
                    //获取个人物品原先的最小计量总数
                    if (StringUtil.isEmpty(userStorehouseDtos.get(0).getMiniStock())) {
                        throw new IllegalArgumentException("信息错误，个人物品最小计量总数不能为空！");
                    } else {
                        miniStock = new BigDecimal(userStorehouseDtos.get(0).getMiniStock());
                    }
                    //计算领用后个人物品总的最小计量总数
                    BigDecimal miniQuantity = quantity.add(miniStock);
                    userStorehousePo.setMiniStock(String.valueOf(miniQuantity));
                } else {
                    userStorehousePo.setMiniStock(String.valueOf(total));
                }
                userStorehouseInnerServiceSMOImpl.updateUserStorehouses(userStorehousePo);
            }
        }
        //获取订单号
        String applyOrderId = purchaseApplyPo.getApplyOrderId();
        PurchaseApplyPo purchaseApply = new PurchaseApplyPo();
        purchaseApply.setApplyOrderId(applyOrderId);
        purchaseApply.setState(PurchaseApplyDto.STATE_AUDITED);
        purchaseApply.setStatusCd("0");
        purchaseApplyInnerServiceSMOImpl.updatePurchaseApply(purchaseApply);
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "出库成功");
    }
}
