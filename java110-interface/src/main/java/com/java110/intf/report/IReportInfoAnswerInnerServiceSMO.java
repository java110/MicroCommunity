package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportInfoAnswer.ReportInfoAnswerDto;
import com.java110.po.reportInfoAnswer.ReportInfoAnswerPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportInfoAnswerInnerServiceSMO
 * @Description 批量操作日志详情接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportInfoAnswerApi")
public interface IReportInfoAnswerInnerServiceSMO {


    @RequestMapping(value = "/saveReportInfoAnswer", method = RequestMethod.POST)
    public int saveReportInfoAnswer(@RequestBody ReportInfoAnswerPo reportInfoAnswerPo);

    @RequestMapping(value = "/updateReportInfoAnswer", method = RequestMethod.POST)
    public int updateReportInfoAnswer(@RequestBody  ReportInfoAnswerPo reportInfoAnswerPo);

    @RequestMapping(value = "/deleteReportInfoAnswer", method = RequestMethod.POST)
    public int deleteReportInfoAnswer(@RequestBody  ReportInfoAnswerPo reportInfoAnswerPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param reportInfoAnswerDto 数据对象分享
     * @return ReportInfoAnswerDto 对象数据
     */
    @RequestMapping(value = "/queryReportInfoAnswers", method = RequestMethod.POST)
    List<ReportInfoAnswerDto> queryReportInfoAnswers(@RequestBody ReportInfoAnswerDto reportInfoAnswerDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportInfoAnswerDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportInfoAnswersCount", method = RequestMethod.POST)
    int queryReportInfoAnswersCount(@RequestBody ReportInfoAnswerDto reportInfoAnswerDto);
}
