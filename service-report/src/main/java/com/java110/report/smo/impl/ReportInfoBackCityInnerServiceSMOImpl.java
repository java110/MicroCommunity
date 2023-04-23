package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.reportInfoAnswer.ReportInfoBackCityDto;
import com.java110.intf.report.IReportInfoBackCityInnerServiceSMO;
import com.java110.po.reportInfoBackCity.ReportInfoBackCityPo;
import com.java110.report.dao.IReportInfoBackCityServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 返省上报内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportInfoBackCityInnerServiceSMOImpl extends BaseServiceSMO implements IReportInfoBackCityInnerServiceSMO {

    @Autowired
    private IReportInfoBackCityServiceDao reportInfoBackCityServiceDaoImpl;


    @Override
    public int saveReportInfoBackCity(@RequestBody ReportInfoBackCityPo reportInfoBackCityPo) {
        int saveFlag = 1;
        reportInfoBackCityServiceDaoImpl.saveReportInfoBackCityInfo(BeanConvertUtil.beanCovertMap(reportInfoBackCityPo));
        return saveFlag;
    }

    @Override
    public int updateReportInfoBackCity(@RequestBody ReportInfoBackCityPo reportInfoBackCityPo) {
        int saveFlag = 1;
        reportInfoBackCityServiceDaoImpl.updateReportInfoBackCityInfo(BeanConvertUtil.beanCovertMap(reportInfoBackCityPo));
        return saveFlag;
    }

    @Override
    public int deleteReportInfoBackCity(@RequestBody ReportInfoBackCityPo reportInfoBackCityPo) {
        int saveFlag = 1;
        reportInfoBackCityPo.setStatusCd("1");
        reportInfoBackCityServiceDaoImpl.updateReportInfoBackCityInfo(BeanConvertUtil.beanCovertMap(reportInfoBackCityPo));
        return saveFlag;
    }

    @Override
    public List<ReportInfoBackCityDto> queryReportInfoBackCitys(@RequestBody ReportInfoBackCityDto reportInfoBackCityDto) {

        //校验是否传了 分页信息

        int page = reportInfoBackCityDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportInfoBackCityDto.setPage((page - 1) * reportInfoBackCityDto.getRow());
        }

        List<ReportInfoBackCityDto> reportInfoBackCitys = BeanConvertUtil.covertBeanList(reportInfoBackCityServiceDaoImpl.getReportInfoBackCityInfo(BeanConvertUtil.beanCovertMap(reportInfoBackCityDto)), ReportInfoBackCityDto.class);

        return reportInfoBackCitys;
    }


    @Override
    public int queryReportInfoBackCitysCount(@RequestBody ReportInfoBackCityDto reportInfoBackCityDto) {
        return reportInfoBackCityServiceDaoImpl.queryReportInfoBackCitysCount(BeanConvertUtil.beanCovertMap(reportInfoBackCityDto));
    }

    public IReportInfoBackCityServiceDao getReportInfoBackCityServiceDaoImpl() {
        return reportInfoBackCityServiceDaoImpl;
    }

    public void setReportInfoBackCityServiceDaoImpl(IReportInfoBackCityServiceDao reportInfoBackCityServiceDaoImpl) {
        this.reportInfoBackCityServiceDaoImpl = reportInfoBackCityServiceDaoImpl;
    }
}
