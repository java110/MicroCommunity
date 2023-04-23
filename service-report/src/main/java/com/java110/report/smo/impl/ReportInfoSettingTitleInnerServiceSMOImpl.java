package com.java110.report.smo.impl;


import com.java110.intf.report.IReportInfoSettingTitleInnerServiceSMO;
import com.java110.po.reportInfoSettingTitle.ReportInfoSettingTitlePo;
import com.java110.report.dao.IReportInfoSettingTitleServiceDao;
import com.java110.dto.reportInfoSetting.ReportInfoSettingTitleDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 进出上报题目设置内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportInfoSettingTitleInnerServiceSMOImpl extends BaseServiceSMO implements IReportInfoSettingTitleInnerServiceSMO {

    @Autowired
    private IReportInfoSettingTitleServiceDao reportInfoSettingTitleServiceDaoImpl;


    @Override
    public int saveReportInfoSettingTitle(@RequestBody ReportInfoSettingTitlePo reportInfoSettingTitlePo) {
        int saveFlag = 1;
        reportInfoSettingTitleServiceDaoImpl.saveReportInfoSettingTitleInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingTitlePo));
        return saveFlag;
    }

     @Override
    public int updateReportInfoSettingTitle(@RequestBody  ReportInfoSettingTitlePo reportInfoSettingTitlePo) {
        int saveFlag = 1;
         reportInfoSettingTitleServiceDaoImpl.updateReportInfoSettingTitleInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingTitlePo));
        return saveFlag;
    }

     @Override
    public int deleteReportInfoSettingTitle(@RequestBody  ReportInfoSettingTitlePo reportInfoSettingTitlePo) {
        int saveFlag = 1;
        reportInfoSettingTitlePo.setStatusCd("1");
        reportInfoSettingTitleServiceDaoImpl.updateReportInfoSettingTitleInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingTitlePo));
        return saveFlag;
    }

    @Override
    public List<ReportInfoSettingTitleDto> queryReportInfoSettingTitles(@RequestBody  ReportInfoSettingTitleDto reportInfoSettingTitleDto) {

        //校验是否传了 分页信息

        int page = reportInfoSettingTitleDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportInfoSettingTitleDto.setPage((page - 1) * reportInfoSettingTitleDto.getRow());
        }

        List<ReportInfoSettingTitleDto> reportInfoSettingTitles = BeanConvertUtil.covertBeanList(reportInfoSettingTitleServiceDaoImpl.getReportInfoSettingTitleInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingTitleDto)), ReportInfoSettingTitleDto.class);

        return reportInfoSettingTitles;
    }


    @Override
    public int queryReportInfoSettingTitlesCount(@RequestBody ReportInfoSettingTitleDto reportInfoSettingTitleDto) {
        return reportInfoSettingTitleServiceDaoImpl.queryReportInfoSettingTitlesCount(BeanConvertUtil.beanCovertMap(reportInfoSettingTitleDto));    }

    public IReportInfoSettingTitleServiceDao getReportInfoSettingTitleServiceDaoImpl() {
        return reportInfoSettingTitleServiceDaoImpl;
    }

    public void setReportInfoSettingTitleServiceDaoImpl(IReportInfoSettingTitleServiceDao reportInfoSettingTitleServiceDaoImpl) {
        this.reportInfoSettingTitleServiceDaoImpl = reportInfoSettingTitleServiceDaoImpl;
    }
}
