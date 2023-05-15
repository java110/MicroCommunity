package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportInfoSetting.ReportInfoSettingTitleValueDto;
import com.java110.po.reportInfoSettingTitleValue.ReportInfoSettingTitleValuePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportInfoSettingTitleValueInnerServiceSMO
 * @Description 批量操作日志详情接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportInfoSettingTitleValueApi")
public interface IReportInfoSettingTitleValueInnerServiceSMO {


    @RequestMapping(value = "/saveReportInfoSettingTitleValue", method = RequestMethod.POST)
    public int saveReportInfoSettingTitleValue(@RequestBody ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo);

    @RequestMapping(value = "/updateReportInfoSettingTitleValue", method = RequestMethod.POST)
    public int updateReportInfoSettingTitleValue(@RequestBody  ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo);

    @RequestMapping(value = "/deleteReportInfoSettingTitleValue", method = RequestMethod.POST)
    public int deleteReportInfoSettingTitleValue(@RequestBody  ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param reportInfoSettingTitleValueDto 数据对象分享
     * @return ReportInfoSettingTitleValueDto 对象数据
     */
    @RequestMapping(value = "/queryReportInfoSettingTitleValues", method = RequestMethod.POST)
    List<ReportInfoSettingTitleValueDto> queryReportInfoSettingTitleValues(@RequestBody ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto);
    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param reportInfoSettingTitleValueDto 数据对象分享
     * @return ReportInfoSettingTitleValueDto 对象数据
     */
    @RequestMapping(value = "/getReportInfoSettingTitleValueInfoResult", method = RequestMethod.POST)
    List<ReportInfoSettingTitleValueDto> getReportInfoSettingTitleValueInfoResult(@RequestBody ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportInfoSettingTitleValueDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportInfoSettingTitleValuesCount", method = RequestMethod.POST)
    int queryReportInfoSettingTitleValuesCount(@RequestBody ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto);
}
