package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.po.ReportFeeMonthStatisticsPrepaymentPo.ReportFeeMonthStatisticsPrepaymentPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/generatorFeeMonthStatisticsPrepaymentApi")
public interface IGeneratorFeeMonthStatisticsPrepaymentInnerServiceSMO {

    @RequestMapping(value = "/generatorPrepaymentData", method = RequestMethod.POST)
    public int generatorData(@RequestBody ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo);

}
