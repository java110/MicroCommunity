package com.java110.report.smo.impl;


import com.java110.intf.report.IReportInfoAnswerInnerServiceSMO;
import com.java110.po.reportInfoAnswer.ReportInfoAnswerPo;
import com.java110.report.dao.IReportInfoAnswerServiceDao;
import com.java110.dto.reportInfoAnswer.ReportInfoAnswerDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 批量操作日志详情内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportInfoAnswerInnerServiceSMOImpl extends BaseServiceSMO implements IReportInfoAnswerInnerServiceSMO {

    @Autowired
    private IReportInfoAnswerServiceDao reportInfoAnswerServiceDaoImpl;


    @Override
    public int saveReportInfoAnswer(@RequestBody ReportInfoAnswerPo reportInfoAnswerPo) {
        int saveFlag = 1;
        reportInfoAnswerServiceDaoImpl.saveReportInfoAnswerInfo(BeanConvertUtil.beanCovertMap(reportInfoAnswerPo));
        return saveFlag;
    }

     @Override
    public int updateReportInfoAnswer(@RequestBody  ReportInfoAnswerPo reportInfoAnswerPo) {
        int saveFlag = 1;
         reportInfoAnswerServiceDaoImpl.updateReportInfoAnswerInfo(BeanConvertUtil.beanCovertMap(reportInfoAnswerPo));
        return saveFlag;
    }

     @Override
    public int deleteReportInfoAnswer(@RequestBody  ReportInfoAnswerPo reportInfoAnswerPo) {
        int saveFlag = 1;
        reportInfoAnswerPo.setStatusCd("1");
        reportInfoAnswerServiceDaoImpl.updateReportInfoAnswerInfo(BeanConvertUtil.beanCovertMap(reportInfoAnswerPo));
        return saveFlag;
    }

    @Override
    public List<ReportInfoAnswerDto> queryReportInfoAnswers(@RequestBody  ReportInfoAnswerDto reportInfoAnswerDto) {

        //校验是否传了 分页信息

        int page = reportInfoAnswerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportInfoAnswerDto.setPage((page - 1) * reportInfoAnswerDto.getRow());
        }

        List<ReportInfoAnswerDto> reportInfoAnswers = BeanConvertUtil.covertBeanList(reportInfoAnswerServiceDaoImpl.getReportInfoAnswerInfo(BeanConvertUtil.beanCovertMap(reportInfoAnswerDto)), ReportInfoAnswerDto.class);

        return reportInfoAnswers;
    }


    @Override
    public int queryReportInfoAnswersCount(@RequestBody ReportInfoAnswerDto reportInfoAnswerDto) {
        return reportInfoAnswerServiceDaoImpl.queryReportInfoAnswersCount(BeanConvertUtil.beanCovertMap(reportInfoAnswerDto));    }

    public IReportInfoAnswerServiceDao getReportInfoAnswerServiceDaoImpl() {
        return reportInfoAnswerServiceDaoImpl;
    }

    public void setReportInfoAnswerServiceDaoImpl(IReportInfoAnswerServiceDao reportInfoAnswerServiceDaoImpl) {
        this.reportInfoAnswerServiceDaoImpl = reportInfoAnswerServiceDaoImpl;
    }
}
