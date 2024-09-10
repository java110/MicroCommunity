package com.java110.intf.job;

import com.alibaba.fastjson.JSONObject;
import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.IotDataDto;
import com.java110.dto.data.DatabusDataDto;
import com.java110.dto.fee.TempCarPayOrderDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.system.Business;
import com.java110.dto.user.UserDto;
import com.java110.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ITaskInnerServiceSMO
 * @Description dataBus统一处理类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "job-service", configuration = {FeignConfiguration.class})
@RequestMapping("/iotApi")
public interface IIotInnerServiceSMO {


    /**
     * <p>重启设备</p>
     *
     * @param reqJson 请求信息
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/postIot", method = RequestMethod.POST)
    ResultVo postIot(@RequestBody JSONObject reqJson);


    /**
     * <p>重启设备</p>
     *
     * @param iotDataDto 请求信息
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/postIotData", method = RequestMethod.POST)
    ResultVo postIotData(@RequestBody IotDataDto iotDataDto);

    @RequestMapping(value = "/sendUserInfo", method = RequestMethod.POST)
    ResultVo  sendUserInfo(@RequestBody UserDto userDto);
}
