package com.java110.job.task.report;

import com.java110.dto.task.TaskDto;
import com.java110.intf.report.IGeneratorOweFeeInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName GenerateOwnerBillTemplate
 * @Description TODO  房屋费用账单生成
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class GenerateOweFeeNewTemplate extends TaskSystemQuartz {
    private static final Logger logger = LoggerFactory.getLogger(GenerateOweFeeNewTemplate.class);

    @Autowired
    private IGeneratorOweFeeInnerServiceSMO generatorOweFeeInnerServiceSMOImpl;


    @Override
    protected void process(TaskDto taskDto) throws Exception {
        GenerateFeeYearStatistic(taskDto);
    }

    private void GenerateFeeYearStatistic(TaskDto taskDto) {
        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        generatorOweFeeInnerServiceSMOImpl.generatorOweData(reportFeeMonthStatisticsPo);
    }


}
