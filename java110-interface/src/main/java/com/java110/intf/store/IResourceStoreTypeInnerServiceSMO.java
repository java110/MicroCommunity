package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.resourceStoreType.ResourceStoreTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IResourceStoreTypeInnerServiceSMO
 * @Description 物品类型接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/resourceResourceStoreTypeApi")
public interface IResourceStoreTypeInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param resourceResourceStoreTypeTypeDto 数据对象分享
     * @return ResourceStoreTypeDto 对象数据
     */
    @RequestMapping(value = "/queryResourceStoreTypes", method = RequestMethod.POST)
    List<ResourceStoreTypeDto> queryResourceStoreTypes(@RequestBody ResourceStoreTypeDto resourceResourceStoreTypeTypeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param resourceResourceStoreTypeTypeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryResourceStoreTypesCount", method = RequestMethod.POST)
    int queryResourceStoreTypesCount(@RequestBody ResourceStoreTypeDto resourceResourceStoreTypeTypeDto);
}
