package com.java110.report.smo.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.report.ReportDeposit;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.po.reportFee.ReportFeeMonthStatisticsPo;
import com.java110.report.dao.IReportFeeMonthStatisticsServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用月统计内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportFeeMonthStatisticsInnerServiceSMOImpl extends BaseServiceSMO implements IReportFeeMonthStatisticsInnerServiceSMO {

    @Autowired
    private IReportFeeMonthStatisticsServiceDao reportFeeMonthStatisticsServiceDaoImpl;


    @Override
    public int saveReportFeeMonthStatistics(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {
        int saveFlag = 1;
        reportFeeMonthStatisticsServiceDaoImpl.saveReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        return saveFlag;
    }

    @Override
    public int updateReportFeeMonthStatistics(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {
        int saveFlag = 1;
        reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        return saveFlag;
    }

    @Override
    public int deleteReportFeeMonthStatistics(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {
        int saveFlag = 1;
        reportFeeMonthStatisticsPo.setStatusCd("1");
        reportFeeMonthStatisticsServiceDaoImpl.deleteReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsPo));
        return saveFlag;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryReportFeeMonthStatisticss(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {

        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.getReportFeeMonthStatisticsInfo(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }


    @Override
    public int queryReportFeeMonthStatisticssCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeMonthStatisticssCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public int queryReportFeeSummaryCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummaryCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryReportFeeSummary(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummary(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryReportFeeSummaryDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummaryDetailCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryReportFeeSummaryDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummaryDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public ReportFeeMonthStatisticsDto queryReportFeeSummaryMajor(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        ReportFeeMonthStatisticsDto reportFeeMonthStatistics = BeanConvertUtil.covertBean(
                reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummaryMajor(
                        BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);
        return reportFeeMonthStatistics;
    }

    @Override
    public int queryReportFloorUnitFeeSummaryCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummaryCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryReportFloorUnitFeeSummary(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummary(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryReportFloorUnitFeeSummaryDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummaryDetailCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryReportFloorUnitFeeDetailSummary(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummaryDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public ReportFeeMonthStatisticsDto queryReportFloorUnitFeeSummaryMajor(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        ReportFeeMonthStatisticsDto reportFeeMonthStatistics = BeanConvertUtil.covertBean(
                reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummaryMajor(
                        BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);
        return reportFeeMonthStatistics;
    }

    @Override
    public int queryFeeBreakdownCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdownCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryFeeBreakdown(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdown(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryFeeBreakdownDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdownDetailCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryFeeBreakdownDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdownDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public ReportFeeMonthStatisticsDto queryFeeBreakdownMajor(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        ReportFeeMonthStatisticsDto reportFeeMonthStatistics = BeanConvertUtil.covertBean(
                reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdownMajor(
                        BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);
        return reportFeeMonthStatistics;
    }

    @Override
    public int queryFeeDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryFeeDetailCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryAllFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryAllFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);
        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryPrePaymentNewCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryPrePaymentNewCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryPrePayment(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryPrePayment(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryOweFeeDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryOweFeeDetailCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryOweFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryOweFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public ReportFeeMonthStatisticsDto queryOweFeeDetailMajor(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        ReportFeeMonthStatisticsDto reportFeeMonthStatistics = BeanConvertUtil.covertBean(
                reportFeeMonthStatisticsServiceDaoImpl.queryOweFeeDetailMajor(
                        BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);
        return reportFeeMonthStatistics;
    }

    @Override
    public JSONObject queryPayFeeDetailCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        Map info = reportFeeMonthStatisticsServiceDaoImpl.queryPayFeeDetailCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));

        return JSONObject.parseObject(JSONObject.toJSONString(info));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryPayFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryPayFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryFeeAccountDetailSum(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryFeeAccountDetailSum(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);
        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryAllPayFeeDetail(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(
                reportFeeMonthStatisticsServiceDaoImpl.queryAllPayFeeDetail(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)),
                ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryPayFeeDetailSum(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryPayFeeDetailSum(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);
        return reportFeeMonthStatisticss;
    }

    @Override
    public int queryDeadlineFeeCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryDeadlineFeeCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryPayFeeDetailDiscount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息
        int page = reportFeeMonthStatisticsDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryPayFeeDetailDiscount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);
        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryDeadlineFee(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息

        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryDeadlineFee(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryPrePaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息


        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryPrePaymentCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryDeadlinePaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息


        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryDeadlinePaymentCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryOwePaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        //校验是否传了 分页信息


        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryOwePaymentCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryAllPaymentCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticss = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryAllPaymentCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)), ReportFeeMonthStatisticsDto.class);

        return reportFeeMonthStatisticss;
    }

    @Override
    public JSONObject queryReportProficientCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {

        JSONObject result = new JSONObject();
        Map info = reportFeeMonthStatisticsServiceDaoImpl.getReceivableInformation(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
        result.put("receivableInformation", info);

        List<Map> infos = reportFeeMonthStatisticsServiceDaoImpl.getFloorReceivableInformation(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
        result.put("floorReceivableInformations", JSONArray.parseArray(JSONArray.toJSONString(infos)));

        List<Map> tempInfos = reportFeeMonthStatisticsServiceDaoImpl.getFeeConfigReceivableInformation(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
        result.put("feeConfigReceivableInformations", JSONArray.parseArray(JSONArray.toJSONString(tempInfos)));


        reportFeeMonthStatisticsDto.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        reportFeeMonthStatisticsDto.setEndTime(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));

        int deadlineFeeCount = reportFeeMonthStatisticsServiceDaoImpl.queryDeadlineFeeCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
        int prePaymentCount = reportFeeMonthStatisticsServiceDaoImpl.queryPrePaymentNewCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));


        JSONObject remindInfomation = new JSONObject();
        remindInfomation.put("deadlineFeeCount", deadlineFeeCount);
        remindInfomation.put("prePaymentCount", prePaymentCount);

        result.put("remindInfomation", remindInfomation);

        return result;
    }

    @Override
    public List<FeeConfigDto> queryFeeConfigs(FeeConfigDto feeConfigDto) {
        List<FeeConfigDto> feeConfigs = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.getFeeConfigInfo(BeanConvertUtil.beanCovertMap(feeConfigDto)), FeeConfigDto.class);
        return feeConfigs;
    }

    /**
     * 查询报修信息
     *
     * @param repairUserDto
     * @return
     */
    @Override
    public List<RepairUserDto> queryRepair(RepairUserDto repairUserDto) {
        //校验是否传了 分页信息

        int page = repairUserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            repairUserDto.setPage((page - 1) * repairUserDto.getRow());
        }
        List<RepairUserDto> repairUserDtoList = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.getRepairUserInfo(BeanConvertUtil.beanCovertMap(repairUserDto)), RepairUserDto.class);
        return repairUserDtoList;
    }

    /**
     * 查询报修信息
     *
     * @param repairUserDto
     * @return
     */
    @Override
    public List<RepairUserDto> queryRepairWithOutPage(RepairUserDto repairUserDto) {
        List<RepairUserDto> repairUserDtoList = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.getRepairWithOutPage(BeanConvertUtil.beanCovertMap(repairUserDto)), RepairUserDto.class);
        return repairUserDtoList;
    }

    /**
     * 查询员工报修表员工信息
     *
     * @param repairUserDto
     * @return
     */
    @Override
    public List<RepairUserDto> queryRepairForStaff(RepairUserDto repairUserDto) {
        //校验是否传了 分页信息

        int page = repairUserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            repairUserDto.setPage((page - 1) * repairUserDto.getRow());
        }
        List<RepairUserDto> repairUserDtoList = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.getRepairStaff(BeanConvertUtil.beanCovertMap(repairUserDto)), RepairUserDto.class);
        return repairUserDtoList;
    }

    @Override
    public int queryNoFeeRoomsCount(@RequestBody RoomDto roomDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryNoFeeRoomsCount(BeanConvertUtil.beanCovertMap(roomDto));
    }

    @Override
    public List<RoomDto> queryNoFeeRooms(@RequestBody RoomDto roomDto) {
//校验是否传了 分页信息

        int page = roomDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomDto.setPage((page - 1) * roomDto.getRow());
        }

        List<RoomDto> rooms =
                BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryNoFeeRooms(BeanConvertUtil.beanCovertMap(roomDto)),
                        RoomDto.class);

        return rooms;
    }

    @Override
    public List<ReportDeposit> queryPayFeeDeposit(@RequestBody ReportDeposit reportDeposit) {
        int page = reportDeposit.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportDeposit.setPage((page - 1) * reportDeposit.getRow());
        }
        List<ReportDeposit> deposits = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryPayFeeDeposit(BeanConvertUtil.beanCovertMap(reportDeposit)),
                ReportDeposit.class);
        return deposits;
    }

    @Override
    public List<ReportDeposit> queryFeeDepositAmount(@RequestBody ReportDeposit reportDeposit) {
        List<ReportDeposit> deposits = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryFeeDepositAmount(BeanConvertUtil.beanCovertMap(reportDeposit)),
                ReportDeposit.class);
        return deposits;
    }

    @Override
    public int queryHuaningOweFeeCount(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryHuaningOweFeeCount(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public List<ReportFeeMonthStatisticsDto> queryHuaningOweFee(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        int page = reportFeeMonthStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportFeeMonthStatisticsDto.setPage((page - 1) * reportFeeMonthStatisticsDto.getRow());
        }
        List<ReportFeeMonthStatisticsDto> deposits = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryHuaningOweFee(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto)),
                ReportFeeMonthStatisticsDto.class);
        return deposits;
    }

    @Override
    public int queryHuaningOweFeeCounts(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryHuaningOweFeeCounts(BeanConvertUtil.beanCovertMap(reportFeeMonthStatisticsDto));
    }

    @Override
    public int queryHuaningPayFeeCount(@RequestBody Map paramInfo) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryHuaningPayFeeCount(paramInfo);
    }

    @Override
    public List<Map> queryHuaningPayFee(@RequestBody Map paramInfo) {
        int page = (int)paramInfo.get("page");

        if (page != PageDto.DEFAULT_PAGE) {
            paramInfo.put("page",(page - 1) * (int)paramInfo.get("row"));
        }
        List<Map> deposits = reportFeeMonthStatisticsServiceDaoImpl.queryHuaningPayFee(paramInfo);
        return deposits;
    }

    @Override
    public int queryHuaningPayFeeCounts(@RequestBody Map paramInfo) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryHuaningPayFeeCounts(paramInfo);
    }

    @Override
    public int queryHuaningPayFeeTwoCount(@RequestBody Map paramInfo) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryHuaningPayFeeTwoCount(paramInfo);
    }

    @Override
    public List<Map> queryHuaningPayFeeTwo(@RequestBody Map paramInfo) {
        int page = (int)paramInfo.get("page");

        if (page != PageDto.DEFAULT_PAGE) {
            paramInfo.put("page",(page - 1) * (int)paramInfo.get("row"));
        }
        List<Map> deposits = reportFeeMonthStatisticsServiceDaoImpl.queryHuaningPayFeeTwo(paramInfo);
        return deposits;
    }

    @Override
    public int queryHuaningOweFeeDetailCount(@RequestBody Map paramInfo) {
        return reportFeeMonthStatisticsServiceDaoImpl.queryHuaningOweFeeDetailCount(paramInfo);
    }

    @Override
    public List<Map> queryHuaningOweFeeDetail(@RequestBody Map paramInfo) {
        int page = (int)paramInfo.get("page");

        if (page != PageDto.DEFAULT_PAGE) {
            paramInfo.put("page",(page - 1) * (int)paramInfo.get("row"));
        }
        List<Map> deposits = reportFeeMonthStatisticsServiceDaoImpl.queryHuaningOweFeeDetail(paramInfo);
        return deposits;
    }

    @Override
    public List<OwnerDto> queryRoomAndParkingSpace(@RequestBody OwnerDto ownerDto) {
        List<OwnerDto> deposits = BeanConvertUtil.covertBeanList(reportFeeMonthStatisticsServiceDaoImpl.queryRoomAndParkingSpace(BeanConvertUtil.beanCovertMap(ownerDto)),
                OwnerDto.class);
        return deposits;
    }



    public IReportFeeMonthStatisticsServiceDao getReportFeeMonthStatisticsServiceDaoImpl() {
        return reportFeeMonthStatisticsServiceDaoImpl;
    }

    public void setReportFeeMonthStatisticsServiceDaoImpl(IReportFeeMonthStatisticsServiceDao reportFeeMonthStatisticsServiceDaoImpl) {
        this.reportFeeMonthStatisticsServiceDaoImpl = reportFeeMonthStatisticsServiceDaoImpl;
    }
}
