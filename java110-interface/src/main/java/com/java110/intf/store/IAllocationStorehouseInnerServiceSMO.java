package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAllocationStorehouseInnerServiceSMO
 * @Description 仓库调拨接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/allocationStorehouseApi")
public interface IAllocationStorehouseInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param allocationStorehouseDto 数据对象分享
     * @return AllocationStorehouseDto 对象数据
     */
    @RequestMapping(value = "/queryAllocationStorehouses", method = RequestMethod.POST)
    List<AllocationStorehouseDto> queryAllocationStorehouses(@RequestBody AllocationStorehouseDto allocationStorehouseDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param allocationStorehouseDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAllocationStorehousesCount", method = RequestMethod.POST)
    int queryAllocationStorehousesCount(@RequestBody AllocationStorehouseDto allocationStorehouseDto);

    /**
     * 新增调拨记录
     *
     * @param allocationStorehouseDto
     */
    @RequestMapping(value = "/saveAllocationStorehouses", method = RequestMethod.POST)
    void saveAllocationStorehouses(@RequestBody AllocationStorehouseDto allocationStorehouseDto);
}
