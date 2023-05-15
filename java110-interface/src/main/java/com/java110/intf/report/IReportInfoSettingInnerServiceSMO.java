package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportInfoSetting.ReportInfoSettingDto;
import com.java110.po.reportInfoSetting.ReportInfoSettingPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportInfoSettingInnerServiceSMO
 * @Description 进出上报接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportInfoSettingApi")
public interface IReportInfoSettingInnerServiceSMO {


    @RequestMapping(value = "/saveReportInfoSetting", method = RequestMethod.POST)
    public int saveReportInfoSetting(@RequestBody ReportInfoSettingPo reportInfoSettingPo);

    @RequestMapping(value = "/updateReportInfoSetting", method = RequestMethod.POST)
    public int updateReportInfoSetting(@RequestBody  ReportInfoSettingPo reportInfoSettingPo);

    @RequestMapping(value = "/deleteReportInfoSetting", method = RequestMethod.POST)
    public int deleteReportInfoSetting(@RequestBody  ReportInfoSettingPo reportInfoSettingPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param reportInfoSettingDto 数据对象分享
     * @return ReportInfoSettingDto 对象数据
     */
    @RequestMapping(value = "/queryReportInfoSettings", method = RequestMethod.POST)
    List<ReportInfoSettingDto> queryReportInfoSettings(@RequestBody ReportInfoSettingDto reportInfoSettingDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportInfoSettingDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportInfoSettingsCount", method = RequestMethod.POST)
    int queryReportInfoSettingsCount(@RequestBody ReportInfoSettingDto reportInfoSettingDto);
}
