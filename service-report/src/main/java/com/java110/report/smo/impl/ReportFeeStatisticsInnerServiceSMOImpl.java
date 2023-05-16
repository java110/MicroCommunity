package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.report.dao.IReportFeeStatisticsServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ReportFeeStatisticsInnerServiceSMOImpl
 * @Description 费用统计类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportFeeStatisticsInnerServiceSMOImpl extends BaseServiceSMO implements IReportFeeStatisticsInnerServiceSMO {

    @Autowired
    private IReportFeeStatisticsServiceDao reportFeeStatisticsServiceDaoImpl;

    /**
     * 查询历史欠费
     *
     * @param queryFeeStatisticsDto 数据对象分享
     * @return
     */
    @Override
    public double getHisMonthOweFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getHisMonthOweFee(BeanConvertUtil.beanCovertMap(queryFeeStatisticsDto));
        return info;
    }

    @Override
    public double getCurMonthOweFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getCurMonthOweFee(BeanConvertUtil.beanCovertMap(queryFeeStatisticsDto));
        return info;
    }

    /**
     * 查询当月应收
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getCurReceivableFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getCurReceivableFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getHisReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getHisReceivedFee(BeanConvertUtil.beanCovertMap(queryFeeStatisticsDto));
        return info;
    }

    @Override
    public double getPreReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getPreReceivedFee(BeanConvertUtil.beanCovertMap(queryFeeStatisticsDto));
        return info;
    }

    @Override
    public double getReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getReceivedFee(BeanConvertUtil.beanCovertMap(queryFeeStatisticsDto));
        return info;
    }

    /**
     * 查询欠费户数
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public int getOweRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        int info = reportFeeStatisticsServiceDaoImpl.getOweRoomCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    /**
     * 查询收费房屋数
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public long getFeeRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        int info = reportFeeStatisticsServiceDaoImpl.getFeeRoomCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    /**
     * 楼栋收费率信息统计
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public List<Map> getFloorFeeSummary(QueryStatisticsDto queryStatisticsDto) {

        List<Map> info = reportFeeStatisticsServiceDaoImpl.getFloorFeeSummary(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }
}
