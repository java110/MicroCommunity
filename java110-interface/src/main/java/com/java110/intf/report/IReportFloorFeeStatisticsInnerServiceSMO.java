package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.dto.report.ReportFloorFeeStatisticsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IReportFeeStatisticsInnerServiceSMO
 * @Description 费用统计类 服务类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportFloorFeeStatisticsApi")
public interface IReportFloorFeeStatisticsInnerServiceSMO {




    /**
     * 查询   //todo 欠费房屋数 oweRoomCount
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getFloorOweRoomCount", method = RequestMethod.POST)
    List<ReportFloorFeeStatisticsDto> getFloorOweRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询   //todo 收费房屋数 feeRoomCount
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getFloorFeeRoomCount", method = RequestMethod.POST)
    List<ReportFloorFeeStatisticsDto> getFloorFeeRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询    //todo 实收金额 receivedFee
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getFloorReceivedFee", method = RequestMethod.POST)
    List<ReportFloorFeeStatisticsDto> getFloorReceivedFee(@RequestBody QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询    //todo 预收金额 preReceivedFee
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getFloorPreReceivedFee", method = RequestMethod.POST)
    List<ReportFloorFeeStatisticsDto> getFloorPreReceivedFee(@RequestBody QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询     //todo 历史欠费金额 hisOweFee
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getFloorHisOweFee", method = RequestMethod.POST)
    List<ReportFloorFeeStatisticsDto> getFloorHisOweFee(@RequestBody QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询   //todo 当期应收金额 curReceivableFee
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getFloorCurReceivableFee", method = RequestMethod.POST)
    List<ReportFloorFeeStatisticsDto> getFloorCurReceivableFee(@RequestBody QueryStatisticsDto queryStatisticsDto);






    /**
     * 查询   //todo 当期实收金额 curReceivedFee
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getFloorCurReceivedFee", method = RequestMethod.POST)
    List<ReportFloorFeeStatisticsDto> getFloorCurReceivedFee(@RequestBody QueryStatisticsDto queryStatisticsDto);



    /**
     * 查询  //todo 欠费追回 hisReceivedFee
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getFloorHisReceivedFee", method = RequestMethod.POST)
    List<ReportFloorFeeStatisticsDto> getFloorHisReceivedFee(@RequestBody QueryStatisticsDto queryStatisticsDto);
}
