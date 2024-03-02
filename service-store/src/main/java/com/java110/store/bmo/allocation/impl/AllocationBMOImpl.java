package com.java110.store.bmo.allocation.impl;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.purchase.AllocationStorehouseDto;
import com.java110.dto.resource.ResourceStoreDto;
import com.java110.dto.resource.ResourceStoreTimesDto;
import com.java110.dto.store.StorehouseDto;
import com.java110.intf.store.*;
import com.java110.po.purchase.AllocationStorehousePo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resource.ResourceStoreTimesPo;
import com.java110.store.bmo.allocation.IAllocationBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class AllocationBMOImpl implements IAllocationBMO {

    @Autowired
    private IStorehouseInnerServiceSMO storehouseInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreV1InnerServiceSMO resourceStoreV1InnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseV1InnerServiceSMO allocationStorehouseV1InnerServiceSMOImpl;

    @Override
    public void doToAllocationStorehouse(AllocationStorehouseDto tmpAllocationStorehouseDto, double allocationStock) {
        //查询z 仓库
        StorehouseDto storehouseDto = new StorehouseDto();
        storehouseDto.setShId(tmpAllocationStorehouseDto.getShIdz());
        List<StorehouseDto> targetStorehouseDtos = storehouseInnerServiceSMOImpl.queryStorehouses(storehouseDto);
        Assert.listOnlyOne(targetStorehouseDtos, "目标仓库不存在");

        // todo -------------------------------------------------原仓库中做扣除 (start)-----------------------------------------------------//
        // todo 原仓库中扣除 数量
        ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
        resourceStoreDto.setResId(tmpAllocationStorehouseDto.getResId());
        resourceStoreDto.setShId(tmpAllocationStorehouseDto.getShIda());
        List<ResourceStoreDto> resourceStoreDtoAs = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
        //todo 这种场景不存在
        if (resourceStoreDtoAs == null || resourceStoreDtoAs.size() < 1) {
            return;
        }
        ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
        resourceStoreTimesDto.setTimesId(tmpAllocationStorehouseDto.getTimesId());
        resourceStoreTimesDto.setShId(tmpAllocationStorehouseDto.getShIda());
        List<ResourceStoreTimesDto> resourceStoreTimesDtoAs = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
        //todo 这种场景不存在
        if (resourceStoreTimesDtoAs == null || resourceStoreTimesDtoAs.size() < 1) {
            return;
        }
        double stockA = Double.parseDouble(resourceStoreDtoAs.get(0).getStock());
        //todo 库存不够时，只能调拨 库存，这种场景应该执行不到 前文做了校验
        if (stockA < allocationStock) {
            allocationStock = stockA;
        }
        ResourceStorePo resourceStorePo = new ResourceStorePo();
        resourceStorePo.setResId(resourceStoreDtoAs.get(0).getResId());
        resourceStorePo.setStoreId(resourceStoreDtoAs.get(0).getStoreId());
        resourceStorePo.setStock(allocationStock * -1 + "");// 这里去扣出
        resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);
        stockA = Double.parseDouble(resourceStoreTimesDtoAs.get(0).getStock());
        //todo 库存不够时，只能调拨 库存，这种场景应该执行不到 前文做了校验
        if (stockA < allocationStock) {
            allocationStock = stockA;
        }
        ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
        resourceStoreTimesPo.setPrice(resourceStoreTimesDtoAs.get(0).getPrice());
        resourceStoreTimesPo.setShId(resourceStoreTimesDtoAs.get(0).getShId());
        resourceStoreTimesPo.setResCode(resourceStoreTimesDtoAs.get(0).getResCode());
        resourceStoreTimesPo.setStoreId(resourceStoreTimesDtoAs.get(0).getStoreId());
        resourceStoreTimesPo.setStock(allocationStock * -1 + "");// 这里去扣出
        resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);
        // todo -------------------------------------------------原仓库中做扣除 (end)-----------------------------------------------------//

        // todo -------------------------------------------------目标仓库中做增加 (start)-----------------------------------------------------//
        //todo 查询目标仓库中 库存
        resourceStoreDto = new ResourceStoreDto();
        resourceStoreDto.setResCode(resourceStoreDtoAs.get(0).getResCode());
        resourceStoreDto.setShId(tmpAllocationStorehouseDto.getShIdz());
        List<ResourceStoreDto> resourceStoreDtoZs = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
        //todo 这种场景不存在
        if (resourceStoreDtoZs == null || resourceStoreDtoZs.size() < 1) {
            //todo 添加物品到该仓库
            ResourceStorePo tmpResourceStorePo = BeanConvertUtil.covertBean(resourceStoreDtoAs.get(0), ResourceStorePo.class);
            tmpResourceStorePo.setResId(GenerateCodeFactory.getGeneratorId("11"));
            tmpResourceStorePo.setStock(allocationStock + "");
            tmpResourceStorePo.setShId(tmpAllocationStorehouseDto.getShIdz());
            tmpResourceStorePo.setCommunityId(targetStorehouseDtos.get(0).getCommunityId());
            BigDecimal miniUnitStock = new BigDecimal(resourceStoreDtoAs.get(0).getMiniUnitStock()); //获取最小计量单位数量
            BigDecimal stock = new BigDecimal(tmpResourceStorePo.getStock()); //获取物品库存
            BigDecimal miniStock = miniUnitStock.multiply(stock); //计算最小计量单位总数
            tmpResourceStorePo.setMiniStock(String.valueOf(miniStock));
            resourceStoreV1InnerServiceSMOImpl.saveResourceStore(tmpResourceStorePo);
            // todo 添加 times
            ResourceStoreTimesPo tmpResourceStoreTimesPo = BeanConvertUtil.covertBean(resourceStoreTimesDtoAs.get(0), ResourceStoreTimesPo.class);
            tmpResourceStoreTimesPo.setTimesId(GenerateCodeFactory.getGeneratorId("11"));
            tmpResourceStoreTimesPo.setStock(allocationStock + "");
            tmpResourceStoreTimesPo.setShId(tmpAllocationStorehouseDto.getShIdz());
            tmpResourceStoreTimesPo.setCommunityId(targetStorehouseDtos.get(0).getCommunityId());
            resourceStoreTimesV1InnerServiceSMOImpl.saveResourceStoreTimes(tmpResourceStoreTimesPo);

            //更新调拨物品id
            AllocationStorehousePo allocationStorehousePo = new AllocationStorehousePo();
            allocationStorehousePo.setAsId(tmpAllocationStorehouseDto.getAsId());
            allocationStorehousePo.setResId(tmpResourceStorePo.getResId());
            allocationStorehousePo.setStock(String.valueOf(allocationStock));
            allocationStorehouseV1InnerServiceSMOImpl.updateAllocationStorehouse(allocationStorehousePo);

            //加入被调拨记录明细
            AllocationStorehouseDto allocationStorehouseDto1 = tmpAllocationStorehouseDto;
            allocationStorehouseDto1.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
            allocationStorehouseDto1.setApplyId(tmpAllocationStorehouseDto.getApplyIda());
            allocationStorehouseDto1.setbId("-1");
            allocationStorehouseDto1.setStock(String.valueOf(allocationStock));
            allocationStorehouseDto1.setResId(resourceStorePo.getResId());
            allocationStorehouseDto1.setCreateTime(new Date());
            allocationStorehouseInnerServiceSMOImpl.saveAllocationStorehouses(allocationStorehouseDto1);


            return;
        }
        resourceStorePo = new ResourceStorePo();
        resourceStorePo.setResId(resourceStoreDtoZs.get(0).getResId());
        resourceStorePo.setStoreId(resourceStoreDtoZs.get(0).getStoreId());
        resourceStorePo.setStock(allocationStock + "");// 这里去添加到目标仓库
        resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);
        resourceStoreTimesPo = new ResourceStoreTimesPo();
        resourceStoreTimesPo.setPrice(resourceStoreTimesDtoAs.get(0).getPrice());
        resourceStoreTimesPo.setShId(resourceStoreDtoZs.get(0).getShId());
        resourceStoreTimesPo.setResCode(resourceStoreTimesDtoAs.get(0).getResCode());
        resourceStoreTimesPo.setStoreId(resourceStoreTimesDtoAs.get(0).getStoreId());
        resourceStoreTimesPo.setStock(allocationStock + "");// 这里去增加
        resourceStoreTimesPo.setCommunityId(resourceStoreDtoZs.get(0).getCommunityId());
        resourceStoreTimesPo.setApplyOrderId(tmpAllocationStorehouseDto.getApplyId());
        resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);


        //更新调拨物品id
        AllocationStorehousePo allocationStorehousePo = new AllocationStorehousePo();
        allocationStorehousePo.setAsId(tmpAllocationStorehouseDto.getAsId());
        allocationStorehousePo.setResId(resourceStorePo.getResId());
        allocationStorehousePo.setStock(String.valueOf(allocationStock));
        allocationStorehouseV1InnerServiceSMOImpl.updateAllocationStorehouse(allocationStorehousePo);
        //加入被调拨记录明细
        AllocationStorehouseDto allocationStorehouseDto2 = tmpAllocationStorehouseDto;
        allocationStorehouseDto2.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
        allocationStorehouseDto2.setApplyId(tmpAllocationStorehouseDto.getApplyIda());
        allocationStorehouseDto2.setbId("-1");
        allocationStorehouseDto2.setStock(String.valueOf(allocationStock));
        allocationStorehouseDto2.setResId(tmpAllocationStorehouseDto.getResId());
        allocationStorehouseDto2.setCreateTime(new Date());
        allocationStorehouseInnerServiceSMOImpl.saveAllocationStorehouses(allocationStorehouseDto2);

        // todo -------------------------------------------------目标仓库中做增加 (end)-----------------------------------------------------//
    }
}
