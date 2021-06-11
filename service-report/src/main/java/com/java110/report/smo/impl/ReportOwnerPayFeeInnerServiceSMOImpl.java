package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.reportOwnerPayFee.ReportOwnerPayFeeDto;
import com.java110.intf.report.IReportOwnerPayFeeInnerServiceSMO;
import com.java110.po.reportOwnerPayFee.ReportOwnerPayFeePo;
import com.java110.report.dao.IReportOwnerPayFeeServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 业主缴费明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportOwnerPayFeeInnerServiceSMOImpl extends BaseServiceSMO implements IReportOwnerPayFeeInnerServiceSMO {

    @Autowired
    private IReportOwnerPayFeeServiceDao reportOwnerPayFeeServiceDaoImpl;


    @Override
    public int saveReportOwnerPayFee(@RequestBody ReportOwnerPayFeePo reportOwnerPayFeePo) {
        int saveFlag = 1;
        reportOwnerPayFeeServiceDaoImpl.saveReportOwnerPayFeeInfo(BeanConvertUtil.beanCovertMap(reportOwnerPayFeePo));
        return saveFlag;
    }

    @Override
    public int updateReportOwnerPayFee(@RequestBody ReportOwnerPayFeePo reportOwnerPayFeePo) {
        int saveFlag = 1;
        reportOwnerPayFeeServiceDaoImpl.updateReportOwnerPayFeeInfo(BeanConvertUtil.beanCovertMap(reportOwnerPayFeePo));
        return saveFlag;
    }

    @Override
    public int deleteReportOwnerPayFee(@RequestBody ReportOwnerPayFeePo reportOwnerPayFeePo) {
        int saveFlag = 1;
        reportOwnerPayFeePo.setStatusCd("1");
        reportOwnerPayFeeServiceDaoImpl.updateReportOwnerPayFeeInfo(BeanConvertUtil.beanCovertMap(reportOwnerPayFeePo));
        return saveFlag;
    }

    @Override
    public List<ReportOwnerPayFeeDto> queryReportOwnerPayFees(@RequestBody ReportOwnerPayFeeDto reportOwnerPayFeeDto) {

        //校验是否传了 分页信息

        int page = reportOwnerPayFeeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportOwnerPayFeeDto.setPage((page - 1) * reportOwnerPayFeeDto.getRow());
        }

        List<ReportOwnerPayFeeDto> reportOwnerPayFees = BeanConvertUtil.covertBeanList(reportOwnerPayFeeServiceDaoImpl.getReportOwnerPayFeeInfo(BeanConvertUtil.beanCovertMap(reportOwnerPayFeeDto)), ReportOwnerPayFeeDto.class);

        return reportOwnerPayFees;
    }

    @Override
    public List<ReportOwnerPayFeeDto> queryReportOwnerMonthPayFees(ReportOwnerPayFeeDto reportOwnerPayFeeDto) {
        //校验是否传了 分页信息

        int page = reportOwnerPayFeeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportOwnerPayFeeDto.setPage((page - 1) * reportOwnerPayFeeDto.getRow());
        }

        List<ReportOwnerPayFeeDto> reportOwnerPayFees = BeanConvertUtil.covertBeanList(reportOwnerPayFeeServiceDaoImpl.queryReportOwnerMonthPayFees(BeanConvertUtil.beanCovertMap(reportOwnerPayFeeDto)), ReportOwnerPayFeeDto.class);

        return reportOwnerPayFees;
    }


    @Override
    public int queryReportOwnerPayFeesCount(@RequestBody ReportOwnerPayFeeDto reportOwnerPayFeeDto) {
        return reportOwnerPayFeeServiceDaoImpl.queryReportOwnerPayFeesCount(BeanConvertUtil.beanCovertMap(reportOwnerPayFeeDto));
    }

    public IReportOwnerPayFeeServiceDao getReportOwnerPayFeeServiceDaoImpl() {
        return reportOwnerPayFeeServiceDaoImpl;
    }

    public void setReportOwnerPayFeeServiceDaoImpl(IReportOwnerPayFeeServiceDao reportOwnerPayFeeServiceDaoImpl) {
        this.reportOwnerPayFeeServiceDaoImpl = reportOwnerPayFeeServiceDaoImpl;
    }
}
