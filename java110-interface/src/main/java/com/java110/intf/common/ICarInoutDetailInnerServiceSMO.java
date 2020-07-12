package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.machine.CarInoutDetailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ICarInoutDetailInnerServiceSMO
 * @Description 进出场详情接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/carInoutDetailApi")
public interface ICarInoutDetailInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param carInoutDetailDto 数据对象分享
     * @return CarInoutDetailDto 对象数据
     */
    @RequestMapping(value = "/queryCarInoutDetails", method = RequestMethod.POST)
    List<CarInoutDetailDto> queryCarInoutDetails(@RequestBody CarInoutDetailDto carInoutDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param carInoutDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryCarInoutDetailsCount", method = RequestMethod.POST)
    int queryCarInoutDetailsCount(@RequestBody CarInoutDetailDto carInoutDetailDto);
}
