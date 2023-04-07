package com.java110.job.task.fee;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.common.ILogSystemErrorInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.po.reportOweFee.ReportOweFeePo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GenerateOwnerBillTemplate
 * @Description TODO  房屋费用账单生成
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class GenerateOweFeeTemplate extends TaskSystemQuartz {

    public static final double DEFAULT_ROW = 200.0;


    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private ILogSystemErrorInnerServiceSMO logSystemErrorInnerServiceSMOImpl;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            GenerateOweFee(taskDto, communityDto);
        }

    }

    /**
     * 根据小区生成账单
     *
     * @param communityDto
     */
    private void GenerateOweFee(TaskDto taskDto, CommunityDto communityDto) {


        //删除无用数据

        feeDataFiltering(communityDto.getCommunityId());

        //查询费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(communityDto.getCommunityId());

        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        for (FeeConfigDto tmpFeeConfigDto : feeConfigDtos) {
            try {
                GenerateOweFeeByFeeConfig(taskDto, tmpFeeConfigDto);
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
        reportOweFeeInnerServiceSMOImpl.deleteInvalidFee(reportFeeDto);
    }

    /**
     * 按费用项来出账
     *
     * @param taskDto
     * @param feeConfigDto
     */
    private void GenerateOweFeeByFeeConfig(TaskDto taskDto, FeeConfigDto feeConfigDto) throws Exception {

        //当前费用项是否
        FeeDto feeDto = new FeeDto();
        feeDto.setConfigId(feeConfigDto.getConfigId());
        feeDto.setCommunityId(feeConfigDto.getCommunityId());

        //先查询总数
        int count = feeInnerServiceSMOImpl.queryFeesCount(feeDto);

        double record = Math.ceil(count / DEFAULT_ROW);

        for (int page = 1; page <= record; page++) {
            try {
                feeDto.setPage(page);
                feeDto.setRow(new Double(DEFAULT_ROW).intValue());
                List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
                //没有关联费用
                if (feeDtos == null || feeDtos.size() < 1) {
                    continue;
                }
                for (FeeDto tmpFeeDto : feeDtos) {
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
            } catch (Exception e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("费用出账失败" + feeConfigDto.getConfigId(), e);
            }
        }

    }

    /**
     * 生成 费用
     *
     * @param feeDto
     */
    private void generateFee(FeeDto feeDto, FeeConfigDto feeConfigDto) {

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
        List<ReportOweFeeDto> reportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFees(reportOweFeeDto);
        if (reportOweFeeDtos == null || reportOweFeeDtos.size() < 1) {
            reportOweFeePo.setOweId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_oweId));
            reportOweFeeInnerServiceSMOImpl.saveReportOweFee(reportOweFeePo);
        } else {
            reportOweFeePo.setOweId(reportOweFeeDtos.get(0).getOweId());
            reportOweFeeInnerServiceSMOImpl.updateReportOweFee(reportOweFeePo);
        }
    }
}
