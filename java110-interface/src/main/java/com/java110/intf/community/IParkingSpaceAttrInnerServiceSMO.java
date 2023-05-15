package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.parking.ParkingSpaceAttrDto;
import com.java110.po.parkingSpaceAttr.ParkingSpaceAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IParkingSpaceAttrInnerServiceSMO
 * @Description 车位属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/parkingSpaceAttrApi")
public interface IParkingSpaceAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param parkingSpaceAttrDto 数据对象分享
     * @return ParkingSpaceAttrDto 对象数据
     */
    @RequestMapping(value = "/queryParkingSpaceAttrs", method = RequestMethod.POST)
    List<ParkingSpaceAttrDto> queryParkingSpaceAttrs(@RequestBody ParkingSpaceAttrDto parkingSpaceAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param parkingSpaceAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryParkingSpaceAttrsCount", method = RequestMethod.POST)
    int queryParkingSpaceAttrsCount(@RequestBody ParkingSpaceAttrDto parkingSpaceAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param parkingSpaceAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveParkingSpaceAttr", method = RequestMethod.POST)
    public int saveParkingSpaceAttr(@RequestBody ParkingSpaceAttrPo parkingSpaceAttrPo);
}
