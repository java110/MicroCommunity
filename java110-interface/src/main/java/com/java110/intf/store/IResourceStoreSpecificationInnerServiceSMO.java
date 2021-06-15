package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.resourceStoreSpecification.ResourceStoreSpecificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * @ClassName IResourceStoreSpecificationInnerServiceSMO
 * @Description 物品规格接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/resourceResourceStoreSpecificationSpecificationApi")
public interface IResourceStoreSpecificationInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param resourceResourceStoreSpecificationSpecificationDto 数据对象分享
     * @return ResourceStoreSpecificationDto 对象数据
     */
    @RequestMapping(value = "/queryResourceStoreSpecifications", method = RequestMethod.POST)
    List<ResourceStoreSpecificationDto> queryResourceStoreSpecifications(@RequestBody ResourceStoreSpecificationDto resourceResourceStoreSpecificationSpecificationDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param resourceResourceStoreSpecificationSpecificationDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryResourceStoreSpecificationsCount", method = RequestMethod.POST)
    int queryResourceStoreSpecificationsCount(@RequestBody ResourceStoreSpecificationDto resourceResourceStoreSpecificationSpecificationDto);
}
