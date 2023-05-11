package com.java110.job.task.fee;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.dto.payFeeDetailMonth.PayFeeDetailRefreshFeeMonthDto;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.common.ILogSystemErrorInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeMonthInnerServiceSMO;
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
 * @ClassName GeneratePayFeeDetailMonthTemplate
 * @Description TODO  费用离散处理任务
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class GeneratePayFeeDetailMonthTemplate extends TaskSystemQuartz {

    @Autowired
    private IPayFeeMonthInnerServiceSMO payFeeMonthInnerServiceSMOImpl;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                generateMonthFee(taskDto, communityDto);
            } catch (Exception e) {
                e.printStackTrace();
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("费用出账失败" + communityDto.getCommunityId(), e);
            }
        }

    }

    /**
     * 根据小区生成账单
     *
     * @param communityDto
     */
    private void generateMonthFee(TaskDto taskDto, CommunityDto communityDto) {

        PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto = new PayFeeDetailRefreshFeeMonthDto();
        payFeeDetailRefreshFeeMonthDto.setCommunityId(communityDto.getCommunityId());
        payFeeMonthInnerServiceSMOImpl.doGeneratorOrRefreshAllFeeMonth(payFeeDetailRefreshFeeMonthDto);

    }


}
