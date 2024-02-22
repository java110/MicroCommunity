package com.java110.report.statistics;

import com.java110.dto.report.QueryStatisticsDto;
import com.java110.dto.report.ReportFloorFeeStatisticsDto;

import java.util.List;
import java.util.Map;

/**
 * 楼栋费用统计
 */
public interface IFloorFeeStatistics {

    /**
     * 查询   //todo 欠费房屋数 oweRoomCount
     * @param queryStatisticsDto
     * @return
     */
    List<ReportFloorFeeStatisticsDto> getFloorOweRoomCount(QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询   //todo 收费房屋数 feeRoomCount
     * @param queryStatisticsDto
     * @return
     */
    List<ReportFloorFeeStatisticsDto> getFloorFeeRoomCount(QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询    //todo 实收金额 receivedFee
     * @param queryStatisticsDto
     * @return
     */
    List<ReportFloorFeeStatisticsDto> getFloorReceivedFee(QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询    //todo 预收金额 preReceivedFee
     * @param queryStatisticsDto
     * @return
     */
    List<ReportFloorFeeStatisticsDto> getFloorPreReceivedFee(QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询     //todo 历史欠费金额 hisOweFee
     * @param queryStatisticsDto
     * @return
     */
    List<ReportFloorFeeStatisticsDto> getFloorHisOweFee(QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询   //todo 当期应收金额 curReceivableFee
     * @param queryStatisticsDto
     * @return
     */
    List<ReportFloorFeeStatisticsDto> getFloorCurReceivableFee(QueryStatisticsDto queryStatisticsDto);






    /**
     * 查询   //todo 当期实收金额 curReceivedFee
     * @param queryStatisticsDto
     * @return
     */
    List<ReportFloorFeeStatisticsDto> getFloorCurReceivedFee(QueryStatisticsDto queryStatisticsDto);



    /**
     * 查询  //todo 欠费追回 hisReceivedFee
     * @param queryStatisticsDto
     * @return
     */
    List<ReportFloorFeeStatisticsDto> getFloorHisReceivedFee(QueryStatisticsDto queryStatisticsDto);











}
