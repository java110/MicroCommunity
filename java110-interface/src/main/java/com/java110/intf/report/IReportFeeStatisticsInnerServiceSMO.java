package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.report.QueryStatisticsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IReportFeeStatisticsInnerServiceSMO
 * @Description 费用统计类 服务类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportFeeStatisticsApi")
public interface IReportFeeStatisticsInnerServiceSMO {


    /**
     * <p>查询历史月欠费</p>
     *
     * @param queryFeeStatisticsDto 数据对象分享
     */
    @RequestMapping(value = "/getHisMonthOweFee", method = RequestMethod.POST)
    double getHisMonthOweFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto);

    /**
     * 查询当月欠费
     *
     * @param queryFeeStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getCurMonthOweFee", method = RequestMethod.POST)
    double getCurMonthOweFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto);

    /**
     * 查询欠费金额
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getOweFee", method = RequestMethod.POST)
    double getOweFee(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询当月应收
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getCurReceivableFee", method = RequestMethod.POST)
    double getCurReceivableFee(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getHisReceivedFee", method = RequestMethod.POST)
    double getHisReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto);

    @RequestMapping(value = "/getPreReceivedFee", method = RequestMethod.POST)
    double getPreReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto);

    @RequestMapping(value = "/getReceivedFee", method = RequestMethod.POST)
    double getReceivedFee(@RequestBody QueryStatisticsDto queryFeeStatisticsDto);

    /**
     * 欠费户数
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getOweRoomCount", method = RequestMethod.POST)
    int getOweRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询收费房屋数
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getFeeRoomCount", method = RequestMethod.POST)
    long getFeeRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 楼栋收费率信息统计
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getFloorFeeSummary", method = RequestMethod.POST)
    List<Map> getFloorFeeSummary(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 费用项收费率统计
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getConfigFeeSummary", method = RequestMethod.POST)
    List<Map> getConfigFeeSummary(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getObjFeeSummaryCount", method = RequestMethod.POST)
    int getObjFeeSummaryCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getObjFeeSummary", method = RequestMethod.POST)
    List<Map> getObjFeeSummary(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 业主明细表
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getOwnerFeeSummary", method = RequestMethod.POST)
    List<Map> getOwnerFeeSummary(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 优惠费用
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getDiscountFee", method = RequestMethod.POST)
    double getDiscountFee(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 滞纳金
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getLateFee", method = RequestMethod.POST)
    double getLateFee(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询预存金额
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getPrestoreAccount", method = RequestMethod.POST)
    double getPrestoreAccount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询扣款金额
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getWithholdAccount", method = RequestMethod.POST)
    double getWithholdAccount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询临时车收入
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getTempCarFee", method = RequestMethod.POST)
    double getTempCarFee(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询退款押金
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/geRefundDeposit", method = RequestMethod.POST)
    double geRefundDeposit(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/geRefundOrderCount", method = RequestMethod.POST)
    double geRefundOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/geRefundFee", method = RequestMethod.POST)
    double geRefundFee(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询充电桩 充电金额
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getChargeFee", method = RequestMethod.POST)
    double getChargeFee(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询楼栋实收统计
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getReceivedFeeByFloor", method = RequestMethod.POST)
    List<Map> getReceivedFeeByFloor(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getReceivedFeeByPrimeRate", method = RequestMethod.POST)
    List<Map> getReceivedFeeByPrimeRate(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 根据楼栋查询欠费信息
     *
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getOweFeeByFloor", method = RequestMethod.POST)
    List<Map> getOweFeeByFloor(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getObjOweFee", method = RequestMethod.POST)
    List<Map> getObjOweFee(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询当日 或者当月已收房屋数
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getReceivedRoomCount", method = RequestMethod.POST)
    long getReceivedRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询当日 或者当月已收金额
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getReceivedRoomAmount", method = RequestMethod.POST)
    double getReceivedRoomAmount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询历史欠费 清缴户
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getHisOweReceivedRoomCount", method = RequestMethod.POST)
    long getHisOweReceivedRoomCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getHisOweReceivedRoomAmount", method = RequestMethod.POST)
    double getHisOweReceivedRoomAmount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getObjReceivedFee", method = RequestMethod.POST)
    List<Map> getObjReceivedFee(@RequestBody QueryStatisticsDto queryStatisticsDto);
}
