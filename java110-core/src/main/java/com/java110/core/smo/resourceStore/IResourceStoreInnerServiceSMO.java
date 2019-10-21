package com.java110.core.smo.resourceStore;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.resourceStore.ResourceStoreDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IResourceStoreInnerServiceSMO
 * @Description 资源接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/resourceResourceStoreApi")
public interface IResourceStoreInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param resourceResourceStoreDto 数据对象分享
     * @return ResourceStoreDto 对象数据
     */
    @RequestMapping(value = "/queryResourceStores", method = RequestMethod.POST)
    List<ResourceStoreDto> queryResourceStores(@RequestBody ResourceStoreDto resourceResourceStoreDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param resourceResourceStoreDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryResourceStoresCount", method = RequestMethod.POST)
    int queryResourceStoresCount(@RequestBody ResourceStoreDto resourceResourceStoreDto);
}
