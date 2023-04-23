package com.java110.report.smo.impl;


import com.java110.intf.report.IReportInfoSettingTitleValueInnerServiceSMO;
import com.java110.po.reportInfoSettingTitleValue.ReportInfoSettingTitleValuePo;
import com.java110.report.dao.IReportInfoSettingTitleValueServiceDao;
import com.java110.dto.reportInfoSetting.ReportInfoSettingTitleValueDto;
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
public class ReportInfoSettingTitleValueInnerServiceSMOImpl extends BaseServiceSMO implements IReportInfoSettingTitleValueInnerServiceSMO {

    @Autowired
    private IReportInfoSettingTitleValueServiceDao reportInfoSettingTitleValueServiceDaoImpl;


    @Override
    public int saveReportInfoSettingTitleValue(@RequestBody ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo) {
        int saveFlag = 1;
        reportInfoSettingTitleValueServiceDaoImpl.saveReportInfoSettingTitleValueInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingTitleValuePo));
        return saveFlag;
    }

     @Override
    public int updateReportInfoSettingTitleValue(@RequestBody  ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo) {
        int saveFlag = 1;
         reportInfoSettingTitleValueServiceDaoImpl.updateReportInfoSettingTitleValueInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingTitleValuePo));
        return saveFlag;
    }

     @Override
    public int deleteReportInfoSettingTitleValue(@RequestBody  ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo) {
        int saveFlag = 1;
        reportInfoSettingTitleValuePo.setStatusCd("1");
        reportInfoSettingTitleValueServiceDaoImpl.updateReportInfoSettingTitleValueInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingTitleValuePo));
        return saveFlag;
    }

    @Override
    public List<ReportInfoSettingTitleValueDto> queryReportInfoSettingTitleValues(@RequestBody  ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto) {

        //校验是否传了 分页信息

        int page = reportInfoSettingTitleValueDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportInfoSettingTitleValueDto.setPage((page - 1) * reportInfoSettingTitleValueDto.getRow());
        }

        List<ReportInfoSettingTitleValueDto> reportInfoSettingTitleValues = BeanConvertUtil.covertBeanList(reportInfoSettingTitleValueServiceDaoImpl.getReportInfoSettingTitleValueInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingTitleValueDto)), ReportInfoSettingTitleValueDto.class);

        return reportInfoSettingTitleValues;
    }
    @Override
    public List<ReportInfoSettingTitleValueDto> getReportInfoSettingTitleValueInfoResult(@RequestBody  ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto) {

        //校验是否传了 分页信息

        int page = reportInfoSettingTitleValueDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportInfoSettingTitleValueDto.setPage((page - 1) * reportInfoSettingTitleValueDto.getRow());
        }

        List<ReportInfoSettingTitleValueDto> reportInfoSettingTitleValues = BeanConvertUtil.covertBeanList(reportInfoSettingTitleValueServiceDaoImpl.getReportInfoSettingTitleValueInfoResult(BeanConvertUtil.beanCovertMap(reportInfoSettingTitleValueDto)), ReportInfoSettingTitleValueDto.class);

        return reportInfoSettingTitleValues;
    }


    @Override
    public int queryReportInfoSettingTitleValuesCount(@RequestBody ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto) {
        return reportInfoSettingTitleValueServiceDaoImpl.queryReportInfoSettingTitleValuesCount(BeanConvertUtil.beanCovertMap(reportInfoSettingTitleValueDto));    }

    public IReportInfoSettingTitleValueServiceDao getReportInfoSettingTitleValueServiceDaoImpl() {
        return reportInfoSettingTitleValueServiceDaoImpl;
    }

    public void setReportInfoSettingTitleValueServiceDaoImpl(IReportInfoSettingTitleValueServiceDao reportInfoSettingTitleValueServiceDaoImpl) {
        this.reportInfoSettingTitleValueServiceDaoImpl = reportInfoSettingTitleValueServiceDaoImpl;
    }
}
