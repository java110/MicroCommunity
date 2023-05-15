package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportFeeYearCollection.ReportFeeYearCollectionDto;
import com.java110.po.reportFeeYearCollection.ReportFeeYearCollectionPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IReportFeeYearCollectionInnerServiceSMO
 * @Description 费用年收费接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportFeeYearCollectionApi")
public interface IReportFeeYearCollectionInnerServiceSMO {


    @RequestMapping(value = "/saveReportFeeYearCollection", method = RequestMethod.POST)
    public int saveReportFeeYearCollection(@RequestBody ReportFeeYearCollectionPo reportFeeYearCollectionPo);

    @RequestMapping(value = "/updateReportFeeYearCollection", method = RequestMethod.POST)
    public int updateReportFeeYearCollection(@RequestBody  ReportFeeYearCollectionPo reportFeeYearCollectionPo);

    @RequestMapping(value = "/deleteReportFeeYearCollection", method = RequestMethod.POST)
    public int deleteReportFeeYearCollection(@RequestBody  ReportFeeYearCollectionPo reportFeeYearCollectionPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param reportFeeYearCollectionDto 数据对象分享
     * @return ReportFeeYearCollectionDto 对象数据
     */
    @RequestMapping(value = "/queryReportFeeYearCollections", method = RequestMethod.POST)
    List<ReportFeeYearCollectionDto> queryReportFeeYearCollections(@RequestBody ReportFeeYearCollectionDto reportFeeYearCollectionDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportFeeYearCollectionDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportFeeYearCollectionsCount", method = RequestMethod.POST)
    int queryReportFeeYearCollectionsCount(@RequestBody ReportFeeYearCollectionDto reportFeeYearCollectionDto);

    @RequestMapping(value = "/getReportFeeYearCollectionInfo", method = RequestMethod.POST)
    List<Map> getReportFeeYearCollectionInfo(@RequestBody Map beanCovertMap);

    @RequestMapping(value = "/saveReportFeeYearCollectionInfo", method = RequestMethod.POST)
    void saveReportFeeYearCollectionInfo(@RequestBody Map beanCovertMap);
}
