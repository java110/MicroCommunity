package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.reportFeeYearCollection.ReportFeeYearCollectionDto;
import com.java110.dto.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailDto;
import com.java110.intf.report.IReportFeeYearCollectionDetailInnerServiceSMO;
import com.java110.intf.report.IReportFeeYearCollectionInnerServiceSMO;
import com.java110.po.reportFeeYearCollection.ReportFeeYearCollectionPo;
import com.java110.report.dao.IReportFeeYearCollectionServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用年收费内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportFeeYearCollectionInnerServiceSMOImpl extends BaseServiceSMO implements IReportFeeYearCollectionInnerServiceSMO {

    @Autowired
    private IReportFeeYearCollectionServiceDao reportFeeYearCollectionServiceDaoImpl;

    @Autowired
    private IReportFeeYearCollectionDetailInnerServiceSMO reportFeeYearCollectionDetailInnerServiceSMOImpl;


    @Override
    public int saveReportFeeYearCollection(@RequestBody ReportFeeYearCollectionPo reportFeeYearCollectionPo) {
        int saveFlag = 1;
        reportFeeYearCollectionServiceDaoImpl.saveReportFeeYearCollectionInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionPo));
        return saveFlag;
    }

    @Override
    public int updateReportFeeYearCollection(@RequestBody ReportFeeYearCollectionPo reportFeeYearCollectionPo) {
        int saveFlag = 1;
        reportFeeYearCollectionServiceDaoImpl.updateReportFeeYearCollectionInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionPo));
        return saveFlag;
    }

    @Override
    public int deleteReportFeeYearCollection(@RequestBody ReportFeeYearCollectionPo reportFeeYearCollectionPo) {
        int saveFlag = 1;
        reportFeeYearCollectionPo.setStatusCd("1");
        reportFeeYearCollectionServiceDaoImpl.deleteReportFeeYearCollectionInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionPo));
        return saveFlag;
    }

    @Override
    public List<ReportFeeYearCollectionDto> queryReportFeeYearCollections(@RequestBody ReportFeeYearCollectionDto reportFeeYearCollectionDto) {

        //校验是否传了 分页信息

        int page = reportFeeYearCollectionDto.getPage();
        List<ReportFeeYearCollectionDto> reportFeeYearCollections = null;

        //不分页直接查全量数据
        if (reportFeeYearCollectionDto.getRow() > 10000) {
            List<Map> reportFeeYearCollectionMaps =
                    reportFeeYearCollectionServiceDaoImpl.getReportFeeYearCollectionInfos(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDto));

            reportFeeYearCollections = doCreateReportFeeYearCollections(reportFeeYearCollectionMaps);
            return reportFeeYearCollections;
        }

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeYearCollectionDto.setPage((page - 1) * reportFeeYearCollectionDto.getRow());
        }

        reportFeeYearCollections = BeanConvertUtil.covertBeanList(reportFeeYearCollectionServiceDaoImpl.getReportFeeYearCollectionInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDto)), ReportFeeYearCollectionDto.class);

        freshDetails(reportFeeYearCollections);
        return reportFeeYearCollections;
    }

    /**
     * 创建对象
     *
     * @param reportFeeYearCollectionMaps
     * @return
     */
    private List<ReportFeeYearCollectionDto> doCreateReportFeeYearCollections(List<Map> reportFeeYearCollectionMaps) {
        List<ReportFeeYearCollectionDto> reportFeeYearCollectionDtos = new ArrayList<>();
        ReportFeeYearCollectionDto reportFeeYearCollectionDto = null;

        for (Map reportFeeYearCollectionMap : reportFeeYearCollectionMaps) {
            if (!hasReportFeeYearCollectionDto(reportFeeYearCollectionMap, reportFeeYearCollectionDtos)) {
                reportFeeYearCollectionDto = BeanConvertUtil.covertBean(reportFeeYearCollectionMap, ReportFeeYearCollectionDto.class);
                reportFeeYearCollectionDtos.add(reportFeeYearCollectionDto);
            }
        }
        List<ReportFeeYearCollectionDetailDto> reportFeeYearCollectionDetailDtos = null;
        ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto = null;
        for (ReportFeeYearCollectionDto tmpReportFeeYearCollectionDto : reportFeeYearCollectionDtos) {
            reportFeeYearCollectionDetailDtos = new ArrayList<>();
            for (Map reportFeeYearCollectionMap : reportFeeYearCollectionMaps) {
                if (tmpReportFeeYearCollectionDto.getCollectionId().equals(reportFeeYearCollectionMap.get("collectionId"))) {
                    reportFeeYearCollectionDetailDto = BeanConvertUtil.covertBean(reportFeeYearCollectionMap, ReportFeeYearCollectionDetailDto.class);
                    reportFeeYearCollectionDetailDtos.add(reportFeeYearCollectionDetailDto);
                }
            }
            tmpReportFeeYearCollectionDto.setReportFeeYearCollectionDetailDtos(reportFeeYearCollectionDetailDtos);
        }

        return reportFeeYearCollectionDtos;
    }

    private boolean hasReportFeeYearCollectionDto(Map reportFeeYearCollectionMap, List<ReportFeeYearCollectionDto> reportFeeYearCollectionDtos) {

        for (ReportFeeYearCollectionDto reportFeeYearCollectionDto : reportFeeYearCollectionDtos) {
            if (reportFeeYearCollectionDto.getCollectionId().equals(reportFeeYearCollectionMap.get("collectionId"))) {
                return true;
            }
        }

        return false;
    }

    private void freshDetails(List<ReportFeeYearCollectionDto> reportFeeYearCollections) {
        if (reportFeeYearCollections == null || reportFeeYearCollections.size() < 1 || reportFeeYearCollections.size() > 20) {
            return;
        }

        List<String> collectionIds = new ArrayList<>();
        for (ReportFeeYearCollectionDto reportFeeYearCollectionDto : reportFeeYearCollections) {
            collectionIds.add(reportFeeYearCollectionDto.getCollectionId());
        }
        ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto = new ReportFeeYearCollectionDetailDto();
        reportFeeYearCollectionDetailDto.setCommunityId(reportFeeYearCollections.get(0).getCommunityId());
        reportFeeYearCollectionDetailDto.setCollectionIds(collectionIds.toArray(new String[collectionIds.size()]));
        List<ReportFeeYearCollectionDetailDto> reportFeeYearCollectionDetailDtos
                = reportFeeYearCollectionDetailInnerServiceSMOImpl.queryReportFeeYearCollectionDetails(reportFeeYearCollectionDetailDto);

        List<ReportFeeYearCollectionDetailDto> tmpReportFeeYearCollectionDetailDtos = null;
        //循环 主表
        for (ReportFeeYearCollectionDto reportFeeYearCollectionDto : reportFeeYearCollections) {
            tmpReportFeeYearCollectionDetailDtos = new ArrayList<>();
            for (ReportFeeYearCollectionDetailDto tmpReportFeeYearCollectionDetailDto : reportFeeYearCollectionDetailDtos) {
                if (reportFeeYearCollectionDto.getCollectionId().equals(tmpReportFeeYearCollectionDetailDto.getCollectionId())) {
                    tmpReportFeeYearCollectionDetailDtos.add(tmpReportFeeYearCollectionDetailDto);
                }
            }
            if (tmpReportFeeYearCollectionDetailDtos.size() > 0) {
                reportFeeYearCollectionDto.setReceivableAmount(tmpReportFeeYearCollectionDetailDtos.get(0).getReceivableAmount());
            } else {
                reportFeeYearCollectionDto.setReceivableAmount("0");
            }
            reportFeeYearCollectionDto.setReportFeeYearCollectionDetailDtos(tmpReportFeeYearCollectionDetailDtos);
        }
    }


    @Override
    public int queryReportFeeYearCollectionsCount(@RequestBody ReportFeeYearCollectionDto reportFeeYearCollectionDto) {
        return reportFeeYearCollectionServiceDaoImpl.queryReportFeeYearCollectionsCount(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDto));
    }

    @Override
    public List<Map> getReportFeeYearCollectionInfo(@RequestBody Map beanCovertMap) {
        return reportFeeYearCollectionServiceDaoImpl.getReportFeeYearCollectionInfo(beanCovertMap);
    }

    @Override
    public void saveReportFeeYearCollectionInfo(@RequestBody Map beanCovertMap) {
         reportFeeYearCollectionServiceDaoImpl.saveReportFeeYearCollectionInfo(beanCovertMap);
    }

    public IReportFeeYearCollectionServiceDao getReportFeeYearCollectionServiceDaoImpl() {
        return reportFeeYearCollectionServiceDaoImpl;
    }

    public void setReportFeeYearCollectionServiceDaoImpl(IReportFeeYearCollectionServiceDao reportFeeYearCollectionServiceDaoImpl) {
        this.reportFeeYearCollectionServiceDaoImpl = reportFeeYearCollectionServiceDaoImpl;
    }
}
