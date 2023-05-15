package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.machine.MachineTranslateErrorDto;
import com.java110.po.machineTranslateError.MachineTranslateErrorPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IMachineTranslateErrorInnerServiceSMO
 * @Description IOT同步错误日志记录接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/machineTranslateErrorApi")
public interface IMachineTranslateErrorInnerServiceSMO {


    @RequestMapping(value = "/saveMachineTranslateError", method = RequestMethod.POST)
    public int saveMachineTranslateError(@RequestBody MachineTranslateErrorPo machineTranslateErrorPo);

    @RequestMapping(value = "/updateMachineTranslateError", method = RequestMethod.POST)
    public int updateMachineTranslateError(@RequestBody MachineTranslateErrorPo machineTranslateErrorPo);

    @RequestMapping(value = "/deleteMachineTranslateError", method = RequestMethod.POST)
    public int deleteMachineTranslateError(@RequestBody MachineTranslateErrorPo machineTranslateErrorPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param machineTranslateErrorDto 数据对象分享
     * @return MachineTranslateErrorDto 对象数据
     */
    @RequestMapping(value = "/queryMachineTranslateErrors", method = RequestMethod.POST)
    List<MachineTranslateErrorDto> queryMachineTranslateErrors(@RequestBody MachineTranslateErrorDto machineTranslateErrorDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param machineTranslateErrorDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryMachineTranslateErrorsCount", method = RequestMethod.POST)
    int queryMachineTranslateErrorsCount(@RequestBody MachineTranslateErrorDto machineTranslateErrorDto);
}
