package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportInfoSetting.ReportInfoSettingTitleDto;
import com.java110.po.reportInfoSettingTitle.ReportInfoSettingTitlePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportInfoSettingTitleInnerServiceSMO
 * @Description 进出上报题目设置接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportInfoSettingTitleApi")
public interface IReportInfoSettingTitleInnerServiceSMO {


    @RequestMapping(value = "/saveReportInfoSettingTitle", method = RequestMethod.POST)
    public int saveReportInfoSettingTitle(@RequestBody ReportInfoSettingTitlePo reportInfoSettingTitlePo);

    @RequestMapping(value = "/updateReportInfoSettingTitle", method = RequestMethod.POST)
    public int updateReportInfoSettingTitle(@RequestBody  ReportInfoSettingTitlePo reportInfoSettingTitlePo);

    @RequestMapping(value = "/deleteReportInfoSettingTitle", method = RequestMethod.POST)
    public int deleteReportInfoSettingTitle(@RequestBody  ReportInfoSettingTitlePo reportInfoSettingTitlePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param reportInfoSettingTitleDto 数据对象分享
     * @return ReportInfoSettingTitleDto 对象数据
     */
    @RequestMapping(value = "/queryReportInfoSettingTitles", method = RequestMethod.POST)
    List<ReportInfoSettingTitleDto> queryReportInfoSettingTitles(@RequestBody ReportInfoSettingTitleDto reportInfoSettingTitleDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportInfoSettingTitleDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportInfoSettingTitlesCount", method = RequestMethod.POST)
    int queryReportInfoSettingTitlesCount(@RequestBody ReportInfoSettingTitleDto reportInfoSettingTitleDto);
}
