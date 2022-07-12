package com.java110.report.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.report.ReportCarDto;
import com.java110.dto.report.ReportFeeDetailDto;
import com.java110.dto.report.ReportFeeDto;
import com.java110.dto.report.ReportRoomDto;
import com.java110.dto.reportFeeYearCollection.ReportFeeYearCollectionDto;
import com.java110.dto.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailDto;
import com.java110.intf.report.IGeneratorFeeYearStatisticsInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import com.java110.po.reportFeeYearCollection.ReportFeeYearCollectionPo;
import com.java110.po.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailPo;
import com.java110.report.dao.IReportCommunityServiceDao;
import com.java110.report.dao.IReportFeeServiceDao;
import com.java110.report.dao.IReportFeeYearCollectionDetailServiceDao;
import com.java110.report.dao.IReportFeeYearCollectionServiceDao;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
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
public class GeneratorFeeYearStatisticsInnerServiceSMOImpl implements IGeneratorFeeYearStatisticsInnerServiceSMO {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorFeeYearStatisticsInnerServiceSMOImpl.class);

    //默认 处理房屋数量
    private static final int DEFAULT_DEAL_ROOM_COUNT = 1000;

    private static final String RECEIVED_TIME = "RECEIVED_TIME";
    private static final String RECEIVED_TIME_START = "START";
    private static final String RECEIVED_TIME_END = "END";

    @Autowired
    private IReportFeeYearCollectionServiceDao reportFeeYearCollectionServiceDaoImpl;

    @Autowired
    private IReportFeeYearCollectionDetailServiceDao reportFeeYearCollectionDetailServiceDaoImpl;

    @Autowired
    private IReportCommunityServiceDao reportCommunityServiceDaoImpl;

    @Autowired
    private IReportFeeServiceDao reportFeeServiceDaoImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public int generatorData(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        doGeneratorData(reportFeeMonthStatisticsPo);
        return 0;
    }

    @Async
    private void doGeneratorData(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {
        String communityId = reportFeeMonthStatisticsPo.getCommunityId();

        Assert.hasLength(communityId, "未包含小区信息");

        //这里处理 报表中的费用是否被人为 取消 或者费用项是否被删除，这种数据 报表中做清理，以防影响 报表的准确度
        feeDataFiltering(communityId);

        //处理房屋费用
        dealRoomFee(reportFeeMonthStatisticsPo);

        //处理车位费用
        dealCarFee(reportFeeMonthStatisticsPo);

    }

    private void feeDataFiltering(String communityId) {
        Map reportFeeDto = new HashMap();
        reportFeeDto.put("communityId", communityId);
        List<Map> feeDtos = reportCommunityServiceDaoImpl.queryInvalidFeeMonthStatistics(reportFeeDto);

        List<String> feeIds = new ArrayList<>();
        for (Map feeDto : feeDtos) {
            if (!feeDto.containsKey("feeId") || StringUtil.isNullOrNone(feeDto.get("feeId"))) {
                continue;
            }

            feeIds.add(feeDto.get("feeId").toString());

            if (feeIds.size() >= 50) {
                reportFeeDto.put("feeIds", feeIds);
                reportCommunityServiceDaoImpl.deleteInvalidFee(reportFeeDto);
                feeIds = new ArrayList<>();
            }
        }
        reportFeeDto.put("feeIds", feeIds);
        if (feeIds.size() > 0) {
            reportCommunityServiceDaoImpl.deleteInvalidFee(reportFeeDto);
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
        //reportFeeDto.setFeeFlag(FeeDto.FEE_FLAG_CYCLE);
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

        ReportFeeYearCollectionDto reportFeeYearCollectionDto = new ReportFeeYearCollectionDto();
        reportFeeYearCollectionDto.setCommunityId(tmpReportCarDto.getCommunityId());
        reportFeeYearCollectionDto.setConfigId(tmpReportFeeDto.getConfigId());
        reportFeeYearCollectionDto.setObjId(tmpReportFeeDto.getPayerObjId());
        //reportFeeYearCollectionDto.setFeeId(tmpReportFeeDto.getFeeId());
        reportFeeYearCollectionDto.setObjType(tmpReportFeeDto.getPayerObjType());
        List<ReportFeeYearCollectionDto> statistics = BeanConvertUtil.covertBeanList(
                reportFeeYearCollectionServiceDaoImpl.getReportFeeYearCollectionInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDto)),
                ReportFeeYearCollectionDto.class);

        ReportFeeYearCollectionPo reportFeeYearCollectionPo = new ReportFeeYearCollectionPo();
        if (ListUtil.isNull(statistics)) {
            reportFeeYearCollectionPo.setBuiltUpArea("0");
            reportFeeYearCollectionPo.setCollectionId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_collectionId));
            reportFeeYearCollectionPo.setCommunityId(tmpReportFeeDto.getCommunityId());
            reportFeeYearCollectionPo.setConfigId(tmpReportFeeDto.getConfigId());
            reportFeeYearCollectionPo.setFeeId(tmpReportFeeDto.getFeeId());
            reportFeeYearCollectionPo.setObjId(tmpReportCarDto.getCarId());
            reportFeeYearCollectionPo.setObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
            reportFeeYearCollectionPo.setFeeName(StringUtil.isEmpty(tmpReportFeeDto.getImportFeeName()) ? tmpReportFeeDto.getFeeName() : tmpReportFeeDto.getImportFeeName());
            reportFeeYearCollectionPo.setObjName(tmpReportCarDto.getCarNum());
            reportFeeYearCollectionPo.setOwnerId(tmpReportCarDto.getOwnerId());
            reportFeeYearCollectionPo.setOwnerName(tmpReportCarDto.getOwnerName());
            reportFeeYearCollectionPo.setOwnerLink(tmpReportCarDto.getLink());

            //reportFeeYearCollectionPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            reportFeeYearCollectionServiceDaoImpl.saveReportFeeYearCollectionInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionPo));
        } else {
            BeanConvertUtil.covertBean(statistics.get(0), reportFeeYearCollectionPo);
        }

        //计算费用项开始时间 起始 时间至现在的年份
        Calendar configStartTime = Calendar.getInstance();
        configStartTime.setTime(tmpReportFeeDto.getConfigStartTime());

        int startYear = configStartTime.get(Calendar.YEAR);
        Calendar configEndTime = Calendar.getInstance();
        configStartTime.setTime(tmpReportFeeDto.getConfigEndTime());
        int endYear = configEndTime.get(Calendar.YEAR);
        int curYear = Calendar.getInstance().get(Calendar.YEAR) + 1;

        FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
        //刷入欠费金额
        computeFeeSMOImpl.computeEveryOweFee(feeDto);

        if (endYear > curYear) {
            endYear = curYear;
        }

        for (int year = startYear; year <= endYear; year++) {
            computeYearFee(year, feeDto, reportFeeYearCollectionPo);
        }

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
        //reportFeeDto.setFeeFlag(FeeDto.FEE_FLAG_CYCLE);

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


        ReportFeeYearCollectionDto reportFeeYearCollectionDto = new ReportFeeYearCollectionDto();
        reportFeeYearCollectionDto.setCommunityId(reportRoomDto.getCommunityId());
        reportFeeYearCollectionDto.setConfigId(tmpReportFeeDto.getConfigId());
        reportFeeYearCollectionDto.setObjId(tmpReportFeeDto.getPayerObjId());
        //reportFeeYearCollectionDto.setFeeId(tmpReportFeeDto.getFeeId());
        reportFeeYearCollectionDto.setObjType(tmpReportFeeDto.getPayerObjType());
        List<ReportFeeYearCollectionDto> statistics = BeanConvertUtil.covertBeanList(
                reportFeeYearCollectionServiceDaoImpl.getReportFeeYearCollectionInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDto)),
                ReportFeeYearCollectionDto.class);

        ReportFeeYearCollectionPo reportFeeYearCollectionPo = new ReportFeeYearCollectionPo();
        if (ListUtil.isNull(statistics)) {
            reportFeeYearCollectionPo.setBuiltUpArea(reportRoomDto.getBuiltUpArea());
            reportFeeYearCollectionPo.setCollectionId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_collectionId));
            reportFeeYearCollectionPo.setCommunityId(tmpReportFeeDto.getCommunityId());
            reportFeeYearCollectionPo.setConfigId(tmpReportFeeDto.getConfigId());
            reportFeeYearCollectionPo.setFeeId(tmpReportFeeDto.getFeeId());
            reportFeeYearCollectionPo.setObjId(reportRoomDto.getRoomId());
            reportFeeYearCollectionPo.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            reportFeeYearCollectionPo.setFeeName(StringUtil.isEmpty(tmpReportFeeDto.getImportFeeName()) ? tmpReportFeeDto.getFeeName() : tmpReportFeeDto.getImportFeeName());
            if (RoomDto.ROOM_TYPE_ROOM.equals(reportRoomDto.getRoomType())) {
                reportFeeYearCollectionPo.setObjName(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getUnitNum() + "-" + reportRoomDto.getRoomNum());
            } else {
                reportFeeYearCollectionPo.setObjName(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getRoomNum());
            }
            reportFeeYearCollectionPo.setOwnerId(reportRoomDto.getOwnerId());
            reportFeeYearCollectionPo.setOwnerName(reportRoomDto.getOwnerName());
            reportFeeYearCollectionPo.setOwnerLink(reportRoomDto.getLink());

            //reportFeeYearCollectionPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            reportFeeYearCollectionServiceDaoImpl.saveReportFeeYearCollectionInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionPo));
        } else {
            BeanConvertUtil.covertBean(statistics.get(0), reportFeeYearCollectionPo);
        }

        //计算费用项开始时间 起始 时间至现在的年份
        Calendar configStartTime = Calendar.getInstance();
        configStartTime.setTime(tmpReportFeeDto.getConfigStartTime());
        int startYear = configStartTime.get(Calendar.YEAR);
        //结束年
        Calendar configEndTime = Calendar.getInstance();
        configStartTime.setTime(tmpReportFeeDto.getConfigEndTime());
        int endYear = configEndTime.get(Calendar.YEAR);

        //当前年
        int curYear = Calendar.getInstance().get(Calendar.YEAR) + 1;

        FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
        //刷入欠费金额
        computeFeeSMOImpl.computeEveryOweFee(feeDto);

        if (endYear > curYear) {
            endYear = curYear;
        }

        for (int year = startYear; year <= endYear; year++) {
            computeYearFee(year, feeDto, reportFeeYearCollectionPo);
        }
    }

    /**
     * 计算指定年的数据
     *
     * @param year
     * @param feeDto
     * @param reportFeeYearCollectionPo
     */
    private void computeYearFee(int year, FeeDto feeDto, ReportFeeYearCollectionPo reportFeeYearCollectionPo) {
        int curYear = Calendar.getInstance().get(Calendar.YEAR);

        ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto = new ReportFeeYearCollectionDetailDto();
        reportFeeYearCollectionDetailDto.setCollectionId(reportFeeYearCollectionPo.getCollectionId());
        reportFeeYearCollectionDetailDto.setCommunityId(reportFeeYearCollectionPo.getCommunityId());
        reportFeeYearCollectionDetailDto.setCollectionYear(year + "");

        List<ReportFeeYearCollectionDetailDto> reportFeeYearCollectionDetailDtos
                = BeanConvertUtil.covertBeanList(reportFeeYearCollectionDetailServiceDaoImpl.getReportFeeYearCollectionDetailInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDetailDto)),
                ReportFeeYearCollectionDetailDto.class);

