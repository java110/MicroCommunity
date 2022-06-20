package com.java110.report.dao;

import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.report.ReportFeeDetailDto;
import com.java110.dto.report.ReportFeeDto;
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

    /**
     * 查询 房屋 楼栋 单元 和 业主 信息
     * @return
     */
    List<Map> getFeeConfigs(Map reportFeeDto);

    /**
     * 实收费用查询
     * @param reportFeeDetailDto
     * @return
     */
    double getFeeReceivedAmount(ReportFeeDetailDto reportFeeDetailDto);

}
