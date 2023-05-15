package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.machine.CarInoutDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ICarInoutInnerServiceSMO
 * @Description 进出场接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/carInoutApi")
public interface ICarInoutInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param carInoutDto 数据对象分享
     * @return CarInoutDto 对象数据
     */
    @RequestMapping(value = "/queryCarInouts", method = RequestMethod.POST)
    List<CarInoutDto> queryCarInouts(@RequestBody CarInoutDto carInoutDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param carInoutDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryCarInoutsCount", method = RequestMethod.POST)
    int queryCarInoutsCount(@RequestBody CarInoutDto carInoutDto);
}
