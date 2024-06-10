package com.java110.intf.job;

import com.alibaba.fastjson.JSONObject;
import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.IotDataDto;
import com.java110.dto.MallDataDto;
import com.java110.dto.user.UserDto;
import com.java110.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName ITaskInnerServiceSMO
 * @Description dataBus统一处理类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "job-service", configuration = {FeignConfiguration.class})
@RequestMapping("/mallApi")
public interface IMallInnerServiceSMO {


    /**
     * <p>重启设备</p>
     *
     * @param reqJson 请求信息
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/postMall", method = RequestMethod.POST)
    ResultVo postMall(@RequestBody JSONObject reqJson);

    /**
     * <p>重启设备</p>
     *
     * @param mallDataDto 请求信息
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/postMallData", method = RequestMethod.POST)
    ResultVo postMallData(@RequestBody MallDataDto mallDataDto);


    @RequestMapping(value = "/generatorMallCode", method = RequestMethod.POST)
    String generatorMallCode(@RequestBody UserDto userDto);

    @RequestMapping(value = "/sendUserInfo", method = RequestMethod.POST)
    ResultVo sendUserInfo(@RequestBody UserDto userDto);
}
