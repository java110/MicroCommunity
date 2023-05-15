package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.allocationStorehouse.AllocationStorehouseApplyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAllocationStorehouseApplyInnerServiceSMO
 * @Description 调拨申请接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/allocationStorehouseApplyApi")
public interface IAllocationStorehouseApplyInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param allocationAllocationStorehouseApplyhouseApplyDto 数据对象分享
     * @return AllocationStorehouseApplyDto 对象数据
     */
    @RequestMapping(value = "/queryAllocationStorehouseApplys", method = RequestMethod.POST)
    List<AllocationStorehouseApplyDto> queryAllocationStorehouseApplys(@RequestBody AllocationStorehouseApplyDto allocationAllocationStorehouseApplyhouseApplyDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param allocationAllocationStorehouseApplyhouseApplyDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAllocationStorehouseApplysCount", method = RequestMethod.POST)
    int queryAllocationStorehouseApplysCount(@RequestBody AllocationStorehouseApplyDto allocationAllocationStorehouseApplyhouseApplyDto);

    /**
     * 添加调拨申请记录
     *
     * @param allocationStorehouseApplyDto
     */
    @RequestMapping(value = "/saveAllocationStorehouseApplys", method = RequestMethod.POST)
    void saveAllocationStorehouseApplys(@RequestBody AllocationStorehouseApplyDto allocationStorehouseApplyDto);
}
