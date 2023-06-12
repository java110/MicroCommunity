package com.java110.report.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.report.dao.IReportFeeStatisticsServiceDao;
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
public class ReportFeeStatisticsInnerServiceSMOImpl extends BaseServiceSMO implements IReportFeeStatisticsInnerServiceSMO {

    @Autowired
    private IReportFeeStatisticsServiceDao reportFeeStatisticsServiceDaoImpl;

    /**
     * 查询历史欠费
     *
     * @param queryFeeStatisticsDto 数据对象分享
     * @return
     */
    @Override
    public double getHisMonthOweFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getHisMonthOweFee(BeanConvertUtil.beanCovertMap(queryFeeStatisticsDto));
        return info;
    }

    @Override
    public double getCurMonthOweFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getCurMonthOweFee(BeanConvertUtil.beanCovertMap(queryFeeStatisticsDto));
        return info;
    }

    @Override
    public double getOweFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getOweFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    /**
     * 查询当月应收
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getCurReceivableFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getCurReceivableFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getHisReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getHisReceivedFee(BeanConvertUtil.beanCovertMap(queryFeeStatisticsDto));
        return info;
    }

    @Override
    public double getPreReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getPreReceivedFee(BeanConvertUtil.beanCovertMap(queryFeeStatisticsDto));
        return info;
    }

    @Override
    public double getReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getReceivedFee(BeanConvertUtil.beanCovertMap(queryFeeStatisticsDto));
        return info;
    }

    /**
     * 查询欠费户数
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public int getOweRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        int info = reportFeeStatisticsServiceDaoImpl.getOweRoomCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    /**
     * 查询收费房屋数
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public long getFeeRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        int info = reportFeeStatisticsServiceDaoImpl.getFeeRoomCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    /**
     * 楼栋收费率信息统计
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public List<Map> getFloorFeeSummary(@RequestBody QueryStatisticsDto queryStatisticsDto) {

        List<Map> info = reportFeeStatisticsServiceDaoImpl.getFloorFeeSummary(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    /**
     * 费用项收费率信息统计
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public List<Map> getConfigFeeSummary(@RequestBody QueryStatisticsDto queryStatisticsDto) {

        List<Map> info = reportFeeStatisticsServiceDaoImpl.getConfigFeeSummary(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public int getObjFeeSummaryCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        int info = reportFeeStatisticsServiceDaoImpl.getObjFeeSummaryCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public List<Map> getObjFeeSummary(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        //校验是否传了 分页信息

        int page = queryStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            queryStatisticsDto.setPage((page - 1) * queryStatisticsDto.getRow());
        }

        List<Map> info = reportFeeStatisticsServiceDaoImpl.getObjFeeSummary(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public List<Map> getOwnerFeeSummary(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        int page = queryStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            queryStatisticsDto.setPage((page - 1) * queryStatisticsDto.getRow());
        }

        List<Map> info = reportFeeStatisticsServiceDaoImpl.getOwnerFeeSummary(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getDiscountFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getDiscountFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getLateFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getLateFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getPrestoreAccount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getPrestoreAccount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getWithholdAccount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getWithholdAccount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    /**
     * 临时车收入
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getTempCarFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getTempCarFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    /**
     * 查询退款押金
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double geRefundDeposit(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.geRefundDeposit(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double geRefundOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.geRefundOrderCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double geRefundFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.geRefundFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getChargeFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getChargeFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public List<Map> getReceivedFeeByFloor(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> infos = reportFeeStatisticsServiceDaoImpl.getReceivedFeeByFloor(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return infos;
    }

    /**
     * 按收款方式统计收入
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public List<Map> getReceivedFeeByPrimeRate(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> infos = reportFeeStatisticsServiceDaoImpl.getReceivedFeeByPrimeRate(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return infos;
    }

    @Override
    public List<Map> getOweFeeByFloor(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> infos = reportFeeStatisticsServiceDaoImpl.getOweFeeByFloor(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return infos;
    }

    @Override
    public List<Map> getObjOweFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> infos = reportFeeStatisticsServiceDaoImpl.getObjOweFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return infos;
    }

    @Override
    public long getReceivedRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportFeeStatisticsServiceDaoImpl.getReceivedRoomCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getReceivedRoomAmount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getReceivedRoomAmount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public long getHisOweReceivedRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        long info = reportFeeStatisticsServiceDaoImpl.getHisOweReceivedRoomCount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public double getHisOweReceivedRoomAmount(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        double info = reportFeeStatisticsServiceDaoImpl.getHisOweReceivedRoomAmount(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return info;
    }

    @Override
    public List<Map> getObjReceivedFee(@RequestBody QueryStatisticsDto queryStatisticsDto) {
        List<Map> infos = reportFeeStatisticsServiceDaoImpl.getObjReceivedFee(BeanConvertUtil.beanCovertMap(queryStatisticsDto));
        return infos;
    }
}
