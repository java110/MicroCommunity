package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailDto;
import com.java110.po.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportFeeYearCollectionDetailInnerServiceSMO
 * @Description 费用年收费明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportFeeYearCollectionDetailApi")
public interface IReportFeeYearCollectionDetailInnerServiceSMO {


    @RequestMapping(value = "/saveReportFeeYearCollectionDetail", method = RequestMethod.POST)
    public int saveReportFeeYearCollectionDetail(@RequestBody ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo);

    @RequestMapping(value = "/updateReportFeeYearCollectionDetail", method = RequestMethod.POST)
    public int updateReportFeeYearCollectionDetail(@RequestBody  ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo);

    @RequestMapping(value = "/deleteReportFeeYearCollectionDetail", method = RequestMethod.POST)
    public int deleteReportFeeYearCollectionDetail(@RequestBody  ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param reportFeeYearCollectionDetailDto 数据对象分享
     * @return ReportFeeYearCollectionDetailDto 对象数据
     */
    @RequestMapping(value = "/queryReportFeeYearCollectionDetails", method = RequestMethod.POST)
    List<ReportFeeYearCollectionDetailDto> queryReportFeeYearCollectionDetails(@RequestBody ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportFeeYearCollectionDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportFeeYearCollectionDetailsCount", method = RequestMethod.POST)
    int queryReportFeeYearCollectionDetailsCount(@RequestBody ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto);
}
