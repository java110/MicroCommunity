package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailDto;
import com.java110.intf.report.IReportFeeMonthCollectionDetailInnerServiceSMO;
import com.java110.po.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailPo;
import com.java110.report.dao.IReportFeeMonthCollectionDetailServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 月缴费表内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportFeeMonthCollectionDetailInnerServiceSMOImpl extends BaseServiceSMO implements IReportFeeMonthCollectionDetailInnerServiceSMO {

    @Autowired
    private IReportFeeMonthCollectionDetailServiceDao reportFeeMonthCollectionDetailServiceDaoImpl;


    @Override
    public int saveReportFeeMonthCollectionDetail(@RequestBody ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo) {
        int saveFlag = 1;
        reportFeeMonthCollectionDetailServiceDaoImpl.saveReportFeeMonthCollectionDetailInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public int updateReportFeeMonthCollectionDetail(@RequestBody ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo) {
        int saveFlag = 1;
        reportFeeMonthCollectionDetailServiceDaoImpl.updateReportFeeMonthCollectionDetailInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteReportFeeMonthCollectionDetail(@RequestBody ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo) {
        int saveFlag = 1;
        reportFeeMonthCollectionDetailPo.setStatusCd("1");
        reportFeeMonthCollectionDetailServiceDaoImpl.updateReportFeeMonthCollectionDetailInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public List<ReportFeeMonthCollectionDetailDto> queryReportFeeMonthCollectionDetails(@RequestBody ReportFeeMonthCollectionDetailDto reportFeeMonthCollectionDetailDto) {

        //校验是否传了 分页信息

        int page = reportFeeMonthCollectionDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthCollectionDetailDto.setPage((page - 1) * reportFeeMonthCollectionDetailDto.getRow());
        }

        List<ReportFeeMonthCollectionDetailDto> reportFeeMonthCollectionDetails = BeanConvertUtil.covertBeanList(reportFeeMonthCollectionDetailServiceDaoImpl.getReportFeeMonthCollectionDetailInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthCollectionDetailDto)), ReportFeeMonthCollectionDetailDto.class);

        return reportFeeMonthCollectionDetails;
    }


    @Override
    public int queryReportFeeMonthCollectionDetailsCount(@RequestBody ReportFeeMonthCollectionDetailDto reportFeeMonthCollectionDetailDto) {
        return reportFeeMonthCollectionDetailServiceDaoImpl.queryReportFeeMonthCollectionDetailsCount(BeanConvertUtil.beanCovertMap(reportFeeMonthCollectionDetailDto));
    }

    public IReportFeeMonthCollectionDetailServiceDao getReportFeeMonthCollectionDetailServiceDaoImpl() {
        return reportFeeMonthCollectionDetailServiceDaoImpl;
    }

    public void setReportFeeMonthCollectionDetailServiceDaoImpl(IReportFeeMonthCollectionDetailServiceDao reportFeeMonthCollectionDetailServiceDaoImpl) {
        this.reportFeeMonthCollectionDetailServiceDaoImpl = reportFeeMonthCollectionDetailServiceDaoImpl;
    }
}
