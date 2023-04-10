package com.java110.report.dao;

import com.java110.dto.report.ReportCarDto;
import com.java110.dto.report.ReportRoomDto;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ICommunityServiceDao
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 22:10
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
public interface IReportCommunityServiceDao {

    /**
     * 查询房屋个数
     *
     * @param reportRoomDto
     * @return
     */
    int getRoomCount(ReportRoomDto reportRoomDto);

    /**
     * 查询 房屋 楼栋 单元 和 业主 信息
     *
     * @return
     */
    List<ReportRoomDto> getRoomFloorUnitAndOwner(ReportRoomDto reportRoomDto);


    /**
     * 查询房屋个数
     *
     * @param reportCarDto
     * @return
     */
    int getCarCount(ReportCarDto reportCarDto);

    /**
     * 查询 房屋 楼栋 单元 和 业主 信息
     *
     * @return
     */
    List<ReportCarDto> getCarParkingSpace(ReportCarDto reportCarDto);

    /**
     * 查询 房屋 楼栋 单元 和 业主 信息
     *
     * @return
     */
    List<Map> getCommunitys(Map communityDto);

    /**
     * 查询房屋结构化数据
     *
     * @param info
     * @return
     */
    List<Map> queryRoomStructures(Map info);

    /**
     * 查询车位 结构化数据
     *
     * @param info
     * @return
     */
    List<Map> queryCarStructures(Map info);

    int deleteInvalidFee(Map info);

    /**
     * 查询无效的数据
     * @param reportFeeDto
     * @return
     */
    List<Map> queryInvalidFeeMonthStatistics(Map reportFeeDto);


    List<Map>  queryRoomsTree(Map info);

    /**
     * 查询车辆变更记录
     * @param info
     * @return
     */
    int queryHisOwnerCarCount(Map info);

    /**
     * 车辆变更记录
     * @param info
     * @return
     */
    List<Map> queryHisOwnerCars(Map info);

    int queryHisOwnerCount(Map info);

    List<Map> queryHisOwners(Map info);

    int queryHisFeeCount(Map info);

    List<Map> queryHisFees(Map info);
}
