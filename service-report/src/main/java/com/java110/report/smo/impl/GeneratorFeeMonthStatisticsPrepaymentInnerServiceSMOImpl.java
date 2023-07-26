package com.java110.report.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.report.ReportCarDto;
import com.java110.dto.report.ReportFeeDetailDto;
import com.java110.dto.report.ReportFeeDto;
import com.java110.dto.report.ReportRoomDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.report.IGeneratorFeeMonthStatisticsPrepaymentInnerServiceSMO;
import com.java110.po.ReportFeeMonthStatisticsPrepaymentPo.ReportFeeMonthStatisticsPrepaymentPo;
import com.java110.report.dao.IReportCommunityServiceDao;
import com.java110.report.dao.IReportFeeMonthStatisticsPrepaymentServiceDao;
import com.java110.report.dao.IReportFeeServiceDao;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 定时任务生成预付期账单报表
 *
 * @author fqz
 * @date 2023-04-01
 */
@RestController
public class GeneratorFeeMonthStatisticsPrepaymentInnerServiceSMOImpl implements IGeneratorFeeMonthStatisticsPrepaymentInnerServiceSMO {

    @Autowired
    private IReportFeeMonthStatisticsPrepaymentServiceDao reportFeeMonthStatisticsPrepaymentServiceDaoImpl;

    @Autowired
    private IReportCommunityServiceDao reportCommunityServiceDaoImpl;

    @Autowired
    private IReportFeeServiceDao reportFeeServiceDaoImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO feeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    private static final Logger logger = LoggerFactory.getLogger(GeneratorFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.class);

    //默认 处理房屋数量
    private static final int DEFAULT_DEAL_ROOM_COUNT = 1000;

    @Override
    public int generatorData(@RequestBody ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo) {
        doGeneratorData(reportFeeMonthStatisticsPrepaymentPo);
        return 0;
    }

    @Async
    public void doGeneratorData(ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo) {
        String communityId = reportFeeMonthStatisticsPrepaymentPo.getCommunityId();
        Assert.hasLength(communityId, "未包含小区信息");
        //这里处理报表中的费用是否被人为取消,或者费用项是否被删除，这种数据报表中做清理，以防影响报表的准确度
        feeDataFiltering(communityId);
        //处理房屋费用
        dealRoomFee(reportFeeMonthStatisticsPrepaymentPo);
        //处理车位费用
        dealCarFee(reportFeeMonthStatisticsPrepaymentPo);
        //处理缴费结束的情况
        dealFinishFee(communityId);
    }

