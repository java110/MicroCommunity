package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.po.reportOweFee.ReportOweFeePo;
import com.java110.report.dao.IReportOweFeeServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 欠费统计内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportOweFeeInnerServiceSMOImpl extends BaseServiceSMO implements IReportOweFeeInnerServiceSMO {

    @Autowired
    private IReportOweFeeServiceDao reportOweFeeServiceDaoImpl;


    @Override
    public int saveReportOweFee(@RequestBody ReportOweFeePo reportOweFeePo) {
        int saveFlag = 1;
        reportOweFeeServiceDaoImpl.saveReportOweFeeInfo(BeanConvertUtil.beanCovertMap(reportOweFeePo));
        return saveFlag;
    }

    @Override
    public int updateReportOweFee(@RequestBody ReportOweFeePo reportOweFeePo) {
        int saveFlag = 1;
        reportOweFeeServiceDaoImpl.updateReportOweFeeInfo(BeanConvertUtil.beanCovertMap(reportOweFeePo));
        return saveFlag;
    }

    @Override
    public int deleteReportOweFee(@RequestBody ReportOweFeePo reportOweFeePo) {
        int saveFlag = 1;
        reportOweFeePo.setStatusCd("1");
        reportOweFeeServiceDaoImpl.updateReportOweFeeInfo(BeanConvertUtil.beanCovertMap(reportOweFeePo));
        return saveFlag;
    }

    @Override
    public List<ReportOweFeeDto> queryReportOweFees(@RequestBody ReportOweFeeDto reportOweFeeDto) {

        //校验是否传了 分页信息

        int page = reportOweFeeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportOweFeeDto.setPage((page - 1) * reportOweFeeDto.getRow());
        }

        List<ReportOweFeeDto> reportOweFees = BeanConvertUtil.covertBeanList(reportOweFeeServiceDaoImpl.getReportOweFeeInfo(BeanConvertUtil.beanCovertMap(reportOweFeeDto)), ReportOweFeeDto.class);

        return reportOweFees;
    }


    @Override
    public int queryReportOweFeesCount(@RequestBody ReportOweFeeDto reportOweFeeDto) {
        return reportOweFeeServiceDaoImpl.queryReportOweFeesCount(BeanConvertUtil.beanCovertMap(reportOweFeeDto));
    }

    @Override
    public List<ReportOweFeeDto> queryReportAllOweFees(@RequestBody ReportOweFeeDto reportOweFeeDto) {
        List<ReportOweFeeDto> reportOweFees = BeanConvertUtil.covertBeanList(reportOweFeeServiceDaoImpl.queryReportAllOweFees(BeanConvertUtil.beanCovertMap(reportOweFeeDto)), ReportOweFeeDto.class);
        return reportOweFees;

    }

    public IReportOweFeeServiceDao getReportOweFeeServiceDaoImpl() {
        return reportOweFeeServiceDaoImpl;
    }

    public void setReportOweFeeServiceDaoImpl(IReportOweFeeServiceDao reportOweFeeServiceDaoImpl) {
        this.reportOweFeeServiceDaoImpl = reportOweFeeServiceDaoImpl;
    }
}
