package com.java110.report.statistics.impl;

import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.report.statistics.IFeeStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 基础报表统计 实现类
 */
@Service
public class FeeStatisticsImpl implements IFeeStatistics {

    @Autowired
    private IReportFeeStatisticsInnerServiceSMO reportFeeStatisticsInnerServiceSMOImpl;

    /**
     * 查询 历史欠费
     *
     * @param queryFeeStatisticsDto
     * @return
     */
    @Override
    public double getHisMonthOweFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getHisMonthOweFee(queryFeeStatisticsDto);
    }

    /**
     * 查询 当月欠费
     *
     * @param queryFeeStatisticsDto
     * @return
     */
    @Override
    public double getCurMonthOweFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getCurMonthOweFee(queryFeeStatisticsDto);
    }

    /**
     * 查询总欠费
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getOweFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getOweFee(queryStatisticsDto);
    }


    /**
     * 查询当月应收
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getCurReceivableFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getCurReceivableFee(queryStatisticsDto);
    }


    /**
     * 查询 欠费追回
     *
     * @param queryFeeStatisticsDto
     * @return
     */
    @Override
    public double getHisReceivedFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getHisReceivedFee(queryFeeStatisticsDto);
    }

    /**
     * 查询 预交费用
     *
     * @param queryFeeStatisticsDto
     * @return
     */
    @Override
    public double getPreReceivedFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getPreReceivedFee(queryFeeStatisticsDto);
    }

    /**
     * 查询 实收费用
     *
     * @param queryFeeStatisticsDto
     * @return
     */
    @Override
    public double getReceivedFee(QueryStatisticsDto queryFeeStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getReceivedFee(queryFeeStatisticsDto);
    }

    @Override
    public int getOweRoomCount(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getOweRoomCount(queryStatisticsDto);
    }

    @Override
    public long getFeeRoomCount(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getFeeRoomCount(queryStatisticsDto);
    }

    /**
     * 楼栋收费率信息统计
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public List<Map> getFloorFeeSummary(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getFloorFeeSummary(queryStatisticsDto);
    }

    /**
     * 费用项收费率信息统计
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public List<Map> getConfigFeeSummary(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getConfigFeeSummary(queryStatisticsDto);
    }

    @Override
    public int getObjFeeSummaryCount(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getObjFeeSummaryCount(queryStatisticsDto);
    }

    @Override
    public List<Map> getObjFeeSummary(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getObjFeeSummary(queryStatisticsDto);
    }

    /**
     * 查询 业主明细表
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public List<Map> getOwnerFeeSummary(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getOwnerFeeSummary(queryStatisticsDto);
    }

    /**
     * 优惠金额
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getDiscountFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getDiscountFee(queryStatisticsDto);
    }

    /**
     * 滞纳金
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getLateFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getLateFee(queryStatisticsDto);
    }

    @Override
    public double getPrestoreAccount(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getPrestoreAccount(queryStatisticsDto);
    }

    @Override
    public double getWithholdAccount(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getWithholdAccount(queryStatisticsDto);
    }

    /**
     * 查询临时车收入
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public double getTempCarFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getTempCarFee(queryStatisticsDto);
    }

    @Override
    public double geRefundDeposit(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.geRefundDeposit(queryStatisticsDto);
    }

    @Override
    public double geRefundOrderCount(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.geRefundOrderCount(queryStatisticsDto);
    }

    @Override
    public double geRefundFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.geRefundFee(queryStatisticsDto);
    }

    @Override
    public double getChargeFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getChargeFee(queryStatisticsDto);
    }

    @Override
    public List<Map> getReceivedFeeByFloor(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getReceivedFeeByFloor(queryStatisticsDto);
    }

    /**
     * 收款方式统计
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public List<Map> getReceivedFeeByPrimeRate(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getReceivedFeeByPrimeRate(queryStatisticsDto);
    }

    /**
     * 根据楼栋查询欠费信息
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public List<Map> getOweFeeByFloor(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getOweFeeByFloor(queryStatisticsDto);
    }

    /**
     * 计算对象欠费明细
     *
     * @param queryStatisticsDto
     * @return
     */
    @Override
    public List<Map> getObjOweFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getObjOweFee(queryStatisticsDto);
    }

    @Override
    public long getReceivedRoomCount(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getReceivedRoomCount(queryStatisticsDto);
    }

    @Override
    public double getReceivedRoomAmount(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getReceivedRoomAmount(queryStatisticsDto);
    }

    @Override
    public long getHisOweReceivedRoomCount(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getHisOweReceivedRoomCount(queryStatisticsDto);
    }

    @Override
    public double getHisOweReceivedRoomAmount(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getHisOweReceivedRoomAmount(queryStatisticsDto);
    }

    @Override
    public List<Map> getObjReceivedFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFeeStatisticsInnerServiceSMOImpl.getObjReceivedFee(queryStatisticsDto);
    }

}
