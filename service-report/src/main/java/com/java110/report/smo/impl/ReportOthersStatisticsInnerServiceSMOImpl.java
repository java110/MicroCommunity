package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IReportInoutStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportOthersStatisticsInnerServiceSMO;
import com.java110.report.dao.IReportInoutStatisticsServiceDao;
import com.java110.report.dao.IReportOthersStatisticsServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ReportOthersStatisticsInnerServiceSMOImpl
 * @Description 其他类统计类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportOthersStatisticsInnerServiceSMOImpl extends BaseServiceSMO implements IReportOthersStatisticsInnerServiceSMO {

    @Autowired
    private IReportOthersStatisticsServiceDao reportOthersStatisticsServiceDaoImpl;



    @Override
    public long venueReservationCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportOthersStatisticsServiceDaoImpl.venueReservationCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long contractCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportOthersStatisticsServiceDaoImpl.contractCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long contractChangeCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportOthersStatisticsServiceDaoImpl.contractChangeCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long leaseChangeCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportOthersStatisticsServiceDaoImpl.leaseChangeCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long mainChange(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportOthersStatisticsServiceDaoImpl.mainChange(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long expirationContract(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportOthersStatisticsServiceDaoImpl.expirationContract(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long carCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportOthersStatisticsServiceDaoImpl.carCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long carApplyCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportOthersStatisticsServiceDaoImpl.carApplyCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double buyParkingCouponCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOthersStatisticsServiceDaoImpl.buyParkingCouponCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long writeOffParkingCouponCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportOthersStatisticsServiceDaoImpl.writeOffParkingCouponCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double sendCouponCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOthersStatisticsServiceDaoImpl.sendCouponCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long writeOffCouponCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportOthersStatisticsServiceDaoImpl.writeOffCouponCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double sendIntegralCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOthersStatisticsServiceDaoImpl.sendIntegralCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double writeOffIntegralCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOthersStatisticsServiceDaoImpl.writeOffIntegralCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }
}

