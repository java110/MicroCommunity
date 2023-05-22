package com.java110.report.statistics;

import com.java110.dto.RoomDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.report.QueryStatisticsDto;

import java.util.List;

/**
 * 基础数据统计
 */
public interface IBaseDataStatistics {

    /**
     * 查询房屋数
     * @param queryStatisticsDto
     * @return
     */
    long getRoomCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询房屋
     * @param queryStatisticsDto
     * @return
     */
    List<RoomDto> getRoomInfo(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询空闲房屋数
     * @param queryStatisticsDto
     * @return
     */
    long getFreeRoomCount(QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询 业主数量
     * @param queryStatisticsDto
     * @return
     */
    long getOwnerCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询业主信息
     * @param queryStatisticsDto
     * @return
     */
    List<OwnerDto> getOwnerInfo(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询合同总数
     * @param queryStatisticsDto
     * @return
     */
    long getContractCount(QueryStatisticsDto queryStatisticsDto);

    List<ContractDto> getContract(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询车辆数量
     * @param queryStatisticsDto
     * @return
     */
    long getCarCount(QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询车辆
     * @param queryStatisticsDto
     * @return
     */
    List<OwnerCarDto> getCar(QueryStatisticsDto queryStatisticsDto);
}
