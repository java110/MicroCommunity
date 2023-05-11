package com.java110.core.smo;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.integral.IntegralRuleConfigDto;
import com.java110.dto.machine.CarInoutDetailDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.report.ReportCarDto;
import com.java110.dto.report.ReportFeeDto;
import com.java110.dto.report.ReportRoomDto;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 费用计算 服务类
 * <p>
 * add by wuxw 2020-09-23
 *
 * @openSource https://gitee.com/wuxw7/MicroCommunity.git
 */
public interface IComputeFeeSMO {


    /**
     * 计算费用结束时间
     *
     * @return
     */
    Date getFeeEndTime();

    /**
     * 获取 deadlineTime
     * @param feeDto
     * @return
     */
    Date getDeadlineTime(FeeDto feeDto);

    /**
     * 计算欠费金额
     *
     * @param tmpFeeDto
     */
    public void computeOweFee(FeeDto tmpFeeDto);

    /**
     * 刷新
     *
     * @param feeDto
     * @param feeReceiptDetailPo
     */
    void freshFeeReceiptDetail(FeeDto feeDto, FeeReceiptDetailPo feeReceiptDetailPo);

    /**
     * 根据周期 计算费用状态
     *
     * @param feeDto
     * @param cycles
     * @return
     */
    public String getFeeStateByCycles(FeeDto feeDto, String cycles);

    /**
     * 查询费用对象名称
     *
     * @param feeDto
     * @return
     */
    public String getFeeObjName(FeeDto feeDto);

    /**
     * 查询费用的业主信息
     *
     * @param feeDto
     * @return
     */
    public OwnerDto getFeeOwnerDto(FeeDto feeDto);

    /**
     * 刷入 付费方名称
     *
     * @param feeDtos
     */
    public void freshFeeObjName(List<FeeDto> feeDtos);

    /**
     * 根据缴费周期计算 结束时间
     *
     * @param feeDto
     * @param cycles 缴费周期
     * @return
     */
    public Date getFeeEndTimeByCycles(FeeDto feeDto, String cycles);


    /**
     * 获取目标结束时间
     *
     * @param month     月份
     * @param startDate 开始时间
     * @return 新的日期
     */
    Date getTargetEndTime(double month, Date startDate);


    /**
     * 计算周期
     *
     * @return
     */
    double getCycle();

    public Map getTargetEndDateAndOweMonth(FeeDto feeDto, OwnerCarDto ownerCarDto);

    /**
     * 获取目标结束时间和欠费月份
     *
     * @param feeDto
     * @return {
     * targetEndDate:2020-09-09 11:11:11
     * oweMonth:1.0
     * }
     */
    public Map getTargetEndDateAndOweMonth(FeeDto feeDto);


    /**
     * 计算费用单价
     *
     * @return
     */
    public Map getFeePrice(FeeDto feeDto);


    public Map getFeePrice(FeeDto feeDto, RoomDto roomDto);

    /**
     * 时间差 按天折算
     *
     * @param fromDate 开始时间
     * @param toDate   结束时间
     * @return 相差月数
     */
    double dayCompare(Date fromDate, Date toDate);

    /**
     　　 *字符串的日期格式的计算
     　　 */
    long daysBetween(Date smdate,Date bdate) ;

    double getReportFeePrice(ReportFeeDto tmpReportFeeDto, ReportRoomDto reportRoomDto, ReportCarDto reportCarDto);

    void computeEveryOweFee(FeeDto tmpFeeDto);

    void computeEveryOweFee(FeeDto tmpFeeDto, RoomDto roomDto);

    /**
     * 计算停车时间和费用
     *
     * @param carInoutDtos
     */
    List<CarInoutDto> computeTempCarStopTimeAndFee(List<CarInoutDto> carInoutDtos);

    /**
     * 计算停车时间和费用
     *
     * @param carInoutDtos
     */
    List<CarInoutDetailDto> computeTempCarInoutDetailStopTimeAndFee(List<CarInoutDetailDto> carInoutDtos);

    /**
     * 租金处理
     *
     * @param feeDto
     */
    void dealRentRate(FeeDto feeDto);

    /**
     * 租金处理
     *
     * @param feeDto
     */
    void dealRentRateCycle(FeeDto feeDto,double cycle);


    /**
     * 租金处理
     * @param feeDto
     * @param custEndTime
     */
    void dealRentRateCustEndTime(FeeDto feeDto, Date custEndTime);

    long computeOneIntegralQuantity(IntegralRuleConfigDto integralRuleConfigDto, JSONObject reqJson);
}
