package com.java110.report.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.report.ReportCarDto;
import com.java110.dto.report.ReportFeeDetailDto;
import com.java110.dto.report.ReportFeeDto;
import com.java110.dto.report.ReportRoomDto;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.intf.report.IGeneratorFeeMonthStatisticsInnerServiceSMO;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import com.java110.report.dao.IReportCommunityServiceDao;
import com.java110.report.dao.IReportFeeMonthStatisticsServiceDao;
import com.java110.report.dao.IReportFeeServiceDao;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private IReportCommunityServiceDao reportCommunityServiceDaoImpl;

    @Autowired
    private IReportFeeServiceDao reportFeeServiceDaoImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Override
    public int generatorData(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        doGeneratorData(reportFeeMonthStatisticsPo);
        return 0;
    }

    @Async
    private void doGeneratorData(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {
        String communityId = reportFeeMonthStatisticsPo.getCommunityId();

        Assert.hasLength(communityId, "未包含小区信息");

        //处理房屋费用
        dealRoomFee(reportFeeMonthStatisticsPo);

        //处理车位费用
        dealCarFee(reportFeeMonthStatisticsPo);
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
        reportFeeDto.setState(FeeDto.STATE_DOING);
        List<ReportFeeDto> feeDtos = reportFeeServiceDaoImpl.getFees(reportFeeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }

        for (ReportFeeDto tmpReportFeeDto : feeDtos) {
            try {
                doDealCarFee(tmpReportCarDto, tmpReportFeeDto);
            } catch (Exception e) {
                logger.error("处理房屋费用失败" + JSONObject.toJSONString(tmpReportFeeDto), e);
            }
        }
    }

    private void doDealCarFee(ReportCarDto tmpReportCarDto, ReportFeeDto tmpReportFeeDto) {

        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setCommunityId(tmpReportCarDto.getCommunityId());
        reportFeeMonthStatisticsDto.setConfigId(tmpReportFeeDto.getConfigId());
        reportFeeMonthStatisticsDto.setObjId(tmpReportFeeDto.getPayerObjId());
        reportFeeMonthStatisticsDto.setObjType(tmpReportFeeDto.getPayerObjType());
        reportFeeMonthStatisticsDto.setFeeYear(DateUtil.getYear() + "");
        reportFeeMonthStatisticsDto.setFeeMonth(DateUtil.getMonth() + "");
        List<ReportFeeMonthStatisticsDto> statistics = BeanConvertUtil.covertBeanList(
                reportFeeMonthStatisticsServiceDaoImpl.getReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)),
                ReportFeeMonthStatisticsDto.class);

        double receivedAmount = getReceivedAmount(tmpReportFeeDto); //实收
        //费用已经结束 并且当月实收为0 那就是 之前就结束了 无需处理  && ListUtil.isNull(statistics)
        if (FeeDto.STATE_FINISH.equals(tmpReportFeeDto.getState())
                && receivedAmount == 0) {
            return;
        }
        double receivableAmount = getReceivableAmount(tmpReportFeeDto,null,tmpReportCarDto); //应收
        double oweAmount = getOweAmount(tmpReportFeeDto, receivableAmount, receivedAmount); //欠费

        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        if (!ListUtil.isNull(statistics)) {
            ReportFeeMonthStatisticsDto statistic = statistics.get(0);
            reportFeeMonthStatisticsPo.setStatisticsId(statistic.getStatisticsId());
            reportFeeMonthStatisticsPo.setReceivableAmount(receivableAmount + "");
            reportFeeMonthStatisticsPo.setReceivedAmount(receivedAmount + "");
            reportFeeMonthStatisticsPo.setOweAmount(oweAmount + "");
            reportFeeMonthStatisticsPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        } else {
            reportFeeMonthStatisticsPo.setOweAmount(oweAmount + "");
            reportFeeMonthStatisticsPo.setReceivedAmount(receivedAmount + "");
            reportFeeMonthStatisticsPo.setReceivableAmount(receivableAmount + "");
            reportFeeMonthStatisticsPo.setStatisticsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
            reportFeeMonthStatisticsPo.setCommunityId(tmpReportFeeDto.getCommunityId());
            reportFeeMonthStatisticsPo.setConfigId(tmpReportFeeDto.getConfigId());
            reportFeeMonthStatisticsPo.setFeeCreateTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsPo.setFeeId(tmpReportFeeDto.getFeeId());
            reportFeeMonthStatisticsPo.setFeeMonth(DateUtil.getMonth() + "");
            reportFeeMonthStatisticsPo.setFeeYear(DateUtil.getYear() + "");
            reportFeeMonthStatisticsPo.setObjId(tmpReportCarDto.getCarId());
            reportFeeMonthStatisticsPo.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            reportFeeMonthStatisticsPo.setFeeName(tmpReportFeeDto.getFeeName());
            reportFeeMonthStatisticsPo.setObjName(tmpReportCarDto.getCarNum() + "(" + tmpReportCarDto.getAreaNum() + "停车场" + tmpReportCarDto.getNum() + "车位");
            reportFeeMonthStatisticsPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsServiceDaoImpl.saveReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        }


        Date endTime = tmpReportFeeDto.getEndTime();

        Calendar calender = Calendar.getInstance();
        calender.setTime(endTime);
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);

        ReportFeeMonthStatisticsPo tmpReportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        tmpReportFeeMonthStatisticsPo.setFeeId(tmpReportFeeDto.getFeeId());
        tmpReportFeeMonthStatisticsPo.setFeeYear(year + "");
        tmpReportFeeMonthStatisticsPo.setFeeMonth(month + "");
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
        reportFeeDto.setState(FeeDto.STATE_DOING);
        List<ReportFeeDto> feeDtos = reportFeeServiceDaoImpl.getFees(reportFeeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }

        for (ReportFeeDto tmpReportFeeDto : feeDtos) {
            try {
                doDealRoomFee(reportRoomDto, tmpReportFeeDto);
            } catch (Exception e) {
                logger.error("处理房屋费用失败" + JSONObject.toJSONString(tmpReportFeeDto), e);
            }
        }


    }

    private void doDealRoomFee(ReportRoomDto reportRoomDto, ReportFeeDto tmpReportFeeDto) {

        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setCommunityId(reportRoomDto.getCommunityId());
        reportFeeMonthStatisticsDto.setConfigId(tmpReportFeeDto.getConfigId());
        reportFeeMonthStatisticsDto.setObjId(tmpReportFeeDto.getPayerObjId());
        reportFeeMonthStatisticsDto.setObjType(tmpReportFeeDto.getPayerObjType());
        reportFeeMonthStatisticsDto.setFeeYear(DateUtil.getYear() + "");
        reportFeeMonthStatisticsDto.setFeeMonth(DateUtil.getMonth() + "");
        List<ReportFeeMonthStatisticsDto> statistics = BeanConvertUtil.covertBeanList(
                reportFeeMonthStatisticsServiceDaoImpl.getReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)),
                ReportFeeMonthStatisticsDto.class);

        double receivedAmount = getReceivedAmount(tmpReportFeeDto); //实收
        //费用已经结束 并且当月实收为0 那就是 之前就结束了 无需处理  && ListUtil.isNull(statistics)
        if (FeeDto.STATE_FINISH.equals(tmpReportFeeDto.getState())
                && receivedAmount == 0) {
            return;
        }
        double receivableAmount = getReceivableAmount(tmpReportFeeDto,reportRoomDto,null); //应收
        double oweAmount = getOweAmount(tmpReportFeeDto, receivableAmount, receivedAmount); //欠费

        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        if (!ListUtil.isNull(statistics)) {
            ReportFeeMonthStatisticsDto statistic = statistics.get(0);
            reportFeeMonthStatisticsPo.setStatisticsId(statistic.getStatisticsId());
            reportFeeMonthStatisticsPo.setReceivableAmount(receivableAmount + "");
            reportFeeMonthStatisticsPo.setReceivedAmount(receivedAmount + "");
            reportFeeMonthStatisticsPo.setOweAmount(oweAmount + "");
            reportFeeMonthStatisticsPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        } else {
            reportFeeMonthStatisticsPo.setOweAmount(oweAmount + "");
            reportFeeMonthStatisticsPo.setReceivedAmount(receivedAmount + "");
            reportFeeMonthStatisticsPo.setReceivableAmount(receivableAmount + "");
            reportFeeMonthStatisticsPo.setStatisticsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
            reportFeeMonthStatisticsPo.setCommunityId(tmpReportFeeDto.getCommunityId());
            reportFeeMonthStatisticsPo.setConfigId(tmpReportFeeDto.getConfigId());
            reportFeeMonthStatisticsPo.setFeeCreateTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsPo.setFeeId(tmpReportFeeDto.getFeeId());
            reportFeeMonthStatisticsPo.setFeeMonth(DateUtil.getMonth() + "");
            reportFeeMonthStatisticsPo.setFeeYear(DateUtil.getYear() + "");
            reportFeeMonthStatisticsPo.setObjId(reportRoomDto.getRoomId());
            reportFeeMonthStatisticsPo.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            reportFeeMonthStatisticsPo.setFeeName(tmpReportFeeDto.getFeeName());
            reportFeeMonthStatisticsPo.setObjName(reportRoomDto.getFloorNum() + "栋" + reportRoomDto.getUnitNum() + "单元" + reportRoomDto.getRoomNum() + "室");
            reportFeeMonthStatisticsPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            reportFeeMonthStatisticsServiceDaoImpl.saveReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        }


        Date endTime = tmpReportFeeDto.getEndTime();

        Calendar calender = Calendar.getInstance();
        calender.setTime(endTime);
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);

        ReportFeeMonthStatisticsPo tmpReportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        tmpReportFeeMonthStatisticsPo.setFeeId(tmpReportFeeDto.getFeeId());
        tmpReportFeeMonthStatisticsPo.setFeeYear(year + "");
        tmpReportFeeMonthStatisticsPo.setFeeMonth(month + "");
        tmpReportFeeMonthStatisticsPo.setOweAmount("0");
        reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsOwe(BeanConvertUtil.beanCovertMap(tmpReportFeeMonthStatisticsPo));
    }

    /**
     * 当月欠费
     *
     * @param tmpReportFeeDto
     * @param receivableAmount
     * @param receivedAmount
     * @return
     */
    private double getOweAmount(ReportFeeDto tmpReportFeeDto, double receivableAmount, double receivedAmount) {

        if (receivableAmount == 0) {
            return 0;
        }

        if (receivedAmount > receivableAmount) {
            return 0;
        }

        BigDecimal receivedAmountDec = new BigDecimal(receivedAmount);
        BigDecimal receivableAmountDec = new BigDecimal(receivedAmount);

        return receivableAmountDec.subtract(receivedAmountDec).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
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
        feeDetailDto.setFeeId(tmpReportFeeDto.getFeeId());
        double receivedAmount = reportFeeServiceDaoImpl.getFeeReceivedAmount(feeDetailDto);

        return receivedAmount;
    }

    /**
     * 获取当月应收
     *
     * @param tmpReportFeeDto
     * @return
     */
    private double getReceivableAmount(ReportFeeDto tmpReportFeeDto,ReportRoomDto reportRoomDto,ReportCarDto reportCarDto) {

        double feePrice = computeFeeSMOImpl.getReportFeePrice(tmpReportFeeDto,reportRoomDto,reportCarDto);
        BigDecimal feePriceDec = new BigDecimal(feePrice);

        if (FeeDto.FEE_FLAG_ONCE.equals(tmpReportFeeDto.getFeeTypeCd())) {
            return feePrice;
        }

        //1.0 费用到期时间和费用结束时间 都不在当月
        if (!belongCurMonth(tmpReportFeeDto.getEndTime())
                && !belongCurMonth(tmpReportFeeDto.getConfigEndTime())
                && tmpReportFeeDto.getEndTime().getTime() < DateUtil.getFirstDate().getTime()) {
            return feePrice;
        }

        //2.0 费用到期时间 在当月，费用结束时间不在当月
        if (belongCurMonth(tmpReportFeeDto.getEndTime())
                && !belongCurMonth(tmpReportFeeDto.getConfigEndTime())) {
            //算天数
            double month = computeFeeSMOImpl.dayCompare(tmpReportFeeDto.getEndTime(), DateUtil.getNextMonthFirstDate());
            BigDecimal curDegree = new BigDecimal(month);
            return curDegree.multiply(feePriceDec).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }
        //3.0 费用到期时间 不在当月，费用结束时间在当月
        if (!belongCurMonth(tmpReportFeeDto.getEndTime())
                && belongCurMonth(tmpReportFeeDto.getConfigEndTime())) {
            //算天数
            double month = computeFeeSMOImpl.dayCompare(DateUtil.getFirstDate(), tmpReportFeeDto.getConfigEndTime());
            BigDecimal curDegree = new BigDecimal(month);
            return curDegree.multiply(feePriceDec).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
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
