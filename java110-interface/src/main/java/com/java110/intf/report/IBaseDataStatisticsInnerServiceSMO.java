package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.RoomDto;
import com.java110.dto.report.QueryStatisticsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IReportFeeStatisticsInnerServiceSMO
 * @Description 报表 基础数据统计表
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/baseDataStatisticsApi")
public interface IBaseDataStatisticsInnerServiceSMO {


    /**
     * <p>查询房屋总数</p>
     *
     * @param roomDto 数据对象分享
     */
    @RequestMapping(value = "/getRoomCount", method = RequestMethod.POST)
    long getRoomCount(@RequestBody RoomDto roomDto);



    /**
     * <p>查询房屋</p>
     *
     * @param roomDto 数据对象分享
     */
    @RequestMapping(value = "/getRoomInfo", method = RequestMethod.POST)
    List<RoomDto> getRoomInfo(@RequestBody RoomDto roomDto);
}
