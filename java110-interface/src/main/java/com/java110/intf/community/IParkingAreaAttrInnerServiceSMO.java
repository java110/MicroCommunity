package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.parking.ParkingAreaAttrDto;
import com.java110.po.parkingAreaAttr.ParkingAreaAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IParkingAreaAttrInnerServiceSMO
 * @Description 单元属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/parkingAreaAttrApi")
public interface IParkingAreaAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param parkingAreaAttrDto 数据对象分享
     * @return ParkingAreaAttrDto 对象数据
     */
    @RequestMapping(value = "/queryParkingAreaAttrs", method = RequestMethod.POST)
    List<ParkingAreaAttrDto> queryParkingAreaAttrs(@RequestBody ParkingAreaAttrDto parkingAreaAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param parkingAreaAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryParkingAreaAttrsCount", method = RequestMethod.POST)
    int queryParkingAreaAttrsCount(@RequestBody ParkingAreaAttrDto parkingAreaAttrDto);


    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param parkingAreaAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveParkingAreaAttr", method = RequestMethod.POST)
    int saveParkingAreaAttr(@RequestBody ParkingAreaAttrPo parkingAreaAttrPo);
}
