package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.dto.report.ReportFloorFeeStatisticsDto;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFloorFeeStatisticsInnerServiceSMO;
import com.java110.report.dao.IReportFeeStatisticsServiceDao;
import com.java110.report.dao.IReportFloorFeeStatisticsServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
public class ReportFloorFeeStatisticsInnerServiceSMOImpl extends BaseServiceSMO implements IReportFloorFeeStatisticsInnerServiceSMO {

    @Autowired
    private IReportFloorFeeStatisticsServiceDao reportFloorFeeStatisticsServiceDaoImpl;



    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorOweRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> data = reportFloorFeeStatisticsServiceDaoImpl.getFloorOweRoomCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return BeanConvertUtil.covertBeanList(data, ReportFloorFeeStatisticsDto.class);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorFeeRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> data = reportFloorFeeStatisticsServiceDaoImpl.getFloorFeeRoomCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return BeanConvertUtil.covertBeanList(data, ReportFloorFeeStatisticsDto.class);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorReceivedFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> data = reportFloorFeeStatisticsServiceDaoImpl.getFloorReceivedFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return BeanConvertUtil.covertBeanList(data, ReportFloorFeeStatisticsDto.class);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorPreReceivedFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> data = reportFloorFeeStatisticsServiceDaoImpl.getFloorPreReceivedFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return BeanConvertUtil.covertBeanList(data, ReportFloorFeeStatisticsDto.class);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorHisOweFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> data = reportFloorFeeStatisticsServiceDaoImpl.getFloorHisOweFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return BeanConvertUtil.covertBeanList(data, ReportFloorFeeStatisticsDto.class);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorCurReceivableFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> data = reportFloorFeeStatisticsServiceDaoImpl.getFloorCurReceivableFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return BeanConvertUtil.covertBeanList(data, ReportFloorFeeStatisticsDto.class);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorCurReceivedFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> data = reportFloorFeeStatisticsServiceDaoImpl.getFloorCurReceivedFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return BeanConvertUtil.covertBeanList(data, ReportFloorFeeStatisticsDto.class);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorHisReceivedFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> data = reportFloorFeeStatisticsServiceDaoImpl.getFloorHisReceivedFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return BeanConvertUtil.covertBeanList(data, ReportFloorFeeStatisticsDto.class);
    }
}