    private void feeDataFiltering(String communityId) {
        Map reportFeeDto = new HashMap();
        reportFeeDto.put("communityId", communityId);
        //查询费用或费用项失效的数据(即status_cd为1)
        List<Map> feeDtos = reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryInvalidFeeMonthStatisticsPrepayment(reportFeeDto);
        List<String> feeIds = new ArrayList<>();
        for (Map feeDto : feeDtos) {
            if (!feeDto.containsKey("feeId") || StringUtil.isNullOrNone(feeDto.get("feeId"))) {
                continue;
            }
            FeeConfigDto feeConfigDto = new FeeConfigDto();
            feeConfigDto.setConfigId(String.valueOf(feeDto.get("configId")));
            List<FeeConfigDto> feeConfigDtos = feeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
            if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
                continue;
            }
            if (StringUtil.isEmpty(feeConfigDtos.get(0).getPrepaymentPeriod()) || feeConfigDtos.get(0).getPrepaymentPeriod().equals("-1")) {
                continue;
            }
            feeIds.add(feeDto.get("feeId").toString());
            if (feeIds.size() >= 50) {
                reportFeeDto.put("feeIds", feeIds);
                reportFeeMonthStatisticsPrepaymentServiceDaoImpl.deleteInvalidFee(reportFeeDto);
                feeIds = new ArrayList<>();
            }
        }
        reportFeeDto.put("feeIds", feeIds);
        if (feeIds.size() > 0) {
            reportFeeMonthStatisticsPrepaymentServiceDaoImpl.deleteInvalidFee(reportFeeDto);
        }
    }

    /**
     * 处理 房屋费用
     *
     * @param reportFeeMonthStatisticsPrepaymentPo
     */
    private void dealRoomFee(ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo) {
        int page = 0;
        int max = DEFAULT_DEAL_ROOM_COUNT;
        ReportRoomDto reportRoomDto = new ReportRoomDto();
        reportRoomDto.setCommunityId(reportFeeMonthStatisticsPrepaymentPo.getCommunityId());
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

    private void dealFinishFee(String communityId) {
        Map reportFeeDto = new HashMap();
        reportFeeDto.put("communityId", communityId);
        List<Map> feeDtos = reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryFinishOweFee(reportFeeDto);
        ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = null;
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
                reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
                reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(info.get("prepaymentId").toString());
                reportFeeMonthStatisticsPrepaymentPo.setReceivedAmount(receivedAmount + "");
                reportFeeMonthStatisticsPrepaymentPo.setOweAmount("0");
                reportFeeMonthStatisticsPrepaymentPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                reportFeeMonthStatisticsPrepaymentServiceDaoImpl.updateReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
            } catch (Exception e) {
                logger.error("处理 缴费结束报表失败", e);
            }
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

    /**
     * 处理车位 车辆费用
     *
     * @param reportFeeMonthStatisticsPrepaymentPo
     */
    private void dealCarFee(ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo) {
        int page = 0;
        int max = DEFAULT_DEAL_ROOM_COUNT;
        ReportCarDto reportCarDto = new ReportCarDto();
        reportCarDto.setCommunityId(reportFeeMonthStatisticsPrepaymentPo.getCommunityId());
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

    private void doDealCarFees(ReportCarDto tmpReportCarDto) {
        ReportFeeDto reportFeeDto = new ReportFeeDto();
        reportFeeDto.setPayerObjId(tmpReportCarDto.getCarId());
        reportFeeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
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
        if (FeeDto.STATE_FINISH.equals(tmpReportFeeDto.getState())) {
            return;
        }
        //查询费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(tmpReportFeeDto.getConfigId());
        List<FeeConfigDto> feeConfigDtos = feeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
            return;
        }
        //获取预付期
        String prepaymentPeriod = feeConfigDtos.get(0).getPrepaymentPeriod();
        if (StringUtil.isEmpty(prepaymentPeriod) || prepaymentPeriod.equals("-1")) {
            return;
        }
        int prepaymentDay = Integer.parseInt(prepaymentPeriod);
        //获取费用的开始时间（费用结束时间即为下次费用开始时间）
        Date beginTime = tmpReportFeeDto.getEndTime();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(beginTime);
        long time1 = cal1.getTimeInMillis(); //费用开始时间
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(new Date());
        long time2 = cal2.getTimeInMillis(); //当前时间
        if (time2 <= time1) { //如果当前时间小于等于下次计费开始时间，说明该费用没有欠费
            long between_days = (time1 - time2) / (1000 * 3600 * 24); //计算费用开始时间距当前时间有多少天
            int betweenDay = Integer.parseInt(String.valueOf(between_days));
            if (prepaymentDay >= betweenDay) { //费用开始时间距当前时间在费用项预付期内，就生成报表
                ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
                reportFeeMonthStatisticsPrepaymentDto.setCommunityId(tmpReportCarDto.getCommunityId());
                reportFeeMonthStatisticsPrepaymentDto.setConfigId(tmpReportFeeDto.getConfigId());
                reportFeeMonthStatisticsPrepaymentDto.setObjId(tmpReportFeeDto.getPayerObjId());
                reportFeeMonthStatisticsPrepaymentDto.setFeeId(tmpReportFeeDto.getFeeId());//这里不能注释 如果一个费用创建多条时会有bug
                reportFeeMonthStatisticsPrepaymentDto.setObjType(tmpReportFeeDto.getPayerObjType());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                reportFeeMonthStatisticsPrepaymentDto.setFeeBeginTime(sdf.format(tmpReportFeeDto.getStartTime()));
                reportFeeMonthStatisticsPrepaymentDto.setFeeFinishTime(sdf.format(tmpReportFeeDto.getEndTime()));
                List<ReportFeeMonthStatisticsPrepaymentDto> statistics = BeanConvertUtil.covertBeanList(
                        reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)),
                        ReportFeeMonthStatisticsPrepaymentDto.class);
                if (!ListUtil.isNull(statistics)) { //表示报表中已经存在一条该费用信息了(说明该费用跑过一次定时任务，且生成了一条预付期费用账单报表)，此时无需处理
                    return;
                } else {
                    //获取费用标识
                    String feeFlag = feeConfigDtos.get(0).getFeeFlag();
                    if (!StringUtil.isEmpty(feeFlag) && (feeFlag.equals("1003006") || feeFlag.equals("4012024"))) { //1003006 为周期性费用;2006012 为一次性费用;4012024 间接性费用
                        //获取缴费周期
                        String paymentCycle = feeConfigDtos.get(0).getPaymentCycle();
                        int cycle = Integer.parseInt(paymentCycle);
                        cal1.add(Calendar.MONTH, cycle);
                        Date time = cal1.getTime();
                        tmpReportFeeDto.setNewEndTime(time);
                    } else {
                        tmpReportFeeDto.setNewEndTime(tmpReportFeeDto.getEndTime());
                    }
                    String sign = "1";
                    savaReportCarFeeMonthStatisticsPrepayment(tmpReportCarDto, tmpReportFeeDto, feeConfigDtos, sign);
                }
            }
        } else { //当前时间超过费用下次费用开始时间，说明已经逾期了，有欠费
            int startYear = cal1.get(Calendar.YEAR); //获取费用开始时间年份
            int startMonth = cal1.get(Calendar.MONTH); //获取费用开始时间月份
            int startDay = cal1.get(Calendar.DATE); //获取费用开始日份
            int nowYear = cal2.get(Calendar.YEAR); //获取当前时间年份
            int nowMonth = cal2.get(Calendar.MONTH); //获取当前时间月份
            int nowDay = cal2.get(Calendar.DATE); //获取当前日份
            int monthNum = (nowYear - startYear) * 12 + (nowMonth - startMonth); //计算当前时间与费用开始时间相差几个月
            if (monthNum == 0) {
                int day = 0;
                if (nowDay > startDay) {
                    day = nowDay - startDay;
                } else {
                    day = startDay - nowDay;
                }
                if (day > 0) {
                    monthNum = 1;
                }
            }
            //获取费用标识
            String feeFlag = feeConfigDtos.get(0).getFeeFlag();
            if (!StringUtil.isEmpty(feeFlag) && (feeFlag.equals("1003006") || feeFlag.equals("4012024"))) { //1003006 为周期性费用;2006012 为一次性费用;4012024 间接性费用
                //获取缴费周期
                String paymentCycle = feeConfigDtos.get(0).getPaymentCycle();
                double cycle = Double.parseDouble(paymentCycle);
                double hasCycle = Math.ceil(monthNum / cycle); //向上取整(计算当前日期与费用开始时间之间有几个周期)
                for (int i = 1; i <= hasCycle; i++) {
                    //获取费用开始时间
                    Date endTime = tmpReportFeeDto.getEndTime();
                    int cle = Integer.parseInt(paymentCycle); //周期
                    int newCycle = cle * i; //有几个周期
                    Calendar cal4 = Calendar.getInstance();
                    cal4.setTime(endTime);
                    cal4.add(Calendar.MONTH, newCycle);
                    Date nextFeeTime = cal4.getTime(); //获取下次计费开始时间
                    ReportFeeMonthStatisticsPrepaymentDto feeMonthStatisticsPrepayment = new ReportFeeMonthStatisticsPrepaymentDto();
                    feeMonthStatisticsPrepayment.setCommunityId(tmpReportCarDto.getCommunityId());
                    feeMonthStatisticsPrepayment.setConfigId(tmpReportFeeDto.getConfigId());
                    feeMonthStatisticsPrepayment.setObjId(tmpReportFeeDto.getPayerObjId());
                    feeMonthStatisticsPrepayment.setFeeId(tmpReportFeeDto.getFeeId());
                    feeMonthStatisticsPrepayment.setObjType(tmpReportFeeDto.getPayerObjType());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    feeMonthStatisticsPrepayment.setFeeFinishTime(sdf.format(nextFeeTime));
                    //查询预付期费用账单报表里是否已经存在过该费用的报表信息了
                    List<ReportFeeMonthStatisticsPrepaymentDto> statistics = BeanConvertUtil.covertBeanList(
                            reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(feeMonthStatisticsPrepayment)),
                            ReportFeeMonthStatisticsPrepaymentDto.class);
                    if (!ListUtil.isNull(statistics)) { //表示报表中已经存在一条该费用信息了(说明该费用跑过一次定时任务，且生成了一条预付期费用账单报表)，此时无需处理
                        continue;
                    } else {
                        String sign = "3";
                        Calendar cal3 = Calendar.getInstance();
                        cal3.setTime(nextFeeTime);
                        cal3.add(Calendar.MONTH, -cle);
                        Date time = cal3.getTime();
                        tmpReportFeeDto.setNewStartTime(time); //本次费用开始时间
                        tmpReportFeeDto.setNewEndTime(nextFeeTime); //本次费用结束时间
                        savaReportCarFeeMonthStatisticsPrepayment(tmpReportCarDto, tmpReportFeeDto, feeConfigDtos, sign);
                    }
                    Calendar cal5 = Calendar.getInstance();
                    cal5.setTime(nextFeeTime);
                    long newBeginTime = cal5.getTimeInMillis(); //下次费用开始时间(即本次费用结束时间)
                    Calendar cal6 = Calendar.getInstance();
                    cal6.setTime(new Date()); //当前时间
                    long newDateTime = cal6.getTimeInMillis(); //当前时间
                    if (newBeginTime > newDateTime) { //如果下次费用开始时间大于当前时间，说明本次费用还没有逾期，接下来判断是否在预付期内
                        long between_days = (newBeginTime - newDateTime) / (1000 * 3600 * 24); //计算下次费用开始时间距当前时间有多少天
                        int betweenDay = Integer.parseInt(String.valueOf(between_days));
                        if (prepaymentDay >= betweenDay) { //费用开始时间距当前时间在费用项预付期内，就生成报表
                            tmpReportFeeDto.setNewStartTime(nextFeeTime);
                            cal5.add(Calendar.MONTH, cle);
                            Date nextEndTime = cal5.getTime();
                            tmpReportFeeDto.setNewEndTime(nextEndTime);
                            String sign = "2";
                            savaReportCarFeeMonthStatisticsPrepayment(tmpReportCarDto, tmpReportFeeDto, feeConfigDtos, sign);
                        }
                    } else { //如果当前时间大于下次费用开始时间，说明本次费用已经逾期，接下来就需要查询出当前时间之前报表里状态位未缴费的数据，把数据改成已逾期
                        ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
                        reportFeeMonthStatisticsPrepaymentDto.setFeeId(tmpReportFeeDto.getFeeId());
                        reportFeeMonthStatisticsPrepaymentDto.setPrepaymentState("0"); //未缴费
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        reportFeeMonthStatisticsPrepaymentDto.setNextStartTime(format.format(nextFeeTime)); //下次计费开始日期
                        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentDtos = BeanConvertUtil.covertBeanList(
                                reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getReportFeeMonthStatisticsPrepaymentInfo(
                                        BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)), ReportFeeMonthStatisticsPrepaymentDto.class);
                        if (reportFeeMonthStatisticsPrepaymentDtos != null && reportFeeMonthStatisticsPrepaymentDtos.size() > 0) {
                            for (ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepayment : reportFeeMonthStatisticsPrepaymentDtos) {
                                ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
                                reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(reportFeeMonthStatisticsPrepayment.getPrepaymentId());
                                reportFeeMonthStatisticsPrepaymentPo.setbillState("1"); //账单状态：0 正常；1 已逾期； 2已还清
                                FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
                                FeeDto feeDto1 = dealAmount(feeDto);
                                reportFeeMonthStatisticsPrepaymentPo.setOweAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //欠费
                                reportFeeMonthStatisticsPrepaymentPo.setRemark("预付期逾期账单更新生成");
                                reportFeeMonthStatisticsPrepaymentPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                                reportFeeMonthStatisticsPrepaymentServiceDaoImpl.updateReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
                            }
                        }
                    }
                }
            } else { //一次性费用
                ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
                reportFeeMonthStatisticsPrepaymentDto.setCommunityId(tmpReportCarDto.getCommunityId());
                reportFeeMonthStatisticsPrepaymentDto.setConfigId(tmpReportFeeDto.getConfigId());
                reportFeeMonthStatisticsPrepaymentDto.setObjId(tmpReportFeeDto.getPayerObjId());
                reportFeeMonthStatisticsPrepaymentDto.setFeeId(tmpReportFeeDto.getFeeId());
                reportFeeMonthStatisticsPrepaymentDto.setObjType(tmpReportFeeDto.getPayerObjType());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                reportFeeMonthStatisticsPrepaymentDto.setFeeBeginTime(sdf.format(tmpReportFeeDto.getStartTime()));
                reportFeeMonthStatisticsPrepaymentDto.setFeeFinishTime(sdf.format(tmpReportFeeDto.getEndTime()));
                //查询预付期费用账单报表里是否已经存在过该费用的报表信息了
                List<ReportFeeMonthStatisticsPrepaymentDto> statistics = BeanConvertUtil.covertBeanList(
                        reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)),
                        ReportFeeMonthStatisticsPrepaymentDto.class);
                if (!ListUtil.isNull(statistics)) { //表示报表中已经存在一条该费用信息了(说明该费用跑过定时任务了)，就更改预付期费用账单报表状态，不新增预付期费用账单
                    if (!StringUtil.isEmpty(statistics.get(0).getbillState()) && statistics.get(0).getbillState().equals("1")) { //账单状态：0 正常；1 已逾期； 2已还清
                        return;
                    }
                    ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(statistics.get(0).getPrepaymentId());
                    reportFeeMonthStatisticsPrepaymentPo.setbillState("1"); //账单状态：0 正常；1 已逾期； 2已还清
                    reportFeeMonthStatisticsPrepaymentPo.setRemark("一次性预付期费用账单已逾期");
                    reportFeeMonthStatisticsPrepaymentPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                    FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
                    FeeDto feeDto1 = dealAmount(feeDto);
                    reportFeeMonthStatisticsPrepaymentPo.setOweAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //欠费
                    reportFeeMonthStatisticsPrepaymentServiceDaoImpl.updateReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
                } else {//说明没跑过定时任务
                    ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
                    reportFeeMonthStatisticsPrepaymentPo.setCommunityId(tmpReportFeeDto.getCommunityId());
                    reportFeeMonthStatisticsPrepaymentPo.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
                    reportFeeMonthStatisticsPrepaymentPo.setObjId(tmpReportCarDto.getCarId());
                    reportFeeMonthStatisticsPrepaymentPo.setObjName(tmpReportCarDto.getCarNum() + "(" + tmpReportCarDto.getAreaNum() + "停车场" + tmpReportCarDto.getNum() + "车位)");
                    reportFeeMonthStatisticsPrepaymentPo.setObjNameNum(tmpReportCarDto.getAreaNum() + "-" + tmpReportCarDto.getNum() + "-" + tmpReportCarDto.getCarNum());
                    reportFeeMonthStatisticsPrepaymentPo.setFeeId(tmpReportFeeDto.getFeeId());
                    reportFeeMonthStatisticsPrepaymentPo.setFeeName(StringUtil.isEmpty(tmpReportFeeDto.getImportFeeName()) ? tmpReportFeeDto.getFeeName() : tmpReportFeeDto.getImportFeeName());
                    reportFeeMonthStatisticsPrepaymentPo.setConfigId(tmpReportFeeDto.getConfigId());
                    FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
                    FeeDto feeDto1 = dealAmount(feeDto);
                    reportFeeMonthStatisticsPrepaymentPo.setReceivableAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //应收
                    reportFeeMonthStatisticsPrepaymentPo.setPayableAmount(reportFeeMonthStatisticsPrepaymentPo.getReceivableAmount()); //应缴
                    reportFeeMonthStatisticsPrepaymentPo.setReceivedAmount("0"); //实收
                    reportFeeMonthStatisticsPrepaymentPo.setOweAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //欠费
                    reportFeeMonthStatisticsPrepaymentPo.setRemark("一次性预付期费用账单已逾期");
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("0"); //0 未缴费; 1 已缴费
                    reportFeeMonthStatisticsPrepaymentPo.setbillState("1"); //账单状态：0 正常；1 已逾期； 2已还清
                    reportFeeMonthStatisticsPrepaymentPo.setFeeBeginTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));//计费开始时间
                    reportFeeMonthStatisticsPrepaymentPo.setFeeFinishTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A)); //计费结束时间
                    reportFeeMonthStatisticsPrepaymentPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                    reportFeeMonthStatisticsPrepaymentPo.setPayTime(null);
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentCycle(""); //缴费周期
                    reportFeeMonthStatisticsPrepaymentPo.setPayFeeFlag(feeFlag); //费用标识
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentDetailId(""); //费用明细id
                    reportFeeMonthStatisticsPrepaymentServiceDaoImpl.saveReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
                }
            }
        }
    }

    private void doDealRoomFee(ReportRoomDto reportRoomDto, ReportFeeDto tmpReportFeeDto) {
        //费用已经结束，并且当月实收为0，那就是之前就结束了，无需处理
        if (FeeDto.STATE_FINISH.equals(tmpReportFeeDto.getState())) {
            return;
        }
        //查询费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(tmpReportFeeDto.getConfigId());
        List<FeeConfigDto> feeConfigDtos = feeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
            return;
        }
        //获取预付期
        String prepaymentPeriod = feeConfigDtos.get(0).getPrepaymentPeriod();
        if (StringUtil.isEmpty(prepaymentPeriod) || prepaymentPeriod.equals("-1")) {
            return;
        }
        int prepaymentDay = Integer.parseInt(prepaymentPeriod);
        //获取下次费用的开始时间（费用结束时间即为下次费用开始时间）
        Date beginTime = tmpReportFeeDto.getEndTime();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(beginTime);
        long time1 = cal1.getTimeInMillis(); //费用开始时间
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(new Date());
        long time2 = cal2.getTimeInMillis(); //当前时间
        if (time2 <= time1) { //如果当前时间小于等于下次计费开始时间，说明该费用没有欠费
            long between_days = (time1 - time2) / (1000 * 3600 * 24); //计算下次费用开始时间距当前时间有多少天
            int betweenDay = Integer.parseInt(String.valueOf(between_days));
            //获取费用标识
            String feeFlag = feeConfigDtos.get(0).getFeeFlag();
            //获取缴费周期
            String paymentCycle = feeConfigDtos.get(0).getPaymentCycle();
            int cycle = Integer.parseInt(paymentCycle);
            if (prepaymentDay >= betweenDay) { //费用开始时间距当前时间在费用项预付期内，就生成报表
                ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
                reportFeeMonthStatisticsPrepaymentDto.setCommunityId(reportRoomDto.getCommunityId());
                reportFeeMonthStatisticsPrepaymentDto.setConfigId(tmpReportFeeDto.getConfigId());
                reportFeeMonthStatisticsPrepaymentDto.setObjId(tmpReportFeeDto.getPayerObjId());
                reportFeeMonthStatisticsPrepaymentDto.setFeeId(tmpReportFeeDto.getFeeId());
                reportFeeMonthStatisticsPrepaymentDto.setObjType(tmpReportFeeDto.getPayerObjType());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (!StringUtil.isEmpty(feeFlag) && (feeFlag.equals("1003006") || feeFlag.equals("4012024"))) { //1003006 为周期性费用;2006012 为一次性费用;4012024 间接性费用
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(tmpReportFeeDto.getEndTime());
                    calendar.add(Calendar.MONTH,cycle);
                    Date endTime = calendar.getTime();
                    reportFeeMonthStatisticsPrepaymentDto.setFeeBeginTime(sdf.format(tmpReportFeeDto.getEndTime()));
                    reportFeeMonthStatisticsPrepaymentDto.setFeeFinishTime(sdf.format(endTime));
                } else {
                    reportFeeMonthStatisticsPrepaymentDto.setFeeBeginTime(sdf.format(tmpReportFeeDto.getStartTime()));
                    reportFeeMonthStatisticsPrepaymentDto.setFeeFinishTime(sdf.format(tmpReportFeeDto.getEndTime()));
                }
                //查询预付期费用账单报表里是否已经存在过该费用的报表信息了
                List<ReportFeeMonthStatisticsPrepaymentDto> statistics = BeanConvertUtil.covertBeanList(
                        reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)),
                        ReportFeeMonthStatisticsPrepaymentDto.class);
                if (!ListUtil.isNull(statistics)) { //表示报表中已经存在一条该费用信息了(说明该费用跑过一次定时任务，且生成了一条预付期费用账单报表)，此时无需处理
                    return;
                } else { //说明没跑过定时任务
                    if (!StringUtil.isEmpty(feeFlag) && (feeFlag.equals("1003006") || feeFlag.equals("4012024"))) { //1003006 为周期性费用;2006012 为一次性费用;4012024 间接性费用
                        cal1.add(Calendar.MONTH, cycle);
                        Date time = cal1.getTime();
                        tmpReportFeeDto.setNewEndTime(time);
                    } else {
                        tmpReportFeeDto.setNewEndTime(tmpReportFeeDto.getEndTime());
                    }
                    String sign = "1";
                    savaReportFeeMonthStatisticsPrepayment(reportRoomDto, tmpReportFeeDto, feeConfigDtos, sign);
                }
            }
        } else { //当前时间超过费用下次费用开始时间，说明已经逾期了，有欠费
            int startYear = cal1.get(Calendar.YEAR); //获取费用开始时间年份
            int startMonth = cal1.get(Calendar.MONTH) + 1; //获取费用开始时间月份
            int startDay = cal1.get(Calendar.DATE); //获取费用开始日份
            int nowYear = cal2.get(Calendar.YEAR); //获取当前时间年份
            int nowMonth = cal2.get(Calendar.MONTH) + 1; //获取当前时间月份
            int nowDay = cal2.get(Calendar.DATE); //获取当前日份
            int monthNum = (nowYear - startYear) * 12 + (nowMonth - startMonth); //计算当前时间与费用开始时间相差几个月
            if (monthNum == 0) {
                int day = 0;
                if (nowDay > startDay) {
                    day = nowDay - startDay;
                } else {
                    day = startDay - nowDay;
                }
                if (day > 0) {
                    monthNum = 1;
                }
            }
            //获取费用标识
            String feeFlag = feeConfigDtos.get(0).getFeeFlag();
            if (!StringUtil.isEmpty(feeFlag) && (feeFlag.equals("1003006") || feeFlag.equals("4012024"))) { //1003006 为周期性费用;2006012 为一次性费用;4012024 间接性费用
                //获取缴费周期
                String paymentCycle = feeConfigDtos.get(0).getPaymentCycle();
                double cycle = Double.parseDouble(paymentCycle);
                double hasCycle = Math.ceil(monthNum / cycle); //向上取整(计算当前日期与费用开始时间之间有几个周期)
                for (int i = 1; i <= hasCycle; i++) {
                    //获取费用开始时间
                    Date endTime = tmpReportFeeDto.getEndTime();
                    int cle = Integer.parseInt(paymentCycle); //周期
                    int newCycle = cle * i; //有几个周期
                    Calendar cal4 = Calendar.getInstance();
                    cal4.setTime(endTime);
                    cal4.add(Calendar.MONTH, newCycle);
                    Date nextFeeTime = cal4.getTime(); //获取下次计费开始时间
                    ReportFeeMonthStatisticsPrepaymentDto feeMonthStatisticsPrepayment = new ReportFeeMonthStatisticsPrepaymentDto();
                    feeMonthStatisticsPrepayment.setCommunityId(reportRoomDto.getCommunityId());
                    feeMonthStatisticsPrepayment.setConfigId(tmpReportFeeDto.getConfigId());
                    feeMonthStatisticsPrepayment.setObjId(tmpReportFeeDto.getPayerObjId());
                    feeMonthStatisticsPrepayment.setFeeId(tmpReportFeeDto.getFeeId());
                    feeMonthStatisticsPrepayment.setObjType(tmpReportFeeDto.getPayerObjType());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    feeMonthStatisticsPrepayment.setFeeFinishTime(sdf.format(nextFeeTime));
                    //查询预付期费用账单报表里是否已经存在过该费用的报表信息了
                    List<ReportFeeMonthStatisticsPrepaymentDto> statistics = BeanConvertUtil.covertBeanList(
                            reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(feeMonthStatisticsPrepayment)),
                            ReportFeeMonthStatisticsPrepaymentDto.class);
                    if (!ListUtil.isNull(statistics)) { //表示报表中已经存在一条该费用信息了(说明该费用跑过一次定时任务，且生成了一条预付期费用账单报表)，此时无需处理
                        continue;
                    } else {
                        String sign = "3";
                        Calendar cal3 = Calendar.getInstance();
                        cal3.setTime(nextFeeTime);
                        cal3.add(Calendar.MONTH, -cle);
                        Date time = cal3.getTime();
                        tmpReportFeeDto.setNewStartTime(time); //本次费用开始时间
                        tmpReportFeeDto.setNewEndTime(nextFeeTime); //本次费用结束时间
                        savaReportFeeMonthStatisticsPrepayment(reportRoomDto, tmpReportFeeDto, feeConfigDtos, sign);
                    }
                    Calendar cal5 = Calendar.getInstance();
                    cal5.setTime(nextFeeTime);
                    long newBeginTime = cal5.getTimeInMillis(); //下次费用开始时间(即本次费用结束时间)
                    Calendar cal6 = Calendar.getInstance();
                    cal6.setTime(new Date()); //当前时间
                    long newDateTime = cal6.getTimeInMillis(); //当前时间
                    if (newBeginTime > newDateTime) { //如果下次费用开始时间大于当前时间，说明本次费用还没有逾期，接下来判断是否在预付期内
                        long between_days = (newBeginTime - newDateTime) / (1000 * 3600 * 24); //计算下次费用开始时间距当前时间有多少天
                        int betweenDay = Integer.parseInt(String.valueOf(between_days));
                        if (prepaymentDay >= betweenDay) { //费用开始时间距当前时间在费用项预付期内，就生成报表
                            tmpReportFeeDto.setNewStartTime(nextFeeTime);
                            cal5.add(Calendar.MONTH, cle);
                            Date nextEndTime = cal5.getTime();
                            tmpReportFeeDto.setNewEndTime(nextEndTime);
                            String sign = "2";
                            savaReportFeeMonthStatisticsPrepayment(reportRoomDto, tmpReportFeeDto, feeConfigDtos, sign);
                        }
                    } else { //如果当前时间大于下次费用开始时间，说明本次费用已经逾期，接下来就需要查询出当前时间之前报表里状态位未缴费的数据，把数据改成已逾期
                        ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
                        reportFeeMonthStatisticsPrepaymentDto.setFeeId(tmpReportFeeDto.getFeeId());
                        reportFeeMonthStatisticsPrepaymentDto.setPrepaymentState("0"); //未缴费
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        reportFeeMonthStatisticsPrepaymentDto.setNextStartTime(format.format(nextFeeTime)); //下次计费开始日期
                        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentDtos = BeanConvertUtil.covertBeanList(
                                reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getReportFeeMonthStatisticsPrepaymentInfo(
                                        BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)), ReportFeeMonthStatisticsPrepaymentDto.class);
                        if (reportFeeMonthStatisticsPrepaymentDtos != null && reportFeeMonthStatisticsPrepaymentDtos.size() > 0) {
                            for (ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepayment : reportFeeMonthStatisticsPrepaymentDtos) {
                                ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
                                reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(reportFeeMonthStatisticsPrepayment.getPrepaymentId());
                                reportFeeMonthStatisticsPrepaymentPo.setbillState("1"); //账单状态：0 正常；1 已逾期； 2已还清
                                FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
                                FeeDto feeDto1 = dealAmount(feeDto);
                                reportFeeMonthStatisticsPrepaymentPo.setOweAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //欠费
                                reportFeeMonthStatisticsPrepaymentPo.setRemark("预付期逾期账单更新生成");
                                reportFeeMonthStatisticsPrepaymentPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                                reportFeeMonthStatisticsPrepaymentServiceDaoImpl.updateReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
                            }
                        }
                    }
                }
            } else { //一次性费用
                ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
                reportFeeMonthStatisticsPrepaymentDto.setCommunityId(reportRoomDto.getCommunityId());
                reportFeeMonthStatisticsPrepaymentDto.setConfigId(tmpReportFeeDto.getConfigId());
                reportFeeMonthStatisticsPrepaymentDto.setObjId(tmpReportFeeDto.getPayerObjId());
                reportFeeMonthStatisticsPrepaymentDto.setFeeId(tmpReportFeeDto.getFeeId());
                reportFeeMonthStatisticsPrepaymentDto.setObjType(tmpReportFeeDto.getPayerObjType());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                reportFeeMonthStatisticsPrepaymentDto.setFeeBeginTime(sdf.format(tmpReportFeeDto.getStartTime()));
                reportFeeMonthStatisticsPrepaymentDto.setFeeFinishTime(sdf.format(tmpReportFeeDto.getEndTime()));
                //查询预付期费用账单报表里是否已经存在过该费用的报表信息了
                List<ReportFeeMonthStatisticsPrepaymentDto> statistics = BeanConvertUtil.covertBeanList(
                        reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentDto)),
                        ReportFeeMonthStatisticsPrepaymentDto.class);
                if (!ListUtil.isNull(statistics)) { //表示报表中已经存在一条该费用信息了(说明该费用跑过定时任务了)，就更改预付期费用账单报表状态，不新增预付期费用账单
                    if (!StringUtil.isEmpty(statistics.get(0).getbillState()) && statistics.get(0).getbillState().equals("1")) { //账单状态：0 正常；1 已逾期； 2已还清
                        return;
                    }
                    ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(statistics.get(0).getPrepaymentId());
                    reportFeeMonthStatisticsPrepaymentPo.setbillState("1"); //账单状态：0 正常；1 已逾期； 2已还清
                    reportFeeMonthStatisticsPrepaymentPo.setRemark("一次性预付期费用账单已逾期");
                    reportFeeMonthStatisticsPrepaymentPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                    FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
                    FeeDto feeDto1 = dealAmount(feeDto);
                    reportFeeMonthStatisticsPrepaymentPo.setOweAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //欠费
                    reportFeeMonthStatisticsPrepaymentServiceDaoImpl.updateReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
                } else {//说明没跑过定时任务
                    ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
                    reportFeeMonthStatisticsPrepaymentPo.setCommunityId(tmpReportFeeDto.getCommunityId());
                    reportFeeMonthStatisticsPrepaymentPo.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
                    reportFeeMonthStatisticsPrepaymentPo.setObjId(reportRoomDto.getRoomId());
                    if (RoomDto.ROOM_TYPE_ROOM.equals(reportRoomDto.getRoomType())) {
                        reportFeeMonthStatisticsPrepaymentPo.setObjName(reportRoomDto.getFloorNum() + "栋" + reportRoomDto.getUnitNum() + "单元" + reportRoomDto.getRoomNum() + "室");
                        reportFeeMonthStatisticsPrepaymentPo.setObjNameNum(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getUnitNum() + "-" + reportRoomDto.getRoomNum());
                    } else {
                        reportFeeMonthStatisticsPrepaymentPo.setObjName(reportRoomDto.getFloorNum() + "栋" + reportRoomDto.getRoomNum() + "室");
                        reportFeeMonthStatisticsPrepaymentPo.setObjNameNum(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getRoomNum());
                    }
                    reportFeeMonthStatisticsPrepaymentPo.setFeeId(tmpReportFeeDto.getFeeId());
                    reportFeeMonthStatisticsPrepaymentPo.setFeeName(StringUtil.isEmpty(tmpReportFeeDto.getImportFeeName()) ? tmpReportFeeDto.getFeeName() : tmpReportFeeDto.getImportFeeName());
                    reportFeeMonthStatisticsPrepaymentPo.setConfigId(tmpReportFeeDto.getConfigId());
                    FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
                    FeeDto feeDto1 = dealAmount(feeDto);
                    reportFeeMonthStatisticsPrepaymentPo.setReceivableAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //应收
                    reportFeeMonthStatisticsPrepaymentPo.setPayableAmount(reportFeeMonthStatisticsPrepaymentPo.getReceivableAmount()); //应缴
                    reportFeeMonthStatisticsPrepaymentPo.setReceivedAmount("0"); //实收
                    reportFeeMonthStatisticsPrepaymentPo.setOweAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //欠费
                    reportFeeMonthStatisticsPrepaymentPo.setRemark("一次性预付期费用账单已逾期");
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("0"); //0 未缴费; 1 已缴费
                    reportFeeMonthStatisticsPrepaymentPo.setbillState("1"); //账单状态：0 正常；1 已逾期； 2已还清
                    reportFeeMonthStatisticsPrepaymentPo.setFeeBeginTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));//计费开始时间
                    reportFeeMonthStatisticsPrepaymentPo.setFeeFinishTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A)); //计费结束时间
                    reportFeeMonthStatisticsPrepaymentPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                    reportFeeMonthStatisticsPrepaymentPo.setPayTime(null);
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentCycle(""); //缴费周期
                    reportFeeMonthStatisticsPrepaymentPo.setPayFeeFlag(feeFlag); //费用标识
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentDetailId(""); //费用明细id
                    reportFeeMonthStatisticsPrepaymentServiceDaoImpl.saveReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
                }
            }
        }
    }

    //添加预付期账单报表
    private void savaReportFeeMonthStatisticsPrepayment(ReportRoomDto reportRoomDto, ReportFeeDto tmpReportFeeDto, List<FeeConfigDto> feeConfigDtos, String sign) {
        //获取费用标识
        String feeFlag = feeConfigDtos.get(0).getFeeFlag(); //1003006 为周期性费用;2006012 为一次性费用;4012024 间接性费用
        ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
        reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
        reportFeeMonthStatisticsPrepaymentPo.setCommunityId(tmpReportFeeDto.getCommunityId());
        reportFeeMonthStatisticsPrepaymentPo.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        reportFeeMonthStatisticsPrepaymentPo.setObjId(reportRoomDto.getRoomId());
        if (RoomDto.ROOM_TYPE_ROOM.equals(reportRoomDto.getRoomType())) {
            reportFeeMonthStatisticsPrepaymentPo.setObjName(reportRoomDto.getFloorNum() + "栋" + reportRoomDto.getUnitNum() + "单元" + reportRoomDto.getRoomNum() + "室");
            reportFeeMonthStatisticsPrepaymentPo.setObjNameNum(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getUnitNum() + "-" + reportRoomDto.getRoomNum());
        } else {
            reportFeeMonthStatisticsPrepaymentPo.setObjName(reportRoomDto.getFloorNum() + "栋" + reportRoomDto.getRoomNum() + "室");
            reportFeeMonthStatisticsPrepaymentPo.setObjNameNum(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getRoomNum());
        }
        reportFeeMonthStatisticsPrepaymentPo.setFeeId(tmpReportFeeDto.getFeeId());
        reportFeeMonthStatisticsPrepaymentPo.setFeeName(StringUtil.isEmpty(tmpReportFeeDto.getImportFeeName()) ? tmpReportFeeDto.getFeeName() : tmpReportFeeDto.getImportFeeName());
        reportFeeMonthStatisticsPrepaymentPo.setConfigId(tmpReportFeeDto.getConfigId());
        FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
        FeeDto feeDto1 = dealAmount(feeDto);
        reportFeeMonthStatisticsPrepaymentPo.setReceivableAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //应收
        reportFeeMonthStatisticsPrepaymentPo.setPayableAmount(reportFeeMonthStatisticsPrepaymentPo.getReceivableAmount()); //应缴
        reportFeeMonthStatisticsPrepaymentPo.setReceivedAmount("0"); //实收
        if (!StringUtil.isEmpty(sign) && sign.equals("1")) {
            reportFeeMonthStatisticsPrepaymentPo.setOweAmount("0"); //欠费
            reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("0"); //0 未缴费; 1 已缴费
            reportFeeMonthStatisticsPrepaymentPo.setbillState("0"); //账单状态：0 正常；1 已逾期； 2已还清
            reportFeeMonthStatisticsPrepaymentPo.setRemark("预付期费用账单生成");
            reportFeeMonthStatisticsPrepaymentPo.setFeeBeginTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));//计费开始时间
            reportFeeMonthStatisticsPrepaymentPo.setFeeFinishTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getNewEndTime(), DateUtil.DATE_FORMATE_STRING_A)); //计费结束时间
        }
        if (!StringUtil.isEmpty(sign) && sign.equals("2")) {
            reportFeeMonthStatisticsPrepaymentPo.setOweAmount("0"); //欠费
            reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("0"); //0 未缴费; 1 已缴费
            reportFeeMonthStatisticsPrepaymentPo.setbillState("0"); //账单状态：0 正常；1 已逾期； 2已还清
            reportFeeMonthStatisticsPrepaymentPo.setRemark("预付期费用账单未缴费状态生成");
            reportFeeMonthStatisticsPrepaymentPo.setFeeBeginTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getNewStartTime(), DateUtil.DATE_FORMATE_STRING_A));//计费开始时间
            reportFeeMonthStatisticsPrepaymentPo.setFeeFinishTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getNewEndTime(), DateUtil.DATE_FORMATE_STRING_A)); //计费结束时间
        }
        if (!StringUtil.isEmpty(sign) && sign.equals("3")) {
            //获取本次费用开始时间
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(tmpReportFeeDto.getNewStartTime());
            long time1 = cal1.getTimeInMillis();
            //获取当前时间
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(new Date());
            long time2 = cal2.getTimeInMillis();
            if (time2 > time1) {
                reportFeeMonthStatisticsPrepaymentPo.setOweAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //欠费
                reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("0"); //0 未缴费; 1 已缴费
                reportFeeMonthStatisticsPrepaymentPo.setbillState("1"); //账单状态：0 正常；1 已逾期； 2已还清
                reportFeeMonthStatisticsPrepaymentPo.setRemark("预付期费用账单已逾期状态生成");
            } else {
                reportFeeMonthStatisticsPrepaymentPo.setOweAmount("0"); //欠费
                reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("0"); //0 未缴费; 1 已缴费
                reportFeeMonthStatisticsPrepaymentPo.setbillState("0"); //账单状态：0 正常；1 已逾期； 2已还清
                reportFeeMonthStatisticsPrepaymentPo.setRemark("预付期费用账单未缴费状态生成");
            }
            reportFeeMonthStatisticsPrepaymentPo.setFeeBeginTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getNewStartTime(), DateUtil.DATE_FORMATE_STRING_A));//计费开始时间
            reportFeeMonthStatisticsPrepaymentPo.setFeeFinishTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getNewEndTime(), DateUtil.DATE_FORMATE_STRING_A)); //计费结束时间
        }
        reportFeeMonthStatisticsPrepaymentPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        reportFeeMonthStatisticsPrepaymentPo.setPayTime(null);
        //获取缴费周期
        String paymentCycle = feeConfigDtos.get(0).getPaymentCycle();
        reportFeeMonthStatisticsPrepaymentPo.setPrepaymentCycle(paymentCycle); //缴费周期
        reportFeeMonthStatisticsPrepaymentPo.setPayFeeFlag(feeFlag); //费用标识
        reportFeeMonthStatisticsPrepaymentPo.setPrepaymentDetailId(""); //费用明细id
        reportFeeMonthStatisticsPrepaymentServiceDaoImpl.saveReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
    }

    //添加预付期账单报表(车辆)
    private void savaReportCarFeeMonthStatisticsPrepayment(ReportCarDto reportCarDto, ReportFeeDto tmpReportFeeDto, List<FeeConfigDto> feeConfigDtos, String sign) {
        //获取费用标识
        String feeFlag = feeConfigDtos.get(0).getFeeFlag(); //1003006 为周期性费用;2006012 为一次性费用;4012024 间接性费用
        ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
        reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
        reportFeeMonthStatisticsPrepaymentPo.setCommunityId(tmpReportFeeDto.getCommunityId());
        reportFeeMonthStatisticsPrepaymentPo.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        reportFeeMonthStatisticsPrepaymentPo.setObjId(reportCarDto.getCarId());
        reportFeeMonthStatisticsPrepaymentPo.setObjName(reportCarDto.getCarNum() + "(" + reportCarDto.getAreaNum() + "停车场" + reportCarDto.getNum() + "车位)");
        reportFeeMonthStatisticsPrepaymentPo.setObjNameNum(reportCarDto.getAreaNum() + "-" + reportCarDto.getNum() + "-" + reportCarDto.getCarNum());
        reportFeeMonthStatisticsPrepaymentPo.setFeeId(tmpReportFeeDto.getFeeId());
        reportFeeMonthStatisticsPrepaymentPo.setFeeName(StringUtil.isEmpty(tmpReportFeeDto.getImportFeeName()) ? tmpReportFeeDto.getFeeName() : tmpReportFeeDto.getImportFeeName());
        reportFeeMonthStatisticsPrepaymentPo.setConfigId(tmpReportFeeDto.getConfigId());
        FeeDto feeDto = BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class);
        FeeDto feeDto1 = dealAmount(feeDto);
        reportFeeMonthStatisticsPrepaymentPo.setReceivableAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //应收
        reportFeeMonthStatisticsPrepaymentPo.setPayableAmount(reportFeeMonthStatisticsPrepaymentPo.getReceivableAmount()); //应缴
        reportFeeMonthStatisticsPrepaymentPo.setReceivedAmount("0"); //实收
        if (!StringUtil.isEmpty(sign) && sign.equals("1")) {
            reportFeeMonthStatisticsPrepaymentPo.setOweAmount("0"); //欠费
            reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("0"); //0 未缴费; 1 已缴费
            reportFeeMonthStatisticsPrepaymentPo.setbillState("0"); //账单状态：0 正常；1 已逾期； 2已还清
            reportFeeMonthStatisticsPrepaymentPo.setRemark("预付期费用账单生成");
            reportFeeMonthStatisticsPrepaymentPo.setFeeBeginTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));//计费开始时间
            reportFeeMonthStatisticsPrepaymentPo.setFeeFinishTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getNewEndTime(), DateUtil.DATE_FORMATE_STRING_A)); //计费结束时间
        }
        if (!StringUtil.isEmpty(sign) && sign.equals("2")) {
            reportFeeMonthStatisticsPrepaymentPo.setOweAmount("0"); //欠费
            reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("0"); //0 未缴费; 1 已缴费
            reportFeeMonthStatisticsPrepaymentPo.setbillState("0"); //账单状态：0 正常；1 已逾期； 2已还清
            reportFeeMonthStatisticsPrepaymentPo.setRemark("预付期费用账单未缴费状态生成");
            reportFeeMonthStatisticsPrepaymentPo.setFeeBeginTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getNewStartTime(), DateUtil.DATE_FORMATE_STRING_A));//计费开始时间
            reportFeeMonthStatisticsPrepaymentPo.setFeeFinishTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getNewEndTime(), DateUtil.DATE_FORMATE_STRING_A)); //计费结束时间
        }
        if (!StringUtil.isEmpty(sign) && sign.equals("3")) {
            //获取本次费用开始时间
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(tmpReportFeeDto.getNewStartTime());
            long time1 = cal1.getTimeInMillis();
            //获取当前时间
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(new Date());
            long time2 = cal2.getTimeInMillis();
            if (time2 > time1) {
                reportFeeMonthStatisticsPrepaymentPo.setOweAmount(String.valueOf(feeDto1.getFeeTotalPrice())); //欠费
                reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("0"); //0 未缴费; 1 已缴费
                reportFeeMonthStatisticsPrepaymentPo.setbillState("1"); //账单状态：0 正常；1 已逾期； 2已还清
                reportFeeMonthStatisticsPrepaymentPo.setRemark("预付期费用账单已逾期状态生成");
            } else {
                reportFeeMonthStatisticsPrepaymentPo.setOweAmount("0"); //欠费
                reportFeeMonthStatisticsPrepaymentPo.setPrepaymentState("0"); //0 未缴费; 1 已缴费
                reportFeeMonthStatisticsPrepaymentPo.setbillState("0"); //账单状态：0 正常；1 已逾期； 2已还清
                reportFeeMonthStatisticsPrepaymentPo.setRemark("预付期费用账单未缴费状态生成");
            }
            reportFeeMonthStatisticsPrepaymentPo.setFeeBeginTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getNewStartTime(), DateUtil.DATE_FORMATE_STRING_A));//计费开始时间
            reportFeeMonthStatisticsPrepaymentPo.setFeeFinishTime(DateUtil.getFormatTimeString(tmpReportFeeDto.getNewEndTime(), DateUtil.DATE_FORMATE_STRING_A)); //计费结束时间
        }
        reportFeeMonthStatisticsPrepaymentPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        reportFeeMonthStatisticsPrepaymentPo.setPayTime(null);
        if (!StringUtil.isEmpty(feeFlag) && (feeFlag.equals("1003006") || feeFlag.equals("4012024"))) { //1003006 为周期性费用;2006012 为一次性费用;4012024 间接性费用
            //获取缴费周期
            String paymentCycle = feeConfigDtos.get(0).getPaymentCycle();
            reportFeeMonthStatisticsPrepaymentPo.setPrepaymentCycle(paymentCycle); //缴费周期
        } else {
            reportFeeMonthStatisticsPrepaymentPo.setPrepaymentCycle(""); //缴费周期
        }
        reportFeeMonthStatisticsPrepaymentPo.setPayFeeFlag(feeFlag); //费用标识
        reportFeeMonthStatisticsPrepaymentPo.setPrepaymentDetailId(""); //费用明细id
        reportFeeMonthStatisticsPrepaymentServiceDaoImpl.saveReportFeeMonthStatisticsPrepaymentInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPrepaymentPo));
    }

    //获取费用
    public FeeDto dealAmount(FeeDto feeDto) {
        feeDto.setCycle(feeDto.getPaymentCycle());
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        feeDto.setFeePrice(Double.parseDouble(feePriceAll.get("feePrice").toString()));
        feeDto.setFeeTotalPrice(
                MoneyUtil.computePriceScale(
                        Double.parseDouble(feePriceAll.get("feeTotalPrice").toString()),
                        feeDto.getScale(),
                        Integer.parseInt(feeDto.getDecimalPlace())
                )
        );
        return feeDto;
    }
}
