package com.java110.report.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.PageDto;
import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentTotalDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsTotalDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.report.IQueryPayFeeDetailInnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsPrepaymentInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
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
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Override
    public ResultVo query(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        JSONObject countInfo = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDetailCount(reportFeeMonthStatisticsDto);

        int count = Integer.parseInt(countInfo.get("count").toString());

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        ReportFeeMonthStatisticsTotalDto reportFeeMonthStatisticsTotalDto = new ReportFeeMonthStatisticsTotalDto();
        List<ReportFeeMonthStatisticsDto> reportList = new ArrayList<>();
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
        //吴学文 注释 感觉和上面的369 功能重复
        //int size = 0;
        if (count > 0) {
            //查询缴费明细
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDetail(reportFeeMonthStatisticsDto);
            //查询应收、实收总金额(大计)
            List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsList = reportFeeMonthStatisticsInnerServiceSMOImpl.queryAllPayFeeDetail(reportFeeMonthStatisticsDto);
            //查询(优惠、减免、滞纳金、空置房打折、空置房减免金额等)大计总金额
            List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsSum = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDetailSum(reportFeeMonthStatisticsDto);
            allReceivableAmount = Double.valueOf(reportFeeMonthStatisticsList.get(0).getAllReceivableAmount());
            allReceivedAmount = Double.valueOf(reportFeeMonthStatisticsList.get(0).getAllReceivedAmount());
            for (ReportFeeMonthStatisticsDto reportFeeMonthStatistics : reportFeeMonthStatisticsSum) {
                //这里是查询出的金额总和
                String discountPrice = reportFeeMonthStatistics.getDiscountPrice();
                //优惠金额(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("1")) {
//                    allPreferentialAmount = Double.valueOf(discountPrice);
                    Double aDouble = Double.valueOf(discountPrice);
                    allPreferentialAmount = allPreferentialAmount + aDouble;
                }
                //减免金额(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("2")) {
                    //allDeductionAmount = Double.valueOf(discountPrice);
                    Double aDouble = Double.valueOf(discountPrice);
                    allDeductionAmount = allDeductionAmount + aDouble;
                }
                //滞纳金(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("3")) {
//                    allLateFee = Double.valueOf(discountPrice);
                    Double aDouble = Double.valueOf(discountPrice);
                    allLateFee = allLateFee + aDouble;
                }
                //空置房打折金额(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("4")) {
//                    allVacantHousingDiscount = Double.valueOf(discountPrice);
                    Double aDouble = Double.valueOf(discountPrice);
                    allVacantHousingDiscount = allVacantHousingDiscount + aDouble;
                }
                //空置房减免金额(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("5")) {
//                    allVacantHousingReduction = Double.valueOf(discountPrice);
                    Double aDouble = Double.valueOf(discountPrice);
                    allVacantHousingReduction = allVacantHousingReduction + aDouble;
                }
                //赠送金额(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("6")) {
