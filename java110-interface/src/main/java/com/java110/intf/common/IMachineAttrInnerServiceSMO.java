package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.machine.MachineAttrDto;
import com.java110.po.machine.MachineAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IMachineAttrInnerServiceSMO
 * @Description 设备属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/machineAttrApi")
public interface IMachineAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param machineAttrDto 数据对象分享
     * @return MachineAttrDto 对象数据
     */
    @RequestMapping(value = "/queryMachineAttrs", method = RequestMethod.POST)
    List<MachineAttrDto> queryMachineAttrs(@RequestBody MachineAttrDto machineAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param machineAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryMachineAttrsCount", method = RequestMethod.POST)
    int queryMachineAttrsCount(@RequestBody MachineAttrDto machineAttrDto);


    /**
     * <p>查询小区楼信息</p>
     *
     * @param machineAttrPo 数据对象分享
     * @return MachineAttrDto 对象数据
     */
    @RequestMapping(value = "/saveMachineAttrs", method = RequestMethod.POST)
    int saveMachineAttrs(@RequestBody MachineAttrPo machineAttrPo);

    @RequestMapping(value = "/updateMachineAttrs", method = RequestMethod.POST)
    int updateMachineAttrs(@RequestBody MachineAttrPo attr);
}
