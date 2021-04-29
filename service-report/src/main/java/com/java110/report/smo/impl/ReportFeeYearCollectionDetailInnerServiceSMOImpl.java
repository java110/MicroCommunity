package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailDto;
import com.java110.intf.report.IReportFeeYearCollectionDetailInnerServiceSMO;
import com.java110.po.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailPo;
import com.java110.report.dao.IReportFeeYearCollectionDetailServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用年收费明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportFeeYearCollectionDetailInnerServiceSMOImpl extends BaseServiceSMO implements IReportFeeYearCollectionDetailInnerServiceSMO {

    @Autowired
    private IReportFeeYearCollectionDetailServiceDao reportFeeYearCollectionDetailServiceDaoImpl;


    @Override
    public int saveReportFeeYearCollectionDetail(@RequestBody ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo) {
        int saveFlag = 1;
        reportFeeYearCollectionDetailServiceDaoImpl.saveReportFeeYearCollectionDetailInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public int updateReportFeeYearCollectionDetail(@RequestBody ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo) {
        int saveFlag = 1;
        reportFeeYearCollectionDetailServiceDaoImpl.updateReportFeeYearCollectionDetailInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteReportFeeYearCollectionDetail(@RequestBody ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo) {
        int saveFlag = 1;
        reportFeeYearCollectionDetailPo.setStatusCd("1");
        reportFeeYearCollectionDetailServiceDaoImpl.updateReportFeeYearCollectionDetailInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDetailPo));
        return saveFlag;
    }

    @Override
    public List<ReportFeeYearCollectionDetailDto> queryReportFeeYearCollectionDetails(@RequestBody ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto) {

        //校验是否传了 分页信息

        int page = reportFeeYearCollectionDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeYearCollectionDetailDto.setPage((page - 1) * reportFeeYearCollectionDetailDto.getRow());
        }

        List<ReportFeeYearCollectionDetailDto> reportFeeYearCollectionDetails = BeanConvertUtil.covertBeanList(reportFeeYearCollectionDetailServiceDaoImpl.getReportFeeYearCollectionDetailInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDetailDto)), ReportFeeYearCollectionDetailDto.class);

        return reportFeeYearCollectionDetails;
    }


    @Override
    public int queryReportFeeYearCollectionDetailsCount(@RequestBody ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto) {
        return reportFeeYearCollectionDetailServiceDaoImpl.queryReportFeeYearCollectionDetailsCount(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDetailDto));
    }

    public IReportFeeYearCollectionDetailServiceDao getReportFeeYearCollectionDetailServiceDaoImpl() {
        return reportFeeYearCollectionDetailServiceDaoImpl;
    }

    public void setReportFeeYearCollectionDetailServiceDaoImpl(IReportFeeYearCollectionDetailServiceDao reportFeeYearCollectionDetailServiceDaoImpl) {
        this.reportFeeYearCollectionDetailServiceDaoImpl = reportFeeYearCollectionDetailServiceDaoImpl;
    }
}
