package com.java110.report.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.PageDto;
import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentTotalDto;
import com.java110.dto.fee.FeeAccountDetailDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsTotalDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IFeeAccountDetailServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.report.IQueryPayFeeDetailInnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsPrepaymentInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询缴费明细
 */
@RestController
public class QueryPayFeeDetailInnerServiceSMOImpl implements IQueryPayFeeDetailInnerServiceSMO {

    private int MAX_ROWS = 500;  // 最大行数

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    @Autowired
    private IReportFeeMonthStatisticsPrepaymentInnerServiceSMO reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IFeeAccountDetailServiceSMO feeAccountDetailServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    /**
     * 查询缴费明细
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @Override
    public ResultVo query(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {

        JSONObject countInfo = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDetailCount(reportFeeMonthStatisticsDto);
        int count = Integer.parseInt(countInfo.get("count").toString());
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        ReportFeeMonthStatisticsTotalDto reportFeeMonthStatisticsTotalDto = new ReportFeeMonthStatisticsTotalDto();
        ResultVo resultVo = null;
        if (count < 1) {
            resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos, reportFeeMonthStatisticsTotalDto);
            return resultVo;
        }

        //todo 查询缴费明细
        reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDetail(reportFeeMonthStatisticsDto);

        //todo 应收金额(大计)
        reportFeeMonthStatisticsTotalDto.setAllReceivableAmount(String.format("%.2f", countInfo.getDouble("totalReceivableAmount")));
        //todo 实收金额(大计)
        reportFeeMonthStatisticsTotalDto.setAllReceivedAmount(String.format("%.2f", countInfo.getDouble("totalReceivedAmount")));

        //todo 打折金额（大计）
        reportFeeMonthStatisticsTotalDto.setAllPreferentialAmount(String.format("%.2f", countInfo.getDouble("discountAmount")));

        //todo 减免金额(大计)
        reportFeeMonthStatisticsTotalDto.setAllDeductionAmount(String.format("%.2f", countInfo.getDouble("deductionAmount")));
        //todo 滞纳金(大计)
        reportFeeMonthStatisticsTotalDto.setAllLateFee(String.format("%.2f", countInfo.getDouble("lateAmount")));

        //todo 赠送规则金额(大计)
        reportFeeMonthStatisticsTotalDto.setAllGiftAmount(String.format("%.2f", countInfo.getDouble("giftAmount")));

        //todo 计算小计
        computeTotalInfo(reportFeeMonthStatisticsTotalDto, reportFeeMonthStatisticsDtos);

        //todo 计算房屋面积 和车位信息
        computeRoomAndParkingSpace(reportFeeMonthStatisticsDtos);


        resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos, reportFeeMonthStatisticsTotalDto);
        return resultVo;
    }

    private void computeRoomAndParkingSpace(List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos) {

        List<String> payerObjIds = new ArrayList<>();

        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            payerObjIds.add(reportFeeMonthStatisticsDto.getPayerObjId());
        }

        if (ListUtil.isNull(payerObjIds)) {
            return;
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(payerObjIds.toArray(new String[payerObjIds.size()]));
        roomDto.setCommunityId(reportFeeMonthStatisticsDtos.get(0).getCommunityId());
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);

        if (!ListUtil.isNull(roomDtos)) {
            for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
                for (RoomDto tmpRoomDto : roomDtos) {
                    if (reportFeeMonthStatisticsDto.getPayerObjId().equals(tmpRoomDto.getRoomId())) {
                        reportFeeMonthStatisticsDto.setBuiltUpArea(tmpRoomDto.getBuiltUpArea());
                    }
                }
            }
        }

        computeParkingSpace(reportFeeMonthStatisticsDtos, payerObjIds);

    }

    private void computeParkingSpace(List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos, List<String> payerObjIds) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setMemberIds(payerObjIds.toArray(new String[payerObjIds.size()]));
        ownerCarDto.setCommunityId(reportFeeMonthStatisticsDtos.get(0).getCommunityId());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            return;
        }
        freshRoomInfo(ownerCarDtos);
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
                if (!reportFeeMonthStatisticsDto.getPayerObjId().equals(tmpOwnerCarDto.getMemberId())) {
                    continue;
                }
                reportFeeMonthStatisticsDto.setPsName(tmpOwnerCarDto.getAreaNum() + "-" + tmpOwnerCarDto.getNum());
                reportFeeMonthStatisticsDto.setRoomName(tmpOwnerCarDto.getRoomName());
            }
        }

    }

    /**
     * 刷入房屋信息
     *
     * @param ownerCarDtos
     */
    private void freshRoomInfo(List<OwnerCarDto> ownerCarDtos) {

        if (ListUtil.isNull(ownerCarDtos) || ownerCarDtos.size() > 30) {
            return;
        }
        for (OwnerCarDto ownerCarDto : ownerCarDtos) {
            doFreshRoomInfo(ownerCarDto);
        }
    }

