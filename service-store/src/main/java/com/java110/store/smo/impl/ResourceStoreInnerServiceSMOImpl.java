package com.java110.store.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.store.dao.IResourceStoreServiceDao;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 资源内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ResourceStoreInnerServiceSMOImpl extends BaseServiceSMO implements IResourceStoreInnerServiceSMO {

    @Autowired
    private IResourceStoreServiceDao resourceResourceStoreServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Override
    public List<ResourceStoreDto> queryResourceStores(@RequestBody ResourceStoreDto resourceResourceStoreDto) {
        //校验是否传了 分页信息
        int page = resourceResourceStoreDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            resourceResourceStoreDto.setPage((page - 1) * resourceResourceStoreDto.getRow());
        }
        List<ResourceStoreDto> resourceResourceStores = BeanConvertUtil.covertBeanList(resourceResourceStoreServiceDaoImpl.getResourceStoreInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreDto)), ResourceStoreDto.class);
        //获取图片地址
        List<ResourceStoreDto> resourceStoreDtos = new ArrayList<>();
        List<String> resCodes = new ArrayList<>();
        for (ResourceStoreDto resourceStoreDto : resourceResourceStores) {

            resCodes.add(resourceStoreDto.getResCode());
            //获取资源id
            String resId = resourceStoreDto.getResId();
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(resId);
            //查询文件表
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos != null && fileRelDtos.size() > 0) {
                List<String> fileUrls = new ArrayList<>();
                for (FileRelDto fileRel : fileRelDtos) {
                    fileUrls.add(fileRel.getFileRealName());
                }
                resourceStoreDto.setFileUrls(fileUrls);
            }
            resourceStoreDtos.add(resourceStoreDto);


            ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
            resourceStoreTimesDto.setStoreId(resourceResourceStoreDto.getStoreId());
            resourceStoreTimesDto.setResCode(resourceStoreDto.getResCode());
            resourceStoreTimesDto.setShId(resourceStoreDto.getShId());
            List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
            resourceStoreDto.setTimes(resourceStoreTimesDtos);
        }
        return resourceStoreDtos;
    }


    @Override
    public int queryResourceStoresCount(@RequestBody ResourceStoreDto resourceResourceStoreDto) {
        return resourceResourceStoreServiceDaoImpl.queryResourceStoresCount(BeanConvertUtil.beanCovertMap(resourceResourceStoreDto));
    }

    @Override
    public int updateResourceStore(@RequestBody ResourceStorePo resourceStorePo) {
        //查询
        //开始锁代码
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + resourceStorePo.getResId();
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            Map info = new HashMap<>();
            info.put("resId", resourceStorePo.getResId());
            info.put("storeId", resourceStorePo.getStoreId());
            List<Map> stores = resourceResourceStoreServiceDaoImpl.getResourceStoreInfo(info);
            Assert.listOnlyOne(stores, "不存在该物品");
            BigDecimal stock = new BigDecimal(stores.get(0).get("stock").toString());
            BigDecimal newStock = new BigDecimal(resourceStorePo.getStock().toString());
            BigDecimal totalStock = stock.add(newStock);
            BigDecimal zeroStock = new BigDecimal(0);
            if (totalStock.compareTo(zeroStock) == -1) {
                throw new IllegalArgumentException("库存不足，参数有误");
            }
            //入库操作 对物品进行加权平均
            if (resourceStorePo.getResOrderType().equals(PurchaseApplyDto.RES_ORDER_TYPE_ENTER)
                    || (resourceStorePo.getResOrderType().equals(PurchaseApplyDto.WAREHOUSING_TYPE_URGENT) && resourceStorePo.getOperationType().equals(PurchaseApplyDto.WEIGHTED_MEAN_TRUE))) {
                //获取原均价
                Object averageOldPrice = stores.get(0).get("averagePrice");
                BigDecimal price = new BigDecimal(0);
                if (averageOldPrice != null) {
                    price = new BigDecimal(averageOldPrice.toString());
                }
                //获取现在采购的价格
                BigDecimal newPrice = new BigDecimal(resourceStorePo.getPurchasePrice());
                //获取均价
                BigDecimal averagePriceTotal = ((newPrice.multiply(newStock)).add(price.multiply(stock)));
                BigDecimal averagePrice = averagePriceTotal.divide(totalStock, 2, BigDecimal.ROUND_HALF_UP);
                resourceStorePo.setAveragePrice(averagePrice.toString());
            }
            if (resourceStorePo.getResOrderType().equals(PurchaseApplyDto.WAREHOUSING_TYPE_URGENT) && resourceStorePo.getOperationType().equals(PurchaseApplyDto.WEIGHTED_MEAN_TRUE)) {
                resourceStorePo.setStock(stock + "");
            } else {
                resourceStorePo.setStock(totalStock + "");
            }
            resourceStorePo.setStatusCd("0");
            return resourceResourceStoreServiceDaoImpl.updateResourceStoreInfoInstance(BeanConvertUtil.beanCovertMap(resourceStorePo));
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
    }

    /**
     * 新增物品
     *
     * @param resourceStoreDto
     */
    @Override
    public void saveResourceStore(@RequestBody ResourceStoreDto resourceStoreDto) {
        resourceResourceStoreServiceDaoImpl.saveResourceStoreInfo(BeanConvertUtil.beanCovertMap(resourceStoreDto));
    }

    public IResourceStoreServiceDao getResourceStoreServiceDaoImpl() {
        return resourceResourceStoreServiceDaoImpl;
    }

    public void setResourceStoreServiceDaoImpl(IResourceStoreServiceDao resourceResourceStoreServiceDaoImpl) {
        this.resourceResourceStoreServiceDaoImpl = resourceResourceStoreServiceDaoImpl;
    }

    @Override
    public String queryResourceStoresTotalPrice(@RequestBody ResourceStoreDto resourceResourceStoreDto) {
        //校验是否传了 分页信息
        int page = resourceResourceStoreDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            resourceResourceStoreDto.setPage((page - 1) * resourceResourceStoreDto.getRow());
        }
        return resourceResourceStoreServiceDaoImpl.queryResourceStoresTotalPrice(BeanConvertUtil.beanCovertMap(resourceResourceStoreDto));

    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