//        if (!ListUtil.isNull(reportFeeYearCollectionDetailDtos) && year != curYear) { // 说明已经处理过了 不再处理
//            return;
//        }

        double receivableAmount = feeDto.getFeePrice();

        double receivedAmount = getReceivedAmount(feeDto, year);

        ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo = null;

        if (!ListUtil.isNull(reportFeeYearCollectionDetailDtos)) {
            reportFeeYearCollectionDetailPo = BeanConvertUtil.covertBean(reportFeeYearCollectionDetailDtos.get(0), ReportFeeYearCollectionDetailPo.class);
            reportFeeYearCollectionDetailPo.setReceivableAmount(receivableAmount + "");
            reportFeeYearCollectionDetailPo.setReceivedAmount(receivedAmount + "");
            reportFeeYearCollectionDetailServiceDaoImpl.updateReportFeeYearCollectionDetailInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDetailPo));
        } else {
            reportFeeYearCollectionDetailPo = new ReportFeeYearCollectionDetailPo();
            reportFeeYearCollectionDetailPo.setCollectionId(reportFeeYearCollectionPo.getCollectionId());
            reportFeeYearCollectionDetailPo.setCommunityId(reportFeeYearCollectionPo.getCommunityId());
            reportFeeYearCollectionDetailPo.setCollectionYear(year + "");
            reportFeeYearCollectionDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            reportFeeYearCollectionDetailPo.setReceivableAmount(receivableAmount + "");
            reportFeeYearCollectionDetailPo.setReceivedAmount(receivedAmount + "");
            reportFeeYearCollectionDetailPo.setRelationYear(year + "");
            reportFeeYearCollectionDetailServiceDaoImpl.saveReportFeeYearCollectionDetailInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDetailPo));
        }
    }


    /**
     * 当月欠费
     *
     * @param tmpReportFeeDto
     * @return
     */
    private double getReceivableAmount(ReportFeeDto tmpReportFeeDto) {

        //最后一年的处理
        Calendar configEndTime = Calendar.getInstance();
        configEndTime.setTime(tmpReportFeeDto.getConfigEndTime());
        int endYear = configEndTime.get(Calendar.YEAR);
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        double cycleMonth = 12;

        if (FeeDto.FEE_FLAG_ONCE.equals(tmpReportFeeDto.getFeeFlag()) && FeeDto.STATE_DOING.equals(tmpReportFeeDto.getState())) { // 一次性费用 费用没有结束
            return StringUtil.isEmpty(tmpReportFeeDto.getAmount()) ? 0 : Double.parseDouble(tmpReportFeeDto.getAmount());
        }

        if (FeeDto.FEE_FLAG_ONCE.equals(tmpReportFeeDto.getFeeFlag())) { // 一次性费用没有应收 所以写成0
            return 0;
        }

        if (endYear == curYear) {
            try {
                cycleMonth = computeFeeSMOImpl.dayCompare(DateUtil.getDateFromString(curYear + "-01-01", DateUtil.DATE_FORMATE_STRING_B), tmpReportFeeDto.getConfigEndTime());
            } catch (ParseException e) {
                logger.error("计算 时间差出错", e);
                cycleMonth = configEndTime.get(Calendar.MONTH) + 1;
            }
        }

        return new BigDecimal(tmpReportFeeDto.getFeePrice()).multiply(new BigDecimal(cycleMonth)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

    }

    /**
     * 获取当年实收
     *
     * @param tmpReportFeeDto
     * @return
     */
    private double getReceivedAmount(FeeDto tmpReportFeeDto, int year) {
        ReportFeeDetailDto feeDetailDto = new ReportFeeDetailDto();
        feeDetailDto.setConfigId(tmpReportFeeDto.getConfigId());
        feeDetailDto.setPayerObjId(tmpReportFeeDto.getPayerObjId());
        String flag = CommonCache.getValue(RECEIVED_TIME);
        if (RECEIVED_TIME_START.equals(flag)) {
            feeDetailDto.setCurStartYear(year + "");
        } else {
            feeDetailDto.setCurEndYear(year + "");
        }
        double receivedAmount = reportFeeServiceDaoImpl.getFeeReceivedAmount(feeDetailDto);

        return receivedAmount;
    }

}