    /**
     * 车位信息刷入房屋信息
     *
     * @param ownerCarDto
     */
    private void doFreshRoomInfo(OwnerCarDto ownerCarDto) {
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerCarDto.getOwnerId());
        ownerRoomRelDto.setPage(1);
        ownerRoomRelDto.setRow(3); //只展示3个房屋以内 不然页面太乱
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ListUtil.isNull(ownerRoomRelDtos)) {
            ownerCarDto.setRoomName("-");
            return;
        }
        List<String> roomIds = new ArrayList<>();
        for (OwnerRoomRelDto tOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tOwnerRoomRelDto.getRoomId());
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(ownerCarDto.getCommunityId());
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        String roomName = "";
        for (RoomDto tRoomDto : roomDtos) {
            roomName += (tRoomDto.getFloorNum() + "-" + tRoomDto.getUnitNum() + "-" + tRoomDto.getRoomNum() + "-" + "/");
        }
        roomName = roomName.endsWith("/") ? roomName.substring(0, roomName.length() - 1) : roomName;
        ownerCarDto.setRoomName(roomName);
    }

    /**
     * 计算大计小计
     *
     * @param reportFeeMonthStatisticsTotalDto
     */
    private void computeTotalInfo(ReportFeeMonthStatisticsTotalDto reportFeeMonthStatisticsTotalDto,
                                  List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos) {

        BigDecimal totalReceivableAmount = new BigDecimal(0.00);

        BigDecimal totalReceivedAmount = new BigDecimal(0.00);
        BigDecimal totalPreferentialAmount = new BigDecimal(0.00);
        BigDecimal totalDeductionAmount = new BigDecimal(0.00);
        BigDecimal totalLateFee = new BigDecimal(0.00);
        BigDecimal totalGiftAmount = new BigDecimal(0.00);

        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            totalReceivableAmount = totalReceivableAmount.add(new BigDecimal(reportFeeMonthStatisticsDto.getReceivableAmount()));
            totalReceivedAmount = totalReceivedAmount.add(new BigDecimal(reportFeeMonthStatisticsDto.getReceivedAmount()));
            totalPreferentialAmount = totalPreferentialAmount.add(new BigDecimal(reportFeeMonthStatisticsDto.getDiscountAmount()));
            totalDeductionAmount = totalDeductionAmount.add(new BigDecimal(reportFeeMonthStatisticsDto.getDeductionAmount()));
            totalLateFee = totalLateFee.add(new BigDecimal(reportFeeMonthStatisticsDto.getLateAmount()));
            totalGiftAmount = totalGiftAmount.add(new BigDecimal(reportFeeMonthStatisticsDto.getGiftAmount()));

            reportFeeMonthStatisticsDto.setWithholdAmount(reportFeeMonthStatisticsDto.getAcctAmount());
            reportFeeMonthStatisticsDto.setPreferentialAmount(reportFeeMonthStatisticsDto.getDiscountAmount());
            reportFeeMonthStatisticsDto.setLateFee(reportFeeMonthStatisticsDto.getLateAmount());
        }

        //todo 应收金额(小计)
        reportFeeMonthStatisticsTotalDto.setTotalReceivableAmount(String.format("%.2f", totalReceivableAmount.doubleValue()));

        //todo 实收金额(小计)
        reportFeeMonthStatisticsTotalDto.setTotalReceivedAmount(String.format("%.2f", totalReceivedAmount.doubleValue()));
        //todo 优惠金额(小计)
        reportFeeMonthStatisticsTotalDto.setTotalPreferentialAmount(String.format("%.2f", totalPreferentialAmount));
        //todo 减免金额(小计)
        reportFeeMonthStatisticsTotalDto.setTotalDeductionAmount(String.format("%.2f", totalDeductionAmount));
        //todo 滞纳金(小计)
        reportFeeMonthStatisticsTotalDto.setTotalLateFee(String.format("%.2f", totalLateFee));

    }


    @Override
    public ResultVo queryPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        JSONObject countInfo = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryPayFeeDetailPrepaymentCount(reportFeeMonthStatisticsPrepaymentDto);
        int count = Integer.parseInt(countInfo.get("count").toString());
        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentDtos = null;
        ReportFeeMonthStatisticsPrepaymentTotalDto reportFeeMonthStatisticsPrepaymentTotalDto = new ReportFeeMonthStatisticsPrepaymentTotalDto();
        List<ReportFeeMonthStatisticsPrepaymentDto> reportList = new ArrayList<>();
        //查询该小区下的费用项目
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reportFeeMonthStatisticsPrepaymentDto.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryFeeConfigPrepayments(feeConfigDto);
        //应收总金额(大计)
        Double allReceivableAmount = 0.0;
        //实收金额(大计)
        Double allReceivedAmount = 0.0;
        //优惠金额(大计)
        Double allPreferentialAmount = 0.0;
        //减免金额(大计)
        Double allDeductionAmount = 0.0;
        //滞纳金(大计)
        Double allLateFee = 0.0;
        //空置房打折(大计)
        Double allVacantHousingDiscount = 0.0;
        //空置房减免(大计)
        Double allVacantHousingReduction = 0.0;
        //赠送金额(大计)
        Double allGiftAmount = 0.0;
        //欠费金额(大计)
        Double allOweAmount = 0.0;
        //应缴金额(大计)
        Double allPayableAmount = 0.0;
        if (count > 0) {
            //查询缴费明细
            reportFeeMonthStatisticsPrepaymentDtos = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryNewPayFeeDetailPrepayment(reportFeeMonthStatisticsPrepaymentDto);
            //查询应收、实收总金额(大计)
            List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentList = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryAllPayFeeDetailPrepayment(reportFeeMonthStatisticsPrepaymentDto);
            //查询(优惠、减免、滞纳金、空置房打折、空置房减免金额等)大计总金额
            List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentSum = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryPayFeeDetailPrepaymentSum(reportFeeMonthStatisticsPrepaymentDto);
            allReceivableAmount = Double.valueOf(reportFeeMonthStatisticsPrepaymentList.get(0).getAllReceivableAmount());
            allReceivedAmount = Double.valueOf(reportFeeMonthStatisticsPrepaymentList.get(0).getAllReceivedAmount());
            allOweAmount = Double.valueOf(reportFeeMonthStatisticsPrepaymentList.get(0).getAllOweAmount());
            allPayableAmount = Double.valueOf(reportFeeMonthStatisticsPrepaymentList.get(0).getAllPayableAmount());
            for (ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepayment : reportFeeMonthStatisticsPrepaymentSum) {
                //这里是查询出的金额总和
                String discountPrice = reportFeeMonthStatisticsPrepayment.getDiscountPrice();
                // discountSmallType 1: 打折  2:减免  3:滞纳金  4:空置房打折  5:空置房减免  6:赠送
                //优惠金额(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("1")) {
                    Double aDouble = Double.valueOf(discountPrice);
                    allPreferentialAmount = allPreferentialAmount + aDouble;
                }
                //减免金额(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("2")) {
                    Double aDouble = Double.valueOf(discountPrice);
                    allDeductionAmount = allDeductionAmount + aDouble;
                }
                //滞纳金(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("3")) {
                    Double aDouble = Double.valueOf(discountPrice);
                    allLateFee = allLateFee + aDouble;
                }
                //空置房打折金额(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("4")) {
                    Double aDouble = Double.valueOf(discountPrice);
                    allVacantHousingDiscount = allVacantHousingDiscount + aDouble;
                }
                //空置房减免金额(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("5")) {
                    Double aDouble = Double.valueOf(discountPrice);
                    allVacantHousingReduction = allVacantHousingReduction + aDouble;
                }
                //赠送金额(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("6")) {
                    Double aDouble = Double.valueOf(discountPrice);
                    allGiftAmount = allGiftAmount + aDouble;
                }
            }
            //应收总金额(小计)
            Double totalReceivableAmount = 0.0;
            //实收总金额(小计)
            Double totalReceivedAmount = 0.0;
            //优惠金额(小计)
            Double totalPreferentialAmount = 0.0;
            //减免金额(小计)
            Double totalDeductionAmount = 0.0;
            //空置房打折金额(小计)
            Double totalVacantHousingDiscount = 0.0;
            //空置房减免金额(小计)
            Double totalVacantHousingReduction = 0.0;
            //赠送金额(小计)
            Double totalGiftAmount = 0.0;
            //滞纳金(小计)
            Double totalLateFee = 0.0;
            //欠费金额(小计)
            Double totalOweAmount = 0.0;
            //应缴金额(小计)
            Double totalPayableAmount = 0.0;
            for (ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepayment : reportFeeMonthStatisticsPrepaymentDtos) {
                //应收金额
                Double receivableAmount = 0.0;
                if (!StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getPrepaymentReceivableAmount())) {
                    receivableAmount = Double.valueOf(reportFeeMonthStatisticsPrepayment.getPrepaymentReceivableAmount());
                }
                //实收金额
                Double receivedAmount = 0.0;
                if (!StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getPrepaymentReceivedAmount())) {
                    receivedAmount = Double.valueOf(reportFeeMonthStatisticsPrepayment.getPrepaymentReceivedAmount());
                }
                //欠费金额
                Double oweAmount = 0.0;
                if (!StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getOweAmount())) {
                    oweAmount = Double.valueOf(reportFeeMonthStatisticsPrepayment.getOweAmount());
                }
                //应缴金额
                Double payableAmount = 0.0;
                if (!StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getPayableAmount())) {
                    payableAmount = Double.valueOf(reportFeeMonthStatisticsPrepayment.getPayableAmount());
                }
                totalReceivableAmount = totalReceivableAmount + receivableAmount;
                totalReceivedAmount = totalReceivedAmount + receivedAmount;
                totalOweAmount = totalOweAmount + oweAmount;
                totalPayableAmount = totalPayableAmount + payableAmount;
                ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto1 = new ReportFeeMonthStatisticsPrepaymentDto();
                reportFeeMonthStatisticsPrepaymentDto1.setPrepaymentId(reportFeeMonthStatisticsPrepayment.getPrepaymentId());
                List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryPayFeeDetailPrepayment(reportFeeMonthStatisticsPrepaymentDto1);
                for (ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatistics : reportFeeMonthStatisticsPrepayments) {
                    //优惠金额
                    if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountPrice()) && !StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType())
                            && reportFeeMonthStatistics.getDiscountSmallType().equals("1")) {
                        //获取优惠金额
                        Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                        totalPreferentialAmount = totalPreferentialAmount + discountPrice;
                        //优惠金额
                        reportFeeMonthStatisticsPrepayment.setPreferentialAmount(reportFeeMonthStatistics.getDiscountPrice());
                    } else if (StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getPreferentialAmount())) {
                        reportFeeMonthStatisticsPrepayment.setPreferentialAmount("0");
                    }
                    //减免金额
                    if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountPrice()) && !StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType())
                            && reportFeeMonthStatistics.getDiscountSmallType().equals("2")) {
                        //获取减免金额
                        Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                        totalDeductionAmount = totalDeductionAmount + discountPrice;
                        //减免金额
                        reportFeeMonthStatisticsPrepayment.setDeductionAmount(reportFeeMonthStatistics.getDiscountPrice());
                    } else if (StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDeductionAmount())) {
                        reportFeeMonthStatisticsPrepayment.setDeductionAmount("0");
                    }
                    //滞纳金
                    if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountPrice()) && !StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType())
                            && reportFeeMonthStatistics.getDiscountSmallType().equals("3")) {
                        //获取滞纳金金额
                        Double discountPrice = (Double.valueOf(reportFeeMonthStatistics.getDiscountPrice()));
                        totalLateFee = totalLateFee + discountPrice;
                        //滞纳金
                        reportFeeMonthStatisticsPrepayment.setLateFee(reportFeeMonthStatistics.getDiscountPrice());
                    } else if (StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getLateFee())) {
                        reportFeeMonthStatisticsPrepayment.setLateFee("0");
                    }
                    //空置房打折
                    if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountPrice()) && !StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType())
                            && reportFeeMonthStatistics.getDiscountSmallType().equals("4")) {
                        //空置房打折金额
                        Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                        totalVacantHousingDiscount = totalVacantHousingDiscount + discountPrice;
                        //空置房打折
                        reportFeeMonthStatisticsPrepayment.setVacantHousingDiscount(reportFeeMonthStatistics.getDiscountPrice());
                    } else if (StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getVacantHousingDiscount())) {
                        reportFeeMonthStatisticsPrepayment.setVacantHousingDiscount("0");
                    }
                    //空置房减免
                    if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountPrice()) && !StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType())
                            && reportFeeMonthStatistics.getDiscountSmallType().equals("5")) {
                        //空置房减免金额
                        Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                        totalVacantHousingReduction = totalVacantHousingReduction + discountPrice;
                        //空置房减免
                        reportFeeMonthStatisticsPrepayment.setVacantHousingReduction(reportFeeMonthStatistics.getDiscountPrice());
                    } else if (StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getVacantHousingReduction())) {
                        reportFeeMonthStatisticsPrepayment.setVacantHousingReduction("0");
                    }
                    //赠送金额
                    if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountPrice()) && !StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType())
                            && reportFeeMonthStatistics.getDiscountSmallType().equals("6")) {
                        //赠送金额
                        Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                        totalGiftAmount = totalGiftAmount + discountPrice;
                        //赠送金额
                        reportFeeMonthStatisticsPrepayment.setGiftAmount(reportFeeMonthStatistics.getDiscountPrice());
                    } else if (StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getGiftAmount())) {
                        reportFeeMonthStatisticsPrepayment.setGiftAmount("0");
                    }
                }
                //费用项目
                reportFeeMonthStatisticsPrepayment.setFeeConfigDtos(feeConfigDtos);
                reportList.add(reportFeeMonthStatisticsPrepayment);
            }
            //应收总金额(小计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setTotalReceivableAmount(String.format("%.2f", totalReceivableAmount));
            //实收金额(小计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setTotalReceivedAmount(String.format("%.2f", totalReceivedAmount));
            //欠费金额(小计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setTotalOweAmount(String.format("%.2f", totalOweAmount));
            //应缴金额(小计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setTotalPayableAmount(String.format("%.2f", totalPayableAmount));
            //优惠金额(小计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setTotalPreferentialAmount(String.format("%.2f", totalPreferentialAmount));
            //减免金额(小计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setTotalDeductionAmount(String.format("%.2f", totalDeductionAmount));
            //滞纳金(小计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setTotalLateFee(String.format("%.2f", totalLateFee));
            //空置房打折(小计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setTotalVacantHousingDiscount(String.format("%.2f", totalVacantHousingDiscount));
            //空置房减免(小计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setTotalVacantHousingReduction(String.format("%.2f", totalVacantHousingReduction));
            //赠送规则金额(小计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setTotalGiftAmount(String.format("%.2f", totalGiftAmount));
            //应收金额(大计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setAllReceivableAmount(String.format("%.2f", allReceivableAmount));
            //欠费金额(大计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setAllOweAmount(String.format("%.2f", allOweAmount));
            //欠费金额(大计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setAllPayableAmount(String.format("%.2f", allPayableAmount));
            //实收金额(大计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setAllReceivedAmount(String.format("%.2f", allReceivedAmount));
            //优惠金额(大计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setAllPreferentialAmount(String.format("%.2f", allPreferentialAmount));
            //减免金额(大计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setAllDeductionAmount(String.format("%.2f", allDeductionAmount));
            //滞纳金(大计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setAllLateFee(String.format("%.2f", allLateFee));
            //空置房打折金额(大计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setAllVacantHousingDiscount(String.format("%.2f", allVacantHousingDiscount));
            //空置房减免金额(大计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setAllVacantHousingReduction(String.format("%.2f", allVacantHousingReduction));
            //赠送规则金额(大计)
            reportFeeMonthStatisticsPrepaymentTotalDto.setAllGiftAmount(String.format("%.2f", allGiftAmount));
        } else {
            reportFeeMonthStatisticsPrepaymentDtos = new ArrayList<>();
            reportList.addAll(reportFeeMonthStatisticsPrepaymentDtos);
            reportFeeMonthStatisticsPrepaymentTotalDto = new ReportFeeMonthStatisticsPrepaymentTotalDto();
        }
