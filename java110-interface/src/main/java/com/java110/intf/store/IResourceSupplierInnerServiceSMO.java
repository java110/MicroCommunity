package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.resourceSupplier.ResourceSupplierDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IResourceSupplierInnerServiceSMO
 * @Description 物品供应商接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/resourceSupplierApi")
public interface IResourceSupplierInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param resourceSupplierDto 数据对象分享
     * @return ResourceSupplierDto 对象数据
     */
    @RequestMapping(value = "/queryResourceSuppliers", method = RequestMethod.POST)
    List<ResourceSupplierDto> queryResourceSuppliers(@RequestBody ResourceSupplierDto resourceSupplierDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param resourceSupplierDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryResourceSuppliersCount", method = RequestMethod.POST)
    int queryResourceSuppliersCount(@RequestBody ResourceSupplierDto resourceSupplierDto);
}
