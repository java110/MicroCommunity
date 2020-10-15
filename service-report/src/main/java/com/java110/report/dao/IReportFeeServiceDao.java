package com.java110.report.dao;

import com.java110.dto.report.ReportFeeDto;
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
public interface IReportFeeServiceDao {

    /**
     * 查询房屋个数
     * @param reportFeeDto
     * @return
     */
    int getFeeCount(ReportFeeDto reportFeeDto);

    /**
     * 查询 房屋 楼栋 单元 和 业主 信息
     * @return
     */
    List<ReportFeeDto> getFees(ReportFeeDto reportFeeDto);
}
