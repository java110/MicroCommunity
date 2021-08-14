package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailDto;
import com.java110.po.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportFeeMonthCollectionDetailInnerServiceSMO
 * @Description 月缴费表接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportFeeMonthCollectionDetailApi")
public interface IReportFeeMonthCollectionDetailInnerServiceSMO {


    @RequestMapping(value = "/saveReportFeeMonthCollectionDetail", method = RequestMethod.POST)
    public int saveReportFeeMonthCollectionDetail(@RequestBody ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo);

    @RequestMapping(value = "/updateReportFeeMonthCollectionDetail", method = RequestMethod.POST)
    public int updateReportFeeMonthCollectionDetail(@RequestBody  ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo);

    @RequestMapping(value = "/deleteReportFeeMonthCollectionDetail", method = RequestMethod.POST)
    public int deleteReportFeeMonthCollectionDetail(@RequestBody  ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param reportFeeMonthCollectionDetailDto 数据对象分享
     * @return ReportFeeMonthCollectionDetailDto 对象数据
     */
    @RequestMapping(value = "/queryReportFeeMonthCollectionDetails", method = RequestMethod.POST)
    List<ReportFeeMonthCollectionDetailDto> queryReportFeeMonthCollectionDetails(@RequestBody ReportFeeMonthCollectionDetailDto reportFeeMonthCollectionDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param reportFeeMonthCollectionDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryReportFeeMonthCollectionDetailsCount", method = RequestMethod.POST)
    int queryReportFeeMonthCollectionDetailsCount(@RequestBody ReportFeeMonthCollectionDetailDto reportFeeMonthCollectionDetailDto);
}
