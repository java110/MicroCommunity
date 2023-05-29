package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IReportInoutStatisticsInnerServiceSMO;
import com.java110.report.dao.IReportInoutStatisticsServiceDao;
import com.java110.report.dao.IReportOrderStatisticsServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ReportInnerStatisticsInnerServiceSMOImpl
 * @Description 出入类统计类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportInnerStatisticsInnerServiceSMOImpl extends BaseServiceSMO implements IReportInoutStatisticsInnerServiceSMO {

    @Autowired
    private IReportInoutStatisticsServiceDao reportInoutStatisticsServiceDaoImpl;



    @Override
    public long getCarInCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.getCarInCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long getCarOutCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.getCarOutCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long getPersonInCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.getPersonInCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long getPersonFaceToMachineCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.getPersonFaceToMachineCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long purchaseInCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.purchaseInCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long purchaseOutCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.purchaseOutCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double purchaseInAmount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportInoutStatisticsServiceDaoImpl.purchaseInAmount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double purchaseOutAmount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportInoutStatisticsServiceDaoImpl.purchaseOutAmount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long allocationCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.allocationCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long roomRenovationCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.roomRenovationCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long itemReleaseCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.itemReleaseCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long roomInCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.roomInCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long roomOutCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.roomOutCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long ownerRegisterCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.ownerRegisterCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long noAttendanceCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportInoutStatisticsServiceDaoImpl.noAttendanceCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }
}

