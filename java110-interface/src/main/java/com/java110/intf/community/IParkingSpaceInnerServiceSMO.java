package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.po.parking.ParkingSpacePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IParkingSpaceInnerServiceSMO
 * @Description 停车位接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/parkingSpaceApi")
public interface IParkingSpaceInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param parkingSpaceDto 数据对象分享
     * @return ParkingSpaceDto 对象数据
     */
    @RequestMapping(value = "/queryParkingSpaces", method = RequestMethod.POST)
    List<ParkingSpaceDto> queryParkingSpaces(@RequestBody ParkingSpaceDto parkingSpaceDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param parkingSpaceDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryParkingSpacesCount", method = RequestMethod.POST)
    int queryParkingSpacesCount(@RequestBody ParkingSpaceDto parkingSpaceDto);

    @RequestMapping(value = "/saveParkingSpace", method = RequestMethod.POST)
    int saveParkingSpace(@RequestBody ParkingSpacePo parkingSpacePo);

    @RequestMapping(value = "/updateParkingSpace", method = RequestMethod.POST)
    void updateParkingSpace(@RequestBody ParkingSpacePo parkingSpacePo);
}
