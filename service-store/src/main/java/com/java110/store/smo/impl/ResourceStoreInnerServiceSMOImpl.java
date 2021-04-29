package com.java110.store.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
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
        for (ResourceStoreDto resourceStoreDto : resourceResourceStores) {
            //获取资源id
            String resId = resourceStoreDto.getResId();
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(resId);
            //查询文件表
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos != null && fileRelDtos.size() > 0) {
                List<String> fileUrls = new ArrayList<>();
                for (FileRelDto fileRel : fileRelDtos) {
                    String url = "/callComponent/download/getFile/file?fileId=" + fileRel.getFileRealName() + "&communityId=-1";
                    fileUrls.add(url);
                }
                resourceStoreDto.setFileUrls(fileUrls);
            }
            resourceStoreDtos.add(resourceStoreDto);
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
            Double stock = Double.parseDouble(stores.get(0).get("stock").toString());
            Double newStock = Double.parseDouble(resourceStorePo.getStock());
            Double totalStock = stock + newStock;
            if (totalStock < 0) {
                throw new IllegalArgumentException("库存不足，参数有误");
            }
            //获取原均价
            Object averageOldPrice = stores.get(0).get("averagePrice");
            Double price = 0.0;
            if (averageOldPrice != null) {
                price = Double.parseDouble(averageOldPrice.toString());
            }
            //获取现在采购的价格
            Double newPrice = Double.parseDouble(resourceStorePo.getPurchasePrice());
            //获取均价
            double averagePrice = ((newPrice * newStock) + (price * stock)) / totalStock;
            BigDecimal b0 = new BigDecimal(averagePrice);
            //四舍五入保留两位
            double f0 = b0.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            resourceStorePo.setAveragePrice(String.valueOf(f0));
            resourceStorePo.setStock(totalStock + "");
            resourceStorePo.setStatusCd("0");
            return resourceResourceStoreServiceDaoImpl.updateResourceStoreInfoInstance(BeanConvertUtil.beanCovertMap(resourceStorePo));
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
    }

    public IResourceStoreServiceDao getResourceStoreServiceDaoImpl() {
        return resourceResourceStoreServiceDaoImpl;
    }

    public void setResourceStoreServiceDaoImpl(IResourceStoreServiceDao resourceResourceStoreServiceDaoImpl) {
        this.resourceResourceStoreServiceDaoImpl = resourceResourceStoreServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
