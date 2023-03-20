package com.java110.report.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.report.ReportCarDto;
import com.java110.dto.report.ReportFeeDetailDto;
import com.java110.dto.report.ReportFeeDto;
import com.java110.dto.report.ReportRoomDto;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.report.IGeneratorFeeMonthStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import com.java110.report.dao.IReportCommunityServiceDao;
import com.java110.report.dao.IReportFeeMonthStatisticsServiceDao;
import com.java110.report.dao.IReportFeeServiceDao;
import com.java110.utils.constant.FeeConfigConstant;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName GeneratorFeeMonthStatisticsInnerServiceSMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 21:53
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
@RestController
public class GeneratorFeeMonthStatisticsInnerServiceSMOImpl implements IGeneratorFeeMonthStatisticsInnerServiceSMO {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorFeeMonthStatisticsInnerServiceSMOImpl.class);

    //默认 处理房屋数量
    private static final int DEFAULT_DEAL_ROOM_COUNT = 1000;

    @Autowired
    private IReportFeeMonthStatisticsServiceDao reportFeeMonthStatisticsServiceDaoImpl;

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    @Autowired
    private IReportCommunityServiceDao reportCommunityServiceDaoImpl;

    @Autowired
    private IReportFeeServiceDao reportFeeServiceDaoImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Override
    public int generatorData(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        doGeneratorData(reportFeeMonthStatisticsPo);
        return 0;
    }

    @Async
    public void doGeneratorData(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {
        String communityId = reportFeeMonthStatisticsPo.getCommunityId();

        Assert.hasLength(communityId, "未包含小区信息");

        //这里处理 报表中的费用是否被人为 取消 或者费用项是否被删除，这种数据 报表中做清理，以防影响 报表的准确度
        feeDataFiltering(communityId);

        //处理房屋费用
        dealRoomFee(reportFeeMonthStatisticsPo);

        //处理车位费用
        dealCarFee(reportFeeMonthStatisticsPo);

        //处理缴费结束的情况
        dealFinishFee(communityId);
    }

    private void feeDataFiltering(String communityId) {
        Map reportFeeDto = new HashMap();
        reportFeeDto.put("communityId", communityId);
        List<Map> feeDtos = reportFeeMonthStatisticsServiceDaoImpl.queryInvalidFeeMonthStatistics(reportFeeDto);

        List<String> feeIds = new ArrayList<>();
        for (Map feeDto : feeDtos) {
            if (!feeDto.containsKey("feeId") || StringUtil.isNullOrNone(feeDto.get("feeId"))) {
                continue;
            }

            feeIds.add(feeDto.get("feeId").toString());

            if (feeIds.size() >= 50) {
                reportFeeDto.put("feeIds", feeIds);
                reportFeeMonthStatisticsServiceDaoImpl.deleteInvalidFee(reportFeeDto);
                feeIds = new ArrayList<>();
            }
        }
        reportFeeDto.put("feeIds", feeIds);
        if (feeIds.size() > 0) {
            reportFeeMonthStatisticsServiceDaoImpl.deleteInvalidFee(reportFeeDto);
        }
    }

    private void dealFinishFee(String communityId) {
        Map reportFeeDto = new HashMap();
        reportFeeDto.put("communityId", communityId);
        List<Map> feeDtos = reportFeeMonthStatisticsServiceDaoImpl.queryFinishOweFee(reportFeeDto);
        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = null;
        for (Map info : feeDtos) {
            try {
                Date tmpTime = DateUtil.getDateFromString(info.get("feeYear").toString() + "-" + info.get("feeMonth").toString() + "-01", DateUtil.DATE_FORMATE_STRING_B);
                Calendar c = Calendar.getInstance();
                c.setTime(tmpTime);
                c.add(Calendar.MONTH, 1);
                ReportFeeDetailDto feeDetailDto = new ReportFeeDetailDto();
                feeDetailDto.setStartTime(DateUtil.getFormatTimeString(tmpTime, DateUtil.DATE_FORMATE_STRING_A));
                feeDetailDto.setEndTime(DateUtil.getFormatTimeString(c.getTime(), DateUtil.DATE_FORMATE_STRING_A));
                feeDetailDto.setFeeId(info.get("feeId").toString());
                double receivedAmount = reportFeeServiceDaoImpl.getFeeReceivedAmount(feeDetailDto);
                reportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
                reportFeeMonthStatisticsPo.setStatisticsId(info.get("statisticsId").toString());
                reportFeeMonthStatisticsPo.setReceivedAmount(receivedAmount + "");
                reportFeeMonthStatisticsPo.setOweAmount("0");
                reportFeeMonthStatisticsPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
            } catch (Exception e) {
                logger.error("处理 缴费结束报表失败", e);
            }
        }
    }

    /**
     * 处理车位 车辆费用
     *
     * @param reportFeeMonthStatisticsPo
     */
    private void dealCarFee(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        int page = 0;
        int max = DEFAULT_DEAL_ROOM_COUNT;

        ReportCarDto reportCarDto = new ReportCarDto();
        reportCarDto.setCommunityId(reportFeeMonthStatisticsPo.getCommunityId());
        reportCarDto.setLeaseTypes(new String[]{OwnerCarDto.LEASE_TYPE_MONTH,OwnerCarDto.LEASE_TYPE_INNER,OwnerCarDto.LEASE_TYPE_SALE,OwnerCarDto.LEASE_TYPE_NO_MONEY});
        int count = reportCommunityServiceDaoImpl.getCarCount(reportCarDto);


        if (count < DEFAULT_DEAL_ROOM_COUNT) {
            page = 1;
            max = count;
        } else {
            page = (int) Math.ceil((double) count / (double) DEFAULT_DEAL_ROOM_COUNT);
            max = DEFAULT_DEAL_ROOM_COUNT;
        }

        for (int pageIndex = 0; pageIndex < page; pageIndex++) {
            reportCarDto.setPage(pageIndex * max);
            reportCarDto.setRow(max);
            List<ReportCarDto> reportRoomDtos = reportCommunityServiceDaoImpl.getCarParkingSpace(reportCarDto);
            for (ReportCarDto tmpReportCarDto : reportRoomDtos) {
                try {
                    doDealCarFees(tmpReportCarDto);
                } catch (Exception e) {
                    logger.error("生成费用报表失败" + JSONObject.toJSONString(tmpReportCarDto), e);
                }
            }
        }
    }


    /**
     * 处理 房屋费用
     *
     * @param reportFeeMonthStatisticsPo
     */
    private void dealRoomFee(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        int page = 0;
        int max = DEFAULT_DEAL_ROOM_COUNT;

        ReportRoomDto reportRoomDto = new ReportRoomDto();
        reportRoomDto.setCommunityId(reportFeeMonthStatisticsPo.getCommunityId());
        int count = reportCommunityServiceDaoImpl.getRoomCount(reportRoomDto);


        if (count < DEFAULT_DEAL_ROOM_COUNT) {
            page = 1;
            max = count;
        } else {
            page = (int) Math.ceil((double) count / (double) DEFAULT_DEAL_ROOM_COUNT);
            max = DEFAULT_DEAL_ROOM_COUNT;
        }

        for (int pageIndex = 0; pageIndex < page; pageIndex++) {
            reportRoomDto.setPage(pageIndex * max);
            reportRoomDto.setRow(max);
            List<ReportRoomDto> reportRoomDtos = reportCommunityServiceDaoImpl.getRoomFloorUnitAndOwner(reportRoomDto);
            for (ReportRoomDto tmpReportRoomDto : reportRoomDtos) {
                try {
                    doDealRoomFees(tmpReportRoomDto);
                } catch (Exception e) {
                    logger.error("生成费用报表失败" + JSONObject.toJSONString(tmpReportRoomDto), e);
                }
            }
        }
    }

    private void doDealCarFees(ReportCarDto tmpReportCarDto) {
        ReportFeeDto reportFeeDto = new ReportFeeDto();
        reportFeeDto.setPayerObjId(tmpReportCarDto.getCarId());
        reportFeeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        //reportFeeDto.setState(FeeDto.STATE_DOING);
        List<ReportFeeDto> feeDtos = reportFeeServiceDaoImpl.getFees(reportFeeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }

        for (ReportFeeDto tmpReportFeeDto : feeDtos) {
            try {
                doDealCarFee(tmpReportCarDto, tmpReportFeeDto);
            } catch (Exception e) {
                logger.error("处理车辆费用失败" + JSONObject.toJSONString(tmpReportFeeDto), e);
            }
        }
    }

    private void doDealCarFee(ReportCarDto tmpReportCarDto, ReportFeeDto tmpReportFeeDto) {

        //费用已经结束 并且当月实收为0 那就是 之前就结束了 无需处理  && ListUtil.isNull(statistics)
        if (FeeDto.STATE_FINISH.equals(tmpReportFeeDto.getState())
                && getCurFeeReceivedAmount(tmpReportFeeDto) == 0) {
            return;
        }
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setCommunityId(tmpReportCarDto.getCommunityId());
        reportFeeMonthStatisticsDto.setConfigId(tmpReportFeeDto.getConfigId());
        reportFeeMonthStatisticsDto.setObjId(tmpReportFeeDto.getPayerObjId());
        reportFeeMonthStatisticsDto.setFeeId(tmpReportFeeDto.getFeeId());//这里不能注释 如果一个费用创建多条时会有bug
        reportFeeMonthStatisticsDto.setObjType(tmpReportFeeDto.getPayerObjType());
        reportFeeMonthStatisticsDto.setFeeYear(DateUtil.getYear() + "");
        reportFeeMonthStatisticsDto.setFeeMonth(DateUtil.getMonth() + "");
        List<ReportFeeMonthStatisticsDto> statistics = BeanConvertUtil.covertBeanList(
                reportFeeMonthStatisticsServiceDaoImpl.getReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)),
                ReportFeeMonthStatisticsDto.class);

        double receivedAmount = getReceivedAmount(tmpReportFeeDto); //实收

        FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
        OwnerCarDto ownerCarDto = BeanConvertUtil.covertBean(tmpReportCarDto, OwnerCarDto.class);
        Map<String, Object> targetEndDateAndOweMonth = computeFeeSMOImpl.getTargetEndDateAndOweMonth(feeDto, ownerCarDto);

        Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
        tmpReportFeeDto.setDeadlineTime(targetEndDate);
        double oweAmount = getOweAmountByCar(tmpReportFeeDto, null, tmpReportCarDto); //应收

        //dealBeforeUploadCarFee(tmpReportFeeDto, tmpReportCarDto);
        //double receivableAmount = getReceivableAmount(tmpReportFeeDto, receivedAmount); //欠费

        double feePrice = computeFeeSMOImpl.getReportFeePrice(tmpReportFeeDto, null, tmpReportCarDto);
        tmpReportFeeDto.setFeePrice(feePrice);

        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        reportFeeMonthStatisticsPo.setDeadlineTime(DateUtil.getFormatTimeString(targetEndDate, DateUtil.DATE_FORMATE_STRING_A));
        if (!ListUtil.isNull(statistics)) {
            ReportFeeMonthStatisticsDto statistic = statistics.get(0);
            reportFeeMonthStatisticsPo.setStatisticsId(statistic.getStatisticsId());
//            reportFeeMonthStatisticsPo.setReceivableAmount("0");
//            reportFeeMonthStatisticsPo.setReceivedAmount("0");
            reportFeeMonthStatisticsPo.setOweAmount(oweAmount + "");
            reportFeeMonthStatisticsPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            // 缴费了就得刷
            reportFeeMonthStatisticsPo.setHisOweAmount(getHisOweAmount(tmpReportFeeDto) + "");
            //有可能是月内创建的费用 比如电费水费
            reportFeeMonthStatisticsPo.setCurReceivableAmount(getCurFeeReceivableAmount(tmpReportFeeDto) + "");
            reportFeeMonthStatisticsPo.setCurReceivedAmount(getReceivedAmount(tmpReportFeeDto, 1) + "");
            reportFeeMonthStatisticsPo.setHisOweReceivedAmount(getReceivedAmount(tmpReportFeeDto, 2) + "");
            reportFeeMonthStatisticsPo.setPreReceivedAmount(getReceivedAmount(tmpReportFeeDto, 3) + "");
            if (!StringUtil.isEmpty(statistic.getObjType()) && statistic.getObjType().equals("3333")) { //房屋
                reportFeeMonthStatisticsPo.setObjNameNum(statistic.getFloorNum() + "-" + statistic.getUnitNum() + "-" + statistic.getRoomNum());
            }
            reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        } else {
            reportFeeMonthStatisticsPo.setOweAmount(oweAmount + "");
            reportFeeMonthStatisticsPo.setReceivedAmount("0");
            reportFeeMonthStatisticsPo.setReceivableAmount("0");
            reportFeeMonthStatisticsPo.setStatisticsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
            reportFeeMonthStatisticsPo.setCommunityId(tmpReportFeeDto.getCommunityId());
            reportFeeMonthStatisticsPo.setConfigId(tmpReportFeeDto.getConfigId());
            reportFeeMonthStatisticsPo.setFeeCreateTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsPo.setFeeId(tmpReportFeeDto.getFeeId());
            reportFeeMonthStatisticsPo.setFeeMonth(DateUtil.getMonth() + "");
            reportFeeMonthStatisticsPo.setFeeYear(DateUtil.getYear() + "");
            reportFeeMonthStatisticsPo.setCurMaxTime(DateUtil.getNextMonthFirstDay(DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsPo.setObjId(tmpReportCarDto.getCarId());
            reportFeeMonthStatisticsPo.setObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
            reportFeeMonthStatisticsPo.setFeeName(tmpReportFeeDto.getFeeName());
            reportFeeMonthStatisticsPo.setObjName(tmpReportCarDto.getCarNum() + "(" + tmpReportCarDto.getAreaNum() + "停车场" + tmpReportCarDto.getNum() + "车位)");
            reportFeeMonthStatisticsPo.setObjNameNum(tmpReportCarDto.getAreaNum() + "-" + tmpReportCarDto.getNum() + "-" + tmpReportCarDto.getCarNum());
            reportFeeMonthStatisticsPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsPo.setHisOweAmount(getHisOweAmount(tmpReportFeeDto) + "");
            reportFeeMonthStatisticsPo.setCurReceivableAmount(getCurFeeReceivableAmount(tmpReportFeeDto) + "");
            reportFeeMonthStatisticsPo.setCurReceivedAmount(getReceivedAmount(tmpReportFeeDto, 1) + "");
            reportFeeMonthStatisticsPo.setHisOweReceivedAmount(getReceivedAmount(tmpReportFeeDto, 2) + "");
            reportFeeMonthStatisticsPo.setPreReceivedAmount(getReceivedAmount(tmpReportFeeDto, 3) + "");
            reportFeeMonthStatisticsServiceDaoImpl.saveReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        }


        Date endTime = tmpReportFeeDto.getEndTime();

        Calendar calender = Calendar.getInstance();
        calender.setTime(endTime);

        ReportFeeMonthStatisticsPo tmpReportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        tmpReportFeeMonthStatisticsPo.setFeeId(tmpReportFeeDto.getFeeId());
        tmpReportFeeMonthStatisticsPo.setCurMaxTime(DateUtil.getFormatTimeString(endTime, DateUtil.DATE_FORMATE_STRING_A));
        tmpReportFeeMonthStatisticsPo.setOweAmount("0");
        reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsOwe(BeanConvertUtil.beanCovertMap(tmpReportFeeMonthStatisticsPo));
    }

    /**
     * 处理费用
     *
     * @param reportRoomDto
     */
    private void doDealRoomFees(ReportRoomDto reportRoomDto) {
        ReportFeeDto reportFeeDto = new ReportFeeDto();
        reportFeeDto.setPayerObjId(reportRoomDto.getRoomId());
        reportFeeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        //reportFeeDto.setState(FeeDto.STATE_DOING);
        List<ReportFeeDto> feeDtos = reportFeeServiceDaoImpl.getFees(reportFeeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }

        for (ReportFeeDto tmpReportFeeDto : feeDtos) {
            try {
                doDealRoomFee(reportRoomDto, tmpReportFeeDto);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("处理房屋费用失败" + JSONObject.toJSONString(tmpReportFeeDto), e);
            }
        }


    }

    private void doDealRoomFee(ReportRoomDto reportRoomDto, ReportFeeDto tmpReportFeeDto) {

        //费用已经结束 并且当月实收为0 那就是 之前就结束了 无需处理  && ListUtil.isNull(statistics)
        if (FeeDto.STATE_FINISH.equals(tmpReportFeeDto.getState())
                && getCurFeeReceivedAmount(tmpReportFeeDto) == 0) {
            return;
        }

        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setCommunityId(reportRoomDto.getCommunityId());
        reportFeeMonthStatisticsDto.setConfigId(tmpReportFeeDto.getConfigId());
        reportFeeMonthStatisticsDto.setObjId(tmpReportFeeDto.getPayerObjId());
        reportFeeMonthStatisticsDto.setFeeId(tmpReportFeeDto.getFeeId()); //这里不能注释，因为一个费用多次创建时会存在覆盖 存在bug问题
        reportFeeMonthStatisticsDto.setObjType(tmpReportFeeDto.getPayerObjType());

        reportFeeMonthStatisticsDto.setFeeYear(DateUtil.getYear() + "");
        reportFeeMonthStatisticsDto.setFeeMonth(DateUtil.getMonth() + "");
        List<ReportFeeMonthStatisticsDto> statistics = BeanConvertUtil.covertBeanList(
                reportFeeMonthStatisticsServiceDaoImpl.getReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)),
                ReportFeeMonthStatisticsDto.class);
        //List<ReportFeeMonthStatisticsDto> statistics = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFeeMonthStatisticss(reportFeeMonthStatisticsDto);


        //double receivedAmount = getReceivedAmount(tmpReportFeeDto); //实收

        FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
        Map<String, Object> targetEndDateAndOweMonth = computeFeeSMOImpl.getTargetEndDateAndOweMonth(feeDto, null);

        Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
        tmpReportFeeDto.setDeadlineTime(targetEndDate);
        double oweAmount = getOweAmount(tmpReportFeeDto, reportRoomDto, null); //欠费
        double feePrice = computeFeeSMOImpl.getReportFeePrice(tmpReportFeeDto, reportRoomDto, null);
        tmpReportFeeDto.setFeePrice(feePrice);

        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        reportFeeMonthStatisticsPo.setDeadlineTime(DateUtil.getFormatTimeString(targetEndDate, DateUtil.DATE_FORMATE_STRING_A));
        if (!ListUtil.isNull(statistics)) {
            ReportFeeMonthStatisticsDto statistic = statistics.get(0);
            reportFeeMonthStatisticsPo.setStatisticsId(statistic.getStatisticsId());

            reportFeeMonthStatisticsPo.setOweAmount(oweAmount + "");
            reportFeeMonthStatisticsPo.setFeeId(tmpReportFeeDto.getFeeId());
            reportFeeMonthStatisticsPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsPo.setFeeName(StringUtil.isEmpty(tmpReportFeeDto.getImportFeeName()) ? tmpReportFeeDto.getFeeName() : tmpReportFeeDto.getImportFeeName());
            // 缴费了就得刷
            reportFeeMonthStatisticsPo.setHisOweAmount(MoneyUtil.computePriceScale(getHisOweAmount(tmpReportFeeDto),
                    tmpReportFeeDto.getScale(),
                    Integer.parseInt(tmpReportFeeDto.getDecimalPlace())) + "");
            //有可能是月内创建的费用 比如电费水费
            reportFeeMonthStatisticsPo.setCurReceivableAmount(
                    MoneyUtil.computePriceScale(getCurFeeReceivableAmount(tmpReportFeeDto),
                            tmpReportFeeDto.getScale(),
                            Integer.parseInt(tmpReportFeeDto.getDecimalPlace())) + "");
            reportFeeMonthStatisticsPo.setCurReceivedAmount(
                    MoneyUtil.computePriceScale(getReceivedAmount(tmpReportFeeDto, 1),
                            tmpReportFeeDto.getScale(),
                            Integer.parseInt(tmpReportFeeDto.getDecimalPlace())) + "");
            reportFeeMonthStatisticsPo.setHisOweReceivedAmount(
                    MoneyUtil.computePriceScale(getReceivedAmount(tmpReportFeeDto, 2),
                            tmpReportFeeDto.getScale(),
                            Integer.parseInt(tmpReportFeeDto.getDecimalPlace())) + "");
            reportFeeMonthStatisticsPo.setPreReceivedAmount(MoneyUtil.computePriceScale(getReceivedAmount(tmpReportFeeDto, 3),
                    tmpReportFeeDto.getScale(),
                    Integer.parseInt(tmpReportFeeDto.getDecimalPlace())) + "");
            reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        } else {

            reportFeeMonthStatisticsPo.setOweAmount(oweAmount + "");
            reportFeeMonthStatisticsPo.setReceivedAmount("0");
            reportFeeMonthStatisticsPo.setReceivableAmount("0");
            reportFeeMonthStatisticsPo.setStatisticsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
            reportFeeMonthStatisticsPo.setCommunityId(tmpReportFeeDto.getCommunityId());
            reportFeeMonthStatisticsPo.setConfigId(tmpReportFeeDto.getConfigId());
            reportFeeMonthStatisticsPo.setFeeCreateTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsPo.setFeeId(tmpReportFeeDto.getFeeId());
            reportFeeMonthStatisticsPo.setFeeMonth(DateUtil.getMonth() + "");
            reportFeeMonthStatisticsPo.setFeeYear(DateUtil.getYear() + "");
            reportFeeMonthStatisticsPo.setCurMaxTime(DateUtil.getNextMonthFirstDay(DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsPo.setObjId(reportRoomDto.getRoomId());
            reportFeeMonthStatisticsPo.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            reportFeeMonthStatisticsPo.setFeeName(StringUtil.isEmpty(tmpReportFeeDto.getImportFeeName()) ? tmpReportFeeDto.getFeeName() : tmpReportFeeDto.getImportFeeName());
            if (RoomDto.ROOM_TYPE_ROOM.equals(reportRoomDto.getRoomType())) {
                reportFeeMonthStatisticsPo.setObjName(reportRoomDto.getFloorNum() + "栋" + reportRoomDto.getUnitNum() + "单元" + reportRoomDto.getRoomNum() + "室");
                reportFeeMonthStatisticsPo.setObjNameNum(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getUnitNum() + "-" + reportRoomDto.getRoomNum());
            } else {
                reportFeeMonthStatisticsPo.setObjName(reportRoomDto.getFloorNum() + "栋" + reportRoomDto.getRoomNum() + "室");
                reportFeeMonthStatisticsPo.setObjNameNum(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getRoomNum());
            }
            //计算历史欠费
            reportFeeMonthStatisticsPo.setHisOweAmount(getHisOweAmount(tmpReportFeeDto) + "");
            reportFeeMonthStatisticsPo.setCurReceivableAmount(getCurFeeReceivableAmount(tmpReportFeeDto) + "");
            reportFeeMonthStatisticsPo.setCurReceivedAmount(getReceivedAmount(tmpReportFeeDto, 1) + "");
            reportFeeMonthStatisticsPo.setHisOweReceivedAmount(getReceivedAmount(tmpReportFeeDto, 2) + "");
            reportFeeMonthStatisticsPo.setPreReceivedAmount(getReceivedAmount(tmpReportFeeDto, 3) + "");
            reportFeeMonthStatisticsPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            //如果是 水费 电费 煤气费
            if (!FeeConfigDto.FEE_TYPE_CD_METER.equals(tmpReportFeeDto.getFeeTypeCd())
                    && !FeeConfigDto.FEE_TYPE_CD_WATER.equals(tmpReportFeeDto.getFeeTypeCd())
                    && !FeeConfigDto.FEE_TYPE_CD_GAS.equals(tmpReportFeeDto.getFeeTypeCd())
            ) {
                reportFeeMonthStatisticsServiceDaoImpl.saveReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
            } else {
                //处理水电费，水电费根据开始时间要在相应月补充数据
                dealMeteWater(reportFeeMonthStatisticsPo, tmpReportFeeDto);
            }
        }


        //将缴费 到期时间之前得欠费刷为0
        Date endTime = tmpReportFeeDto.getEndTime();

        ReportFeeMonthStatisticsPo tmpReportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        tmpReportFeeMonthStatisticsPo.setFeeId(tmpReportFeeDto.getFeeId());
        tmpReportFeeMonthStatisticsPo.setCurMaxTime(DateUtil.getFormatTimeString(endTime, DateUtil.DATE_FORMATE_STRING_A));
        tmpReportFeeMonthStatisticsPo.setOweAmount("0");
        reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsOwe(BeanConvertUtil.beanCovertMap(tmpReportFeeMonthStatisticsPo));
    }

    /**
     * 处理水电费
     *
     * @param reportFeeMonthStatisticsPo
     * @param tmpReportFeeDto
     */
    private void dealMeteWater(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo, ReportFeeDto tmpReportFeeDto) {

        //如果是 水费 电费 煤气费
        if (!FeeConfigDto.FEE_TYPE_CD_METER.equals(tmpReportFeeDto.getFeeTypeCd())
                && !FeeConfigDto.FEE_TYPE_CD_WATER.equals(tmpReportFeeDto.getFeeTypeCd())
                && !FeeConfigDto.FEE_TYPE_CD_GAS.equals(tmpReportFeeDto.getFeeTypeCd())
        ) {
            return;
        }
        //根据费用开始时间 计算月份
        Date endTime = tmpReportFeeDto.getEndTime();
        //去除 0 因为表里的月份是没有零
        String curMonth = Integer.parseInt(DateUtil.getFormatTimeString(endTime, "MM")) + "";
        String curYear = DateUtil.getFormatTimeString(endTime, "YYYY");
        //查询是否存在 数据
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setCommunityId(tmpReportFeeDto.getCommunityId());
        reportFeeMonthStatisticsDto.setConfigId(tmpReportFeeDto.getConfigId());
        reportFeeMonthStatisticsDto.setObjId(tmpReportFeeDto.getPayerObjId());
        reportFeeMonthStatisticsDto.setFeeId(tmpReportFeeDto.getFeeId());
        reportFeeMonthStatisticsDto.setObjType(tmpReportFeeDto.getPayerObjType());
        reportFeeMonthStatisticsDto.setFeeYear(curYear);
        reportFeeMonthStatisticsDto.setFeeMonth(curMonth);
        List<ReportFeeMonthStatisticsDto> statistics = BeanConvertUtil.covertBeanList(
                reportFeeMonthStatisticsServiceDaoImpl.getReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)),
                ReportFeeMonthStatisticsDto.class);
        if (!ListUtil.isNull(statistics)) {
            return;
        }
        reportFeeMonthStatisticsPo.setStatisticsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
        reportFeeMonthStatisticsServiceDaoImpl.saveReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
    }


    /**
     * @return
     */
    private double getReceivedAmount(ReportFeeDto tmpReportFeeDto, int flag) {
        Map paramIn = new HashMap();
        paramIn.put("startTime", DateUtil.getFormatTimeString(DateUtil.getFirstDate(), DateUtil.DATE_FORMATE_STRING_A));
        paramIn.put("endTime", DateUtil.getFormatTimeString(DateUtil.getNextMonthFirstDate(), DateUtil.DATE_FORMATE_STRING_A));
        paramIn.put("feeId", tmpReportFeeDto.getFeeId());
        paramIn.put("yearMonth", DateUtil.getFormatTimeString(DateUtil.getFirstDate(), DateUtil.DATE_FORMATE_STRING_M));
        paramIn.put("flag", flag);
        double curReceivedAmount = reportFeeMonthStatisticsServiceDaoImpl.getReceivedAmountByMonth(paramIn);
        return curReceivedAmount;
    }

    /**
     * 当月应收
     *
     * @param tmpReportFeeDto
     * @return
     */
    private double getCurFeeReceivableAmount(ReportFeeDto tmpReportFeeDto) {
        BigDecimal feePriceDec = new BigDecimal(tmpReportFeeDto.getFeePrice());

        Date curDate = DateUtil.getFirstDate();
        Date nowDate = DateUtil.getCurrentDate();
        double month = 0.0;
        //已经超过截止时间 和 还没有到开始时间
        if (curDate.getTime() > tmpReportFeeDto.getDeadlineTime().getTime()
                || nowDate.getTime() < tmpReportFeeDto.getEndTime().getTime()) {
            return 0.0;
        }

        //如果 1号小于 计费起始时间 那么 将curDate 改为 当前时间
        if (curDate.getTime() < tmpReportFeeDto.getEndTime().getTime()) {
            curDate = tmpReportFeeDto.getEndTime();
        }

        //这里需要判断 结束时间 是否 大于了当月最后一天，当月天数 * 每天金额
        Calendar nextDateC = Calendar.getInstance();
        nextDateC.setTime(curDate);
        nextDateC.add(Calendar.MONTH, 1);
        Date nextDate = nextDateC.getTime();

        if (FeeDto.FEE_FLAG_ONCE.equals(tmpReportFeeDto.getFeeFlag())) {
            double allDays = computeFeeSMOImpl.daysBetween(tmpReportFeeDto.getEndTime(), tmpReportFeeDto.getDeadlineTime());
            allDays = Math.ceil(allDays);
            if (allDays == 0) { // 防止除数为0
                return 0;
            }
            BigDecimal moneyPreDay = feePriceDec.divide(new BigDecimal(allDays), FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_EVEN);
            if (tmpReportFeeDto.getDeadlineTime().getTime() > nextDate.getTime()) {
                int day = DateUtil.getCurrentMonthDay();
                return moneyPreDay.multiply(new BigDecimal(day)).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            // 结束时间 在当月内
            double hisDays = computeFeeSMOImpl.daysBetween(tmpReportFeeDto.getEndTime(), curDate);
            BigDecimal hisDayDec = moneyPreDay.multiply(new BigDecimal(hisDays)).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            return feePriceDec.subtract(hisDayDec).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        month = computeFeeSMOImpl.dayCompare(curDate, tmpReportFeeDto.getDeadlineTime());
        if (month < 0) {
            return 0;
        }
        if (month < 1) {
            return feePriceDec.multiply(new BigDecimal(month)).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }

        return tmpReportFeeDto.getFeePrice();
    }


    /**
     * 计算历史欠费
     *
     * @param tmpReportFeeDto
     * @return
     */
    private double getHisOweAmount(ReportFeeDto tmpReportFeeDto) {

        BigDecimal feePriceDec = new BigDecimal(tmpReportFeeDto.getFeePrice());
        Date curDate = DateUtil.getFirstDate();//11月1日
        //说明没有历史欠费
        if (curDate.getTime() < tmpReportFeeDto.getEndTime().getTime()) {
            return 0.0;
        }

        if (FeeDto.FEE_FLAG_ONCE.equals(tmpReportFeeDto.getFeeFlag())) {
            //说明一次性费用都欠了
            if (tmpReportFeeDto.getDeadlineTime().getTime() < curDate.getTime()) {
                return tmpReportFeeDto.getFeePrice();
            }
            double allDays = computeFeeSMOImpl.daysBetween(tmpReportFeeDto.getEndTime(), tmpReportFeeDto.getDeadlineTime());
            allDays = Math.ceil(allDays);
            if (allDays == 0) { // 防止除数为0
                return 0;
            }
            //这是每天的钱
            BigDecimal moneyPreDay = feePriceDec.divide(new BigDecimal(allDays), FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_EVEN);

            double hisDays = computeFeeSMOImpl.daysBetween(tmpReportFeeDto.getEndTime(), curDate);

            return moneyPreDay.multiply(new BigDecimal(hisDays)).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        double month = 0.0;
        if (tmpReportFeeDto.getDeadlineTime().getTime() < curDate.getTime()) {
            month = computeFeeSMOImpl.dayCompare(tmpReportFeeDto.getEndTime(), tmpReportFeeDto.getDeadlineTime());
        } else {
            month = computeFeeSMOImpl.dayCompare(tmpReportFeeDto.getEndTime(), curDate);
        }
        BigDecimal curDegree = new BigDecimal(month);
        return curDegree.multiply(feePriceDec).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }


    /**
     * 解决上线前 欠费数据
     *
     * @param reportRoomDto
     * @param tmpReportFeeDto
     */
    private void dealBeforeUploadRoomFee(ReportRoomDto reportRoomDto, ReportFeeDto tmpReportFeeDto) {


        Calendar preMonthDate = Calendar.getInstance();
        preMonthDate.set(Calendar.DAY_OF_MONTH, 1);
        preMonthDate.add(Calendar.DAY_OF_MONTH, -1);

        //当月一日
        Calendar curMonthDate = Calendar.getInstance();
        curMonthDate.set(Calendar.DAY_OF_MONTH, 1);
        curMonthDate.set(Calendar.HOUR_OF_DAY, 0);
        curMonthDate.set(Calendar.MINUTE, 0);
        curMonthDate.set(Calendar.SECOND, 0);
        if (tmpReportFeeDto.getEndTime().getTime() > curMonthDate.getTime().getTime()) { //说明没有欠费
            return;
        }

        if (tmpReportFeeDto.getDeadlineTime().getTime() < curMonthDate.getTime().getTime()) {
            curMonthDate.setTime(tmpReportFeeDto.getDeadlineTime());
        }

        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setCommunityId(reportRoomDto.getCommunityId());
        reportFeeMonthStatisticsDto.setConfigId(tmpReportFeeDto.getConfigId());
        reportFeeMonthStatisticsDto.setObjId(tmpReportFeeDto.getPayerObjId());
        reportFeeMonthStatisticsDto.setFeeId(tmpReportFeeDto.getFeeId());
        reportFeeMonthStatisticsDto.setObjType(tmpReportFeeDto.getPayerObjType());
        reportFeeMonthStatisticsDto.setFeeYear(preMonthDate.get(Calendar.YEAR) + "");
        reportFeeMonthStatisticsDto.setFeeMonth((preMonthDate.get(Calendar.MONTH) + 1) + "");
        List<ReportFeeMonthStatisticsDto> statistics = BeanConvertUtil.covertBeanList(
                reportFeeMonthStatisticsServiceDaoImpl.getReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)),
                ReportFeeMonthStatisticsDto.class);
        //上个月有数据 不处理
        if (statistics != null && statistics.size() > 0) {
            return;
        }

        double receivableAmount = 0.0;
        if (FeeDto.FEE_FLAG_ONCE.equals(tmpReportFeeDto.getFeeFlag())) {
            receivableAmount = tmpReportFeeDto.getFeePrice();
        } else {
            double month = computeFeeSMOImpl.dayCompare(tmpReportFeeDto.getEndTime(), curMonthDate.getTime());
            BigDecimal curDegree = new BigDecimal(month);
            receivableAmount = curDegree.multiply(new BigDecimal(tmpReportFeeDto.getFeePrice())).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }
        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        reportFeeMonthStatisticsPo.setDeadlineTime(DateUtil.getFormatTimeString(curMonthDate.getTime(), DateUtil.DATE_FORMATE_STRING_A));

        reportFeeMonthStatisticsPo.setOweAmount(receivableAmount + "");
        reportFeeMonthStatisticsPo.setReceivedAmount("0");
        reportFeeMonthStatisticsPo.setReceivableAmount(receivableAmount + "");
        reportFeeMonthStatisticsPo.setStatisticsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
        reportFeeMonthStatisticsPo.setCommunityId(tmpReportFeeDto.getCommunityId());
        reportFeeMonthStatisticsPo.setConfigId(tmpReportFeeDto.getConfigId());
        reportFeeMonthStatisticsPo.setFeeCreateTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        reportFeeMonthStatisticsPo.setFeeId(tmpReportFeeDto.getFeeId());
        reportFeeMonthStatisticsPo.setFeeMonth((preMonthDate.get(Calendar.MONTH) + 1) + "");
        reportFeeMonthStatisticsPo.setFeeYear(preMonthDate.get(Calendar.YEAR) + "");
        reportFeeMonthStatisticsPo.setCurMaxTime(DateUtil.getFormatTimeString(DateUtil.getFirstDate(), DateUtil.DATE_FORMATE_STRING_A));

        reportFeeMonthStatisticsPo.setObjId(reportRoomDto.getRoomId());
        reportFeeMonthStatisticsPo.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        reportFeeMonthStatisticsPo.setFeeName(StringUtil.isEmpty(tmpReportFeeDto.getImportFeeName()) ? tmpReportFeeDto.getFeeName() : tmpReportFeeDto.getImportFeeName());
        if (RoomDto.ROOM_TYPE_ROOM.equals(reportRoomDto.getRoomType())) {
            reportFeeMonthStatisticsPo.setObjName(reportRoomDto.getFloorNum() + "栋" + reportRoomDto.getUnitNum() + "单元" + reportRoomDto.getRoomNum() + "室");
            reportFeeMonthStatisticsPo.setObjNameNum(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getUnitNum() + "-" + reportRoomDto.getRoomNum());
        } else {
            reportFeeMonthStatisticsPo.setObjName(reportRoomDto.getFloorNum() + "栋" + reportRoomDto.getRoomNum() + "室");
            reportFeeMonthStatisticsPo.setObjNameNum(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getRoomNum());
        }
        //计算历史欠费
        reportFeeMonthStatisticsPo.setHisOweAmount("0");
        reportFeeMonthStatisticsPo.setCurReceivableAmount(receivableAmount + "");
        reportFeeMonthStatisticsPo.setCurReceivedAmount("0");
        reportFeeMonthStatisticsPo.setHisOweReceivedAmount("0");
        reportFeeMonthStatisticsPo.setPreReceivedAmount("0");

        reportFeeMonthStatisticsPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        reportFeeMonthStatisticsServiceDaoImpl.saveReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));


    }

    /**
     * 当月欠费
     *
     * @param tmpReportFeeDto
     * @param receivedAmount
     * @return
     */
    private double getReceivableAmount(ReportFeeDto tmpReportFeeDto, double receivedAmount) {

        //一次性费用 除以月份 平均
        if (FeeDto.FEE_FLAG_ONCE.equals(tmpReportFeeDto.getFeeFlag())) {
            return computeOnceFee(tmpReportFeeDto);
        }
        return tmpReportFeeDto.getFeePrice();

    }

    /**
     * 获取当月实收
     *
     * @param tmpReportFeeDto
     * @return
     */
    private double getCurFeeReceivedAmount(ReportFeeDto tmpReportFeeDto) {
        ReportFeeDetailDto feeDetailDto = new ReportFeeDetailDto();
        feeDetailDto.setStartTime(DateUtil.getFormatTimeString(DateUtil.getFirstDate(), DateUtil.DATE_FORMATE_STRING_A));
        feeDetailDto.setEndTime(DateUtil.getFormatTimeString(DateUtil.getNextMonthFirstDate(), DateUtil.DATE_FORMATE_STRING_A));
        feeDetailDto.setFeeId(tmpReportFeeDto.getFeeId());

        double receivedAmount = reportFeeServiceDaoImpl.getFeeReceivedAmount(feeDetailDto);

        return receivedAmount;
    }

    /**
     * 获取当月实收
     *
     * @param tmpReportFeeDto
     * @return
     */
    private double getReceivedAmount(ReportFeeDto tmpReportFeeDto) {
        ReportFeeDetailDto feeDetailDto = new ReportFeeDetailDto();
        feeDetailDto.setStartTime(DateUtil.getFormatTimeString(DateUtil.getFirstDate(), DateUtil.DATE_FORMATE_STRING_A));
        feeDetailDto.setEndTime(DateUtil.getFormatTimeString(DateUtil.getNextMonthFirstDate(), DateUtil.DATE_FORMATE_STRING_A));
        feeDetailDto.setConfigId(tmpReportFeeDto.getConfigId());
        feeDetailDto.setPayerObjId(tmpReportFeeDto.getPayerObjId());

        double receivedAmount = reportFeeServiceDaoImpl.getFeeReceivedAmount(feeDetailDto);

        return receivedAmount;
    }

    /**
     * 获取当月应收
     *
     * @param tmpReportFeeDto
     * @return
     */
    private double getOweAmountByCar(ReportFeeDto tmpReportFeeDto, ReportRoomDto reportRoomDto, ReportCarDto reportCarDto) {

        double feePrice = computeFeeSMOImpl.getReportFeePrice(tmpReportFeeDto, reportRoomDto, reportCarDto);
        tmpReportFeeDto.setFeePrice(feePrice);
        BigDecimal feePriceDec = new BigDecimal(feePrice);

        if (DateUtil.getCurrentDate().getTime() < tmpReportFeeDto.getStartTime().getTime()) {
            return 0.0;
        }

        if (FeeDto.FEE_FLAG_ONCE.equals(tmpReportFeeDto.getFeeFlag())) {
            return computeOnceFee(tmpReportFeeDto);
        }
//        OwnerCarDto ownerCarDto = new OwnerCarDto();
//        ownerCarDto.setCommunityId(tmpReportFeeDto.getCommunityId());
//        ownerCarDto.setCarId(tmpReportFeeDto.getCarId());
//        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
//        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
//            return 0.0;
//        }
        Date endTime = reportCarDto.getEndTime();

        if(endTime == null){
            return 0.0;
        }

        Date maxEndDate = tmpReportFeeDto.getDeadlineTime();
        if (FeeDto.FEE_FLAG_CYCLE.equals(tmpReportFeeDto.getFeeFlag())) {
            maxEndDate = tmpReportFeeDto.getConfigEndTime();
        }

        //1.0 费用到期时间和费用结束时间 都不在当月
        if (!belongCurMonth(tmpReportFeeDto.getEndTime())
                && !belongCurMonth(endTime)
                && tmpReportFeeDto.getEndTime().getTime() < DateUtil.getFirstDate().getTime()) {
            return feePrice;
        }

        //2.0 费用到期时间 在当月，费用结束时间不在当月
        if (belongCurMonth(tmpReportFeeDto.getEndTime())
                && !belongCurMonth(endTime)) {
            //算天数
            double month = computeFeeSMOImpl.dayCompare(tmpReportFeeDto.getEndTime(), DateUtil.getNextMonthFirstDate());
            BigDecimal curDegree = new BigDecimal(month);
            return curDegree.multiply(feePriceDec).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }
        //3.0 费用到期时间 不在当月，费用结束时间在当月
        if (!belongCurMonth(tmpReportFeeDto.getEndTime())
                && belongCurMonth(endTime)) {
            //算天数
            double month = computeFeeSMOImpl.dayCompare(DateUtil.getFirstDate(), maxEndDate);
            BigDecimal curDegree = new BigDecimal(month);
            return curDegree.multiply(feePriceDec).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }
        return 0.0;
    }

    private double computeOnceFee(ReportFeeDto tmpReportFeeDto) {
        Date nowTime = DateUtil.getCurrentDate();
        if (tmpReportFeeDto.getEndTime().getTime() > nowTime.getTime()
                || tmpReportFeeDto.getDeadlineTime().getTime() < nowTime.getTime()) {
            return 0.0;
        }
        double month = computeFeeSMOImpl.dayCompare(tmpReportFeeDto.getDeadlineTime(), tmpReportFeeDto.getEndTime());
        month = Math.ceil(month);

        if (month == 0) {
            logger.debug("相差月份为0，{}", JSONObject.toJSONString(tmpReportFeeDto));
            return tmpReportFeeDto.getFeePrice();
        }

        BigDecimal feePriceDec = new BigDecimal(tmpReportFeeDto.getFeePrice());
        double money = feePriceDec.divide(new BigDecimal(month), FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        return money;
    }

    /**
     * 获取当月应收
     *
     * @param tmpReportFeeDto
     * @return
     */
    private double getOweAmount(ReportFeeDto tmpReportFeeDto, ReportRoomDto reportRoomDto, ReportCarDto reportCarDto) {

        double feePrice = computeFeeSMOImpl.getReportFeePrice(tmpReportFeeDto, reportRoomDto, reportCarDto);
        tmpReportFeeDto.setFeePrice(feePrice);
        BigDecimal feePriceDec = new BigDecimal(feePrice);

        if (DateUtil.getCurrentDate().getTime() < tmpReportFeeDto.getStartTime().getTime()) {
            return 0.0;
        }

        if (FeeDto.FEE_FLAG_ONCE.equals(tmpReportFeeDto.getFeeFlag())) {
            return computeOnceFee(tmpReportFeeDto);
        }

        Date maxEndDate = tmpReportFeeDto.getDeadlineTime();
        if (FeeDto.FEE_FLAG_CYCLE.equals(tmpReportFeeDto.getFeeFlag())) {
            maxEndDate = tmpReportFeeDto.getConfigEndTime();
        }

        //1.0 费用到期时间和费用结束时间 都不在当月
        if (!belongCurMonth(tmpReportFeeDto.getEndTime())
                && !belongCurMonth(maxEndDate)
                && tmpReportFeeDto.getEndTime().getTime() < DateUtil.getFirstDate().getTime()) {
            return feePrice;
        }

        //2.0 费用到期时间 在当月，费用结束时间不在当月
        if (belongCurMonth(tmpReportFeeDto.getEndTime())
                && !belongCurMonth(maxEndDate)) {
            //算天数
            double month = computeFeeSMOImpl.dayCompare(tmpReportFeeDto.getEndTime(), DateUtil.getNextMonthFirstDate());
            BigDecimal curDegree = new BigDecimal(month);
            return curDegree.multiply(feePriceDec).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }
        //3.0 费用到期时间 不在当月，费用结束时间在当月
        if (!belongCurMonth(tmpReportFeeDto.getEndTime())
                && belongCurMonth(maxEndDate)) {
            //算天数
            double month = computeFeeSMOImpl.dayCompare(DateUtil.getFirstDate(), maxEndDate);
            BigDecimal curDegree = new BigDecimal(month);
            return curDegree.multiply(feePriceDec).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }
        return 0.0;
    }

    private boolean belongCurMonth(Date date) {
        if (DateUtil.belongCalendar(date, DateUtil.getFirstDate(), DateUtil.getNextMonthFirstDate())) {
            return true;
        }
        return false;
    }
}
