package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportInfoAnswer.ReportInfoAnswerValueDto;
import com.java110.po.reportInfoAnswerValue.ReportInfoAnswerValuePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportInfoAnswerValueInnerServiceSMO
 * @Description 批量操作日志详情接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportInfoAnswerValueApi")
public interface IReportInfoAnswerValueInnerServiceSMO {


    @RequestMapping(value = "/saveReportInfoAnswerValue", method = RequestMethod.POST)
    public int saveReportInfoAnswerValue(@RequestBody ReportInfoAnswerValuePo reportInfoAnswerValuePo);

    @RequestMapping(value = "/updateReportInfoAnswerValue", method = RequestMethod.POST)
    public int updateReportInfoAnswerValue(@RequestBody  ReportInfoAnswerValuePo reportInfoAnswerValuePo);

    @RequestMapping(value = "/deleteReportInfoAnswerValue", method = RequestMethod.POST)
    public int deleteReportInfoAnswerValue(@RequestBody  ReportInfoAnswerValuePo reportInfoAnswerValuePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param reportInfoAnswerValueDto 数据对象分享
     * @return ReportInfoAnswerValueDto 对象数据
     */
    @RequestMapping(value = "/queryReportInfoAnswerValues", method = RequestMethod.POST)
    List<ReportInfoAnswerValueDto> queryReportInfoAnswerValues(@RequestBody ReportInfoAnswerValueDto reportInfoAnswerValueDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportInfoAnswerValueDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportInfoAnswerValuesCount", method = RequestMethod.POST)
    int queryReportInfoAnswerValuesCount(@RequestBody ReportInfoAnswerValueDto reportInfoAnswerValueDto);
}
