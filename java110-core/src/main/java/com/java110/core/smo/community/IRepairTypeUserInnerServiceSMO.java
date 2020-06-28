package com.java110.core.smo.repairTypeUser;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.repair.RepairTypeUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRepairTypeUserInnerServiceSMO
 * @Description 报修设置接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/repairTypeUserApi")
public interface IRepairTypeUserInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param repairTypeUserDto 数据对象分享
     * @return RepairTypeUserDto 对象数据
     */
    @RequestMapping(value = "/queryRepairTypeUsers", method = RequestMethod.POST)
    List<RepairTypeUserDto> queryRepairTypeUsers(@RequestBody RepairTypeUserDto repairTypeUserDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param repairTypeUserDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRepairTypeUsersCount", method = RequestMethod.POST)
    int queryRepairTypeUsersCount(@RequestBody RepairTypeUserDto repairTypeUserDto);
}
