package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportOrderStatisticsInnerServiceSMO;
import com.java110.report.dao.IReportFeeStatisticsServiceDao;
import com.java110.report.dao.IReportOrderStatisticsServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ReportFeeStatisticsInnerServiceSMOImpl
 * @Description 费用统计类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportOrderStatisticsInnerServiceSMOImpl extends BaseServiceSMO implements IReportOrderStatisticsInnerServiceSMO {

    @Autowired
    private IReportOrderStatisticsServiceDao reportOrderStatisticsServiceDaoImpl;

    /**
     * 查询投诉工单数
     *
     * @param queryStatisticsDto 数据对象分享
     * @return
     */
    @Override
    public double getComplaintOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getComplaintOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    /**
     * 查询未处理投诉工单
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getUndoComplaintOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getUndoComplaintOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    /**
     * 查询投诉完成工单
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getFinishComplaintOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getFinishComplaintOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getRepairOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getRepairOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getUndoRepairOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getUndoRepairOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getFinishRepairOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getFinishRepairOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getInspectionOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getInspectionOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getUndoInspectionOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getUndoInspectionOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getFinishInspectionOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getFinishInspectionOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getMaintainanceOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getMaintainanceOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getUndoMaintainanceOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getUndoMaintainanceOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getFinishMaintainanceOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getFinishMaintainanceOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getNotepadOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getNotepadOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getChargeMachineOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getChargeMachineOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getChargeMonthOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportOrderStatisticsServiceDaoImpl.getChargeMonthOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public int getOwnerReserveGoodsCount(@RequestBody OwnerDto ownerDto) {
        int info = reportOrderStatisticsServiceDaoImpl.getOwnerReserveGoodsCount(BeanConvertUtil.beanCovertMap(ownerDto));
        return info;
    }

    @Override
    public List<Map> getOwnerReserveGoods(@RequestBody OwnerDto ownerDto) {
        int page = ownerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerDto.setPage((page - 1) * ownerDto.getRow());
        }

        List<Map> infos = reportOrderStatisticsServiceDaoImpl.getOwnerReserveGoods(BeanConvertUtil.beanCovertMap(ownerDto));
        return infos;
    }

    @Override
    public int getOwnerDiningCount(@RequestBody OwnerDto ownerDto) {
        int info = reportOrderStatisticsServiceDaoImpl.getOwnerDiningCount(BeanConvertUtil.beanCovertMap(ownerDto));
        return info;
    }

    @Override
    public List<Map> getOwnerDinings(@RequestBody OwnerDto ownerDto) {
        int page = ownerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerDto.setPage((page - 1) * ownerDto.getRow());
        }

        List<Map> infos = reportOrderStatisticsServiceDaoImpl.getOwnerDinings(BeanConvertUtil.beanCovertMap(ownerDto));
        return infos;
    }

}
