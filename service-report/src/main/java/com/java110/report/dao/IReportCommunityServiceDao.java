package com.java110.report.dao;

import com.java110.dto.report.ReportCarDto;
import com.java110.dto.report.ReportRoomDto;

import java.util.List;

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
}
