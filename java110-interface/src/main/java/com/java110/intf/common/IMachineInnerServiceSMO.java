package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.machine.MachineDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IMachineInnerServiceSMO
 * @Description 设备接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/machineApi")
public interface IMachineInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param machineDto 数据对象分享
     * @return MachineDto 对象数据
     */
    @RequestMapping(value = "/queryMachines", method = RequestMethod.POST)
    List<MachineDto> queryMachines(@RequestBody MachineDto machineDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param machineDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryMachinesCount", method = RequestMethod.POST)
    int queryMachinesCount(@RequestBody MachineDto machineDto);


    /**
     * <p>查询小区楼信息</p>
     *
     * @param machineDto 数据对象分享
     * @return MachineDto 对象数据
     */
    @RequestMapping(value = "/updateMachineState", method = RequestMethod.POST)
    int updateMachineState(@RequestBody MachineDto machineDto);
}
