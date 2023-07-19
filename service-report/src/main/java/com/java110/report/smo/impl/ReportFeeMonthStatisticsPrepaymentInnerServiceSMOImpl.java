package com.java110.report.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.report.IReportFeeMonthStatisticsPrepaymentInnerServiceSMO;
import com.java110.po.ReportFeeMonthStatisticsPrepaymentPo.ReportFeeMonthStatisticsPrepaymentPo;
import com.java110.report.dao.IReportFeeMonthStatisticsPrepaymentServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ReportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl extends BaseServiceSMO implements IReportFeeMonthStatisticsPrepaymentInnerServiceSMO {

    @Autowired
    private IReportFeeMonthStatisticsPrepaymentServiceDao reportFeeMonthStatisticsPrepaymentServiceDaoImpl;

    @Override
    public int saveReportFeeMonthStatisticsPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo) {
        int saveFlag = 1;
        reportFeeMonthStatisticsPrepaymentServiceDaoImpl.saveReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
        return saveFlag;
    }

    @Override
    public int updateReportFeeMonthStatisticsPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo) {
        int saveFlag = 1;
        reportFeeMonthStatisticsPrepaymentServiceDaoImpl.updateReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
        return saveFlag;
    }

    @Override
    public int deleteReportFeeMonthStatisticsPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo) {
        int saveFlag = 1;
        reportFeeMonthStatisticsPrepaymentPo.setStatusCd("1");
        reportFeeMonthStatisticsPrepaymentServiceDaoImpl.deleteReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
        return saveFlag;
    }

    @Override
    public List<ReportFeeMonthStatisticsPrepaymentDto> queryReportFeeMonthStatisticsPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsPrepaymentDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsPrepaymentDto.setPage((page - 1) * reportFeeMonthStatisticsPrepaymentDto.getRow());
        }

        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)), ReportFeeMonthStatisticsPrepaymentDto.class);

        return reportFeeMonthStatisticsPrepayments;
    }

    @Override
    public List<ReportFeeMonthStatisticsPrepaymentDto> queryPrepaymentConfigs(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsPrepaymentDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsPrepaymentDto.setPage((page - 1) * reportFeeMonthStatisticsPrepaymentDto.getRow());
        }

        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryPrepaymentConfigs(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)), ReportFeeMonthStatisticsPrepaymentDto.class);

        return reportFeeMonthStatisticsPrepayments;
    }

    @Override
    public List<ReportFeeMonthStatisticsPrepaymentDto> queryAllPrepaymentConfigs(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsPrepaymentDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsPrepaymentDto.setPage((page - 1) * reportFeeMonthStatisticsPrepaymentDto.getRow());
        }

        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryAllPrepaymentConfigs(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)), ReportFeeMonthStatisticsPrepaymentDto.class);

        return reportFeeMonthStatisticsPrepayments;
    }

    @Override
    public List<ReportFeeMonthStatisticsPrepaymentDto> queryAllPrepaymentDiscounts(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsPrepaymentDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsPrepaymentDto.setPage((page - 1) * reportFeeMonthStatisticsPrepaymentDto.getRow());
        }

        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentDtos = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryAllPrepaymentDiscounts(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)), ReportFeeMonthStatisticsPrepaymentDto.class);

        return reportFeeMonthStatisticsPrepaymentDtos;
    }

    @Override
    public int queryReportFeeMonthStatisticsPrepaymentsCount(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        return reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryReportFeeMonthStatisticsPrepaymentCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto));
    }

    @Override
    public JSONObject queryPayFeeDetailPrepaymentCount(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        Map info = reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryPayFeeDetailCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto));

        return JSONObject.parseObject(JSONObject.toJSONString(info));
    }

    @Override
    public JSONObject queryReportCollectFeesCount(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        Map info = reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryReportCollectFeesCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto));

        return JSONObject.parseObject(JSONObject.toJSONString(info));
    }

    @Override
    public List<ReportFeeMonthStatisticsPrepaymentDto> queryPayFeeDetailPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsPrepaymentDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsPrepaymentDto.setPage((page - 1) * reportFeeMonthStatisticsPrepaymentDto.getRow());
        }

        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryPayFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)), ReportFeeMonthStatisticsPrepaymentDto.class);

        return reportFeeMonthStatisticsPrepayments;
    }

    @Override
    public List<ReportFeeMonthStatisticsPrepaymentDto> queryNewPayFeeDetailPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsPrepaymentDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsPrepaymentDto.setPage((page - 1) * reportFeeMonthStatisticsPrepaymentDto.getRow());
        }

        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryNewPayFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)), ReportFeeMonthStatisticsPrepaymentDto.class);

        return reportFeeMonthStatisticsPrepayments;
    }

    @Override
    public List<ReportFeeMonthStatisticsPrepaymentDto> queryAllPayFeeDetailPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments = BeanConvertUtil.covertBeanList(
                reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryAllPayFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)),
                ReportFeeMonthStatisticsPrepaymentDto.class);
        return reportFeeMonthStatisticsPrepayments;
    }

    @Override
    public List<ReportFeeMonthStatisticsPrepaymentDto> queryPayFeeDetailPrepaymentSum(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryPayFeeDetailSum(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)), ReportFeeMonthStatisticsPrepaymentDto.class);
        return reportFeeMonthStatisticsPrepayments;
    }

    @Override
    public List<FeeConfigDto> queryFeeConfigPrepayments(@RequestBody FeeConfigDto feeConfigDto) {
        List<FeeConfigDto> feeConfigs = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getFeeConfigInfo(BeanConvertUtil.beanCovertMap(feeConfigDto)), FeeConfigDto.class);
        return feeConfigs;
    }

    @Override
    public List<OwnerDto> queryRoomAndParkingSpacePrepayment(@RequestBody OwnerDto ownerDto) {
        List<OwnerDto> deposits = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryRoomAndParkingSpace(BeanConvertUtil.beanCovertMap(ownerDto)),
                OwnerDto.class);
        return deposits;
    }

    public IReportFeeMonthStatisticsPrepaymentServiceDao getReportFeeMonthStatisticsPrepaymentServiceDaoImpl() {
        return reportFeeMonthStatisticsPrepaymentServiceDaoImpl;
    }

    public void setReportFeeMonthStatisticsPrepaymentServiceDaoImpl(IReportFeeMonthStatisticsPrepaymentServiceDao reportFeeMonthStatisticsPrepaymentServiceDaoImpl) {
        this.reportFeeMonthStatisticsPrepaymentServiceDaoImpl = reportFeeMonthStatisticsPrepaymentServiceDaoImpl;
    }
}
