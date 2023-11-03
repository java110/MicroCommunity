package com.java110.intf.report;

import com.alibaba.fastjson.JSONObject;
import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.room.RoomDto;
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

    /**
     * 查询实收房屋数
     * @param roomDto
     * @return
     */
    @RequestMapping(value = "/getReceivedRoomCount", method = RequestMethod.POST)
    long getReceivedRoomCount(@RequestBody RoomDto roomDto);

    /**
     * 查询实收房屋
     * @param roomDto
     * @return
     */
    @RequestMapping(value = "/getReceivedRoomInfo", method = RequestMethod.POST)
    List<RoomDto> getReceivedRoomInfo(@RequestBody RoomDto roomDto);

    @RequestMapping(value = "/getOweRoomCount", method = RequestMethod.POST)
    long getOweRoomCount(@RequestBody RoomDto roomDto);

    @RequestMapping(value = "/getOweRoomInfo", method = RequestMethod.POST)
    List<RoomDto> getOweRoomInfo(@RequestBody RoomDto roomDto);

    @RequestMapping(value = "/getCommunityFeeDetailCount", method = RequestMethod.POST)
    List<Map> getCommunityFeeDetailCount(@RequestBody  Map info);

    @RequestMapping(value = "/getCommunityRepairCount", method = RequestMethod.POST)
    List<Map> getCommunityRepairCount(@RequestBody Map info);

    @RequestMapping(value = "/getCommunityFeeDetailCountAnalysis", method = RequestMethod.POST)
    List<Map> getCommunityFeeDetailCountAnalysis(@RequestBody Map info);

    @RequestMapping(value = "/getCommunityRepairCountAnalysis", method = RequestMethod.POST)
    List<Map> getCommunityRepairCountAnalysis(@RequestBody Map info);

    @RequestMapping(value = "/getCommunityInspectionAnalysis", method = RequestMethod.POST)
    List<Map> getCommunityInspectionAnalysis(@RequestBody Map info);

    @RequestMapping(value = "/getCommunityMaintainanceAnalysis", method = RequestMethod.POST)
    List<Map> getCommunityMaintainanceAnalysis(@RequestBody Map info);

    @RequestMapping(value = "/getCommunityItemInAnalysis", method = RequestMethod.POST)
    List<Map> getCommunityItemInAnalysis(@RequestBody Map info);

    @RequestMapping(value = "/getCommunityItemOutAnalysis", method = RequestMethod.POST)
    List<Map> getCommunityItemOutAnalysis(@RequestBody Map info);

    @RequestMapping(value = "/getCommunityCarInAnalysis", method = RequestMethod.POST)
    List<Map> getCommunityCarInAnalysis(@RequestBody Map info);

    @RequestMapping(value = "/getCommunityPersonInAnalysis", method = RequestMethod.POST)
    List<Map> getCommunityPersonInAnalysis(@RequestBody Map info);

    @RequestMapping(value = "/getCommunityContractAnalysis", method = RequestMethod.POST)
    List<Map> getCommunityContractAnalysis(@RequestBody Map info);

    @RequestMapping(value = "/getPropertyFeeSummaryData", method = RequestMethod.POST)
    List<Map> getPropertyFeeSummaryData(@RequestBody Map info);


    @RequestMapping(value = "/getPropertyFeeSummaryDataCount", method = RequestMethod.POST)
    int getPropertyFeeSummaryDataCount(@RequestBody Map info);

    @RequestMapping(value = "/computeEveryMonthFee", method = RequestMethod.POST)
    List<Map> computeEveryMonthFee(@RequestBody Map info);

    @RequestMapping(value = "/getParkingFeeSummaryDataCount", method = RequestMethod.POST)
    int getParkingFeeSummaryDataCount(@RequestBody Map info);

    @RequestMapping(value = "/getParkingFeeSummaryData", method = RequestMethod.POST)
    List<Map> getParkingFeeSummaryData(@RequestBody Map info);
}
