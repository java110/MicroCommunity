package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.machine.MachineTranslateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IMachineTranslateInnerServiceSMO
 * @Description 设备同步接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/machineTranslateApi")
public interface IMachineTranslateInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param machineTranslateDto 数据对象分享
     * @return MachineTranslateDto 对象数据
     */
    @RequestMapping(value = "/queryMachineTranslates", method = RequestMethod.POST)
    List<MachineTranslateDto> queryMachineTranslates(@RequestBody MachineTranslateDto machineTranslateDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param machineTranslateDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryMachineTranslatesCount", method = RequestMethod.POST)
    int queryMachineTranslatesCount(@RequestBody MachineTranslateDto machineTranslateDto);


    @RequestMapping(value = "/updateMachineTranslateState", method = RequestMethod.POST)
    int updateMachineTranslateState(@RequestBody MachineTranslateDto machineTranslateDto);


    @RequestMapping(value = "/saveMachineTranslate", method = RequestMethod.POST)
    int saveMachineTranslate(@RequestBody MachineTranslateDto machineTranslateDto);
}
