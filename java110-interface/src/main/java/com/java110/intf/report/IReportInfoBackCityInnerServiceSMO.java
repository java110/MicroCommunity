package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportInfoAnswer.ReportInfoBackCityDto;
import com.java110.po.reportInfoBackCity.ReportInfoBackCityPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportInfoBackCityInnerServiceSMO
 * @Description 返省上报接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportInfoBackCityApi")
public interface IReportInfoBackCityInnerServiceSMO {


    @RequestMapping(value = "/saveReportInfoBackCity", method = RequestMethod.POST)
    public int saveReportInfoBackCity(@RequestBody ReportInfoBackCityPo reportInfoBackCityPo);

    @RequestMapping(value = "/updateReportInfoBackCity", method = RequestMethod.POST)
    public int updateReportInfoBackCity(@RequestBody  ReportInfoBackCityPo reportInfoBackCityPo);

    @RequestMapping(value = "/deleteReportInfoBackCity", method = RequestMethod.POST)
    public int deleteReportInfoBackCity(@RequestBody  ReportInfoBackCityPo reportInfoBackCityPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param reportInfoBackCityDto 数据对象分享
     * @return ReportInfoBackCityDto 对象数据
     */
    @RequestMapping(value = "/queryReportInfoBackCitys", method = RequestMethod.POST)
    List<ReportInfoBackCityDto> queryReportInfoBackCitys(@RequestBody ReportInfoBackCityDto reportInfoBackCityDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportInfoBackCityDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportInfoBackCitysCount", method = RequestMethod.POST)
    int queryReportInfoBackCitysCount(@RequestBody ReportInfoBackCityDto reportInfoBackCityDto);
}
