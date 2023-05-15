package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.allocationStorehouse.AllocationUserStorehouseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAllocationUserStorehouseInnerServiceSMO
 * @Description 物品供应商接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/allocationUserAllocationUserStorehouseApi")
public interface IAllocationUserStorehouseInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param allocationUserAllocationUserStorehouseDto 数据对象分享
     * @return AllocationUserStorehouseDto 对象数据
     */
    @RequestMapping(value = "/queryAllocationUserStorehouses", method = RequestMethod.POST)
    List<AllocationUserStorehouseDto> queryAllocationUserStorehouses(@RequestBody AllocationUserStorehouseDto allocationUserAllocationUserStorehouseDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param allocationUserAllocationUserStorehouseDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAllocationUserStorehousesCount", method = RequestMethod.POST)
    int queryAllocationUserStorehousesCount(@RequestBody AllocationUserStorehouseDto allocationUserAllocationUserStorehouseDto);
}
