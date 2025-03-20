package com.java110.report.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.intf.user.IStaffCommunityV1InnerServiceSMO;
import com.java110.report.bmo.reportFeeMonthStatistics.impl.GetReportFeeMonthStatisticsBMOImpl;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "fee.queryAdminOweFeeDetail")
public class QueryAdminOweFeeDetailCmd extends Cmd {

    private static final Logger logger = LoggerFactory.getLogger(GetReportFeeMonthStatisticsBMOImpl.class);

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IStaffCommunityV1InnerServiceSMO staffCommunityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validateAdmin(context);
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthStatisticsDto.class);
        String staffId = CmdContextUtils.getUserId(context);

        List<String> communityIds = staffCommunityV1InnerServiceSMOImpl.queryStaffCommunityIds(staffId);

        if (!ListUtil.isNull(communityIds)) {
            reportFeeMonthStatisticsDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        }
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryOweFeeDetailCount(reportFeeMonthStatisticsDto);
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryOweFeeDetail(reportFeeMonthStatisticsDto);
            ReportFeeMonthStatisticsDto tmpReportFeeMonthStatisticsDto = reportFeeMonthStatisticsInnerServiceSMOImpl.queryOweFeeDetailMajor(reportFeeMonthStatisticsDto);
            if (!ListUtil.isNull(reportFeeMonthStatisticsDtos)) {
                for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto1 : reportFeeMonthStatisticsDtos) {
//                    reportFeeMonthStatisticsDto1.setAllReceivableAmount(tmpReportFeeMonthStatisticsDto.getAllReceivableAmount());
//                    reportFeeMonthStatisticsDto1.setAllReceivedAmount(tmpReportFeeMonthStatisticsDto.getAllReceivedAmount());
                    reportFeeMonthStatisticsDto1.setAllOweAmount(tmpReportFeeMonthStatisticsDto.getOweAmount());
                }
            }
            freshReportOweDay(reportFeeMonthStatisticsDtos);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        refreshCommunityName(reportFeeMonthStatisticsDtos);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private void freshReportOweDay(List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos) {
        int day = 0;
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            try {
                day = DateUtil.daysBetween(DateUtil.getDateFromStringB(reportFeeMonthStatisticsDto.getEndTime()),
                        DateUtil.getDateFromStringB(reportFeeMonthStatisticsDto.getStartTime()));
                reportFeeMonthStatisticsDto.setOweDay(day);
            } catch (Exception e) {
                logger.error("计算欠费天数失败", e);
            }
        }
    }

    private void refreshCommunityName(List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos) {
        if(ListUtil.isNull(reportFeeMonthStatisticsDtos)){
            return;
        }

        List<String> communityIds = new ArrayList<>();
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            communityIds.add(reportFeeMonthStatisticsDto.getCommunityId());
        }

        if(ListUtil.isNull(communityIds)){
            return ;
        }
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);
        if(ListUtil.isNull(communityDtos)){
            return;
        }
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            for (CommunityDto tCommunityDto : communityDtos) {
                if (!reportFeeMonthStatisticsDto.getCommunityId().equals(tCommunityDto.getCommunityId())) {
                    continue;
                }
                reportFeeMonthStatisticsDto.setCommunityName(tCommunityDto.getName());
            }
        }
    }
}
