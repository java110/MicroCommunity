package com.java110.report.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import com.java110.report.dao.IReportFeeMonthStatisticsServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用月统计内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportFeeMonthStatisticsInnerServiceSMOImpl extends BaseServiceSMO implements IReportFeeMonthStatisticsInnerServiceSMO {

    @Autowired
    private IReportFeeMonthStatisticsServiceDao reportFeeMonthStatisticsServiceDaoImpl;


    @Override
    public int saveReportFeeMonthStatistics(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {
        int saveFlag = 1;
        reportFeeMonthStatisticsServiceDaoImpl.saveReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        return saveFlag;
    }

    @Override
    public int updateReportFeeMonthStatistics(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {
        int saveFlag = 1;
        reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        return saveFlag;
    }

    @Override
    public int deleteReportFeeMonthStatistics(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {
        int saveFlag = 1;
        reportFeeMonthStatisticsPo.setStatusCd("1");
        reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        return saveFlag;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryReportFeeMonthStatisticss(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {

        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.getReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }


    @Override
    public int queryReportFeeMonthStatisticssCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeMonthStatisticssCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public int queryReportFeeSummaryCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummaryCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryReportFeeSummary(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummary(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryReportFloorUnitFeeSummaryCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummaryCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryReportFloorUnitFeeSummary(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummary(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryFeeBreakdownCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdownCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryFeeBreakdown(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdown(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryFeeDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryFeeDetailCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryAllFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryAllFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);
        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryPrePaymentNewCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryPrePaymentNewCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryPrePayment(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryPrePayment(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryOweFeeDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryOweFeeDetailCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryOweFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryOweFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public JSONObject queryPayFeeDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        Map info = reportFeeMonthStatisticsServiceDaoImpl.queryPayFeeDetailCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));

        return JSONObject.parseObject(JSONObject.toJSONString(info));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryPayFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryPayFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryDeadlineFeeCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryDeadlineFeeCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryDeadlineFee(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryDeadlineFee(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryPrePaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息


        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryPrePaymentCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryDeadlinePaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息


        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryDeadlinePaymentCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryOwePaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息


        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryOwePaymentCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryAllPaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryAllPaymentCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public List<FeeConfigDto> queryFeeConfigs(FeeConfigDto feeConfigDto) {
        List<FeeConfigDto> feeConfigs = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.getFeeConfigInfo(BeanConvertUtil.beanCovertMap(feeConfigDto)), FeeConfigDto.class);
        return feeConfigs;
    }


    public IReportFeeMonthStatisticsServiceDao getReportFeeMonthStatisticsServiceDaoImpl() {
        return reportFeeMonthStatisticsServiceDaoImpl;
    }

    public void setReportFeeMonthStatisticsServiceDaoImpl(IReportFeeMonthStatisticsServiceDao reportFeeMonthStatisticsServiceDaoImpl) {
        this.reportFeeMonthStatisticsServiceDaoImpl = reportFeeMonthStatisticsServiceDaoImpl;
    }
}