//                    allGiftAmount = Double.valueOf(discountPrice);
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
            List<String> ownerIds = new ArrayList<>();
            for (ReportFeeMonthStatisticsDto reportFeeMonthStatistics : reportFeeMonthStatisticsDtos) {
                //应收金额
                Double receivableAmount = Double.valueOf(reportFeeMonthStatistics.getReceivableAmount());
                //实收金额
                Double receivedAmount = Double.valueOf(reportFeeMonthStatistics.getReceivedAmount());
                totalReceivableAmount = totalReceivableAmount + receivableAmount;
                totalReceivedAmount = totalReceivedAmount + receivedAmount;

                if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(reportFeeMonthStatistics.getPayerObjType())) {
                    ownerIds.add(reportFeeMonthStatistics.getOwnerId());
                }

                // 最大记录时 就去刷新
                //如果是车位刷房屋信息
                if (ownerIds.size() == MAX_ROWS) {
                    refreshReportFeeMonthStatistics(ownerIds, reportFeeMonthStatisticsDtos);
                    ownerIds = new ArrayList<>();
                }

                //优惠金额
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("1")) {
                    //获取优惠金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalPreferentialAmount = totalPreferentialAmount + discountPrice;
                    //优惠金额
                    reportFeeMonthStatistics.setPreferentialAmount(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setPreferentialAmount("0");
                }
                //减免金额
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("2")) {
                    //获取减免金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalDeductionAmount = totalDeductionAmount + discountPrice;
                    //减免金额
                    reportFeeMonthStatistics.setDeductionAmount(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setDeductionAmount("0");
                }
                //滞纳金
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("3")) {
                    //获取滞纳金金额
                    Double discountPrice = (Double.valueOf(reportFeeMonthStatistics.getDiscountPrice()));
                    totalLateFee = totalLateFee + discountPrice;
                    //滞纳金
                    reportFeeMonthStatistics.setLateFee(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setLateFee("0");
                }
                //空置房打折
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("4")) {
                    //空置房打折金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalVacantHousingDiscount = totalVacantHousingDiscount + discountPrice;
                    //空置房打折
                    reportFeeMonthStatistics.setVacantHousingDiscount(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setVacantHousingDiscount("0");
                }
                //空置房减免
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("5")) {
                    //空置房减免金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalVacantHousingReduction = totalVacantHousingReduction + discountPrice;
                    //空置房减免
                    reportFeeMonthStatistics.setVacantHousingReduction(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setVacantHousingReduction("0");
                }
                //赠送金额
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("6")) {
                    //赠送金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalGiftAmount = totalGiftAmount + discountPrice;
                    //赠送金额
                    reportFeeMonthStatistics.setGiftAmount(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setGiftAmount("0");
                }
                if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(reportFeeMonthStatistics.getPayerObjType())) {
                    reportFeeMonthStatistics.setObjName(reportFeeMonthStatistics.getFloorNum()
                            + "栋" + reportFeeMonthStatistics.getUnitNum()
                            + "单元" + reportFeeMonthStatistics.getRoomNum() + "室");
                } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(reportFeeMonthStatistics.getPayerObjType())) {
                    reportFeeMonthStatistics.setObjName(reportFeeMonthStatistics.getCarNum());
                } else {
                    reportFeeMonthStatistics.setObjName(reportFeeMonthStatistics.getContractCode());

                }
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getImportFeeName())) {
                    reportFeeMonthStatistics.setFeeName(reportFeeMonthStatistics.getImportFeeName());
                }
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getRepairId())) {
                    RepairDto repairDto = new RepairDto();
                    repairDto.setRepairId(reportFeeMonthStatistics.getRepairId());
                    //查询报修单
                    List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
                    //Assert.listOnlyOne(repairDtos, "查询报修单错误！");
                    if (repairDtos != null && repairDtos.size() == 1) {
                        if (!StringUtil.isEmpty(repairDtos.get(0).getRepairObjType()) && repairDtos.get(0).getRepairObjType().equals("004")) {
                            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
                            ownerRoomRelDto.setRoomId(repairDtos.get(0).getRepairObjId());
                            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
                            if (ownerRoomRelDtos != null && ownerRoomRelDtos.size() == 0) { //查询条数为0条
                                OwnerRoomRelDto ownerRoomRel = new OwnerRoomRelDto();
                                ownerRoomRel.setRoomId(repairDtos.get(0).getRepairObjId());
                                ownerRoomRel.setStatusCd("1"); //看看业主房屋关系数据是否删除了
                                List<OwnerRoomRelDto> ownerRoomRels = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRel);
                                Assert.listOnlyOne(ownerRoomRels, "查询业主房屋关系表错误！");
                                OwnerDto owner = new OwnerDto();
                                owner.setOwnerId(ownerRoomRels.get(0).getOwnerId());
                                owner.setOwnerTypeCd("1001"); //业主本人
                                List<OwnerDto> owners = ownerInnerServiceSMOImpl.queryOwners(owner);
                                if (owners != null && owners.size() == 0) { //查出条数为0条
                                    //判断业主是否删除了
                                    OwnerDto newOwner = new OwnerDto();
                                    newOwner.setOwnerId(ownerRoomRels.get(0).getOwnerId());
                                    newOwner.setOwnerTypeCd("1001"); //业主本人
                                    newOwner.setStatusCd("1");
                                    List<OwnerDto> newOwners = ownerInnerServiceSMOImpl.queryOwners(newOwner);
                                    Assert.listOnlyOne(newOwners, "查询业主信息错误！");
                                    reportFeeMonthStatistics.setOwnerName(newOwners.get(0).getName());
                                } else if (owners != null && owners.size() == 1) { //查出条数为1条
                                    reportFeeMonthStatistics.setOwnerName(owners.get(0).getName());
                                } else {
                                    throw new IllegalArgumentException("查询业主信息错误！");
                                }
                            } else if (ownerRoomRelDtos != null && ownerRoomRelDtos.size() == 1) { //查询条数为1条
                                OwnerDto ownerDto = new OwnerDto();
                                ownerDto.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
                                ownerDto.setOwnerTypeCd("1001"); //业主本人
                                List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
                                if (ownerDtos != null && ownerDtos.size() == 0) { //业主查询条数为0条
                                    //判断业主是否删除了
                                    OwnerDto newOwner = new OwnerDto();
                                    newOwner.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
                                    newOwner.setOwnerTypeCd("1001"); //业主本人
                                    newOwner.setStatusCd("1");
                                    List<OwnerDto> newOwners = ownerInnerServiceSMOImpl.queryOwners(newOwner);
                                    Assert.listOnlyOne(newOwners, "查询业主信息错误！");
                                    reportFeeMonthStatistics.setOwnerName(newOwners.get(0).getName());
                                } else if (ownerDtos != null || ownerDtos.size() == 1) {
                                    reportFeeMonthStatistics.setOwnerName(ownerDtos.get(0).getName());
                                } else {
                                    throw new IllegalArgumentException("查询业主信息错误！");
                                }
                            } else {
                                throw new IllegalArgumentException("查询业主房屋关系表错误！");
                            }
                        }
                    }
                }
                if (!hasInReportListAndMerge(reportList, reportFeeMonthStatistics)) {
                    reportList.add(reportFeeMonthStatistics);
                }
            }

            //如果是车位刷房屋信息
            if (ownerIds.size() > 0) {
                refreshReportFeeMonthStatistics(ownerIds, reportFeeMonthStatisticsDtos);
            }

            //应收总金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalReceivableAmount(String.format("%.2f", totalReceivableAmount));
            //实收金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalReceivedAmount(String.format("%.2f", totalReceivedAmount));
            //优惠金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalPreferentialAmount(String.format("%.2f", totalPreferentialAmount));
            //减免金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalDeductionAmount(String.format("%.2f", totalDeductionAmount));
            //滞纳金(小计)
            reportFeeMonthStatisticsTotalDto.setTotalLateFee(String.format("%.2f", totalLateFee));
            //空置房打折(小计)
            reportFeeMonthStatisticsTotalDto.setTotalVacantHousingDiscount(String.format("%.2f", totalVacantHousingDiscount));
            //空置房减免(小计)
            reportFeeMonthStatisticsTotalDto.setTotalVacantHousingReduction(String.format("%.2f", totalVacantHousingReduction));
            //赠送规则金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalGiftAmount(String.format("%.2f", totalGiftAmount));
            //应收金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllReceivableAmount(String.format("%.2f", allReceivableAmount));
            //实收金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllReceivedAmount(String.format("%.2f", allReceivedAmount));
            //优惠金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllPreferentialAmount(String.format("%.2f", allPreferentialAmount));
            //减免金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllDeductionAmount(String.format("%.2f", allDeductionAmount));
            //滞纳金(大计)
            reportFeeMonthStatisticsTotalDto.setAllLateFee(String.format("%.2f", allLateFee));
            //空置房打折金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllVacantHousingDiscount(String.format("%.2f", allVacantHousingDiscount));
            //空置房减免金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllVacantHousingReduction(String.format("%.2f", allVacantHousingReduction));
            //赠送规则金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllGiftAmount(String.format("%.2f", allGiftAmount));
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
            reportList.addAll(reportFeeMonthStatisticsDtos);
            reportFeeMonthStatisticsTotalDto = new ReportFeeMonthStatisticsTotalDto();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportList, reportFeeMonthStatisticsTotalDto);

        return resultVo;
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

    /**
     * @param ownerIds
     * @param reportFeeMonthStatisticsDtos
     */
    private void refreshReportFeeMonthStatistics(List<String> ownerIds, List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos) {
        if (ownerIds == null || ownerIds.size() < 1) {
            return;
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerIds(ownerIds.toArray(new String[ownerIds.size()]));
        List<OwnerDto> ownerDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryRoomAndParkingSpace(ownerDto);
        String objName = "";
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(reportFeeMonthStatisticsDto.getPayerObjType())) {
                continue;
            }
            for (OwnerDto ownerDto1 : ownerDtos) {
                if (!StringUtil.isEmpty(reportFeeMonthStatisticsDto.getOwnerId()) && !reportFeeMonthStatisticsDto.getOwnerId().equals(ownerDto1.getOwnerId())) {
                    continue;
                }
                objName = reportFeeMonthStatisticsDto.getObjName() + "(" + ownerDto1.getFloorNum() + "栋" + ownerDto1.getUnitNum() + "单元" + ownerDto1.getRoomNum() + "室)";
                reportFeeMonthStatisticsDto.setObjName(objName);
            }
        }
    }

    private boolean hasInReportListAndMerge(List<ReportFeeMonthStatisticsDto> reportList, ReportFeeMonthStatisticsDto reportFeeMonthStatistics) {
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportList) {
            if (reportFeeMonthStatisticsDto.getDetailId().equals(reportFeeMonthStatistics.getDetailId())) {
                combineSydwCore(reportFeeMonthStatistics, reportFeeMonthStatisticsDto);
                return true;
            }
        }
        return false;
    }

    //针对所用对象
    private static ReportFeeMonthStatisticsDto combineSydwCore(ReportFeeMonthStatisticsDto sourceBean, ReportFeeMonthStatisticsDto targetBean) {
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();
        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = sourceBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if (!(sourceField.get(sourceBean) == null)) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetBean;
    }
}
