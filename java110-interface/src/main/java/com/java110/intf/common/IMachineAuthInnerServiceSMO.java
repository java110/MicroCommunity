package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.machineAuth.MachineAuthDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IMachineAuthInnerServiceSMO
 * @Description 设备权限接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/machineAuthApi")
public interface IMachineAuthInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param machineAuthDto 数据对象分享
     * @return MachineAuthDto 对象数据
     */
    @RequestMapping(value = "/queryMachineAuths", method = RequestMethod.POST)
    List<MachineAuthDto> queryMachineAuths(@RequestBody MachineAuthDto machineAuthDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param machineAuthDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryMachineAuthsCount", method = RequestMethod.POST)
    int queryMachineAuthsCount(@RequestBody MachineAuthDto machineAuthDto);
}
