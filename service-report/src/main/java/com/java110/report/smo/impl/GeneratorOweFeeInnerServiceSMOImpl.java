package com.java110.report.smo.impl;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.dto.report.ReportFeeDto;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.report.IGeneratorOweFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import com.java110.po.reportOweFee.ReportOweFeePo;
import com.java110.report.dao.IReportCommunityServiceDao;
import com.java110.report.dao.IReportFeeServiceDao;
import com.java110.report.dao.IReportFeeYearCollectionDetailServiceDao;
import com.java110.report.dao.IReportFeeYearCollectionServiceDao;
import com.java110.report.dao.IReportOweFeeServiceDao;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GeneratorFeeMonthStatisticsInnerServiceSMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 21:53
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
@RestController
public class GeneratorOweFeeInnerServiceSMOImpl implements IGeneratorOweFeeInnerServiceSMO {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorOweFeeInnerServiceSMOImpl.class);

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

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Autowired
    private IReportOweFeeServiceDao reportOweFeeServiceDaoImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Override
    public int generatorOweData(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        CommunityDto communityDto = new CommunityDto();

//        List<CommunityDto> communityDtos = BeanConvertUtil.covertBeanList(
//                reportCommunityServiceDaoImpl.getCommunitys(BeanConvertUtil.beanCovertMap(communityDto)), CommunityDto.class);

        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        for (CommunityDto tmpCommunityDto : communityDtos) {
            reportFeeMonthStatisticsPo.setCommunityId(tmpCommunityDto.getCommunityId());
            doGeneratorData(reportFeeMonthStatisticsPo);
        }
        return 0;
    }


    @Async
    private void doGeneratorData(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {
        String communityId = reportFeeMonthStatisticsPo.getCommunityId();

        Assert.hasLength(communityId, "未包含小区信息");

        //
        feeDataFiltering(communityId);

        //查询费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(communityId);

        List<FeeConfigDto> feeConfigDtos = BeanConvertUtil.covertBeanList(reportFeeServiceDaoImpl.getFeeConfigs(
                BeanConvertUtil.beanCovertMap(feeConfigDto)), FeeConfigDto.class);

        for (FeeConfigDto tmpFeeConfigDto : feeConfigDtos) {
            try {
                GenerateOweFeeByFeeConfig(tmpFeeConfigDto);
            } catch (Exception e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("费用出账失败" + tmpFeeConfigDto.getConfigId(), e);
            }
        }

    }

    private void feeDataFiltering(String communityId) {

        Map reportFeeDto = new HashMap();
        reportFeeDto.put("communityId", communityId);
        List<Map> feeDtos = reportOweFeeServiceDaoImpl.queryInvalidOweFee(reportFeeDto);

        List<String> feeIds = new ArrayList<>();
        for (Map feeDto : feeDtos) {
            if (!feeDto.containsKey("feeId") || StringUtil.isNullOrNone(feeDto.get("feeId"))) {
                continue;
            }

            feeIds.add(feeDto.get("feeId").toString());

            if (feeIds.size() >= 50) {
                reportFeeDto.put("feeIds", feeIds);
                reportOweFeeServiceDaoImpl.deleteInvalidFee(reportFeeDto);
                feeIds = new ArrayList<>();
            }
        }
        reportFeeDto.put("feeIds", feeIds);
        if (feeIds.size() > 0) {
            reportOweFeeServiceDaoImpl.deleteInvalidFee(reportFeeDto);
        }
    }


    /**
     * 按费用项来出账
     *
     * @param feeConfigDto
     */
    private void GenerateOweFeeByFeeConfig(FeeConfigDto feeConfigDto) throws Exception {

        //当前费用项是否

        ReportFeeDto feeDto = new ReportFeeDto();
        feeDto.setConfigId(feeConfigDto.getConfigId());
        feeDto.setCommunityId(feeConfigDto.getCommunityId());
        List<ReportFeeDto> feeDtos = reportFeeServiceDaoImpl.getFees(feeDto);

        //没有关联费用
        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }
        for (ReportFeeDto tmpFeeDto : feeDtos) {
            try {
                generateFee(tmpFeeDto, feeConfigDto);
            } catch (Exception e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("生成费用失败", e);
            }
        }

    }

    /**
     * 生成 费用
     *
     * @param reportFeeDto
     */
    private void generateFee(ReportFeeDto reportFeeDto, FeeConfigDto feeConfigDto) {

        FeeDto feeDto = BeanConvertUtil.covertBean(reportFeeDto, FeeDto.class);

        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setFeeId(feeDto.getFeeId());
        List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);

        feeDto.setFeeAttrDtos(feeAttrDtos);
        //刷入欠费金额
        computeFeeSMOImpl.computeEveryOweFee(feeDto);

        //保存数据
        ReportOweFeePo reportOweFeePo = new ReportOweFeePo();
        reportOweFeePo.setAmountOwed(feeDto.getFeeTotalPrice() + "");
        reportOweFeePo.setCommunityId(feeDto.getCommunityId());
        reportOweFeePo.setConfigId(feeConfigDto.getConfigId());
        reportOweFeePo.setConfigName(feeConfigDto.getFeeName());
        reportOweFeePo.setDeadlineTime(DateUtil.getFormatTimeString(feeDto.getDeadlineTime(), DateUtil.DATE_FORMATE_STRING_A));
        reportOweFeePo.setEndTime(DateUtil.getFormatTimeString(feeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        reportOweFeePo.setFeeId(feeDto.getFeeId());
        reportOweFeePo.setFeeName(feeDto.getFeeName());
        reportOweFeePo.setOwnerId(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_ID));
        reportOweFeePo.setOwnerName(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_NAME));
        reportOweFeePo.setOwnerTel(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_LINK));
        reportOweFeePo.setPayerObjId(feeDto.getPayerObjId());
        reportOweFeePo.setPayerObjName(computeFeeSMOImpl.getFeeObjName(feeDto));
        reportOweFeePo.setPayerObjType(feeDto.getPayerObjType());
        reportOweFeePo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setFeeId(feeDto.getFeeId());
        reportOweFeeDto.setPayerObjId(feeDto.getPayerObjId());
        List<Map> reportOweFeeDtos = reportOweFeeServiceDaoImpl.queryReportAllOweFees(BeanConvertUtil.beanCovertMap(reportOweFeeDto));
        if (reportOweFeeDtos == null || reportOweFeeDtos.size() < 1) {
            reportOweFeePo.setOweId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_oweId));
            reportOweFeeServiceDaoImpl.saveReportOweFeeInfo(BeanConvertUtil.beanCovertMap(reportOweFeePo));
        } else {
            reportOweFeePo.setOweId(reportOweFeeDtos.get(0).get("oweId").toString());
            reportOweFeeServiceDaoImpl.updateReportOweFeeInfo(BeanConvertUtil.beanCovertMap(reportOweFeePo));
        }
    }

    @Override
    public int computeOweFee(FeeDto feeDto) {
        //刷入欠费金额
        computeFeeSMOImpl.computeEveryOweFee(feeDto);

        //保存数据
        ReportOweFeePo reportOweFeePo = new ReportOweFeePo();
        reportOweFeePo.setAmountOwed(feeDto.getFeeTotalPrice() + "");
        reportOweFeePo.setCommunityId(feeDto.getCommunityId());
        reportOweFeePo.setConfigId(feeDto.getConfigId());
        reportOweFeePo.setConfigName(feeDto.getFeeName());
        reportOweFeePo.setDeadlineTime(DateUtil.getFormatTimeString(feeDto.getDeadlineTime(), DateUtil.DATE_FORMATE_STRING_A));
        reportOweFeePo.setEndTime(DateUtil.getFormatTimeString(feeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        reportOweFeePo.setFeeId(feeDto.getFeeId());
        reportOweFeePo.setFeeName(feeDto.getFeeName());
        reportOweFeePo.setOwnerId(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_ID));
        reportOweFeePo.setOwnerName(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_NAME));
        reportOweFeePo.setOwnerTel(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_LINK));
        reportOweFeePo.setPayerObjId(feeDto.getPayerObjId());
        reportOweFeePo.setPayerObjName(computeFeeSMOImpl.getFeeObjName(feeDto));
        reportOweFeePo.setPayerObjType(feeDto.getPayerObjType());
        reportOweFeePo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setFeeId(feeDto.getFeeId());
        reportOweFeeDto.setPayerObjId(feeDto.getPayerObjId());
        List<Map> reportOweFeeDtos = reportOweFeeServiceDaoImpl.queryReportAllOweFees(BeanConvertUtil.beanCovertMap(reportOweFeeDto));
        if (reportOweFeeDtos == null || reportOweFeeDtos.size() < 1) {
            reportOweFeePo.setOweId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_oweId));
            reportOweFeeServiceDaoImpl.saveReportOweFeeInfo(BeanConvertUtil.beanCovertMap(reportOweFeePo));
        } else {
            reportOweFeePo.setOweId(reportOweFeeDtos.get(0).get("oweId").toString());
            reportOweFeeServiceDaoImpl.updateReportOweFeeInfo(BeanConvertUtil.beanCovertMap(reportOweFeePo));
        }
        return 1;
    }


}