//        count = reportList.size();
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsPrepaymentDto.getRow()), count, reportList, reportFeeMonthStatisticsPrepaymentTotalDto);
        return resultVo;
    }

    @Override
    public ResultVo queryReportCollectFees(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        ReportFeeMonthStatisticsPrepaymentDto feeMonthStatisticsPrepayment = BeanConvertUtil.covertBean(reportFeeMonthStatisticsPrepaymentDto, ReportFeeMonthStatisticsPrepaymentDto.class);
        //查询预付期账单报表的费用项configId
        List<ReportFeeMonthStatisticsPrepaymentDto> configIds = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryPrepaymentConfigs(feeMonthStatisticsPrepayment);
        int count = configIds.size();
        List<ReportFeeMonthStatisticsPrepaymentDto> reportList = new ArrayList<>();
        //查询该小区下的费用项目
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reportFeeMonthStatisticsPrepaymentDto.getCommunityId());
        if (count > 0) {
            for (ReportFeeMonthStatisticsPrepaymentDto feeMonthStatisticsPrepaymentDto : configIds) {
                reportFeeMonthStatisticsPrepaymentDto.setConfigId(feeMonthStatisticsPrepaymentDto.getConfigId());
                if (reportFeeMonthStatisticsPrepaymentDto.getPage() == 0) {
                    reportFeeMonthStatisticsPrepaymentDto.setPage(1);
                }
                //查询缴费明细
                List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentDtos = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryPayFeeDetailPrepayment(reportFeeMonthStatisticsPrepaymentDto);
                if (reportFeeMonthStatisticsPrepaymentDtos == null || reportFeeMonthStatisticsPrepaymentDtos.size() < 1) {
                    continue;
                }
                //查询应收、实收总金额(大计)
                List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentList = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryAllPayFeeDetailPrepayment(reportFeeMonthStatisticsPrepaymentDto);
                //查询(优惠、减免、滞纳金、空置房打折、空置房减免金额等)大计总金额
                List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentSum = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryPayFeeDetailPrepaymentSum(reportFeeMonthStatisticsPrepaymentDto);
                //应收总金额(大计)
                Double allReceivableAmount = 0.0;
                //实收金额(大计)
                Double allReceivedAmount = 0.0;
                //优惠金额(大计)
                Double allPreferentialAmount = 0.0;
                //减免金额(大计)
                Double allDeductionAmount = 0.0;
                //滞纳金(大计)
                Double allLateFee = 0.0;
                //空置房打折(大计)
                Double allVacantHousingDiscount = 0.0;
                //空置房减免(大计)
                Double allVacantHousingReduction = 0.0;
                //赠送金额(大计)
                Double allGiftAmount = 0.0;
                //欠费金额(大计)
                Double allOweAmount = 0.0;
                //应缴金额(大计)
                Double allPayableAmount = 0.0;
                allReceivableAmount = Double.valueOf(reportFeeMonthStatisticsPrepaymentList.get(0).getAllReceivableAmount());
                allReceivedAmount = Double.valueOf(reportFeeMonthStatisticsPrepaymentList.get(0).getAllReceivedAmount());
                allOweAmount = Double.valueOf(reportFeeMonthStatisticsPrepaymentList.get(0).getAllOweAmount());
                allPayableAmount = Double.valueOf(reportFeeMonthStatisticsPrepaymentList.get(0).getAllPayableAmount());
                for (ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepayment : reportFeeMonthStatisticsPrepaymentSum) {
                    //这里是查询出的金额总和
                    String discountPrice = reportFeeMonthStatisticsPrepayment.getDiscountPrice();
                    // discountSmallType 1: 打折  2:减免  3:滞纳金  4:空置房打折  5:空置房减免  6:赠送
                    //优惠金额(大计)
                    if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("1")) {
                        Double aDouble = Double.valueOf(discountPrice);
                        allPreferentialAmount = allPreferentialAmount + aDouble;
                    }
                    //减免金额(大计)
                    if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("2")) {
                        Double aDouble = Double.valueOf(discountPrice);
                        allDeductionAmount = allDeductionAmount + aDouble;
                    }
                    //滞纳金(大计)
                    if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("3")) {
                        Double aDouble = Double.valueOf(discountPrice);
                        allLateFee = allLateFee + aDouble;
                    }
                    //空置房打折金额(大计)
                    if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("4")) {
                        Double aDouble = Double.valueOf(discountPrice);
                        allVacantHousingDiscount = allVacantHousingDiscount + aDouble;
                    }
                    //空置房减免金额(大计)
                    if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("5")) {
                        Double aDouble = Double.valueOf(discountPrice);
                        allVacantHousingReduction = allVacantHousingReduction + aDouble;
                    }
                    //赠送金额(大计)
                    if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(reportFeeMonthStatisticsPrepayment.getDiscountSmallType()) && reportFeeMonthStatisticsPrepayment.getDiscountSmallType().equals("6")) {
                        Double aDouble = Double.valueOf(discountPrice);
                        allGiftAmount = allGiftAmount + aDouble;
                    }
                }
                ReportFeeMonthStatisticsPrepaymentDto feeMonthStatistics = new ReportFeeMonthStatisticsPrepaymentDto();
                //费用名称
                feeMonthStatistics.setFeeName(reportFeeMonthStatisticsPrepaymentDtos.get(0).getFeeName());
                feeMonthStatistics.setFeeTypeCd(reportFeeMonthStatisticsPrepaymentDtos.get(0).getFeeTypeCd());
                feeMonthStatistics.setFeeTypeCdName(reportFeeMonthStatisticsPrepaymentDtos.get(0).getFeeTypeCdName());
                //应收金额(大计)
                feeMonthStatistics.setAllReceivableAmount(String.format("%.2f", allReceivableAmount));
                //欠费金额(大计)
                feeMonthStatistics.setAllOweAmount(String.format("%.2f", allOweAmount));
                //应缴金额(大计)
                feeMonthStatistics.setAllPayableAmount(String.format("%.2f", allPayableAmount));
                //实收金额(大计)
                feeMonthStatistics.setAllReceivedAmount(String.format("%.2f", allReceivedAmount));
                //优惠金额(大计)
                feeMonthStatistics.setAllPreferentialAmount(String.format("%.2f", allPreferentialAmount));
                //减免金额(大计)
                feeMonthStatistics.setAllDeductionAmount(String.format("%.2f", allDeductionAmount));
                //滞纳金(大计)
                feeMonthStatistics.setAllLateFee(String.format("%.2f", allLateFee));
                //空置房打折金额(大计)
                feeMonthStatistics.setAllVacantHousingDiscount(String.format("%.2f", allVacantHousingDiscount));
                //空置房减免金额(大计)
                feeMonthStatistics.setAllVacantHousingReduction(String.format("%.2f", allVacantHousingReduction));
                //赠送规则金额(大计)
                feeMonthStatistics.setAllGiftAmount(String.format("%.2f", allGiftAmount));
                reportList.add(feeMonthStatistics);
            }
        } else {
            reportList = new ArrayList<>();
        }
        //获取收费状况表大计、小计
        ReportFeeMonthStatisticsPrepaymentTotalDto reportFeeMonthStatisticsPrepaymentTotal = getReportFeeMonthStatisticsPrepaymentTotal(feeMonthStatisticsPrepayment);
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsPrepaymentDto.getRow()), count, reportList, reportFeeMonthStatisticsPrepaymentTotal);
        return resultVo;
    }

    //获取收费状况表大计、小计
    private ReportFeeMonthStatisticsPrepaymentTotalDto getReportFeeMonthStatisticsPrepaymentTotal(ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        ReportFeeMonthStatisticsPrepaymentTotalDto reportFeeMonthStatisticsPrepaymentTotal = new ReportFeeMonthStatisticsPrepaymentTotalDto();
        //查询应缴、应收、实收、欠费金额(小计)
        if (reportFeeMonthStatisticsPrepaymentDto.getPage() == 0) {
            reportFeeMonthStatisticsPrepaymentDto.setPage(1);
        }
        List<ReportFeeMonthStatisticsPrepaymentDto> feeMonthStatisticsPrepaymentDtos = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryAllPrepaymentConfigs(reportFeeMonthStatisticsPrepaymentDto);
        if (feeMonthStatisticsPrepaymentDtos != null && feeMonthStatisticsPrepaymentDtos.size() == 1) {
            reportFeeMonthStatisticsPrepaymentTotal.setTotalPayableAmount(feeMonthStatisticsPrepaymentDtos.get(0).getAllPayableAmount()); //应缴金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setTotalReceivableAmount(feeMonthStatisticsPrepaymentDtos.get(0).getAllReceivableAmount()); //应收金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setTotalReceivedAmount(feeMonthStatisticsPrepaymentDtos.get(0).getAllReceivedAmount()); //实收金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setTotalOweAmount(feeMonthStatisticsPrepaymentDtos.get(0).getAllOweAmount()); //欠费金额(小计)
        } else {
            reportFeeMonthStatisticsPrepaymentTotal.setTotalPayableAmount("0.0"); //应缴金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setTotalReceivableAmount("0.0"); //应收金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setTotalReceivedAmount("0.0"); //实收金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setTotalOweAmount("0.0"); //欠费金额(小计)
        }
        //查询应缴、应收、实收、欠费金额(大计)
        reportFeeMonthStatisticsPrepaymentDto.setPage(PageDto.DEFAULT_PAGE);
        List<ReportFeeMonthStatisticsPrepaymentDto> feeMonthStatisticsPrepayments = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryAllPrepaymentConfigs(reportFeeMonthStatisticsPrepaymentDto);
        if (feeMonthStatisticsPrepayments != null && feeMonthStatisticsPrepayments.size() == 1) {
            reportFeeMonthStatisticsPrepaymentTotal.setAllPayableAmount(feeMonthStatisticsPrepayments.get(0).getAllPayableAmount()); //应缴金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setAllReceivableAmount(feeMonthStatisticsPrepayments.get(0).getAllReceivableAmount()); //应收金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setAllReceivedAmount(feeMonthStatisticsPrepayments.get(0).getAllReceivedAmount()); //实收金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setAllOweAmount(feeMonthStatisticsPrepayments.get(0).getAllOweAmount()); //欠费金额(小计)
        } else {
            reportFeeMonthStatisticsPrepaymentTotal.setAllPayableAmount("0.0"); //应缴金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setAllReceivableAmount("0.0"); //应收金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setAllReceivedAmount("0.0"); //实收金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setAllOweAmount("0.0"); //欠费金额(小计)
        }
        //查询优惠、违约金额(小计)
        if (reportFeeMonthStatisticsPrepaymentDto.getPage() == 0) {
            reportFeeMonthStatisticsPrepaymentDto.setPage(1);
        }
        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryAllPrepaymentDiscounts(reportFeeMonthStatisticsPrepaymentDto);
        if (reportFeeMonthStatisticsPrepayments != null && reportFeeMonthStatisticsPrepayments.size() > 0) {
            for (ReportFeeMonthStatisticsPrepaymentDto feeMonthStatisticsPrepayment : reportFeeMonthStatisticsPrepayments) {
                //获取折扣类型
                String discountSmallType = feeMonthStatisticsPrepayment.getDiscountSmallType();
                //获取折扣金额
                String discountPrice = feeMonthStatisticsPrepayment.getDiscountPrice();
                // discountSmallType 1: 打折  2:减免  3:滞纳金  4:空置房打折  5:空置房减免  6:赠送
                //优惠金额(小计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("1")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setTotalPreferentialAmount(discountPrice); //优惠金额(小计)
                }
                //减免金额(小计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("2")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setTotalDeductionAmount(discountPrice); //减免金额(小计)
                }
                //滞纳金(小计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("3")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setTotalLateFee(discountPrice); //滞纳金(小计)
                }
                //空置房打折金额(小计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("4")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setTotalVacantHousingDiscount(discountPrice); //空置房打折金额(小计)
                }
                //空置房减免金额(小计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("5")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setTotalVacantHousingReduction(discountPrice); //空置房减免金额(小计)
                }
                //赠送金额(小计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("6")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setTotalGiftAmount(discountPrice); //赠送金额(小计)
                }
            }
        } else {
            reportFeeMonthStatisticsPrepaymentTotal.setTotalPreferentialAmount("0.0"); //优惠金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setTotalDeductionAmount("0.0"); //减免金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setTotalLateFee("0.0"); //滞纳金(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setTotalVacantHousingDiscount("0.0"); //空置房打折金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setTotalVacantHousingReduction("0.0"); //空置房减免金额(小计)
            reportFeeMonthStatisticsPrepaymentTotal.setTotalGiftAmount("0.0"); //赠送金额(小计)
        }
        //查询优惠、违约金额(大计)
        reportFeeMonthStatisticsPrepaymentDto.setPage(PageDto.DEFAULT_PAGE);
        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentDtos = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryAllPrepaymentDiscounts(reportFeeMonthStatisticsPrepaymentDto);
        if (reportFeeMonthStatisticsPrepaymentDtos != null && reportFeeMonthStatisticsPrepaymentDtos.size() > 0) {
            for (ReportFeeMonthStatisticsPrepaymentDto feeMonthStatisticsPrepaymentDto : reportFeeMonthStatisticsPrepaymentDtos) {
                //获取折扣类型
                String discountSmallType = feeMonthStatisticsPrepaymentDto.getDiscountSmallType();
                //获取折扣金额
                String discountPrice = feeMonthStatisticsPrepaymentDto.getDiscountPrice();
                // discountSmallType 1: 打折  2:减免  3:滞纳金  4:空置房打折  5:空置房减免  6:赠送
                //优惠金额(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("1")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setAllPreferentialAmount(discountPrice); //优惠金额(大计)
                }
                //减免金额(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("2")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setAllDeductionAmount(discountPrice); //减免金额(大计)
                }
                //滞纳金(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("3")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setAllLateFee(discountPrice); //滞纳金(大计)
                }
                //空置房打折金额(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("4")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setAllVacantHousingDiscount(discountPrice); //空置房打折金额(大计)
                }
                //空置房减免金额(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("5")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setAllVacantHousingReduction(discountPrice); //空置房减免金额(大计)
                }
                //赠送金额(大计)
                if (!StringUtil.isEmpty(discountPrice) && !StringUtil.isEmpty(discountSmallType) && discountSmallType.equals("6")) {
                    reportFeeMonthStatisticsPrepaymentTotal.setAllGiftAmount(discountPrice); //赠送金额(大计)
                }
            }
        } else {
            reportFeeMonthStatisticsPrepaymentTotal.setAllPreferentialAmount("0.0"); //优惠金额(大计)
            reportFeeMonthStatisticsPrepaymentTotal.setAllDeductionAmount("0.0"); //减免金额(大计)
            reportFeeMonthStatisticsPrepaymentTotal.setAllLateFee("0.0"); //滞纳金(大计)
            reportFeeMonthStatisticsPrepaymentTotal.setAllVacantHousingDiscount("0.0"); //空置房打折金额(大计)
            reportFeeMonthStatisticsPrepaymentTotal.setAllVacantHousingReduction("0.0"); //空置房减免金额(大计)
            reportFeeMonthStatisticsPrepaymentTotal.setAllGiftAmount("0.0"); //赠送金额(大计)
        }
        return reportFeeMonthStatisticsPrepaymentTotal;
    }


}
