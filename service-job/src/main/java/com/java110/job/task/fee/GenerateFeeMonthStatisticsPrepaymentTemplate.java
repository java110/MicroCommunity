package com.java110.job.task.fee;

import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.report.IGeneratorFeeMonthStatisticsPrepaymentInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.ReportFeeMonthStatisticsPrepaymentPo.ReportFeeMonthStatisticsPrepaymentPo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fqz
 * @description 预付期房屋费用账单生成
 * @date 2023-03-29
 */
@Component
public class GenerateFeeMonthStatisticsPrepaymentTemplate extends TaskSystemQuartz {

    @Autowired
    private IGeneratorFeeMonthStatisticsPrepaymentInnerServiceSMO generatorFeeMonthStatisticsPrepaymentInnerServiceSMOImpl;

    private static final Logger logger = LoggerFactory.getLogger(GenerateFeeMonthStatisticsPrepaymentTemplate.class);

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();
        for (CommunityDto communityDto : communityDtos) {
            try {
                GenerateFeeMonthStatisticPrepayment(taskDto, communityDto);
            } catch (Exception e) {
                logger.error("生成月报表 失败", e);
            }
        }
    }

    private void GenerateFeeMonthStatisticPrepayment(TaskDto taskDto, CommunityDto communityDto) {
        ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
        reportFeeMonthStatisticsPrepaymentPo.setCommunityId(communityDto.getCommunityId());
        generatorFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.generatorData(reportFeeMonthStatisticsPrepaymentPo);
    }
}
